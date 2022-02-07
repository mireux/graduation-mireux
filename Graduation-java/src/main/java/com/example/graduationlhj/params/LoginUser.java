package com.example.graduationlhj.params;


import com.alibaba.fastjson.annotation.JSONField;
import com.example.graduationlhj.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser implements UserDetails {

    private User user;

    private List<String> permissions;

    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;

    public LoginUser(User user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    /**
     * 获取权限
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 因为Collection 参数需要继承GrantedAuthority 我们选择SimpleGrantedAuthority这个实现了GrantedAuthority
        if (authorities == null) {
            authorities = permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .distinct()
                    .collect(Collectors.toList());
        }
        return authorities;
    }

    /**
     * 获取用户密码用于回调
     *
     * @return
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * 获取用户名
     *
     * @return
     */
    @Override
    public String getUsername() {
        return user.getUserName();
    }

    /**
     * 账号是否过期
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账号是否被锁定
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 凭证是否过期
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否禁用
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
