package com.shoponline.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 订单
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Long id;

    private User customer;

    /**
     * 确认与否
     */
    private Boolean confirm;

    private Double cost;

    private Date createdAt;
}
