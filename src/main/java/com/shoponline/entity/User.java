package com.shoponline.entity;

import com.shoponline.validation.UniqueName;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 用户
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    private Long id;

    @Size(max = 20, min = 3, message = "用户名长度为3~20个字符")
    @UniqueName(message = "该用户名已被使用")
    private String username;

    @Pattern(regexp = "^[0-9A-Za-z]{6,20}$",
            message = "密码由6~20个阿拉伯数字或大小写英文字母组成")
    private String password;

    private Role role;

    private Date createdAt;

    /**
     * 角色权限
     */
    public enum Role {
        ADMIN, SALESMAN, CUSTOMER,
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.toString()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
