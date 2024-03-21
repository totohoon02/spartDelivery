package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.dto.MenuRequestDto;
import com.sparta.spartdelivery.dto.MenuResponseDto;
import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.external.security.UserDetailsImpl;
import com.sparta.spartdelivery.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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


    @PreAuthorize("hasAuthority('ROLE_BOSS')")
    @GetMapping()
    public String getMenus(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        List<MenuResponseDto> menuInfos = menuService.getMenus(user);
        model.addAttribute("menuInfos", menuInfos);
        return "menu_list";
    }

    @PreAuthorize("hasAuthority('ROLE_BOSS')")
    @GetMapping("/create-menu")
    public String getCreateMenu(){
        return "menu_new";
    }

    @PostMapping()
    public ResponseEntity<MenuResponseDto> createMenu(@RequestBody MenuRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return menuService.createMenu(requestDto, user.getStoreId());
    }

    @PreAuthorize("hasAuthority('ROLE_BOSS')")
    @GetMapping("/update-menu/{menuId}")
    public String getUpdateMenu(@PathVariable Integer menuId, Model model){
        MenuResponseDto menu = menuService.getMenu(menuId);
        model.addAttribute("menu", menu);
        return "menu_update";
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(@PathVariable("menuId") Integer menuId, @RequestBody MenuRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return menuService.updateMenu(menuId,requestDto, user.getStoreId());
    }
}
