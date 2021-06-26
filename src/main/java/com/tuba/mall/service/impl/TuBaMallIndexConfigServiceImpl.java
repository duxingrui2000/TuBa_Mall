package com.tuba.mall.service.impl;

import com.tuba.mall.common.ServiceResultEnum;
import com.tuba.mall.controller.vo.TuBaMallIndexConfigGoodsVO;
import com.tuba.mall.dao.IndexConfigMapper;
import com.tuba.mall.dao.TuBaMallGoodsMapper;
import com.tuba.mall.entity.IndexConfig;
import com.tuba.mall.entity.TuBaMallGoods;
import com.tuba.mall.service.TuBaMallIndexConfigService;
import com.tuba.mall.util.BeanUtil;
import com.tuba.mall.util.PageQueryUtil;
import com.tuba.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class TuBaMallIndexConfigServiceImpl implements TuBaMallIndexConfigService {
    
    @Autowired
    private IndexConfigMapper indexConfigMapper;
    
    @Autowired
    private TuBaMallGoodsMapper goodsMapper;
    
    @Override
    public PageResult getConfigsPage(PageQueryUtil pageUtil) {
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigList(pageUtil);
        int total = indexConfigMapper.getTotalIndexConfigs(pageUtil);
        PageResult pageResult = new PageResult(indexConfigs, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    
    @Override
    public String saveIndexConfig(IndexConfig indexConfig) {
        if (goodsMapper.selectByPrimaryKey(indexConfig.getGoodsId()) == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        //不允许热销商品/新品上线/为你推荐同一模块中出现一件商品
        if (indexConfigMapper.selectByTypeAndGoodsId(indexConfig.getConfigType(), indexConfig.getGoodsId()) != null) {
            return ServiceResultEnum.SAME_INDEX_CONFIG_EXIST.getResult();
        }
        if (indexConfigMapper.insertSelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }
    
    @Override
    public String updateIndexConfig(IndexConfig indexConfig) {
        //判断参数中的id是否对应数据库中商品
        if (goodsMapper.selectByPrimaryKey(indexConfig.getGoodsId()) == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        //判断首页中是否存在该商品
        IndexConfig temp = indexConfigMapper.selectByPrimaryKey(indexConfig.getConfigId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        IndexConfig temp2 = indexConfigMapper.selectByTypeAndGoodsId(indexConfig.getConfigType(), indexConfig.getGoodsId());
        if (temp2 != null && !temp2.getConfigId().equals(indexConfig.getConfigId())) {
            //goodsId相同且不同id 不能继续修改
            return ServiceResultEnum.SAME_INDEX_CONFIG_EXIST.getResult();
        }
        indexConfig.setUpdateTime(new Date());
        if (indexConfigMapper.updateByPrimaryKeySelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }
    
    @Override
    public IndexConfig getIndexConfigById(Long id) {
        IndexConfig indexConfig = indexConfigMapper.selectByPrimaryKey(id);
        return indexConfig;
    }
    
    @Override
    public List<TuBaMallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number) {
        List<TuBaMallIndexConfigGoodsVO> tuBaMallIndexConfigGoodsVOS = new ArrayList<>(number);
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);
        if (!CollectionUtils.isEmpty(indexConfigs)) {
            //取出所有的goodsId
            List<Long> goodsIds = indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
            //取出id对应的商品
            List<TuBaMallGoods> tuBaMallGoods = goodsMapper.selectByPrimaryKeys(goodsIds);
            //往前端传递对象是TuBaMallIndexConfigGoodsVO，对过长的string作个处理
            tuBaMallIndexConfigGoodsVOS = BeanUtil.copyList(tuBaMallGoods, TuBaMallIndexConfigGoodsVO.class);
            for (TuBaMallIndexConfigGoodsVO tuBaMallIndexConfigGoodsVO : tuBaMallIndexConfigGoodsVOS) {
                String goodsName = tuBaMallIndexConfigGoodsVO.getGoodsName();
                String goodsIntro = tuBaMallIndexConfigGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 30) {
                    goodsName = goodsName.substring(0, 30) + "...";
                    tuBaMallIndexConfigGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 22) {
                    goodsIntro = goodsIntro.substring(0, 22) + "...";
                    tuBaMallIndexConfigGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        return tuBaMallIndexConfigGoodsVOS;
    }
    
    @Override
    public Boolean deleteBatch(Long[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除数据
        return indexConfigMapper.deleteBatch(ids) > 0;
    }
}
