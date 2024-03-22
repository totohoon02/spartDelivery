package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.dto.ReviewResponseDto;
import com.sparta.spartdelivery.dto.ReviewSubmissionDto;
import com.sparta.spartdelivery.entity.Review;
import com.sparta.spartdelivery.entity.Store;
import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.repository.ReviewRepository;
import com.sparta.spartdelivery.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    public ReviewService(ReviewRepository reviewRepository, StoreRepository storeRepository) {
        this.reviewRepository = reviewRepository;
        this.storeRepository = storeRepository;
    }

    // 리뷰 저장
    @Transactional
    public ReviewResponseDto saveReview(Integer storeId, ReviewSubmissionDto reviewDto, User user) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("상점을 찾을 수 없습니다."));

        Review review = Review.builder()
                .store(store)
                .comment(reviewDto.getComment())
                .rating(reviewDto.getRating())
                .user(user)
                .build();

        store.addRating(review.getRating());
        storeRepository.save(store);

        Review savedReview = reviewRepository.save(review);
        return ReviewResponseDto.convertReviewToDto(savedReview);
    }

    // 상점 리뷰 전체 조회
    public List<ReviewResponseDto> getAllReviews(Integer storeId) {
        List<Review> reviews = reviewRepository.findByStore_storeId(storeId);
        return reviews.stream().map(ReviewResponseDto::convertReviewToDto).collect(Collectors.toList());
    }

    // 리뷰 수정
    @Transactional
    public ReviewResponseDto updateReview(Integer reviewId, ReviewSubmissionDto reviewDto) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
        review.updateComment(reviewDto.getComment());
        review.updateRating(reviewDto.getRating());

        return ReviewResponseDto.convertReviewToDto(review);
    }

    @Transactional
    public void deleteReview(Integer reviewId) {
        reviewRepository.deleteById(reviewId);
    }

}
