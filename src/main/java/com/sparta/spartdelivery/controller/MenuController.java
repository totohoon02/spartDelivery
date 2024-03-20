package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.dto.MenuRequestDto;
import com.sparta.spartdelivery.dto.MenuResponseDto;
import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.external.security.UserDetailsImpl;
import com.sparta.spartdelivery.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }


    @GetMapping("")
    public String getMenus(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        List<MenuResponseDto> menuInfos = menuService.getMenus(user);
        model.addAttribute("menuInfos", menuInfos);
        return "menu_list";
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
