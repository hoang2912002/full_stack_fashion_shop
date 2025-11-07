package vn.clothing.fashion_shop.config;


import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.util.MessageUtil;
import vn.clothing.fashion_shop.domain.Permission;
import vn.clothing.fashion_shop.domain.Role;
import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.security.SecurityUtils;
import vn.clothing.fashion_shop.service.UserService;
import vn.clothing.fashion_shop.web.rest.errors.PermissionException;

@Component
@RequiredArgsConstructor
public class PermissionInterceptor implements HandlerInterceptor {

    private final UserService userService;
    private final MessageUtil messageUtil;
    
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
                        throw new PermissionException(messageUtil.getMessage("interceptor.permission.deny"));
                    }
                }
                else{
                    throw new PermissionException(messageUtil.getMessage("interceptor.permission.deny"));
                }
            }
        }
        return true;
    }
}
