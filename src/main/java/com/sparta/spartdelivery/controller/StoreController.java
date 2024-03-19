package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.dto.GetStoreResponseDto;
import com.sparta.spartdelivery.service.StoreService;
import com.sparta.spartdelivery.dto.StoreDetailResponseDto;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;


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
    public String createPage() {
        return "store_new";
    }

    @GetMapping("/update-store")
    public String updatePage() {
        return "store_new";
    }

}
