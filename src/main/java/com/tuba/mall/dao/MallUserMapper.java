package com.tuba.mall.dao;

import com.tuba.mall.entity.MallUser;
import com.tuba.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author dxr
 * @Description 商城用户管理dao层
 * @Date 11:34 6.24
 **/
public interface MallUserMapper {
    int deleteByPrimaryKey(Long userId);
    
    int insert(MallUser record);
    
    int insertSelective(MallUser record);
    
    MallUser selectByPrimaryKey(Long userId);
    
    MallUser selectByLoginName(String loginName);
    
    MallUser selectByLoginNameAndPasswd(@Param("loginName") String loginName, @Param("password") String password);
    
    int updateByPrimaryKeySelective(MallUser record);
    
    int updateByPrimaryKey(MallUser record);
    
    List<MallUser> findMallUserList(PageQueryUtil pageUtil);
    
    int getTotalMallUsers(PageQueryUtil pageUtil);
    
    int lockUserBatch(@Param("ids") Integer[] ids, @Param("lockStatus") int lockStatus);
}
