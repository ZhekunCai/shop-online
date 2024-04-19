package com.shoponline.repository;

import com.shoponline.entity.Item;

import java.util.List;

public interface ItemRepo {

    Item findById(Long id);
/*
    List<Item> find(String name);//search*/

    List<Item> findBySalesmanId(Long salesmanId);

    List<Item> findByNameLike(String query);
    int insert(Item item);

    int update(Item item);

    int delete(Long id);

}
