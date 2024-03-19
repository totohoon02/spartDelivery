package com.sparta.spartdelivery.controller;
import com.sparta.spartdelivery.dto.StoreDetailResponseDto;
import com.sparta.spartdelivery.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreService storeService;
    @GetMapping("/{storeId}")
    public String getStoreDetail(@PathVariable Long storeId, Model model) {
        StoreDetailResponseDto storeDetail = storeService.getStoreDetail(storeId);
        model.addAttribute("store", storeDetail);
        return "store-detail";
    }

}
