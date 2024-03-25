package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.dto.ProfileCompletionDto;
import com.sparta.spartdelivery.dto.ProfileResponseDto;
import com.sparta.spartdelivery.dto.SignupRequestDto;
import com.sparta.spartdelivery.dto.UserResponseDto;
import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.enums.UserRoleEnum;
import com.sparta.spartdelivery.external.email.EmailCode;
import com.sparta.spartdelivery.external.email.EmailCodeRepository;
import com.sparta.spartdelivery.external.security.JwtUtil;
import com.sparta.spartdelivery.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailCodeRepository emailCodeRepository;
    private final JwtUtil jwtUtil;



    public void signup(SignupRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String emailCode = requestDto.getEmailCode();

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByEmail(email);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 이메일 인증코드 확인
        Optional<EmailCode> existingRecord = emailCodeRepository.findByEmail(email);
        if (existingRecord.isEmpty()) {
            throw new IllegalArgumentException("이메일 인증코드를 먼저 발급 받아주세요.");
        } else if(!existingRecord.get().getEmailCode().equals(emailCode)) {
            throw new IllegalArgumentException("이메일 인증코드가 일치하지 않습니다.");
        }

        // 사용자 등록
        User user = User.builder()
                .email(email)
                .password(password)
                .userName(requestDto.getUserName())
                .role(requestDto.getRole())
                .point(requestDto.getRole() == UserRoleEnum.CLIENT ? 1000000 : 0) // Set point based on role
                .build();

        userRepository.save(user);
    }

    public ProfileResponseDto getUserProfile(User user) {

        ProfileResponseDto profileDto = new ProfileResponseDto();
        profileDto.setUserId(user.getUserId());
        profileDto.setUserName(user.getUserName());
        profileDto.setPhoneNumber(user.getPhoneNumber());
        profileDto.setAddress(user.getAddress());
        profileDto.setPoint(user.getPoint());
        return profileDto;
    }



    public UserResponseDto getUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저 정보를 찾을 수 없습니다."));

        return UserResponseDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .userEmail(user.getEmail())
                .userName(user.getUserName())
                .build();
    }


    @Transactional
    public void updateUserProfile( ProfileCompletionDto profileCompletionDto) {
        User user = userRepository.findByEmail(profileCompletionDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setRole(UserRoleEnum.valueOf(profileCompletionDto.getRole()));
            user.setAddress(profileCompletionDto.getAddress());
            user.setPhoneNumber(profileCompletionDto.getPhoneNumber());

            userRepository.save(user);

    }

    public void updateAndRegenerateToken(ProfileCompletionDto profileCompletionDto, HttpSession session, HttpServletResponse response) {
        Map<String, String> tokenMap = new HashMap<String, String>();

        User updatedUser = findByEmail(profileCompletionDto.getEmail());

        // Assuming the role conversion and setting is done here or inside updateUserProfile method
        updateUserProfile(profileCompletionDto);

        // Generate new JWT token with updated user details
        String accessToken = jwtUtil.createAccessToken(updatedUser.getEmail(), updatedUser.getRole());
        String refreshToken = jwtUtil.createRefreshToken(updatedUser.getEmail(), updatedUser.getRole());

        // Add the JWT to a cookie and the session
        jwtUtil.addJwtToCookie(accessToken, response,"accessToken"); // For cookie
        jwtUtil.addJwtToCookie(refreshToken, response,"refreshToken"); // For cookie
        session.setAttribute("accessToken", accessToken);
        session.setAttribute("refreshToken", refreshToken);
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }
}