package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.dto.MenuResponseDto;
import com.sparta.spartdelivery.dto.ReviewResponseDto;
import com.sparta.spartdelivery.dto.StoreDetailResponseDto;
import com.sparta.spartdelivery.entity.Menu;
import com.sparta.spartdelivery.entity.Review;
import com.sparta.spartdelivery.entity.Store;
import com.sparta.spartdelivery.repository.MenuRepository;
import com.sparta.spartdelivery.repository.ReviewRepository;
import com.sparta.spartdelivery.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreService {
    private StoreRepository storeRepository;
    private MenuRepository menuRepository;
    private ReviewRepository reviewRepository;

    @Autowired

    public StoreService(StoreRepository storeRepository, MenuRepository menuRepository, ReviewRepository reviewRepository) {
        this.storeRepository = storeRepository;
        this.menuRepository = menuRepository;
        this.reviewRepository = reviewRepository;
    }

    public StoreDetailResponseDto getStoreDetail(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + storeId));

        StoreDetailResponseDto responseDto = new StoreDetailResponseDto();
        responseDto.setStoreId(store.getId());
        responseDto.setStoreName(store.getName());
        responseDto.setPhoneNumber(store.getPhoneNumber());
        responseDto.setAddress(store.getAddress());
        responseDto.setDescription(store.getDescription());
        responseDto.setCategory(store.getCategory());
        responseDto.setRating(store.getRating());

        List<Menu> menus = menuRepository.findByStore(store);
        List<MenuResponseDto> menuResponseDtos = new ArrayList<>(menus.size());

        for (Menu menu : menus) {
            MenuResponseDto menuDto = new MenuResponseDto();
            menuDto.setMenuId(menu.getId());
            menuDto.setName(menu.getName());
            menuDto.setPrice(menu.getPrice());
            menuDto.setImageUrl(menu.getImageUrl());
            menuDto.setDescription(menu.getDescription());
            menuResponseDtos.add(menuDto);
        }

        List<Review> reviews = reviewRepository.findByStore(store);
        List<ReviewResponseDto> reviewResponseDto = new ArrayList<>(reviews.size());

        for (Review review : reviews) {
            ReviewResponseDto reviewDto = new ReviewResponseDto();
            if (review.getUser() != null) {
                reviewDto.setUserName(review.getUser().getUserName());
            } else {
                reviewDto.setUserName("Anonymous");
            }

            reviewDto.setRating(review.getRating());
            reviewDto.setComment(review.getComment());
            reviewResponseDto.add(reviewDto);
        }

        responseDto.setMenus(menuResponseDtos);
        responseDto.setReviews(reviewResponseDto);

        return responseDto;
    }
}
