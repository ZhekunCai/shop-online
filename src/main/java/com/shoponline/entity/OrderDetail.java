package com.shoponline.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单详情
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {

    private Long id;

    private Order order;

    private Item item;

    private Integer num;
}
