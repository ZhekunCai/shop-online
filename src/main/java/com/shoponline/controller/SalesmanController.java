package com.shoponline.controller;

import com.shoponline.entity.Item;
import com.shoponline.entity.OrderDetail;
import com.shoponline.entity.User;
import com.shoponline.repository.ItemRepo;
import com.shoponline.repository.OrderDetailRepo;
import jakarta.validation.Valid;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/salesman")
public class SalesmanController {

    private ItemRepo itemRepo;

    private OrderDetailRepo orderDetailRepo;

    /**
     * 图片上传目录
     */
    private final String UPLOAD_DIR;

    public SalesmanController(ItemRepo itemRepo, OrderDetailRepo orderDetailRepo, Environment env) {
        this.itemRepo = itemRepo;
        this.orderDetailRepo = orderDetailRepo;
        // 获取第一个静态资源路径下的“/upload”目录作为上传目录
        this.UPLOAD_DIR = env.getProperty("spring.web.resources.static-locations")
                .split(", ")[0] + "/upload/";
    }

    /**
     * 展示负责的商品列表
     * @param salesman 销售人员
     * @param model 模型
     * @return 商品列表视图
     */
    @GetMapping("/itemList")
    public String showItemList(@AuthenticationPrincipal User salesman, Model model) {
        List<Item> itemList = itemRepo.findBySalesmanId(salesman.getId());
        model.addAttribute("itemList", itemList);
        return "salesman/itemList";
    }

    /**
     * 返回新增商品视图
     * @return 新增商品视图
     */
    @GetMapping("/addItem")
    public String toAddItem(@ModelAttribute Item item) {
        return "salesman/addItem";
    }

    /**
     * 新增商品
     * @param item 商品信息
     * @param mpFile 商品图片
     * @param errors 字段错误
     * @param salesman 销售人员
     * @return 商品列表视图
     * @throws URISyntaxException URI语法错误
     * @throws IOException IO异常
     */
    @PostMapping("/addItem")
    public String addItem(@Valid Item item, Errors errors, MultipartFile mpFile,
                          @AuthenticationPrincipal User salesman) throws URISyntaxException, IOException {

        if (errors.hasErrors()) return "salesman/addItem";

        if (mpFile != null) {
            // 将图片重命名后保存至文件系统的UPLOAD_DIR目录下
            String mpFilename = mpFile.getOriginalFilename();
            int suffixIndex = mpFilename.lastIndexOf('.');
            String filename = UUID.randomUUID().toString() + mpFilename.substring(suffixIndex);
            File file = new File(new URI(UPLOAD_DIR + filename));
            mpFile.transferTo(file);
            item.setImage(filename);
        }
        item.setCreatedAt(new Date());
        item.setModifiedAt(item.getCreatedAt());
        item.setSalesman(salesman);

        itemRepo.insert(item);
        return "redirect:/salesman/itemList";
    }

    /**
     * 显示修改商品信息视图
     * @param id 商品ID
     * @param model 模型
     * @param salesman 销售人员
     * @return 修改商品信息视图
     */
    @GetMapping("/updateItem/{id}")
    public String toUpdateItem(@PathVariable("id") Long id, Model model,
                               @AuthenticationPrincipal User salesman) {
        Item item = itemRepo.findById(id);
        if (!item.getSalesman().getId().equals(salesman.getId())) {
            return "redirect:/salesman/itemList";
        }
        model.addAttribute("item", item);
        return "salesman/updateItem";
    }

    /**
     * 修改商品信息
     * @param item 商品信息
     * @param model 模型
     * @param salesman 销售人员
     * @return 商品列表视图
     */
    @PostMapping("/updateItem")
    public String updateItem(@Valid Item item, Model model,
                             @AuthenticationPrincipal User salesman) {
        if (item.getSalesman().getId().equals(salesman.getId())) {
            item.setModifiedAt(new Date());
            itemRepo.update(item);
        }
        return "redirect:/salesman/itemList";
    }

    /**
     * 删除商品
     * @param id 商品ID
     * @param model 模型
     * @param salesman 销售人员
     * @return 商品列表视图
     */
    @GetMapping("/removeItem/{id}")
    public String removeItem(@PathVariable("id") Long id, Model model,
                             @AuthenticationPrincipal User salesman) {
        Item item = itemRepo.findBySalesmanId(id).get(0);
        if (item.getSalesman().getId().equals(salesman.getId())) {
            itemRepo.delete(id);
        }
        return "redirect:/salesman/itemList";
    }

    /**
     * 查询业绩
     * @param salesman 销售人员
     * @param model 模型
     * @return 销售人员业绩视图
     */
    @GetMapping("/sales")
    public String showSales(@AuthenticationPrincipal User salesman, Model model) {
        List<OrderDetail> orderDetailList = orderDetailRepo.findBySalesmanId(salesman.getId());
        model.addAttribute("orderDetailList", orderDetailList);
        return "salesman/orderDetailList";
    }
}
