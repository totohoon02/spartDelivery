package com.sparta.spartdelivery.repository;

import com.sparta.spartdelivery.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Integer>, StoreRepositoryCustom{


}
