package com.tuba.mall.controller.mall;

import com.tuba.mall.common.Constants;
import com.tuba.mall.common.ServiceResultEnum;
import com.tuba.mall.controller.vo.TuBaMallShoppingCartItemVO;
import com.tuba.mall.controller.vo.TuBaMallUserVO;
import com.tuba.mall.entity.TuBaMallShoppingCartItem;
import com.tuba.mall.service.TuBaMallShoppingCartService;
import com.tuba.mall.util.Result;
import com.tuba.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ShoppingCartController {
    @Resource
    private TuBaMallShoppingCartService tuBaMallShoppingCartService;
    
    @GetMapping("/shop-cart")
    public String cartListPage(HttpServletRequest request,
                               HttpSession httpSession) {
        TuBaMallUserVO user = (TuBaMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        int itemsTotal = 0;
        int priceTotal = 0;
        List<TuBaMallShoppingCartItemVO> myShoppingCartItems = tuBaMallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (!CollectionUtils.isEmpty(myShoppingCartItems)) {
            //购物项总数
            itemsTotal = myShoppingCartItems.stream().mapToInt(TuBaMallShoppingCartItemVO::getGoodsCount).sum();
            if (itemsTotal < 1) {
                return "error/error_5xx";
            }
            //总价
            for (TuBaMallShoppingCartItemVO tuBaMallShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += tuBaMallShoppingCartItemVO.getGoodsCount() * tuBaMallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/error_5xx";
            }
        }
        request.setAttribute("itemsTotal", itemsTotal);
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/cart";
    }
    
    @PostMapping("/shop-cart")
    @ResponseBody
    public Result saveTuBaMallShoppingCartItem(@RequestBody TuBaMallShoppingCartItem tuBaMallShoppingCartItem,
                                                 HttpSession httpSession) {
        TuBaMallUserVO user = (TuBaMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        tuBaMallShoppingCartItem.setUserId(user.getUserId());
        String saveResult = tuBaMallShoppingCartService.saveTuBaMallCartItem(tuBaMallShoppingCartItem);
        //添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult(saveResult);
    }
    
    @PutMapping("/shop-cart")
    @ResponseBody
    public Result updateTuBaMallShoppingCartItem(@RequestBody TuBaMallShoppingCartItem tuBaMallShoppingCartItem,
                                                   HttpSession httpSession) {
        TuBaMallUserVO user = (TuBaMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        tuBaMallShoppingCartItem.setUserId(user.getUserId());
        String updateResult = tuBaMallShoppingCartService.updateTuBaMallCartItem(tuBaMallShoppingCartItem);
        //修改成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult(updateResult);
    }
    
    @DeleteMapping("/shop-cart/{tuBaMallShoppingCartItemId}")
    @ResponseBody
    public Result updateTuBaMallShoppingCartItem(@PathVariable("tuBaMallShoppingCartItemId") Long tuBaMallShoppingCartItemId,
                                                 HttpSession httpSession) {
        TuBaMallUserVO user = (TuBaMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        Boolean deleteResult = tuBaMallShoppingCartService.deleteById(tuBaMallShoppingCartItemId,user.getUserId());
        //删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }
    
    @GetMapping("/shop-cart/settle")
    public String settlePage(HttpServletRequest request,
                             HttpSession httpSession) {
        int priceTotal = 0;
        TuBaMallUserVO user = (TuBaMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<TuBaMallShoppingCartItemVO> myShoppingCartItems = tuBaMallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            //无数据则不跳转至结算页
            return "/shop-cart";
        } else {
            //总价
            for (TuBaMallShoppingCartItemVO tuBaMallShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += tuBaMallShoppingCartItemVO.getGoodsCount() * tuBaMallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/error_5xx";
            }
        }
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/order-settle";
    }
}
