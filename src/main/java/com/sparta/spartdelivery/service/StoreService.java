package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.dto.GetStoreResponseDto;
import com.sparta.spartdelivery.entity.CategoryEnum;
import com.sparta.spartdelivery.entity.Store;
import com.sparta.spartdelivery.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StoreService {
    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public List<GetStoreResponseDto> getStoreList(){
        return storeRepository.findAll()
                .stream().map(GetStoreResponseDto::new)
                .toList();
    }

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

}
