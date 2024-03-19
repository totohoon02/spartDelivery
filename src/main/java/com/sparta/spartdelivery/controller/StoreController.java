package com.sparta.spartdelivery.controller;
import com.sparta.spartdelivery.dto.GetStoreResponseDto;
import com.sparta.spartdelivery.entity.CategoryEnum;
import com.sparta.spartdelivery.entity.Store;
import com.sparta.spartdelivery.service.StoreService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.sparta.spartdelivery.entity.Menu;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.*;
import java.util.List;
import java.util.Arrays;

@Controller
public class StoreController {
    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/stores")
    public String getStoreList(Model model, @RequestParam(value = "searchValue", required = false) String searchValue) {
        List<GetStoreResponseDto> storeInfos;

        if (searchValue != null && !searchValue.isEmpty()) {
            storeInfos = storeService.getFilteredStoreList(searchValue);
        } else {
            storeInfos = storeService.getStoreList();
        }

        model.addAttribute("storeInfos", storeInfos);
        return "store_list";
    }

    @GetMapping("/store/{storeId}")
    public String storeDetail(Model model) {
        List<Menu> menus = Arrays.asList(
                new Menu("1", "Classic Cheeseburger", "delicious", "$8.99", "https://korean.visitseoul.net/data/POST/20170425//201704251508226961"),
                new Menu("2", "Veggie Delight Sandwich", "delicious","$7.50", "https://korean.visitseoul.net/data/POST/20170425//201704251508226961"),
                new Menu("3", "Grilled Salmon", "delicious","$12.99", "https://korean.visitseoul.net/data/POST/20170425//201704251508226961"),
                new Menu("4", "Pepperoni Pizza", "delicious","$9.99", "https://korean.visitseoul.net/data/POST/20170425//201704251508226961")
        );

        Store store = new Store("Burger Joint", CategoryEnum.KOREAN, "010-1234-5678", "123 Burger Lane, Flavor Town", "https://korean.visitseoul.net/data/POST/20170425//201704251508226961", 95, 15 );
        model.addAttribute("store", store);
        model.addAttribute("menus", menus);
        return "store-detail";
    }

}
