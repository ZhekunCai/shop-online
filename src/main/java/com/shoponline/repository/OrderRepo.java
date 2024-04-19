package com.shoponline.repository;

import com.shoponline.entity.Order;

import java.util.List;

public interface OrderRepo {

    List<Order> findByCustomerIdAndConfirm(Long customerId, boolean confirm);

    int insert(Order order);

    Order findById(Long id);

    int update(Order order);
}
