package com.shoponline.repository;

import com.shoponline.entity.OrderDetail;

import java.util.List;

public interface OrderDetailRepo {

    List<OrderDetail> findByOrderId(Long orderId);

    OrderDetail findByItemAndOrder(Long itemId, Long orderId);

    int insert(OrderDetail orderDetail);

    int update(OrderDetail orderDetail);

    int delete(Long id);

    List<OrderDetail> findBySalesmanId(Long salesmanId);
}
