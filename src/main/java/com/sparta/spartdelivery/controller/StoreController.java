package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.dto.GetStoreResponseDto;
import com.sparta.spartdelivery.dto.StoreRequestDto;
import com.sparta.spartdelivery.dto.StoreResponseDto;
import com.sparta.spartdelivery.enums.CategoryEnum;
import com.sparta.spartdelivery.entity.Menu;
import com.sparta.spartdelivery.entity.Store;
import com.sparta.spartdelivery.dto.StoreDetailResponseDto;
import com.sparta.spartdelivery.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String getStoreDetail(@PathVariable Integer storeId, Model model) {
        StoreDetailResponseDto storeDetail = storeService.getStoreDetail(storeId);
        model.addAttribute("store", storeDetail);
        return "store-detail";
    }

    // store 등록, 수정
    @GetMapping("/create-store")
    @PreAuthorize("hasAuthority('ROLE_BOSS')")
    public String createPage() {
        return "store_new";
    }

    @PostMapping()
    public ResponseEntity<StoreResponseDto> createStore(@RequestBody StoreRequestDto requestDto) {
        return storeService.createStore(requestDto);
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
