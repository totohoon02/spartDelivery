package com.sparta.spartdelivery.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class RestaurantsController {

    @Getter
    public static class StoreInfo {
        private final String storeName;
        private final String storeImageUrl;
        private final String category;

        public StoreInfo(String storeName, String storeImageUrl, String category) {
            this.storeName = storeName;
            this.storeImageUrl = storeImageUrl;
            this.category = category;
        }
    }
    @Getter
    @Setter
    public static class OrderInfo {
        private Integer orderId;
        private String phoneNumber;
        private Integer totalPrice;
        private String address;
        private LocalDateTime orderedAt;
        private List<OrderDetailInfo> orderDetailInfos;
        private String orderStatus;

        public OrderInfo(Integer orderId,String phoneNumber, Integer totalPrice, String address, LocalDateTime orderedAt, List<OrderDetailInfo>  orderDetailInfos, String orderStatus) {
            this.orderId = orderId;
            this.phoneNumber = phoneNumber;
            this.totalPrice = totalPrice;
            this.address = address;
            this.orderedAt = orderedAt;
            this.orderDetailInfos = orderDetailInfos;
            this.orderStatus = orderStatus;
        }
    }
    @Getter
    public static class OrderDetailInfo {
        private final String menuName;
        private final String menuImageUrl;
        private final Integer price;
        private final Byte quantity;
        public OrderDetailInfo(String menuName, String menuImageUrl, Integer price, Byte quantity) {
            this.menuName = menuName;
            this.menuImageUrl = menuImageUrl;
            this.price = price;
            this.quantity = quantity;
        }
    }

    @Getter
    public static class MenuInfo {
        private final String menuName;
        private final Integer price;
        private final String menuDescription;
        private final String menuImageUrl;
        private transient final String menuJson;

        public MenuInfo(String menuName, Integer price, String menuDescription, String menuImageUrl) {
            this.menuName = menuName;
            this.price = price;
            this.menuDescription = menuDescription;
            this.menuImageUrl = menuImageUrl;
            this.menuJson = convertToJson();
        }

        private String convertToJson() {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return "{}";
            }
        }
    }

    private List<StoreInfo> storeInfos = new ArrayList<>();
    private List<OrderInfo> orderInfos = new ArrayList<>();

    private List<MenuInfo> menuInfos = new ArrayList<>();
    public RestaurantsController() {
        initializeDemoData();
    }

    private void initializeDemoData() {
        // 데모 데이터 추가
        storeInfos.add(new StoreInfo("상호명1", "/images/burger-image.jpeg", "중식"));
        storeInfos.add(new StoreInfo("상호명2", "/images/burger-image.jpeg", "양식"));
        storeInfos.add(new StoreInfo("상호명3", "/images/burger-image.jpeg", "한식"));
        storeInfos.add(new StoreInfo("상호명4", "/images/burger-image.jpeg", "양식"));

        OrderDetailInfo detail1 = new OrderDetailInfo("불고기 버거", "/images/burger-image.jpeg", 10000, (byte)1);
        OrderDetailInfo detail2 = new OrderDetailInfo("치즈 버거", "/images/burger-image.jpeg", 10000,(byte)1);
        OrderDetailInfo detail3 = new OrderDetailInfo("새우 버거", "/images/burger-image.jpeg", 20000, (byte)1);
        OrderDetailInfo detail4 = new OrderDetailInfo("치킨 버거", "/images/burger-image.jpeg", 30000, (byte)1);
        // 주문 데모 데이터
        orderInfos.add(new OrderInfo(1, "010-0000-0000",25000, "123 Main St, Cityville, 12345", LocalDateTime.now(), Arrays.asList(detail1, detail2), "Delivered"));
        orderInfos.add(new OrderInfo(2, "010-0000-0000",30000, "456 Park Ave, Townburg, 67890", LocalDateTime.now(), Arrays.asList(detail3),"Ordered"));
        orderInfos.add(new OrderInfo(3, "010-0000-0000",15000, "789 Oak St, Villageville, 13579", LocalDateTime.now(), Arrays.asList(detail4, detail1, detail3),"Ordered"));
        orderInfos.add(new OrderInfo(4, "010-0000-0000",22000, "321 Pine St, Hamletown, 24680", LocalDateTime.now(), Arrays.asList(detail2, detail4),"Ordered"));
        // 메뉴 데모 데이터 추가
        menuInfos.add(new MenuInfo("불고기 버거", 5000, "맛있는 불고기 버거입니다.", "/images/burger-image.jpeg"));
        menuInfos.add(new MenuInfo("치즈 버거", 5500, "진한 치즈가 들어간 버거입니다.","/images/burger-image.jpeg"));
        menuInfos.add(new MenuInfo("새우 버거", 6000, "신선한 새우가 통째로!", "/images/burger-image.jpeg"));
        menuInfos.add(new MenuInfo("치킨 버거", 6500, "바삭바삭한 치킨 버거입니다.", "/images/burger-image.jpeg"));
    }

    @GetMapping("/stores")
    public String getStore(Model model) {
        model.addAttribute("storeInfos", storeInfos);
        return "store_list";
    }

    @GetMapping("/orders")
    public String get(Model model) {
        model.addAttribute("orderInfos", orderInfos);
        return "order_manage";
    }

    @GetMapping("/orders/{orderId}")
    public String getOrderDetail(@PathVariable Integer orderId, Model model) {

        for (OrderInfo orderInfo : orderInfos) {
            if (orderInfo.getOrderId().equals(orderId)) {
                model.addAttribute("orderInfo", orderInfo);
                return "order_detail";
            }
        }
        return "order_detail";
    }

    @PutMapping("/orders/{orderId}/deliver")
    public ResponseEntity<?> markOrderAsDelivered(@PathVariable Integer orderId) {
        for (OrderInfo orderInfo : orderInfos) {
            if (orderInfo.getOrderId().equals(orderId)) {
                // 이전 orderStatus Ordered 인지 확인
                if (!orderInfo.getOrderStatus().equals("Ordered")){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                };
                // orderStatus Delivered 로 변경
                orderInfo.setOrderStatus("Delivered");
                return ResponseEntity.status(HttpStatus.OK)
                        .build();
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @GetMapping("/menus")
    public String getMenus(Model model) {
        model.addAttribute("menuInfos", menuInfos);
        return "menu_list";
    }

}
