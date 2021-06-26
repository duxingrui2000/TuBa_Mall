package com.tuba.mall.service.impl;

import com.tuba.mall.dao.AdminUserMapper;
import com.tuba.mall.entity.AdminUser;
import com.tuba.mall.service.AdminUserService;
import com.tuba.mall.util.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author dxr
 * @Description 管理员登录修改资料
 * @Date 10:12 6.23
 **/
@Service
public class AdminUserServiceImpl implements AdminUserService {
    
    @Resource
    private AdminUserMapper adminUserMapper;
    
    @Override
    public AdminUser login(String userName, String password) {
        String passwordMd5 = MD5Util.MD5Encode(password, "UTF-8");
        return adminUserMapper.login(userName, passwordMd5);
    }
    
    @Override
    public AdminUser getUserDetailById(Integer loginUserId) {
        return adminUserMapper.selectByPrimaryKey(loginUserId);
    }
    
    @Override
    public Boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword) {
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(loginUserId);
        //当前用户非空才可以进行更改
        if (adminUser != null) {
            String orginalPassword = MD5Util.MD5Encode(originalPassword, "UTF-8");
            //登录密码与输入的初始密码相同，允许更新
            if (orginalPassword.equals(adminUser.getLoginPassword())) {
                //加密输入的新密码
                String newPasswordMd5 = MD5Util.MD5Encode(newPassword, "UTF-8");
                adminUser.setLoginPassword(newPasswordMd5);
                //将更新后的adminUser更新回数据库
                if (adminUserMapper.updateByPrimaryKeySelective(adminUser) > 0) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public Boolean updateName(Integer loginUserId, String loginUserName, String nickName) {
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(loginUserId);
        //当前用户非空才可以进行更改
        if (adminUser != null) {
            //设置新名称并修改
            adminUser.setLoginUserName(loginUserName);
            adminUser.setNickName(nickName);
            if (adminUserMapper.updateByPrimaryKeySelective(adminUser) > 0) {
                //修改成功则返回true
                return true;
            }
        }
        return false;
    }
}
