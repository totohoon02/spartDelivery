package com.sparta.spartdelivery.controller;

import com.sparta.spartdelivery.dto.GetStoreResponseDto;
import com.sparta.spartdelivery.dto.StoreDetailResponseDto;
import com.sparta.spartdelivery.dto.StoreRequestDto;
import com.sparta.spartdelivery.dto.StoreResponseDto;
import com.sparta.spartdelivery.entity.User;
import com.sparta.spartdelivery.external.security.UserDetailsImpl;
import com.sparta.spartdelivery.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/store")
@Slf4j(topic = "StoreController")
public class StoreController {
    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("")
    public String getStoreList(Model model,
                               @RequestParam(value = "searchValue", required = false) String searchValue,
                               @PageableDefault(size = 100) Pageable pageable) {
        Page<GetStoreResponseDto> storePage;

        if (searchValue != null && !searchValue.isEmpty()) {
            storePage = storeService.getFilteredStoreList(searchValue, pageable);
        } else {
            storePage = storeService.getStoreList(pageable);
        }

        model.addAttribute("storePage", storePage);
        return "store_list";
    }

    @GetMapping("/{storeId}")
    public String getStoreDetail(@PathVariable Integer storeId, Model model) {
        StoreDetailResponseDto storeDetail = storeService.getStoreDetail(storeId);
        model.addAttribute("store", storeDetail);
        log.info("getStoreDetail");
        log.info(storeDetail.toString());

        return "store-detail";
    }

    // store 등록, 수정
    @GetMapping("/create-store")
    @PreAuthorize("hasAuthority('ROLE_BOSS')")
    public String createPage() {
        return "store_new";
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_BOSS')")
    public ResponseEntity<StoreResponseDto> createStore(@RequestBody StoreRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return storeService.createStore(requestDto, user);
    }

    @GetMapping("/update-store")
    @PreAuthorize("hasAuthority('ROLE_BOSS')")
    public String updatePage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StoreDetailResponseDto storeDetailResponseDto = storeService.getStoreDetail(userDetails.getUser().getStoreId());
        model.addAttribute("storeData", storeDetailResponseDto);
        return "store_update";
    }

    @PutMapping("/{storeId}")
    @PreAuthorize("hasAuthority('ROLE_BOSS')")
    public ResponseEntity<StoreResponseDto> updateStore(@PathVariable("storeId") Integer storeId,
            @RequestBody StoreRequestDto requestDto) {
        return storeService.updateStore(storeId, requestDto);
    }
    @DeleteMapping("/{storeId}")
    @PreAuthorize("hasAuthority('ROLE_BOSS')")
    public ResponseEntity<StoreResponseDto> deleteStore(@PathVariable("storeId") Integer storeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return storeService.deleteStore(storeId, user.getUserId());
    }
}
