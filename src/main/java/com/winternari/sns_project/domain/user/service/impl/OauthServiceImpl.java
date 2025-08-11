package com.winternari.sns_project.domain.user.service.impl;

import com.winternari.sns_project.domain.user.dto.request.LoginRequest;
import com.winternari.sns_project.domain.user.dto.request.SignRequest;
import com.winternari.sns_project.domain.user.dto.response.LoginDataResponse;
import com.winternari.sns_project.domain.user.dto.response.LoginResponse;
import com.winternari.sns_project.domain.user.dto.response.RefreshToken;
import com.winternari.sns_project.domain.user.entity.UserEntity;
import com.winternari.sns_project.domain.user.entity.User_Profile;
import com.winternari.sns_project.domain.user.repository.OauthRepository;
import com.winternari.sns_project.domain.user.repository.RefreshTokenRepository;
import com.winternari.sns_project.domain.user.repository.UserProfileRepository;
import com.winternari.sns_project.domain.user.service.OauthService;
import com.winternari.sns_project.global.security.cookie.CookieUtil;
import com.winternari.sns_project.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OauthServiceImpl implements OauthService {

    private static final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24; // 1일
    private final OauthRepository oauthRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder; // 추가
    @Autowired
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public boolean signUp(SignRequest signRequest) throws Exception {
        // 이메일 형식 검증 먼저
        if (!signRequest.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다.");
        }

        // 비밀번호 유효성 검증
        if (!isValidPassword(signRequest.getPassword())) {
            throw new IllegalArgumentException("비밀번호는 대문자, 소문자, 숫자, 특수문자를 모두 포함해야 합니다.");
        }

        // 중복 이메일 체크
        Optional<UserEntity> existingUser = oauthRepository.findByEmail(signRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        try {
            // 프로필 생성 (필요하면 SignRequest에서 프로필 관련 데이터도 받아야 합니다)
            User_Profile profile = User_Profile.builder()
                    .nickname(signRequest.getNickname())  // SignRequest에 nickname 필드 있다고 가정
                    .phone(signRequest.getPhone())
                    .bio(signRequest.getBio())
                    .location(signRequest.getLocation())
                    .isVerified(false)
                    .isBanned(false)
                    .build();

            // 유저 생성 및 프로필 연동
            UserEntity user = UserEntity.builder()
                    .email(signRequest.getEmail())
                    .password(passwordEncoder.encode(signRequest.getPassword()))
                    .profile(profile)
                    .build();

            // 저장
            oauthRepository.save(user);
            userProfileRepository.save(profile);
        } catch (Exception e) {
            System.err.println("회원가입 실패: " + e.getMessage());
            throw new Exception("회원가입 중 오류가 발생했습니다.");
        }

        return true;
    }

    public boolean isValidPassword(String password) {
        // 길이도 조건 8자 이상 20이하 추가
        // 특수 문자 조건
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/]).{8,20}$";
        return password != null && password.matches(pattern);
    }

    // 로그인
    @Override
    public LoginResponse signIn(LoginRequest loginRequest, HttpServletResponse response) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        if (!authentication.isAuthenticated()) {
            throw new IllegalArgumentException("인증이 완료되지 않았습니다.");
        }

        UserEntity user = oauthRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자가 존재하지 않습니다."));

        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        // RefreshToken DB에 저장 (신규 or 갱신)
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);

        if (existingToken.isPresent()) {
            RefreshToken tokenEntity = existingToken.get();
            tokenEntity.setToken(refreshToken);
            tokenEntity.setExpiryDate(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_TIME));
            refreshTokenRepository.save(tokenEntity);
        } else {
            RefreshToken tokenEntity = RefreshToken.builder()
                    .user(user)
                    .token(refreshToken)
                    .expiryDate(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_TIME))
                    .build();
            refreshTokenRepository.save(tokenEntity);
        }

        // 쿠키에 저장
        CookieUtil.addCookie(response, "refreshToken", refreshToken, 60 * 60 * 24, true, true, "/", "Strict");

        return new LoginResponse("OK", 200, new LoginDataResponse(accessToken, null));
    }

    // 토큰 재발급
    @Override
    @Transactional
    public LoginResponse refreshToken(String refreshToken, HttpServletResponse response) throws Exception {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Refresh token이 유효하지 않습니다.");
        }

        String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        UUID uuIdUUID = UUID.fromString(userId);

        UserEntity user = oauthRepository.findById(uuIdUUID)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        RefreshToken savedTokenEntity = refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("저장된 RefreshToken이 없습니다."));

        // DB 만료일 검증 추가
        if (savedTokenEntity.getExpiryDate().before(new Date())) {
            refreshTokenRepository.deleteByUserId(uuIdUUID); // 옵션: 만료시 삭제
            throw new IllegalArgumentException("Refresh Token 만료됨");
        }

        // 동일 토큰 불일치 시 재사용 공격 방지
        if (!refreshToken.equals(savedTokenEntity.getToken())) {
            refreshTokenRepository.deleteByUserId(uuIdUUID); // 재사용 차단: 세션 만료 처리
            throw new IllegalArgumentException("토큰 불일치, 재로그인 필요");
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(user);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user);

        savedTokenEntity.setToken(newRefreshToken);
        savedTokenEntity.setExpiryDate(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_TIME));
        refreshTokenRepository.save(savedTokenEntity);

        CookieUtil.addCookie(response, "refreshToken", newRefreshToken, 60 * 60 * 24, true, true, "/", "Strict");

        return new LoginResponse("OK", 200, new LoginDataResponse(newAccessToken, null));
    }
}
