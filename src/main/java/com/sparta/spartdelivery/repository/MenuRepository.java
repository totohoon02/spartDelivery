package com.sparta.spartdelivery.repository;

import com.sparta.spartdelivery.entity.Menu;
import com.sparta.spartdelivery.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByStore(Store store);

}
