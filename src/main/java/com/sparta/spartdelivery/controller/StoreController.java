package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.dto.GetStoreResponseDto;
import com.sparta.spartdelivery.entity.CategoryEnum;
import com.sparta.spartdelivery.entity.Menu;
import com.sparta.spartdelivery.entity.Store;
import com.sparta.spartdelivery.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/store")
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

    @GetMapping("/{storeId}")
    public String storeDetail(Model model) {
//        List<Menu> menus = Arrays.asList(
//                new Menu("Classic Cheeseburger", "delicious", 9,
//                        "https://korean.visitseoul.net/data/POST/20170425//201704251508226961")
//        );
//
//        Store store = new Store("Burger Joint", CategoryEnum.KOREAN, "010-1234-5678", "123 Burger Lane, Flavor Town",
//                "https://korean.visitseoul.net/data/POST/20170425//201704251508226961", 95, 15);
//        model.addAttribute("store", store);
//        model.addAttribute("menus", menus);
        return "store-detail";
    }

    // store 등록, 수정
    @GetMapping("/create-store")
    public String createPage() {
        return "store_new";
    }

    @GetMapping("/update-store")
    public String updatePage() {
        return "store_new";
    }

//    @PostMapping()
//    public ResponseEntity<StoreResponseDto> createStore(@RequestBody StoreRequestDto requestDto) {
//        return storeService.createStore(requestDto);
//    }
//
//    @PutMapping("/{storeId}")
//    public ResponseEntity<StoreResponseDto> updateStore(@PathVariable("storeId") Long storeId,
//            @RequestBody StoreRequestDto requestDto) {
//        return storeService.updateStore(storeId, requestDto);
//    }
}
