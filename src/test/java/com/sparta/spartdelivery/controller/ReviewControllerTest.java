package com.sparta.spartdelivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spartdelivery.dto.ReviewResponseDto;
import com.sparta.spartdelivery.dto.ReviewSubmissionDto;
import com.sparta.spartdelivery.dto.StoreDetailResponseDto;
import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.enums.UserRoleEnum;
import com.sparta.spartdelivery.external.security.UserDetailsImpl;
import com.sparta.spartdelivery.repository.StoreRepository;
import com.sparta.spartdelivery.service.ReviewService;
import com.sparta.spartdelivery.service.StoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private UserDetailsService userDetailsService;
    @Mock
    private StoreRepository storeRepository;

    @MockBean
    private StoreService storeService;


    @Test
    @WithMockUser(username = "user", roles = {"CLIENT"})
    void testSaveReviewWithMockMvc() throws Exception {
        User mockUser = new User();
        mockUser.setRole(UserRoleEnum.CLIENT);
        UserDetailsImpl userDetails = new UserDetailsImpl(mockUser);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Integer storeId = 1;
        ReviewSubmissionDto reviewDto = new ReviewSubmissionDto("훌륭한 식사였어요!", (byte)5);
        ReviewResponseDto expectedResponse = ReviewResponseDto.builder()
                .reviewId(1)
                .userName("userName")
                .comment(reviewDto.getComment())
                .rating(reviewDto.getRating())
                .storeId(storeId)
                .build();
        when(reviewService.saveReview(anyInt(), any(ReviewSubmissionDto.class), any(User.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(post("/store/{storeId}/review", storeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content("{\"comment\":\"훌륭한 식사였어요!\", \"rating\":5}"))
                .andExpect(status().isOk());

        SecurityContextHolder.clearContext();
    }

    @Test
    @WithMockUser(username = "user", roles = {"CLIENT"})
    void getAllReviews_Success() throws Exception {
        Integer storeId = 1;
        List<ReviewResponseDto> mockReviews = List.of(ReviewResponseDto.builder()
                .reviewId(1)
                .userName("userName")
                .comment("맛있어요")
                .rating((byte)5)
                .storeId(storeId)
                .build());
        StoreDetailResponseDto mockStoreDetail = new StoreDetailResponseDto();

        when(reviewService.getAllReviews(storeId)).thenReturn(mockReviews);
        when(storeService.getStoreDetail(storeId)).thenReturn(mockStoreDetail);

        mockMvc.perform(get("/store/{storeId}/reviews", storeId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("reviews"))
                .andExpect(model().attributeExists("store"))
                .andExpect(view().name("reviews"));

        verify(reviewService).getAllReviews(storeId);
        verify(storeService).getStoreDetail(storeId);
    }


    @Test
    @WithMockUser(username = "user", roles = {"CLIENT"})
    void updateReview() throws Exception {
        Integer reviewId = 1;
        ReviewSubmissionDto reviewDto = new ReviewSubmissionDto("Updated comment", (byte)4);

        ReviewResponseDto expectedResponse = ReviewResponseDto.builder()
                .reviewId(reviewId)
                .userName("TestUser")
                .comment(reviewDto.getComment())
                .rating(reviewDto.getRating())
                .storeId(1)
                .build();

        when(reviewService.updateReview(eq(reviewId), eq(reviewDto))).thenReturn(expectedResponse);

        mockMvc.perform(put("/store/{storeId}/review/{reviewId}", 1, reviewId)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(reviewDto))
                        .with(csrf()))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "user", roles = {"CLIENT"})
    void deleteReview_Success() throws Exception {
        Integer reviewId = 1;
        Integer storeId = 1;

        // Mock the behavior of the reviewService when deleteReview is called
        doNothing().when(reviewService).deleteReview(reviewId);

        // Perform a DELETE request and verify the outcome
        mockMvc.perform(delete("/store/{storeId}/review/{reviewId}", storeId, reviewId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("리뷰가 성공적으로 삭제되었습니다."));

        // Verify that the reviewService.deleteReview method was called with the correct parameters
        verify(reviewService).deleteReview(reviewId);
    }
}