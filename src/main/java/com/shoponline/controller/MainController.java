package com.shoponline.controller;

import com.shoponline.entity.Item;
import com.shoponline.entity.User;
import com.shoponline.repository.ItemRepo;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class MainController {

    private ItemRepo itemRepo;

    public MainController(ItemRepo itemRepo) {
        this.itemRepo = itemRepo;
    }

    /**
     * 显示网站首页
     * @return 根据用户角色重定向到不同的路由
     */
    @GetMapping("/")
    public String index() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            User user = (User) authentication.getPrincipal();
            if (user.getRole() == User.Role.ADMIN)
                return "redirect:/admin/salesmanList";
            if (user.getRole() == User.Role.SALESMAN)
                return "redirect:/salesman/itemList";
        }
        return "redirect:/itemList";
    }

    /**
     * 显示商品列表
     * @param query 查询字符串
     * @param model 模型
     * @return 商品列表视图
     */
    @GetMapping("/itemList")
    public String showItemList(String query, Model model) {
        List<Item> itemList = itemRepo.findByNameLike(query);
        model.addAttribute("itemList", itemList);
        return "itemList";
    }

}
