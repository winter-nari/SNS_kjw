package com.winternari.sns_project.domain.board.service.impl;


import com.winternari.sns_project.domain.board.dto.request.BoardWithImageResponse;
import com.winternari.sns_project.domain.board.dto.response.BoardCreateRequest;
import com.winternari.sns_project.domain.board.entity.Board;
import com.winternari.sns_project.domain.board.repository.BoardRepository;
import com.winternari.sns_project.domain.board.service.BoardService;
import com.winternari.sns_project.domain.image.entity.Image;
import com.winternari.sns_project.domain.image.repository.ImageRepository;
import com.winternari.sns_project.domain.user.entity.UserEntity;
import com.winternari.sns_project.domain.user.repository.OauthRepository;
import com.winternari.sns_project.global.security.service.UserForSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final OauthRepository userRepository;
    private final ImageRepository imageRepository;
    // application.properties 에서 경로 주입 받는 방법
    @Value("${file.upload-dir}")
    private String uploadDir;

    // 해당 유저의 Board를 조회하는 메서드
    @Override
    public BoardWithImageResponse getBoardWithImage(Long boardId) {
        Board board = boardRepository.findBoardWithImage(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        UUID currentUserId = getCurrentUserId(); // 위에서 가져온 현재 로그인된 사용자 ID

        if (!board.getCreatedBy().getId().equals(currentUserId)) {
            throw new RuntimeException("해당 게시판을 조회할 권한이 없습니다.");
        }


        return BoardWithImageResponse.builder()
                .id(board.getId())
                .name(board.getName())
                .description(board.getDescription())
                .imageUrl(board.getImage() != null ? board.getImage().getFilePath() : null)
                .build();
    }

    // 현재 로그인한 사용자 정보 가져오는 메서드
    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 현재 로그인한 사용자 정보 꺼내기
        UserForSecurity userDetails = (UserForSecurity) authentication.getPrincipal();

        return userDetails.getId(); // 여기서 유저 ID 가져오기
    }

    // 전체 Board 및 이미지 조회 메서드
    @Override
    public List<BoardWithImageResponse> getAllBoardsWithImage() {
        return boardRepository.findAllWithImage().stream()
                .map(board -> BoardWithImageResponse.builder()
                        .id(board.getId())
                        .name(board.getName())
                        .description(board.getDescription())
                        .imageUrl(board.getImage() != null ? board.getImage().getFilePath() : null)
                        .build()
                ).collect(Collectors.toList());
    }

    // Board 생성 메서드
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BoardWithImageResponse createBoardWithImage(BoardCreateRequest dto, MultipartFile imageFile) {
        UUID userId = getCurrentUserId();

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Board> existingBoard = boardRepository.findByName(dto.getName());
        if (existingBoard.isPresent()) {
            throw new RuntimeException("이미 사용 중인 이름입니다.");
        }

        Board board = Board.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .build();

        Board savedBoard = boardRepository.save(board);

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir).resolve(fileName);

            try {
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, imageFile.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("이미지 저장 실패", e);
            }

            Image image = Image.builder()
                    .fileName(fileName)
                    .filePath("/image/" + fileName)
                    .fileType(imageFile.getContentType())
                    .fileSize(imageFile.getSize())
                    .board(savedBoard)
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();

            imageRepository.save(image);
        }

        return BoardWithImageResponse.builder()
                .id(savedBoard.getId())
                .name(savedBoard.getName())
                .description(savedBoard.getDescription())
                .imageUrl(savedBoard.getImage() != null ? savedBoard.getImage().getFilePath() : null)
                .build();
    }


}
