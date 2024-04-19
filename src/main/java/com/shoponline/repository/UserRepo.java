package com.shoponline.repository;

import com.shoponline.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRepo {

    List<User> find(Long id, String username, User.Role role);

    List<User> findByUsernameAndPassword(User user);

    int insert(User user);

    int update(User user);

    int delete(Long id);

    int countByUsername(String username);
}
