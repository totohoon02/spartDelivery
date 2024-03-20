package com.sparta.spartdelivery.repository;

import com.sparta.spartdelivery.enums.CategoryEnum;
import com.sparta.spartdelivery.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Integer> {
    List<Store> findAllByCategoryEnum(CategoryEnum categoryEnum);

    @Query("SELECT s FROM Store s WHERE s.storeName LIKE :searchValue")
    List<Store> findByStoreNameLike(@Param("searchValue") String searchValue);

    @Query("SELECT s FROM Store s WHERE s.address LIKE :searchValue")
    List<Store> findByAddressLike(@Param("searchValue") String searchValue);

}
