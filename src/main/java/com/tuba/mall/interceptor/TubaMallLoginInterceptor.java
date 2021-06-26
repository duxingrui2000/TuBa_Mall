package com.tuba.mall.interceptor;

import com.tuba.mall.common.Constants;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author dxr
 * @Description 用户登录商城时的身份验证拦截器
 * @Date 17:07 6.21
 * @Param
 * @return
 **/
@Component
public class TubaMallLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //未登录强制调到登录页面
        if (request.getSession().getAttribute(Constants.MALL_USER_SESSION_KEY) == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        } else {
            return true;
        }
    }
}
