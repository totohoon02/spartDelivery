package com.sparta.spartdelivery.external.google;


import com.sparta.spartdelivery.dto.SocialAuthResponseDto;
import com.sparta.spartdelivery.dto.SocialUserResponseDto;
import com.sparta.spartdelivery.enums.UserType;
import com.sparta.spartdelivery.service.SocialLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Component
@Qualifier("defaultLoginService")
public class LoginServiceImpl implements SocialLoginService {
    @Override
    public UserType getServiceName() {
        return UserType.NORMAL;
    }

    @Override
    public SocialAuthResponseDto getAccessToken(String authorizationCode) {
        return null;
    }

    @Override
    public SocialUserResponseDto getUserInfo(String accessToken) {
        return null;
    }
}