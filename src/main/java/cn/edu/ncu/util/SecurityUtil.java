package cn.edu.ncu.util;

import cn.edu.ncu.user.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * The Security Util
 * get user detail from SecurityContextHolder
 * @author lorry
 * @author lin864464995@163.com
 * @see org.springframework.security.core.context.SecurityContextHolder
 */
public class SecurityUtil {
    public static User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Long getUserId() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    public static Collection<? extends GrantedAuthority> getAuthorities() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities();
    }
}
