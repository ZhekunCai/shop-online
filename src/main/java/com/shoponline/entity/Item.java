package com.shoponline.entity;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 商品
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private Long id;

    @Size(max = 20, min = 3, message = "商品名长度为3~20个字符")
    private String name;

    /**
     * 图片文件名
     */
    private String image;

    @Min(value = 1, message = "价格不少于1元")
    private Double price;

    /**
     * 库存
     */
    @Min(value = 0, message = "库存须大于0")
    private Integer inventory;

    /**
     * 是否在售
     */
    @NotNull
    private Boolean onSale;

    private Date createdAt;

    private Date modifiedAt;

    /**
     * 销售人员
     */
    private User salesman;

}
