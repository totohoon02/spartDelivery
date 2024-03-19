package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.dto.ProfileResponseDto;
import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.external.email.EmailCode;
import com.sparta.spartdelivery.external.email.EmailCodeRepository;
import com.sparta.spartdelivery.dto.SignupRequestDto;
import com.sparta.spartdelivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailCodeRepository emailCodeRepository;

    public void signup(SignupRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String emailCode = requestDto.getEmailCode();;

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
        User user = new User(email, password, requestDto.getUserName(), requestDto.getRole());
        userRepository.save(user);
    }

    public ProfileResponseDto getUserProfile(Integer userId) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        ProfileResponseDto profileDto = new ProfileResponseDto();
        profileDto.setUserId(user.getUserId());
        profileDto.setUserName(user.getUserName());
        profileDto.setPhoneNumber(user.getPhoneNumber());
        profileDto.setAddress(user.getAddress());
        profileDto.setPoint(user.getPoint());
        return profileDto;
    }
}