package com.tuba.mall.service.impl;

import com.tuba.mall.common.*;
import com.tuba.mall.controller.vo.*;
import com.tuba.mall.dao.TuBaMallGoodsMapper;
import com.tuba.mall.dao.TuBaMallOrderItemMapper;
import com.tuba.mall.dao.TuBaMallOrderMapper;
import com.tuba.mall.dao.TuBaMallShoppingCartItemMapper;
import com.tuba.mall.entity.StockNumDTO;
import com.tuba.mall.entity.TuBaMallGoods;
import com.tuba.mall.entity.TuBaMallOrder;
import com.tuba.mall.entity.TuBaMallOrderItem;
import com.tuba.mall.service.TuBaMallOrderService;
import com.tuba.mall.util.BeanUtil;
import com.tuba.mall.util.NumberUtil;
import com.tuba.mall.util.PageQueryUtil;
import com.tuba.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
@Service
public class TuBaMallOrderServiceImpl implements TuBaMallOrderService {
    
    @Autowired
    private TuBaMallOrderMapper tuBaMallOrderMapper;
    @Autowired
    private TuBaMallOrderItemMapper tuBaMallOrderItemMapper;
    @Autowired
    private TuBaMallShoppingCartItemMapper tuBaMallShoppingCartItemMapper;
    @Autowired
    private TuBaMallGoodsMapper tuBaMallGoodsMapper;
    
