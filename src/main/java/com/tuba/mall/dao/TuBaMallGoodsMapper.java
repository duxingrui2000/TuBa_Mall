package com.tuba.mall.dao;

import com.tuba.mall.entity.StockNumDTO;
import com.tuba.mall.entity.TuBaMallGoods;
import com.tuba.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author dxr
 * @Description 对商品的操作
 * @Date 19:46 6.24
 **/
public interface TuBaMallGoodsMapper {
    int deleteByPrimaryKey(Long goodsId);
    
    int insert(TuBaMallGoods record);
    
    int insertSelective(TuBaMallGoods record);
    
    TuBaMallGoods selectByPrimaryKey(Long goodsId);
    
    TuBaMallGoods selectByCategoryIdAndName(@Param("goodsName") String goodsName, @Param("goodsCategoryId") Long goodsCategoryId);
    
    int updateByPrimaryKeySelective(TuBaMallGoods record);
    
    int updateByPrimaryKeyWithBLOBs(TuBaMallGoods record);
    
    int updateByPrimaryKey(TuBaMallGoods record);
    
    List<TuBaMallGoods> findTuBaMallGoodsList(PageQueryUtil pageUtil);
    
    int getTotalTuBaMallGoods(PageQueryUtil pageUtil);
    
    List<TuBaMallGoods> selectByPrimaryKeys(List<Long> goodsIds);
    
    List<TuBaMallGoods> findTuBaMallGoodsListBySearch(PageQueryUtil pageUtil);
    
    int getTotalTuBaMallGoodsBySearch(PageQueryUtil pageUtil);
    
    int batchInsert(@Param("TuBaMallGoodsList") List<TuBaMallGoods> TuBaMallGoodsList);
    
    //
    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);
    
    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds,@Param("sellStatus") int sellStatus);
}
