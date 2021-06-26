package com.tuba.mall.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author dxr
 * @Description 自定义一个拦截器，用于后端管理员登录的时候
 * @Date 16:55 6.21
 * @Param
 * @return
 **/
@Component
public class AdminLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String servletPath = request.getServletPath();
        if (servletPath.startsWith("/admin") && request.getSession().getAttribute("loginUser") == null) {
            request.getSession().setAttribute("errorMsg", "请登录");
            //未登录不允许跳后台其他页面，重定向到login页面，return false放弃后续controller的执行
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return false;
        } else {
            request.getSession().removeAttribute("errorMsg");
            return true;//放行
        }
    }
    //暂且不重写postHandle和afterCompletion方法
}
