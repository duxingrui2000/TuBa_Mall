package com.tuba.mall.service.impl;

import com.tuba.mall.common.ServiceResultEnum;
import com.tuba.mall.common.TuBaMallCategoryLevelEnum;
import com.tuba.mall.controller.vo.TuBaMallSearchGoodsVO;
import com.tuba.mall.dao.GoodsCategoryMapper;
import com.tuba.mall.dao.TuBaMallGoodsMapper;
import com.tuba.mall.entity.GoodsCategory;
import com.tuba.mall.entity.TuBaMallGoods;
import com.tuba.mall.service.TuBaMallGoodsService;
import com.tuba.mall.util.BeanUtil;
import com.tuba.mall.util.PageQueryUtil;
import com.tuba.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class TuBaMallGoodsServiceImpl implements TuBaMallGoodsService {
    
    @Autowired
    private TuBaMallGoodsMapper goodsMapper;
    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;
    
    @Override
    public PageResult getTuBaMallGoodsPage(PageQueryUtil pageUtil) {
        List<TuBaMallGoods> goodsList = goodsMapper.findTuBaMallGoodsList(pageUtil);
        int total = goodsMapper.getTotalTuBaMallGoods(pageUtil);
        PageResult pageResult = new PageResult(goodsList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    
    @Override
    public String saveTuBaMallGoods(TuBaMallGoods goods) {
        GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(goods.getGoodsCategoryId());
        // 分类不存在或者不是三级分类，则该参数字段异常
        if (goodsCategory == null || goodsCategory.getCategoryLevel().intValue() != TuBaMallCategoryLevelEnum.LEVEL_THREE.getLevel()) {
            return ServiceResultEnum.GOODS_CATEGORY_ERROR.getResult();
        }
        if (goodsMapper.selectByCategoryIdAndName(goods.getGoodsName(), goods.getGoodsCategoryId()) != null) {
            return ServiceResultEnum.SAME_GOODS_EXIST.getResult();
        }
        if (goodsMapper.insertSelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }
    
    @Override
    public void batchSaveTuBaMallGoods(List<TuBaMallGoods> tuBaMallGoodsList) {
        if (!CollectionUtils.isEmpty(tuBaMallGoodsList)) {
            goodsMapper.batchInsert(tuBaMallGoodsList);
        }
    }
    
    @Override
    public String updateTuBaMallGoods(TuBaMallGoods goods) {
        GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(goods.getGoodsCategoryId());
        // 分类不存在或者不是三级分类，则该参数字段异常
        if (goodsCategory == null || goodsCategory.getCategoryLevel().intValue() != TuBaMallCategoryLevelEnum.LEVEL_THREE.getLevel()) {
            return ServiceResultEnum.GOODS_CATEGORY_ERROR.getResult();
        }
        TuBaMallGoods temp = goodsMapper.selectByPrimaryKey(goods.getGoodsId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        TuBaMallGoods temp2 = goodsMapper.selectByCategoryIdAndName(goods.getGoodsName(), goods.getGoodsCategoryId());
        if (temp2 != null && !temp2.getGoodsId().equals(goods.getGoodsId())) {
            //name和分类id相同且不同id 不能继续修改
            return ServiceResultEnum.SAME_GOODS_EXIST.getResult();
        }
        goods.setUpdateTime(new Date());
        if (goodsMapper.updateByPrimaryKeySelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }
    
    @Override
    public TuBaMallGoods getTuBaMallGoodsById(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }
    
    @Override
    public Boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {
        return goodsMapper.batchUpdateSellStatus(ids, sellStatus) > 0;
    }
    
    @Override
    public PageResult searchTuBaMallGoods(PageQueryUtil pageUtil) {
        List<TuBaMallGoods> goodsList = goodsMapper.findTuBaMallGoodsListBySearch(pageUtil);
        int total = goodsMapper.getTotalTuBaMallGoodsBySearch(pageUtil);
        List<TuBaMallSearchGoodsVO> tuBaMallSearchGoodsVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsList)) {
            tuBaMallSearchGoodsVOS = BeanUtil.copyList(goodsList, TuBaMallSearchGoodsVO.class);
            for (TuBaMallSearchGoodsVO tuBaMallSearchGoodsVO : tuBaMallSearchGoodsVOS) {
                String goodsName = tuBaMallSearchGoodsVO.getGoodsName();
                String goodsIntro = tuBaMallSearchGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 28) {
                    goodsName = goodsName.substring(0, 28) + "...";
                    tuBaMallSearchGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 30) {
                    goodsIntro = goodsIntro.substring(0, 30) + "...";
                    tuBaMallSearchGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        PageResult pageResult = new PageResult(tuBaMallSearchGoodsVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
}
