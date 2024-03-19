package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.dto.ReviewResponseDto;
import com.sparta.spartdelivery.dto.ReviewSubmissionDto;
import com.sparta.spartdelivery.entity.Review;
import com.sparta.spartdelivery.entity.Store;
import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.repository.ReviewRepository;
import com.sparta.spartdelivery.repository.StoreRepository;
import com.sparta.spartdelivery.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private ReviewRepository reviewRepository;
    private UserRepository userRepository;
    private StoreRepository storeRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, StoreRepository storeRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
    }

    @Transactional
    public ReviewResponseDto saveReview(Long storeId, ReviewSubmissionDto reviewDto) {
        User user = userRepository.findById(reviewDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("상점을 찾을 수 없습니다."));

        Review review = new Review();
        review.setUser(user);
        review.setStore(store);
        review.setComment(reviewDto.getComment());
        review.setRating(reviewDto.getRating());

        Review savedReview = reviewRepository.save(review);
        return entityToDto(savedReview);
    }

    public List<ReviewResponseDto> getAllReviews(Long storeId) {
        List<Review> reviews = reviewRepository.findByStoreId(storeId);
        return reviews.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, ReviewSubmissionDto reviewDto) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
        review.setComment(reviewDto.getComment());
        review.setRating(reviewDto.getRating());
        Review updatedReview = reviewRepository.save(review);

        return entityToDto(updatedReview);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    private ReviewResponseDto entityToDto(Review review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setReviewId(review.getId());
        dto.setUserName(review.getUser().getUserName());
        dto.setComment(review.getComment());
        dto.setRating(review.getRating());
        dto.setStoreId(review.getStore().getId());

        return dto;
    }
}
