package com.winternari.sns_project.domain.board.service;

import com.winternari.sns_project.domain.board.dto.request.BoardWithImageResponse;
import com.winternari.sns_project.domain.board.dto.response.BoardCreateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    public BoardWithImageResponse getBoardWithImage(Long boardId);
    public List<BoardWithImageResponse> getAllBoardsWithImage();
    public BoardWithImageResponse createBoardWithImage(BoardCreateRequest dto, MultipartFile imageFile);
}
