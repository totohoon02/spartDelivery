package com.sparta.spartdelivery.controller;
import com.sparta.spartdelivery.dto.StoreRequestDto;
import com.sparta.spartdelivery.dto.StoreResponseDto;
import com.sparta.spartdelivery.entity.Store;
import com.sparta.spartdelivery.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.sparta.spartdelivery.entity.Menu;

import java.awt.*;
import java.util.List;
import java.util.Arrays;

@Controller
@RequestMapping("/store")
public class StoreController {
    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/{storeId}")
    public String storeDetail(Model model) {
        List<Menu> menus = Arrays.asList(
                new Menu("1", "Classic Cheeseburger", "delicious", "$8.99", "https://korean.visitseoul.net/data/POST/20170425//201704251508226961"),
                new Menu("2", "Veggie Delight Sandwich", "delicious","$7.50", "https://korean.visitseoul.net/data/POST/20170425//201704251508226961"),
                new Menu("3", "Grilled Salmon", "delicious","$12.99", "https://korean.visitseoul.net/data/POST/20170425//201704251508226961"),
                new Menu("4", "Pepperoni Pizza", "delicious","$9.99", "https://korean.visitseoul.net/data/POST/20170425//201704251508226961")
        );

        Store store = new Store("Burger Joint", "123 Burger Lane, Flavor Town", "010-1234-5678", 4.9);
        model.addAttribute("store", store);
        model.addAttribute("menus", menus);
        return "store-detail";
    }

    // store 등록, 수정
    @GetMapping("/create-store")
    public String createPage(){
        return "store_new";
    }

    @GetMapping("/update-store")
    public String updatePage(){
        return "store_new";
    }

    @PostMapping()
    public ResponseEntity<StoreResponseDto> createStore(@RequestBody StoreRequestDto requestDto){
        return storeService.createStore(requestDto);
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> updateStore(@PathVariable("storeId") Long storeId, @RequestBody StoreRequestDto requestDto){
        return storeService.updateStore(storeId, requestDto);
    }
}
