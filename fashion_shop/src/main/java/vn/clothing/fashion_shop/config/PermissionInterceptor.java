package vn.clothing.fashion_shop.config;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.clothing.fashion_shop.domain.Permission;
import vn.clothing.fashion_shop.domain.Role;
import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.security.SecurityUtils;
import vn.clothing.fashion_shop.service.UserService;
import vn.clothing.fashion_shop.web.rest.errors.PermissionException;

@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws Exception {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();

        String email = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        if(email != null && !email.isEmpty()){
            User user = this.userService.handleGetUserByEmail(email);
            if(user != null){
                Role role = user.getRole();
                if(role instanceof Role && role != null){
                    List<Permission> listPermissions = role.getPermissions();
                    boolean checkPermission = listPermissions.stream().anyMatch(
                        m -> m.getApiPath().equals(path) && m.getMethod().equals(httpMethod)
                    );
                    if(!checkPermission){
                        throw new PermissionException("Bạn không có quyền truy cập vào đây");
                    }
                }
                else{
                    throw new PermissionException("Bạn không có quyền truy cập vào đây");
                }
            }
        }
        return true;
    }
}
