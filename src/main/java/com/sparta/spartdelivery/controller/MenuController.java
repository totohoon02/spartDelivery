package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.dto.MenuRequestDto;
import com.sparta.spartdelivery.dto.MenuResponseDto;
import com.sparta.spartdelivery.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }


    @PostMapping()
    public ResponseEntity<MenuResponseDto> createMenu(@RequestBody MenuRequestDto requestDto){
        return menuService.createMenu(requestDto);
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(@PathVariable("menuId") Long menuId, @RequestBody MenuRequestDto requestDto){
        return menuService.updateMenu(menuId,requestDto);
    }
}
