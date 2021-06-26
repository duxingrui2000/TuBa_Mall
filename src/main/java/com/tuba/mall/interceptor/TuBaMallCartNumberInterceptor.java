package com.tuba.mall.interceptor;

import com.tuba.mall.common.Constants;
import com.tuba.mall.controller.vo.TuBaMallUserVO;
import com.tuba.mall.dao.TuBaMallShoppingCartItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author dxr
 * @Description 刷新下session中存储的购物车的商品数量
 * @Date 17:03 6.21
 * @Param
 * @return
 **/
@Component
public class TuBaMallCartNumberInterceptor implements HandlerInterceptor {
    
    @Autowired
    private TuBaMallShoppingCartItemMapper tuBaMallShoppingCartItemMapper;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果当前为登陆状态，就查询数据库并设置购物车中的数量值
        if (request.getSession() != null && request.getSession().getAttribute(Constants.MALL_USER_SESSION_KEY) != null) {
            TuBaMallUserVO tuBaMallUserVO = (TuBaMallUserVO) request.getSession().getAttribute(Constants.MALL_USER_SESSION_KEY);
            //设置购物车中的数量
            tuBaMallUserVO.setShopCartItemCount(tuBaMallShoppingCartItemMapper.selectCountByUserId(tuBaMallUserVO.getUserId()));
            request.getSession().setAttribute(Constants.MALL_USER_SESSION_KEY, tuBaMallUserVO);
        }
        return true;
    }
}
