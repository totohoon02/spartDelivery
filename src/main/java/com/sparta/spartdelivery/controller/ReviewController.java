package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.dto.ReviewResponseDto;
import com.sparta.spartdelivery.dto.ReviewSubmissionDto;
import com.sparta.spartdelivery.dto.StoreDetailResponseDto;
import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.external.security.UserDetailsImpl;
import com.sparta.spartdelivery.service.ReviewService;
import com.sparta.spartdelivery.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/store")
public class ReviewController {

    private final ReviewService reviewService;
    private final StoreService storeService;

    public ReviewController(ReviewService reviewService, StoreService storeService) {
        this.reviewService = reviewService;
        this.storeService = storeService;
    }

    // 리뷰 작성
    @PostMapping("/{storeId}/review")
    public ResponseEntity<ReviewResponseDto> saveReview(@PathVariable Integer storeId,
                                                        @RequestBody ReviewSubmissionDto reviewDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        ReviewResponseDto createdReview = reviewService.saveReview(storeId, reviewDto, user);
        return ResponseEntity.ok(createdReview);
    }

    // 리뷰 작성 페이지
    @GetMapping("/{storeId}/review")
    public String showReviewForm(Model model, Integer storeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("storeId", storeId);
        model.addAttribute("userId", userDetails.getUser().getUserId());
        return "review-write";
    }

    // 상점 전체 리뷰 조회
    @GetMapping("/{storeId}/reviews")
    public String getALlReviews(@PathVariable Integer storeId, Model model) {
        List<ReviewResponseDto> reviews = reviewService.getAllReviews(storeId);
        StoreDetailResponseDto storeDetail = storeService.getStoreDetail(storeId);

        model.addAttribute("reviews", reviews);
        model.addAttribute("store", storeDetail);
        return "reviews";
    }

    // 리뷰 수정
    @PutMapping("/{storeId}/review/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(@PathVariable Integer reviewId,
                                                          @RequestBody ReviewSubmissionDto reviewDto) {
        ReviewResponseDto updatedReview = reviewService.updateReview(reviewId, reviewDto);
        return ResponseEntity.ok(updatedReview);
    }

    // 리뷰 삭제
    @DeleteMapping("/{storeId}/review/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Integer reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().body("리뷰가 성공적으로 삭제되었습니다.");
    }

}
