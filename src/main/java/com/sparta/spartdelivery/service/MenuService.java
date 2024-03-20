package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.dto.MenuRequestDto;
import com.sparta.spartdelivery.dto.MenuResponseDto;
import com.sparta.spartdelivery.entity.Menu;
import com.sparta.spartdelivery.entity.Store;
import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.repository.MenuRepository;
import com.sparta.spartdelivery.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository, StoreRepository storeRepository) {
        this.menuRepository = menuRepository;
        this.storeRepository = storeRepository;
    }

    public ResponseEntity<MenuResponseDto> createMenu(MenuRequestDto requestDto) {
        Menu menu = new Menu(requestDto);
        menuRepository.save(menu);
        return new ResponseEntity<>(new MenuResponseDto(menu), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<MenuResponseDto> updateMenu(Long menuId, MenuRequestDto requestDto) {
        Menu menu = menuRepository.findById(menuId).orElseThrow();
        menu.updateMenu(requestDto);
        return new ResponseEntity<>(new MenuResponseDto(menu), HttpStatus.OK);
    }

    public List<MenuResponseDto> getMenus(User user) {
        Store store = storeRepository.findById(user.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스토어입니다."));

        List<MenuResponseDto> menuInfos = menuRepository.findByStore(store).stream().map(MenuResponseDto::new).toList();
        return menuInfos;
    }
}
