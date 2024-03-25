package com.sparta.spartdelivery.external.google;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sparta.spartdelivery.dto.GoogleLoginResponseDto;
import com.sparta.spartdelivery.dto.GoogleRequestAccessTokenDto;
import com.sparta.spartdelivery.dto.SocialAuthResponseDto;
import com.sparta.spartdelivery.dto.SocialUserResponseDto;
import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.enums.UserRoleEnum;
import com.sparta.spartdelivery.enums.UserType;
import com.sparta.spartdelivery.external.security.JwtUtil;
import com.sparta.spartdelivery.feign.google.GoogleAuthApi;
import com.sparta.spartdelivery.feign.google.GoogleUserApi;
import com.sparta.spartdelivery.repository.UserRepository;
import com.sparta.spartdelivery.service.SocialLoginService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("googleLogin")
public class GoogleLoginServiceImpl implements SocialLoginService {
    private final GoogleAuthApi googleAuthApi;
    private final GoogleUserApi googleUserApi;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    @Value("${social.client.google.rest-api-key}")
    private String googleAppKey;
    @Value("${social.client.google.secret-key}")
    private String googleAppSecret;
    @Value("${social.client.google.redirect-uri}")
    private String googleRedirectUri;
    @Value("${social.client.google.grant_type}")
    private String googleGrantType;
    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }


    @Override
    public UserType getServiceName() {
        return UserType.GOOGLE;
    }

    @Override
    public SocialAuthResponseDto getAccessToken(String authorizationCode) {
        ResponseEntity<?> response = googleAuthApi.getAccessToken(
                GoogleRequestAccessTokenDto.builder()
                        .code(authorizationCode)
                        .client_id(googleAppKey)
                        .clientSecret(googleAppSecret)
                        .redirect_uri(googleRedirectUri)
                        .grant_type(googleGrantType)
                        .build()
        );

        log.info("google auth info");
        log.info(response.toString());

        return new Gson()
                .fromJson(
                        response.getBody().toString(),
                        SocialAuthResponseDto.class
                );
    }

    @Override
    public SocialUserResponseDto getUserInfo(String googleToken) {
        ResponseEntity<?> response = googleUserApi.getUserInfo(googleToken);

        log.info("google user response");
        log.info(response.toString());

        String jsonString = response.getBody().toString();

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
                .create();

        GoogleLoginResponseDto googleLoginResponse = gson.fromJson(jsonString, GoogleLoginResponseDto.class);

        return SocialUserResponseDto.builder()
                .id(googleLoginResponse.getId())
                .email(googleLoginResponse.getEmail())
                .name(googleLoginResponse.getName())
                .build();
    }

    public String exchangeCodeForAccessToken(String authorizationCode) {
        ResponseEntity<String> response = googleAuthApi.getAccessToken(
                GoogleRequestAccessTokenDto.builder()
                        .code(authorizationCode)
                        .client_id(googleAppKey)
                        .clientSecret(googleAppSecret)
                        .redirect_uri(googleRedirectUri)
                        .grant_type(googleGrantType)
                        .build()
        );

        // Assuming the SocialAuthResponseDto class has a field for the access token
        SocialAuthResponseDto authResponse = new Gson().fromJson(response.getBody(), SocialAuthResponseDto.class);
        return authResponse.getAccess_token();
    }

    public User processUserDetails(SocialUserResponseDto userDetails) {
        Optional<User> existingUser = userRepository.findByEmail(userDetails.getEmail());
        if(existingUser.isPresent()) {
            return existingUser.get();
        } else {
            User newUser = User.builder()
                    .email(userDetails.getEmail())
                    .userName(userDetails.getName())
                    .role(UserRoleEnum.CLIENT)
                    .userType(UserType.GOOGLE)
                    .build();
            userRepository.save(newUser);
            return newUser;
        }
    }



}