package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.dto.*;
import com.sparta.spartdelivery.entity.Menu;
import com.sparta.spartdelivery.entity.Review;
import com.sparta.spartdelivery.entity.Store;
import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.repository.MenuRepository;
import com.sparta.spartdelivery.repository.ReviewRepository;
import com.sparta.spartdelivery.repository.StoreRepository;
import com.sparta.spartdelivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository, MenuRepository menuRepository,
                        ReviewRepository reviewRepository, UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.menuRepository = menuRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }


    // 메인페이지 - 상점 리스트 GET
    public Page<GetStoreResponseDto> getStoreList(Pageable pageable){
        Page<Store> storePage = storeRepository.findAll(pageable);
        return storePage.map(GetStoreResponseDto::new);
    }

    // 메인페이지 - 상점 검색 리스트 GET
    public Page<GetStoreResponseDto> getFilteredStoreList(String searchValue, Pageable pageable) {
        Page<Store> storePage = storeRepository.findAllBySearchValue(searchValue, pageable);
        return storePage.map(GetStoreResponseDto::new);
    }

    // 상점 상세 페이지 - 상점 정보와 메뉴 리스트 GET
    public StoreDetailResponseDto getStoreDetail(Integer storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + storeId));

        StoreDetailResponseDto responseDto = new StoreDetailResponseDto();
        responseDto.setStoreId(store.getStoreId());
        responseDto.setStoreName(store.getStoreName());
        responseDto.setPhoneNumber(store.getPhoneNumber());
        responseDto.setAddress(store.getAddress());
        responseDto.setCategory(store.getCategoryEnum().toString());
        responseDto.setRating(store.getRating());
        responseDto.setImage(store.getImageUrl());

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
        responseDto.setMenus(menuResponseDtos);

        List<Review> reviews = reviewRepository.findByStore_storeId(storeId);
        List<ReviewResponseDto> reviewResponseDtos = reviews.stream()
                .map(ReviewResponseDto::convertReviewToDto)
                .collect(Collectors.toList());

        responseDto.setReviews(reviewResponseDtos);

        return responseDto;
    }
    @Transactional
    public ResponseEntity<StoreResponseDto> createStore(StoreRequestDto requestDto, User user) {

        Store store = new Store(requestDto);
        storeRepository.save(store);
        User user1 = userRepository.findById(user.getUserId()).orElseThrow();
        user1.updateStoreId(store.getStoreId());

        return new ResponseEntity<>(new StoreResponseDto(store), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<StoreResponseDto> updateStore(Integer storeId, StoreRequestDto requestDto) {
        Optional<Store> optionalStore = storeRepository.findById(storeId);
        if(optionalStore.isPresent()){
            Store store = optionalStore.get();
            store.updateStore(requestDto);
            return new ResponseEntity<>(new StoreResponseDto(store), HttpStatus.OK);
        }else{
            throw new NullPointerException("스토어가 존재하지 않습니다.");
        }
    }

    @Transactional
    public ResponseEntity<StoreResponseDto> deleteStore(Integer storeId, Integer userId) {
        Optional<Store> optionalStore = storeRepository.findById(storeId);
        if(optionalStore.isPresent()){
            Store store = optionalStore.get();
            storeRepository.delete(store);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            user.updateStoreId(null);
            return new ResponseEntity<>(new StoreResponseDto(store), HttpStatus.OK);
        }else{
            throw new NullPointerException("스토어가 존재하지 않습니다.");
        }
    }

}
