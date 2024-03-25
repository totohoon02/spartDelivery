package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.dto.ReviewResponseDto;
import com.sparta.spartdelivery.dto.ReviewSubmissionDto;
import com.sparta.spartdelivery.entity.Review;
import com.sparta.spartdelivery.entity.Store;
import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.repository.ReviewRepository;
import com.sparta.spartdelivery.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    // 리뷰 저장
    @Test
    void saveReview_Success() {
        // 가짜 객체 생성
        Integer storeId = 1;
        ReviewSubmissionDto reviewDto = new ReviewSubmissionDto("분위기가 좋아요", (byte)5);
        // User와 Store 엔티티는 이미 있다고 가정
        User user = new User();
        Store store = new Store();

        // findById가 호출되었을 때 storeRepository와 reviewRepository의 역할을 모방
        when(storeRepository.findById(storeId)).thenReturn(java.util.Optional.of(store));
        Review mockReview = mock(Review.class); // 모의 리뷰 객체 생성
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArguments()[0]);
        // 테스트 데이터로 saveReview 실행 후 result 받아오기
        ReviewResponseDto result = reviewService.saveReview(storeId, reviewDto, user);

        // 맞게 동작 했는지 확인
        verify(reviewRepository).save(any(Review.class));
    }

   @Test
   void saveReview_StoreNotFound() {
        Integer storeId = 1;
       ReviewSubmissionDto reviewDto = new ReviewSubmissionDto("분위기가 좋아요", (byte)5);
       User user = new User();
       when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

       assertThrows(EntityNotFoundException.class, () -> {
           reviewService.saveReview(storeId, reviewDto, user);
       });

       verify(reviewRepository, never()).save(any(Review.class));
   }

    // 리뷰 조회
    @Test
    void getAllReviews_Success() {
        Integer storeId = 1;
        User user = new User();
        Store store = new Store();
        List<Review> mockReviews = List.of(Review.builder()
                .store(store)
                .comment("분위기가 좋아요")
                .rating((byte)5)
                .user(user)
                .build());


        when(reviewRepository.findByStore_storeId(storeId)).thenReturn(mockReviews);

        List<ReviewResponseDto> result = reviewService.getAllReviews(storeId);

        verify(reviewRepository).findByStore_storeId(storeId);
        assertEquals(mockReviews.size(), result.size());
    }

    @Test
    void getAllReviews_StoreNotFound() {
        Integer storeId = 1;
        when(reviewRepository.findByStore_storeId(storeId)).thenThrow(new EntityNotFoundException("상점을 찾을 수 없습니다."));

        assertThrows(EntityNotFoundException.class, () -> {
            reviewService.getAllReviews(storeId);
        });
    }


    // 리뷰 수정
    @Test
    void updateReview_Success() {
        Integer reviewId = 1;
        User user = new User();
        Store store = new Store();
        ReviewSubmissionDto reviewDto = new ReviewSubmissionDto("(수정됨) 분위기가 괜찮아요", (byte)4);
        Review review = Review.builder()
                .store(store)
                .comment("분위기가 좋아요")
                .rating((byte)5)
                .user(user)
                .build();

        when(reviewRepository.findById(reviewId)).thenReturn(java.util.Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArguments()[0]);

        ReviewResponseDto result = reviewService.updateReview(reviewId, reviewDto);

        verify(reviewRepository).save(review);
        assertEquals(reviewDto.getComment(), review.getComment());
        assertEquals(reviewDto.getRating(), review.getRating());

    }

    @Test
    void updateReview_ReviewNotFound() {
        Integer reviewId = 1;
        ReviewSubmissionDto reviewDto = new ReviewSubmissionDto("(수정됨) 분위기가 괜찮아요", (byte)4);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            reviewService.updateReview(reviewId, reviewDto);
        });

        verify(reviewRepository, never()).save(any(Review.class));
    }

    // 리뷰 삭제
    @Test
    void deleteReview_Success() {
        Integer reviewId = 1;
        reviewService.deleteReview(reviewId);

        verify(reviewRepository).deleteById(reviewId);
    }

}