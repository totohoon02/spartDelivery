package com.sparta.spartdelivery.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.sparta.spartdelivery.entity.Menu;
import java.awt.*;
import java.util.List;
import java.util.Arrays;

@Controller
public class StoreController {

    @GetMapping("/store/{storeId}")
    public String storeDetail(Model model) {
        List<Menu> menus = Arrays.asList(
                new Menu("1", "Classic Cheeseburger", "$8.99", "https://korean.visitseoul.net/data/POST/20170425//201704251508226961"),
                new Menu("2", "Veggie Delight Sandwich", "$7.50", "https://korean.visitseoul.net/data/POST/20170425//201704251508226961"),
                new Menu("3", "Grilled Salmon", "$12.99", "https://korean.visitseoul.net/data/POST/20170425//201704251508226961"),
                new Menu("4", "Pepperoni Pizza", "$9.99", "https://korean.visitseoul.net/data/POST/20170425//201704251508226961")
        );

        model.addAttribute("menus", menus);
        return "store-detail"; // Name of your HTML template (without .html extension)
    }

}
