package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.dto.StoreRequestDto;
import com.sparta.spartdelivery.dto.StoreResponseDto;
import com.sparta.spartdelivery.entity.Store;
import com.sparta.spartdelivery.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreService {
    private final StoreRepository storeRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public ResponseEntity<StoreResponseDto> createStore(StoreRequestDto requestDto) {
        Store store = new Store(requestDto);
        storeRepository.save(store);
        return new ResponseEntity<>(new StoreResponseDto(store), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<StoreResponseDto> updateStore(Long storeId, StoreRequestDto requestDto) {
        Store store = storeRepository.findById(storeId).orElseThrow();
        store.updateStore(requestDto);
        return new ResponseEntity<>(new StoreResponseDto(store), HttpStatus.OK);
    }
}
