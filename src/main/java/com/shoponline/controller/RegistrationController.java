package com.shoponline.controller;

import com.shoponline.entity.User;
import com.shoponline.repository.UserRepo;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 显示用户注册视图
     * @param model 模型
     * @return 用户注册视图
     */
    @GetMapping
    public String toRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * 执行注册操作
     * @param user 用户信息
     * @param errors 字段错误信息
     * @return 登录视图
     */
    @PostMapping
    public String register(@Valid User user, Errors errors) {

        if (errors.hasErrors()) return "register";

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.CUSTOMER);
        user.setCreatedAt(new Date());
        userRepo.insert(user);
        return "redirect:/login";
    }

}
