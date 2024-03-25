package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.dto.SocialAuthResponseDto;
import com.sparta.spartdelivery.dto.SocialUserResponseDto;
import com.sparta.spartdelivery.enums.UserType;
import org.springframework.stereotype.Service;

@Service
public interface SocialLoginService {
    UserType getServiceName();
    SocialAuthResponseDto getAccessToken(String authorizationCode);
    SocialUserResponseDto getUserInfo(String accessToken);
}