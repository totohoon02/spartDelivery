package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.dto.ProfileCompletionDto;
import com.sparta.spartdelivery.dto.ProfileResponseDto;
import com.sparta.spartdelivery.dto.SignupRequestDto;
import com.sparta.spartdelivery.dto.SocialUserResponseDto;
import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.external.google.GoogleLoginServiceImpl;
import com.sparta.spartdelivery.external.security.JwtUtil;
import com.sparta.spartdelivery.external.security.UserDetailsImpl;
import com.sparta.spartdelivery.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final GoogleLoginServiceImpl googleLoginService;
    private final JwtUtil jwtUtil;


    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        try {
            userService.signup(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입 실패: " + e.getMessage());
        }
    }

    // 유저 프로
    @GetMapping("/profile")
    public String getUserProfile(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        ProfileResponseDto profileResponseDto = userService.getUserProfile(user);
        model.addAttribute("userProfile", profileResponseDto);
        return "user-profile";
    }


    @GetMapping("/google-login")
    public String googleOAuth2Callback(@RequestParam("code") String code, HttpSession session, HttpServletResponse response) {
        try {
            String accessToken = googleLoginService.exchangeCodeForAccessToken(code);
            SocialUserResponseDto userDetailsDto = googleLoginService.getUserInfo(accessToken);

            // Check if the user exists and has completed their profile
            User user = userService.findByEmail(userDetailsDto.getEmail());

            // If the user doesn't exist or hasn't set up their profile, process their Google details
            if (user == null || user.getRole() == null) {
                user = googleLoginService.processUserDetails(userDetailsDto);
                session.setAttribute("userDetails", userDetailsDto); // Storing the DTO for further needs
                return "redirect:/profile-complete";
            } else {
                // User exists and profile is complete, generate a new token
                String token = jwtUtil.createToken(user.getEmail(), user.getRole());
                jwtUtil.addJwtToCookie(token, response); // For cookie
                session.setAttribute(JwtUtil.AUTHORIZATION_HEADER, token);

                // Directly redirect to the landing page since profile is already complete
                return "redirect:/store";
            }
        } catch (Exception e) {
            return "redirect:/login?error";
        }
    }


    @GetMapping("/profile-complete")
    public String showProfileCompletePage(HttpSession session, Model model) {
        SocialUserResponseDto userDetails = (SocialUserResponseDto) session.getAttribute("userDetails");

        if (userDetails == null) {
            return "redirect:/login?error=missingUserDetails";
        }

        // Add userDetails to model to display in view
        model.addAttribute("userDetails", userDetails);
        return "profile-complete";
    }



    @PostMapping("/profile-complete")
    public String completeProfile(@RequestBody ProfileCompletionDto profileCompletionDto, HttpSession session, HttpServletResponse response) {
        try {
            // Process and save the profile data
            userService.updateUserProfile(profileCompletionDto);

            String newToken = userService.updateAndRegenerateToken(profileCompletionDto);

            // Update JWT in cookie or session
            jwtUtil.addJwtToCookie(newToken, response); // For cookie
            session.setAttribute(JwtUtil.AUTHORIZATION_HEADER, newToken);
            return "redirect:/store";
        } catch (Exception e) {
            return "redirect:/profile-complete";
        }
    }



    }
