package com.tuba.mall.service.impl;

import com.tuba.mall.common.Constants;
import com.tuba.mall.common.ServiceResultEnum;
import com.tuba.mall.controller.vo.TuBaMallShoppingCartItemVO;
import com.tuba.mall.dao.TuBaMallGoodsMapper;
import com.tuba.mall.dao.TuBaMallShoppingCartItemMapper;
import com.tuba.mall.entity.TuBaMallGoods;
import com.tuba.mall.entity.TuBaMallShoppingCartItem;
import com.tuba.mall.service.TuBaMallShoppingCartService;
import com.tuba.mall.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service
public class TuBaMallShoppingCartServiceImpl implements TuBaMallShoppingCartService {
    
    @Autowired
    private TuBaMallShoppingCartItemMapper tuBaMallShoppingCartItemMapper;
    
    @Autowired
    private TuBaMallGoodsMapper tuBaMallGoodsMapper;
    
    @Override
    public String saveTuBaMallCartItem(TuBaMallShoppingCartItem tuBaMallShoppingCartItem) {
        TuBaMallShoppingCartItem temp = tuBaMallShoppingCartItemMapper.selectByUserIdAndGoodsId(tuBaMallShoppingCartItem.getUserId(), tuBaMallShoppingCartItem.getGoodsId());
        if (temp != null) {
            //已存在则修改该记录
            temp.setGoodsCount(temp.getGoodsCount()+tuBaMallShoppingCartItem.getGoodsCount());
            return updateTuBaMallCartItem(temp);
        }
        TuBaMallGoods tuBaMallGoods = tuBaMallGoodsMapper.selectByPrimaryKey(tuBaMallShoppingCartItem.getGoodsId());
        //商品为空
        if (tuBaMallGoods == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        int totalItem = tuBaMallShoppingCartItemMapper.selectCountByUserId(tuBaMallShoppingCartItem.getUserId()) + 1;
        //超出单个商品的最大数量
        if (tuBaMallShoppingCartItem.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //超出最大数量
        if (totalItem > Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult();
        }
        //保存记录
        if (tuBaMallShoppingCartItemMapper.insertSelective(tuBaMallShoppingCartItem) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }
    
    @Override
    public String updateTuBaMallCartItem(TuBaMallShoppingCartItem tuBaMallShoppingCartItem) {
        TuBaMallShoppingCartItem tuBaMallShoppingCartItemUpdate = tuBaMallShoppingCartItemMapper.selectByPrimaryKey(tuBaMallShoppingCartItem.getCartItemId());
        if (tuBaMallShoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        //超出单个商品的最大数量
        if (tuBaMallShoppingCartItem.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //当前登录账号的userId与待修改的cartItem中userId不同，返回错误
        if (!tuBaMallShoppingCartItemUpdate.getUserId().equals(tuBaMallShoppingCartItem.getUserId())) {
            return ServiceResultEnum.NO_PERMISSION_ERROR.getResult();
        }
        //数值相同，则不执行数据操作
        if (tuBaMallShoppingCartItem.getGoodsCount().equals(tuBaMallShoppingCartItemUpdate.getGoodsCount())) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        tuBaMallShoppingCartItemUpdate.setGoodsCount(tuBaMallShoppingCartItem.getGoodsCount());
        tuBaMallShoppingCartItemUpdate.setUpdateTime(new Date());
        //修改记录
        if (tuBaMallShoppingCartItemMapper.updateByPrimaryKeySelective(tuBaMallShoppingCartItemUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }
    
    @Override
    public TuBaMallShoppingCartItem getTuBaMallCartItemById(Long tuBaMallShoppingCartItemId) {
        return tuBaMallShoppingCartItemMapper.selectByPrimaryKey(tuBaMallShoppingCartItemId);
    }
    
    @Override
    public Boolean deleteById(Long shoppingCartItemId, Long userId) {
        TuBaMallShoppingCartItem tuBaMallShoppingCartItem = tuBaMallShoppingCartItemMapper.selectByPrimaryKey(shoppingCartItemId);
        if (tuBaMallShoppingCartItem == null) {
            return false;
        }
        //userId不同不能删除
        if (!userId.equals(tuBaMallShoppingCartItem.getUserId())) {
            return false;
        }
        return tuBaMallShoppingCartItemMapper.deleteByPrimaryKey(shoppingCartItemId) > 0;
    }
    
    @Override
    public List<TuBaMallShoppingCartItemVO> getMyShoppingCartItems(Long tuBaMallUserId) {
        List<TuBaMallShoppingCartItemVO> tuBaMallShoppingCartItemVOS = new ArrayList<>();
        //找出所有购物车数据
        List<TuBaMallShoppingCartItem> tuBaMallShoppingCartItems = tuBaMallShoppingCartItemMapper.selectByUserId(tuBaMallUserId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
        if (!CollectionUtils.isEmpty(tuBaMallShoppingCartItems)) {
            //查询商品信息并做数据转换
            //查出该购物车中所有商品的id
            List<Long> tuBaMallGoodsIds = tuBaMallShoppingCartItems.stream().map(TuBaMallShoppingCartItem::getGoodsId).collect(Collectors.toList());
            //根据商品id找出该购物车的商品的具体信息
            List<TuBaMallGoods> tuBaMallGoods = tuBaMallGoodsMapper.selectByPrimaryKeys(tuBaMallGoodsIds);
            //key为goodsId，value代表对应商品，把数据读取到map避免之后多次读取
            Map<Long, TuBaMallGoods> tuBaMallGoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(tuBaMallGoods)) {
                tuBaMallGoodsMap = tuBaMallGoods.stream().collect(Collectors.toMap(TuBaMallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }
            for (TuBaMallShoppingCartItem tuBaMallShoppingCartItem : tuBaMallShoppingCartItems) {
                //最终都要封装到对应的VO中传给前端（因为我们不需要goods中的所有信息，只想在购物车item的基础上再通过goods_Id得到商品的主图，名字，售价）
                TuBaMallShoppingCartItemVO tuBaMallShoppingCartItemVO = new TuBaMallShoppingCartItemVO();
                BeanUtil.copyProperties(tuBaMallShoppingCartItem, tuBaMallShoppingCartItemVO);
                //理论上这个if应该全部位true
                if (tuBaMallGoodsMap.containsKey(tuBaMallShoppingCartItem.getGoodsId())) {
                    TuBaMallGoods tuBaMallGoodsTemp = tuBaMallGoodsMap.get(tuBaMallShoppingCartItem.getGoodsId());
                    tuBaMallShoppingCartItemVO.setGoodsCoverImg(tuBaMallGoodsTemp.getGoodsCoverImg());
                    String goodsName = tuBaMallGoodsTemp.getGoodsName();
                    // 字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    tuBaMallShoppingCartItemVO.setGoodsName(goodsName);
                    tuBaMallShoppingCartItemVO.setSellingPrice(tuBaMallGoodsTemp.getSellingPrice());
                    tuBaMallShoppingCartItemVOS.add(tuBaMallShoppingCartItemVO);
                }
            }
        }
        return tuBaMallShoppingCartItemVOS;
    }
}
