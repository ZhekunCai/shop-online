package com.shoponline.controller;

import com.shoponline.entity.Item;
import com.shoponline.entity.Order;
import com.shoponline.entity.OrderDetail;
import com.shoponline.entity.User;
import com.shoponline.repository.ItemRepo;
import com.shoponline.repository.OrderDetailRepo;
import com.shoponline.repository.OrderRepo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/order")
@SessionAttributes("order")
public class OrderController {

    private OrderRepo orderRepo;

    private ItemRepo itemRepo;

    private OrderDetailRepo orderDetailRepo;

    public OrderController(OrderRepo orderRepo, ItemRepo itemRepo, OrderDetailRepo orderDetailRepo) {
        this.orderRepo = orderRepo;
        this.itemRepo = itemRepo;
        this.orderDetailRepo = orderDetailRepo;
    }

    /**
     * 获取未确认的订单作为购物车
     * @param customer 用户
     * @return 订单
     */
    @ModelAttribute("order")
    public Order order(@AuthenticationPrincipal User customer) {
        List<Order> orderList = orderRepo.findByCustomerIdAndConfirm(customer.getId(), false);
        if (orderList == null || orderList.isEmpty()) {
            Order order = new Order(null, customer, false, 0.0, new Date());
            orderRepo.insert(order);
            return order;
        }
        return orderList.get(0);
    }

    /**
     * 展示历史订单
     * @param customer 用户
     * @param model 模型
     * @return 历史订单视图
     */
    @GetMapping("/history")
    public String showOrderList(@AuthenticationPrincipal User customer, Model model) {
        List<Order> orderList = orderRepo.findByCustomerIdAndConfirm(customer.getId(), true);
        model.addAttribute("orderList", orderList);
        return "order/orderList";
    }

    /**
     * 展示历史订单详情
     * @param id 订单ID
     * @param model 模型
     * @param user 用户
     * @return 订单详情视图
     */
    @GetMapping("/{id}")
    public String showHistoryOrder(@PathVariable Long id, Model model,
                                   @AuthenticationPrincipal User user) {
        Order order = orderRepo.findById(id);
        if (!Objects.equals(user.getId(), order.getCustomer().getId()))
            return "redirect:/order/history";
        List<OrderDetail> orderDetailList = orderDetailRepo.findByOrderId(id);
        model.addAttribute("orderDetailList", orderDetailList);
        model.addAttribute("order", order);
        model.addAttribute("current", false);
        return "order/orderDetailList";
    }

    /**
     * 展示当前订单（购物车）
     * @param order 订单
     * @param model 模型
     * @return 订单详情视图
     */
    @GetMapping("/current")
    public String showCurrentOrder(@ModelAttribute("order") Order order, Model model) {
        List<OrderDetail> orderDetailList = orderDetailRepo.findByOrderId(order.getId());
        // 计算总价
        double cost = 0.0;
        for (OrderDetail orderDetail : orderDetailList) {
            cost += orderDetail.getItem().getPrice() * orderDetail.getNum();
        }
        order.setCost(cost);
        model.addAttribute("orderDetailList", orderDetailList);
        model.addAttribute("order", order);
        model.addAttribute("current", true);
        return "order/orderDetailList";
    }

    /**
     * 确认订单
     * @param order 订单
     * @param sessionStatus 会话状态
     * @return 商品列表
     */
    @GetMapping("/confirm")
    public String confirmOrder(@ModelAttribute("order") Order order, SessionStatus sessionStatus) {
        order.setConfirm(true);
        orderRepo.update(order);
        sessionStatus.setComplete();
        return "redirect:/itemList";
    }

    /**
     * 展示购买商品视图
     * @param itemId 商品ID
     * @param model 模型
     * @return 购买商品视图
     */
    @GetMapping("/addItem/{itemId}")
    public String toAddItem(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepo.findById(itemId);
        model.addAttribute("item", item);
        return "order/addItem";
    }

    /**
     * 购买商品加入当前订单
     * @param itemId 商品ID
     * @param num 购买数量
     * @param order 当前订单
     * @return 当前订单视图
     */
    @PostMapping("/addItem/{itemId}")
    public String addItem(@PathVariable("itemId") Long itemId, Integer num,
                          @ModelAttribute("order") Order order) {
        Item item = itemRepo.findById(itemId);
        // 商品下架、购买数量不合法、购买数量超过库存
        if (!item.getOnSale() || num == null || num <= 0 || item.getInventory() < num) {
            return "redirect:/order/addItem/" + itemId;
        }
        // 更新商品库存
        item.setInventory(item.getInventory() - num);
        itemRepo.update(item);
        // 当前订单中是否已有该商品
        OrderDetail orderDetail = orderDetailRepo.findByItemAndOrder(itemId, order.getId());
        if (orderDetail == null) {
            orderDetail = new OrderDetail(null, order, item, num);
            orderDetailRepo.insert(orderDetail);
        } else {
            orderDetail.setNum(orderDetail.getNum() + num);
            orderDetailRepo.update(orderDetail);
        }
        return "redirect:/order/current";
    }

    /**
     * 删除订单商品
     * @param itemId 商品ID
     * @param order 订单
     * @return 当前订单视图
     */
    @GetMapping("/removeItem/{itemId}")
    public String removeItem(@PathVariable("itemId") Long itemId, @ModelAttribute("order") Order order) {
        OrderDetail orderDetail = orderDetailRepo.findByItemAndOrder(itemId, order.getId());
        if (orderDetail != null) {
            Item item = itemRepo.findById(itemId);
            item.setInventory(item.getInventory() + orderDetail.getNum());
            itemRepo.update(item);
            orderDetailRepo.delete(orderDetail.getId());
        }
        return "redirect:/order/current";
    }
}
