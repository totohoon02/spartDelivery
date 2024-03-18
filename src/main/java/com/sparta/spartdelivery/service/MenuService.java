package com.sparta.spartdelivery.service;

import com.sparta.spartdelivery.dto.MenuRequestDto;
import com.sparta.spartdelivery.dto.MenuResponseDto;
import com.sparta.spartdelivery.entity.Menu;
import com.sparta.spartdelivery.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
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
}
