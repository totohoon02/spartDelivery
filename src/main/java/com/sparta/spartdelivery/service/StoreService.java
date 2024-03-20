package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.dto.*;
import com.sparta.spartdelivery.entity.Menu;
import com.sparta.spartdelivery.entity.Review;
import com.sparta.spartdelivery.enums.CategoryEnum;
import com.sparta.spartdelivery.entity.Store;
import com.sparta.spartdelivery.repository.MenuRepository;
import com.sparta.spartdelivery.repository.ReviewRepository;
import com.sparta.spartdelivery.repository.StoreRepository;
import com.sparta.spartdelivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StoreService {
    private StoreRepository storeRepository;
    private MenuRepository menuRepository;
    private ReviewRepository reviewRepository;
    private UserRepository userRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository, MenuRepository menuRepository,
                        ReviewRepository reviewRepository, UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.menuRepository = menuRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }


    // 메인페이지 - 상점 리스트 GET
    public List<GetStoreResponseDto> getStoreList(){
        return storeRepository.findAll()
                .stream().map(GetStoreResponseDto::new)
                .toList();
    }

    // 메인페이지 - 상점 검색 리스트 GET
    public List<GetStoreResponseDto> getFilteredStoreList(String searchValue) {
        List<Store> responseList = new ArrayList<>();
        // 1. 카테고리 명이 동일한 경우
        responseList.addAll(storeRepository.findAllByCategoryEnum(
                CategoryEnum.findByValue(searchValue)));

        // 2. 가게명 LIKE 검색이 되는 경우
        responseList.addAll(storeRepository.findByStoreNameLike("%"+searchValue+"%"));

        // 3. 지역명 LIKE 검색이 되는 경우
        responseList.addAll(storeRepository.findByAddressLike("%"+searchValue+"%"));

    return responseList
                .stream().map(GetStoreResponseDto::new)
                .toList();
    }

    // 상점 상세 페이지 - 상점 정보와 메뉴 리스트 GET
    public StoreDetailResponseDto getStoreDetail(Integer storeId) {
        Store store = storeRepository.findById(Long.valueOf(storeId))
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + storeId));

        StoreDetailResponseDto responseDto = new StoreDetailResponseDto();
        responseDto.setStoreId(store.getStoreId());
        responseDto.setStoreName(store.getStoreName());
        responseDto.setPhoneNumber(store.getPhoneNumber());
        responseDto.setAddress(store.getAddress());
        responseDto.setCategory(store.getCategoryEnum().toString());
        responseDto.setRating(store.getRating());

        List<Menu> menus = menuRepository.findByStore(store);
        List<MenuResponseDto> menuResponseDtos = new ArrayList<>(menus.size());

        for (Menu menu : menus) {
            MenuResponseDto menuDto = new MenuResponseDto();
            menuDto.setMenuId(menu.getMenuId());
            menuDto.setMenuName(menu.getMenuName());
            menuDto.setPrice(menu.getPrice());
            menuDto.setImageUrl(menu.getImageUrl());
            menuDto.setDescription(menu.getDescription());
            menuResponseDtos.add(menuDto);
        }

        List<Review> reviews = reviewRepository.findByStore_storeId(storeId);
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
    public ResponseEntity<StoreResponseDto> createStore(StoreRequestDto requestDto) {
        Store store = new Store(requestDto);
        storeRepository.save(store);
        return new ResponseEntity<>(new StoreResponseDto(store), HttpStatus.OK);
    }
}
