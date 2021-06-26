package com.tuba.mall.controller.mall;

import com.tuba.mall.common.Constants;
import com.tuba.mall.common.ServiceResultEnum;
import com.tuba.mall.common.TuBaMallException;
import com.tuba.mall.common.TuBaMallOrderStatusEnum;
import com.tuba.mall.controller.vo.TuBaMallOrderDetailVO;
import com.tuba.mall.controller.vo.TuBaMallShoppingCartItemVO;
import com.tuba.mall.controller.vo.TuBaMallUserVO;
import com.tuba.mall.entity.TuBaMallOrder;
import com.tuba.mall.service.TuBaMallOrderService;
import com.tuba.mall.service.TuBaMallShoppingCartService;
import com.tuba.mall.util.PageQueryUtil;
import com.tuba.mall.util.Result;
import com.tuba.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class orderController {
    @Resource
    private TuBaMallShoppingCartService tuBaMallShoppingCartService;
    @Resource
    private TuBaMallOrderService tuBaMallOrderService;
    
    @GetMapping("/orders/{orderNo}")
    public String orderDetailPage(HttpServletRequest request, @PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        TuBaMallUserVO user = (TuBaMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        TuBaMallOrderDetailVO orderDetailVO = tuBaMallOrderService.getOrderDetailByOrderNo(orderNo, user.getUserId());
        if (orderDetailVO == null) {
            return "error/error_5xx";
        }
        request.setAttribute("orderDetailVO", orderDetailVO);
        return "mall/order-detail";
    }
    
    @GetMapping("/orders")
    public String orderListPage(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpSession httpSession) {
        TuBaMallUserVO user = (TuBaMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        params.put("userId", user.getUserId());
        if (StringUtils.isEmpty(params.get("page"))) {
            params.put("page", 1);
        }
        params.put("limit", Constants.ORDER_SEARCH_PAGE_LIMIT);
        //封装我的订单数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        request.setAttribute("orderPageResult", tuBaMallOrderService.getMyOrders(pageUtil));
        request.setAttribute("path", "orders");
        return "mall/my-orders";
    }
    
    @GetMapping("/saveOrder")
    public String saveOrder(HttpSession httpSession) {
        TuBaMallUserVO user = (TuBaMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<TuBaMallShoppingCartItemVO> myShoppingCartItems = tuBaMallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (StringUtils.isEmpty(user.getAddress().trim())) {
            //无收货地址
            TuBaMallException.fail(ServiceResultEnum.NULL_ADDRESS_ERROR.getResult());
        }
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            //购物车中无数据则跳转至错误页
            TuBaMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        }
        //保存订单并返回订单号
        String saveOrderResult = tuBaMallOrderService.saveOrder(user, myShoppingCartItems);
        //跳转到订单详情页
        return "redirect:/orders/" + saveOrderResult;
    }
    
    @PutMapping("/orders/{orderNo}/cancel")
    @ResponseBody
    public Result cancelOrder(@PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        TuBaMallUserVO user = (TuBaMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        String cancelOrderResult = tuBaMallOrderService.cancelOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(cancelOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(cancelOrderResult);
        }
    }
    
    @PutMapping("/orders/{orderNo}/finish")
    @ResponseBody
    public Result finishOrder(@PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        TuBaMallUserVO user = (TuBaMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        String finishOrderResult = tuBaMallOrderService.finishOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(finishOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(finishOrderResult);
        }
    }
    
    @GetMapping("/selectPayType")
    public String selectPayType(HttpServletRequest request, @RequestParam("orderNo") String orderNo, HttpSession httpSession) {
        TuBaMallUserVO user = (TuBaMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        TuBaMallOrder tuBaMallOrder = tuBaMallOrderService.getTuBaMallOrderByOrderNo(orderNo);
        //判断订单userId
        if (!user.getUserId().equals(tuBaMallOrder.getUserId())) {
            TuBaMallException.fail(ServiceResultEnum.NO_PERMISSION_ERROR.getResult());
        }
        //判断订单状态
        if (tuBaMallOrder.getOrderStatus().intValue() != TuBaMallOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()) {
            TuBaMallException.fail(ServiceResultEnum.ORDER_STATUS_ERROR.getResult());
        }
        request.setAttribute("orderNo", orderNo);
        request.setAttribute("totalPrice", tuBaMallOrder.getTotalPrice());
        return "mall/pay-select";
    }
    
    @GetMapping("/payPage")
    public String payOrder(HttpServletRequest request, @RequestParam("orderNo") String orderNo, HttpSession httpSession, @RequestParam("payType") int payType) {
        TuBaMallUserVO user = (TuBaMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        TuBaMallOrder tuBaMallOrder = tuBaMallOrderService.getTuBaMallOrderByOrderNo(orderNo);
        //判断订单userId
        if (!user.getUserId().equals(tuBaMallOrder.getUserId())) {
            TuBaMallException.fail(ServiceResultEnum.NO_PERMISSION_ERROR.getResult());
        }
        //判断订单状态
        if (tuBaMallOrder.getOrderStatus().intValue() != TuBaMallOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()) {
            TuBaMallException.fail(ServiceResultEnum.ORDER_STATUS_ERROR.getResult());
        }
        request.setAttribute("orderNo", orderNo);
        request.setAttribute("totalPrice", tuBaMallOrder.getTotalPrice());
        if (payType == 1) {
            return "mall/alipay";
        } else {
            return "mall/wxpay";
        }
    }
    
    @GetMapping("/paySuccess")
    @ResponseBody
    public Result paySuccess(@RequestParam("orderNo") String orderNo, @RequestParam("payType") int payType) {
        String payResult = tuBaMallOrderService.paySuccess(orderNo, payType);
        if (ServiceResultEnum.SUCCESS.getResult().equals(payResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(payResult);
        }
    }
}
