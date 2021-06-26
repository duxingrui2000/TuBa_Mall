package com.tuba.mall.dao;

import com.tuba.mall.entity.IndexConfig;
import com.tuba.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author dxr
 * @Description 首页配置(根据首页配置的主键检索，注意同一个商品可以出现在不同type中)
 * @Date 19:35 6.24
 **/
public interface IndexConfigMapper {
    int deleteByPrimaryKey(Long configId);
    
    int insert(IndexConfig record);
    
    int insertSelective(IndexConfig record);
    
    IndexConfig selectByPrimaryKey(Long configId);
    
    IndexConfig selectByTypeAndGoodsId(@Param("configType") int configType, @Param("goodsId") Long goodsId);
    
    int updateByPrimaryKeySelective(IndexConfig record);
    
    int updateByPrimaryKey(IndexConfig record);
    
    List<IndexConfig> findIndexConfigList(PageQueryUtil pageUtil);
    
    int getTotalIndexConfigs(PageQueryUtil pageUtil);
    
    int deleteBatch(Long[] ids);
    
    List<IndexConfig> findIndexConfigsByTypeAndNum(@Param("configType") int configType, @Param("number") int number);
}
