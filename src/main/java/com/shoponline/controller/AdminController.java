package com.shoponline.controller;

import com.shoponline.entity.OrderDetail;
import com.shoponline.entity.User;
import com.shoponline.repository.OrderDetailRepo;
import com.shoponline.repository.UserRepo;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserRepo userRepo;

    private OrderDetailRepo orderDetailRepo;

    private PasswordEncoder passwordEncoder;

    public AdminController(UserRepo userRepo, OrderDetailRepo orderDetailRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.orderDetailRepo = orderDetailRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 查询销售人员列表
     * @param model 模型
     * @return 销售人员列表视图
     */
    @GetMapping("/salesmanList")
    public String showSalesmanList(Model model) {
        List<User> salesmanList = userRepo.find(null, null, User.Role.SALESMAN);
        model.addAttribute("salesmanList", salesmanList);
        return "admin/salesmanList";
    }

    /**
     * 显示新增销售人员视图
     * @return 新增销售人员视图
     */
    @GetMapping("/addSalesman")
    public String toAddSalesman(@ModelAttribute("salesman") User salesman) {
        return "admin/addSalesman";
    }

    /**
     * 新增销售人员信息
     * @param salesman 销售人员信息
     * @param errors 字段错误信息
     * @return 销售人员列表视图
     */
    @PostMapping("/addSalesman")
    public String addSalesman(@ModelAttribute("salesman") @Valid User salesman, Errors errors) {

        if (errors.hasErrors()) return "admin/addSalesman";

        salesman.setPassword(passwordEncoder.encode(salesman.getPassword()));
        salesman.setRole(User.Role.SALESMAN);
        salesman.setCreatedAt(new Date());
        userRepo.insert(salesman);
        return "redirect:/admin/salesmanList";
    }

    /**
     * 删除销售人员账号
     * @param id 销售人员ID
     * @return 销售人员列表视图
     */
    @GetMapping("/removeSalesman/{id}")
    public String removeSalesman(@PathVariable("id") Long id) {
        userRepo.delete(id);
        return "redirect:/admin/salesmanList";
    }

    /**
     * 查询销售人员的业绩
     * @param salesmanId 销售人员ID
     * @param model 模型
     * @return 销售人员业绩视图
     */
    @GetMapping("/salesman/{salesmanId}/sales")
    public String showSales(@PathVariable("salesmanId") Long salesmanId, Model model) {
        List<OrderDetail> orderDetailList = orderDetailRepo.findBySalesmanId(salesmanId);
        model.addAttribute("orderDetailList", orderDetailList);
        return "salesman/orderDetailList";
    }

}
