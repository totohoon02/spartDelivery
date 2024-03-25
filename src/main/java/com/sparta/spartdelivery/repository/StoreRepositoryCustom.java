package com.sparta.spartdelivery.repository;

import com.sparta.spartdelivery.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreRepositoryCustom {
    Page<Store> findAllBySearchValue(String searchValue, Pageable pageable);
}