    @Override
    public PageResult getTuBaMallOrdersPage(PageQueryUtil pageUtil) {
        List<TuBaMallOrder> tuBaMallOrders = tuBaMallOrderMapper.findTuBaMallOrderList(pageUtil);
        int total = tuBaMallOrderMapper.getTotalTuBaMallOrders(pageUtil);
        PageResult pageResult = new PageResult(tuBaMallOrders, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    
    @Override
    public String updateOrderInfo(TuBaMallOrder tuBaMallOrder) {
        TuBaMallOrder temp = tuBaMallOrderMapper.selectByPrimaryKey(tuBaMallOrder.getOrderId());
        //不为空且orderStatus>=0且状态为出库之前可以修改部分信息
        if (temp != null && temp.getOrderStatus() >= 0 && temp.getOrderStatus() < 3) {
            temp.setTotalPrice(tuBaMallOrder.getTotalPrice());
            temp.setUserAddress(tuBaMallOrder.getUserAddress());
            temp.setUpdateTime(new Date());
            if (tuBaMallOrderMapper.updateByPrimaryKeySelective(temp) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            }
            return ServiceResultEnum.DB_ERROR.getResult();
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }
    
    @Override
    public String checkDone(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<TuBaMallOrder> orders = tuBaMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            //实际上目前isDeleted都是默认为0，OrderStatus必须全为1才认为该订单成功，商家可以出库等后续操作
            for (TuBaMallOrder order : orders) {
                if (order.getIsDeleted() == 1) {
                    errorOrderNos += order.getOrderNo() + " ";
                    continue;
                }
                if (order.getOrderStatus() != 1) {
                    errorOrderNos += order.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行配货完成操作 修改订单状态和更新时间
                if (tuBaMallOrderMapper.checkDone(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功的订单，无法执行配货完成操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }
    
    @Override
    public String checkOut(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<TuBaMallOrder> orders = tuBaMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            //确定
            for (TuBaMallOrder order : orders) {
                //不存在 删除的字段=1
                if (order.getIsDeleted() == 1) {
                    errorOrderNos += order.getOrderNo() + " ";
                    continue;
                }
                //1已支付2已配货都代表可以出货，非这两个状态代表出现错误状态
                if (order.getOrderStatus() != 1 && order.getOrderStatus() != 2) {
                    errorOrderNos += order.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行出库操作 修改订单状态和更新时间
                if (tuBaMallOrderMapper.checkOut(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功或配货完成无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功或配货完成的订单，无法执行出库操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }
    
    @Override
    public String closeOrder(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间，管理员关闭订单功能！用户取消订单功能在cancelOrder方法
        List<TuBaMallOrder> orders = tuBaMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (TuBaMallOrder tuBaMallOrder : orders) {
                // isDeleted=1 一定为已关闭订单
                if (tuBaMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += tuBaMallOrder.getOrderNo() + " ";
                    continue;
                }
                //已关闭或者已完成无法关闭订单
                if (tuBaMallOrder.getOrderStatus() == 4 || tuBaMallOrder.getOrderStatus() < 0) {
                    errorOrderNos += tuBaMallOrder.getOrderNo() + " ";
                }
            }
            //只有在012（待支付，已支付，配货完成）阶段可以取消订单，否则需要管理员关闭
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行关闭操作 修改订单状态和更新时间
                if (tuBaMallOrderMapper.closeOrder(Arrays.asList(ids), TuBaMallOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行关闭操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单不能执行关闭操作";
                } else {
                    return "你选择的订单不能执行关闭操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }
    
    @Override
    public String saveOrder(TuBaMallUserVO user, List<TuBaMallShoppingCartItemVO> myShoppingCartItems) {
        List<Long> itemIdList = myShoppingCartItems.stream().map(TuBaMallShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = myShoppingCartItems.stream().map(TuBaMallShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
        //根据购物车中所有商品id查出所有商品信息
        List<TuBaMallGoods> tuBaMallGoods = tuBaMallGoodsMapper.selectByPrimaryKeys(goodsIds);
        //检查是否包含已下架商品
        List<TuBaMallGoods> goodsListNotSelling = tuBaMallGoods.stream()
                .filter(tuBaMallGoodsTemp -> tuBaMallGoodsTemp.getGoodsSellStatus() != Constants.SELL_STATUS_UP)
                .collect(Collectors.toList());
        
        if (!CollectionUtils.isEmpty(goodsListNotSelling)) {
            //goodsListNotSelling 对象非空则表示有下架商品
            TuBaMallException.fail(goodsListNotSelling.get(0).getGoodsName() + "已下架，无法生成订单");
        }
        
        //构造map，key为商品id，value为商品信息的封装，以后用就不需要从数据库查找了
        Map<Long, TuBaMallGoods> tuBaMallGoodsMap = tuBaMallGoods.stream().collect(Collectors.toMap(TuBaMallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        
        //判断购物城中每件商品的库存是否足够满足需求
        for (TuBaMallShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
            //查出的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
            if (!tuBaMallGoodsMap.containsKey(shoppingCartItemVO.getGoodsId())) {
                TuBaMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //存在数量大于库存的情况，直接返回错误提醒
            if (shoppingCartItemVO.getGoodsCount() > tuBaMallGoodsMap.get(shoppingCartItemVO.getGoodsId()).getStockNum()) {
                TuBaMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
        //删除购物项
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(tuBaMallGoods)) {
            if (tuBaMallShoppingCartItemMapper.deleteBatch(itemIdList) > 0) {
                //批量删除购物车中的数据，减库存操作（DTO中只封装了商品id和count数量）
                List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(myShoppingCartItems, StockNumDTO.class);
                /*默认情况下，mybatis 的 update 操作的返回值是 matched 的记录数，并不是受影响的记录数。*/
                int updateStockNumResult = tuBaMallGoodsMapper.updateStockNum(stockNumDTOS);
                if (updateStockNumResult < 1) {
                    TuBaMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
                //生成订单号
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
                //保存订单
                TuBaMallOrder tuBaMallOrder = new TuBaMallOrder();
                tuBaMallOrder.setOrderNo(orderNo);
                tuBaMallOrder.setUserId(user.getUserId());
                tuBaMallOrder.setUserAddress(user.getAddress());
                //总价
                for (TuBaMallShoppingCartItemVO tuBaMallShoppingCartItemVO : myShoppingCartItems) {
                    priceTotal += tuBaMallShoppingCartItemVO.getGoodsCount() * tuBaMallShoppingCartItemVO.getSellingPrice();
                }
                if (priceTotal < 1) {
                    TuBaMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                tuBaMallOrder.setTotalPrice(priceTotal);
                //订单body字段，用来作为生成支付单描述信息，暂时未接入第三方支付接口，故该字段暂时设为空字符串
                String extraInfo = "";
                tuBaMallOrder.setExtraInfo(extraInfo);
                //生成订单项并保存订单项纪录
                if (tuBaMallOrderMapper.insertSelective(tuBaMallOrder) > 0) {
                    //生成所有的订单项快照，并保存至数据库tuba_mall_order_item中去
                    List<TuBaMallOrderItem> tuBaMallOrderItems = new ArrayList<>();
                    for (TuBaMallShoppingCartItemVO tuBaMallShoppingCartItemVO : myShoppingCartItems) {
                        TuBaMallOrderItem tuBaMallOrderItem = new TuBaMallOrderItem();
                        //使用BeanUtil工具类将newBeeMallShoppingCartItemVO中的属性复制到tuBaMallOrderItem对象中
                        BeanUtil.copyProperties(tuBaMallShoppingCartItemVO, tuBaMallOrderItem);
                        //TuBaMallOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
                        tuBaMallOrderItem.setOrderId(tuBaMallOrder.getOrderId());
                        tuBaMallOrderItems.add(tuBaMallOrderItem);
                    }
                    //上一步已将item都封装进链表，一次性批量保存至数据库
                    if (tuBaMallOrderItemMapper.insertBatch(tuBaMallOrderItems) > 0) {
                        //所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
                        return orderNo;
                    }
//                    TuBaMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                TuBaMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            TuBaMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        TuBaMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
    }
    
    @Override
    public TuBaMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        TuBaMallOrder tuBaMallOrder = tuBaMallOrderMapper.selectByOrderNo(orderNo);
        //确保存在该订单号
        if (tuBaMallOrder != null) {
            //验证是否是当前userId下的订单，否则报错
            if (!userId.equals(tuBaMallOrder.getUserId())) {
                TuBaMallException.fail(ServiceResultEnum.NO_PERMISSION_ERROR.getResult());
            }
            //一个用户买一次商品（清空购物车）形成一个订单号，这一个订单号对应一个订单表主键orderId，
            // 在订单项表（关联订单号和商品号）中，
            // 这个orderId会对应当时购物车中的所有商品goodId，即1对多
            List<TuBaMallOrderItem> orderItems = tuBaMallOrderItemMapper.selectByOrderId(tuBaMallOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                //把orderItems的关键内容封装成List<TuBaMallOrderItemVO>
                List<TuBaMallOrderItemVO> tuBaMallOrderItemVOS = BeanUtil.copyList(orderItems, TuBaMallOrderItemVO.class);
                
                TuBaMallOrderDetailVO tuBaMallOrderDetailVO = new TuBaMallOrderDetailVO();
                BeanUtil.copyProperties(tuBaMallOrder, tuBaMallOrderDetailVO);
                //把status字段转为string详细内容字段
                tuBaMallOrderDetailVO.setOrderStatusString(TuBaMallOrderStatusEnum.getTuBaMallOrderStatusEnumByStatus(tuBaMallOrderDetailVO.getOrderStatus()).getName());
                tuBaMallOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(tuBaMallOrderDetailVO.getPayType()).getName());
                tuBaMallOrderDetailVO.setTuBaMallOrderItemVOS(tuBaMallOrderItemVOS);
                return tuBaMallOrderDetailVO;
            }
        }
        return null;
    }
    
    @Override
    public TuBaMallOrder getTuBaMallOrderByOrderNo(String orderNo) {
        return tuBaMallOrderMapper.selectByOrderNo(orderNo);
    }
    
    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) {
        //一个user很多个订单号，每个订单号对应单独的order_id，每个order_id下面很多个商品在order_item表
        int total = tuBaMallOrderMapper.getTotalTuBaMallOrders(pageUtil);
        //找出所有的order
        List<TuBaMallOrder> tuBaMallOrders = tuBaMallOrderMapper.findTuBaMallOrderList(pageUtil);
        List<TuBaMallOrderListVO> orderListVOS = new ArrayList<>();
        if (total > 0) {
            //数据转换 将实体类转成vo
            orderListVOS = BeanUtil.copyList(tuBaMallOrders, TuBaMallOrderListVO.class);
            //设置订单状态中文显示值
            for (TuBaMallOrderListVO tuBaMallOrderListVO : orderListVOS) {
                tuBaMallOrderListVO.setOrderStatusString(TuBaMallOrderStatusEnum.getTuBaMallOrderStatusEnumByStatus(tuBaMallOrderListVO.getOrderStatus()).getName());
            }
            //取出tuBaMallOrders中所有的id号
            List<Long> orderIds = tuBaMallOrders.stream().map(TuBaMallOrder::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                //一个user的所有历史订单号对应的订单表主键orderid对应的所有商品
                List<TuBaMallOrderItem> orderItems = tuBaMallOrderItemMapper.selectByOrderIds(orderIds);
                //根据订单号把上面orderItems中属于一个订单的所有商品取出来放入value，key为此时的orderId
                Map<Long, List<TuBaMallOrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(TuBaMallOrderItem::getOrderId));
                //遍历所有的orderVO（把每个order对应的TuBaMallOrderItem列表信息转为VO列表信息）
                for (TuBaMallOrderListVO tuBaMallOrderListVO : orderListVOS) {
                    if (itemByOrderIdMap.containsKey(tuBaMallOrderListVO.getOrderId())) {
                        //从map中取出该orderId对应的多条orderItem数据
                        List<TuBaMallOrderItem> orderItemListTemp = itemByOrderIdMap.get(tuBaMallOrderListVO.getOrderId());
                        //将TuBaMallOrderItem对象列表转换成TuBaMallOrderItemVO对象列表
                        List<TuBaMallOrderItemVO> tuBaMallOrderItemVOS = BeanUtil.copyList(orderItemListTemp, TuBaMallOrderItemVO.class);
                        //封装进该orderListVO中的List<TuBaMallOrderItemVO> tuBaMallOrderItemVOS中去
                        tuBaMallOrderListVO.setTuBaMallOrderItemVOS(tuBaMallOrderItemVOS);
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(orderListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    
    @Override
    public String cancelOrder(String orderNo, Long userId) {
        TuBaMallOrder tuBaMallOrder = tuBaMallOrderMapper.selectByOrderNo(orderNo);
        if (tuBaMallOrder != null) {
            //验证是否是当前userId下的订单，否则报错
            if (!userId.equals(tuBaMallOrder.getUserId())) {
                TuBaMallException.fail(ServiceResultEnum.NO_PERMISSION_ERROR.getResult());
            }
            //订单状态判断
            if (tuBaMallOrder.getOrderStatus().intValue() == TuBaMallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus()
                    || tuBaMallOrder.getOrderStatus().intValue() == TuBaMallOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()
                    || tuBaMallOrder.getOrderStatus().intValue() == TuBaMallOrderStatusEnum.ORDER_CLOSED_BY_EXPIRED.getOrderStatus()
                    || tuBaMallOrder.getOrderStatus().intValue() == TuBaMallOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) {
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            if (tuBaMallOrderMapper.closeOrder(Collections.singletonList(tuBaMallOrder.getOrderId()), TuBaMallOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }
    
    @Override
    public String finishOrder(String orderNo, Long userId) {
        TuBaMallOrder tuBaMallOrder = tuBaMallOrderMapper.selectByOrderNo(orderNo);
        if (tuBaMallOrder != null) {
            //验证是否是当前userId下的订单，否则报错
            if (!userId.equals(tuBaMallOrder.getUserId())) {
                return ServiceResultEnum.NO_PERMISSION_ERROR.getResult();
            }
            //订单状态判断 非出库状态下不进行修改操作
            if (tuBaMallOrder.getOrderStatus().intValue() != TuBaMallOrderStatusEnum.ORDER_EXPRESS.getOrderStatus()) {
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            tuBaMallOrder.setOrderStatus((byte) TuBaMallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            tuBaMallOrder.setUpdateTime(new Date());
            if (tuBaMallOrderMapper.updateByPrimaryKeySelective(tuBaMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }
    
    @Override
    public String paySuccess(String orderNo, int payType) {
        TuBaMallOrder tuBaMallOrder = tuBaMallOrderMapper.selectByOrderNo(orderNo);
        if (tuBaMallOrder != null) {
            //订单状态判断 非待支付状态下不进行修改操作
            if (tuBaMallOrder.getOrderStatus().intValue() != TuBaMallOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()) {
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            //从待支付状态变为已支付状态，设置订单状态，交易手段支付宝还是微信等等，时间更新
            tuBaMallOrder.setOrderStatus((byte) TuBaMallOrderStatusEnum.ORDER_PAID.getOrderStatus());
            tuBaMallOrder.setPayType((byte) payType);
            tuBaMallOrder.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
            tuBaMallOrder.setPayTime(new Date());
            tuBaMallOrder.setUpdateTime(new Date());
            if (tuBaMallOrderMapper.updateByPrimaryKeySelective(tuBaMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }
    
    @Override
    public List<TuBaMallOrderItemVO> getOrderItems(Long id) {
        //根据orderId获得该orderId所包含的所有商品
        TuBaMallOrder tuBaMallOrder = tuBaMallOrderMapper.selectByPrimaryKey(id);
        if (tuBaMallOrder != null) {
            List<TuBaMallOrderItem> orderItems = tuBaMallOrderItemMapper.selectByOrderId(tuBaMallOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<TuBaMallOrderItemVO> tuBaMallOrderItemVOS = BeanUtil.copyList(orderItems, TuBaMallOrderItemVO.class);
                return tuBaMallOrderItemVOS;
            }
        }
        return null;
    }
}
