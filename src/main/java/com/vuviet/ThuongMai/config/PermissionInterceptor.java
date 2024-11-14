package com.vuviet.ThuongMai.config;

import com.vuviet.ThuongMai.entity.Permission;
import com.vuviet.ThuongMai.entity.Role;
import com.vuviet.ThuongMai.entity.User;
import com.vuviet.ThuongMai.service.UserService;
import com.vuviet.ThuongMai.util.SecurityUtil;
import com.vuviet.ThuongMai.util.error.PermissionException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;

public class PermissionInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        //check permission
        String email= SecurityUtil.getCurrentUserLogin().isPresent()==true
                ?SecurityUtil.getCurrentUserLogin().get() : "";
        if(email!=null&&!email.isEmpty()){
            User user=this.userService.getByUsername(email);
            if(user!=null){
                Role role=user.getRole();
                if(role!=null){
                    List<Permission> permissions=role.getPermissions();
                    boolean isAllow=permissions.stream().anyMatch(item->
                            item.getApiPath().equals(path)
                                    && item.getMethod().equals(httpMethod));

                    if(isAllow==false){
                        throw new PermissionException("Bạn không đủ quyền truy cập");
                    }
                } else {
                    throw new PermissionException("Bạn không đủ quyền truy cập");
                }
            }
        }
        return true;
    }
}
