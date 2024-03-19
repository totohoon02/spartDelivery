package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.dto.ProfileResponseDto;
import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ProfileResponseDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        ProfileResponseDto profileDto = new ProfileResponseDto();
        profileDto.setId(user.getId());
        profileDto.setUserName(user.getUserName());
        profileDto.setPhoneNumber(user.getPhoneNumber());
        profileDto.setAddress(user.getAddress());
        profileDto.setPoint(user.getPoint());
        return profileDto;
    }
}
