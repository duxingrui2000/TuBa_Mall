package com.tuba.mall.config;

import com.tuba.mall.common.Constants;
import com.tuba.mall.interceptor.AdminLoginInterceptor;
import com.tuba.mall.interceptor.TuBaMallCartNumberInterceptor;
import com.tuba.mall.interceptor.TubaMallLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author dxr
 * @Description 抛弃xml配置，
 *          addInterceptors：拦截器
 *          addInterceptor：需要一个实现HandlerInterceptor接口的拦截器实例
 *          addPathPatterns：用于设置拦截器的过滤路径规则；addPathPatterns("/**")对所有请求都拦截
 *          excludePathPatterns：用于设置不需要拦截的过滤规则
 * @Date 17:16 6.21
 * @Param 
 * @return 
 **/       
@Configuration
public class TuBaMallWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private AdminLoginInterceptor adminLoginInterceptor;//管理员拦截
    @Autowired
    private TubaMallLoginInterceptor tubaMallLoginInterceptor;//购物车处理
    @Autowired
    private TuBaMallCartNumberInterceptor tuBaMallCartNumberInterceptor;//用户登录拦截

    public void addInterceptors(InterceptorRegistry registry) {
        // 添加一个拦截器，拦截以/admin为前缀的url路径（后台登陆拦截）
        registry.addInterceptor(adminLoginInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/admin/dist/**")
                .excludePathPatterns("/admin/plugins/**");
        // 购物车中的数量统一处理
        registry.addInterceptor(tuBaMallCartNumberInterceptor)
                .excludePathPatterns("/admin/**")
                .excludePathPatterns("/register")
                .excludePathPatterns("/login")
                .excludePathPatterns("/logout");
        // 商城页面登陆拦截
        registry.addInterceptor(tubaMallLoginInterceptor)
                .excludePathPatterns("/admin/**")
                .excludePathPatterns("/register")
                .excludePathPatterns("/login")
                .excludePathPatterns("/logout")
                .addPathPatterns("/goods/detail/**")
                .addPathPatterns("/shop-cart")
                .addPathPatterns("/shop-cart/**")
                .addPathPatterns("/saveOrder")
                .addPathPatterns("/orders")
                .addPathPatterns("/orders/**")            
                .addPathPatterns("/personal")
                .addPathPatterns("/personal/updateInfo")
                .addPathPatterns("/selectPayType")
                .addPathPatterns("/payPage");
    }

    //配置SpringMVC对静态资源的处理，将/upload/**访问做一个映射，映射到实际配置的File位置的对应资源路径
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + Constants.FILE_UPLOAD_DIC);
        registry.addResourceHandler("/goods-img/**").addResourceLocations("file:" + Constants.FILE_UPLOAD_DIC);
    }
}
