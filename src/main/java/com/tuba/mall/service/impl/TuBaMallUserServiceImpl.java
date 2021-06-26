package com.tuba.mall.service.impl;

import com.tuba.mall.common.Constants;
import com.tuba.mall.common.ServiceResultEnum;
import com.tuba.mall.controller.vo.TuBaMallUserVO;
import com.tuba.mall.dao.MallUserMapper;
import com.tuba.mall.entity.MallUser;
import com.tuba.mall.service.TuBaMallUserService;
import com.tuba.mall.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.List;
@Service
public class TuBaMallUserServiceImpl implements TuBaMallUserService {
    
    @Autowired
    private MallUserMapper mallUserMapper;
    
    @Override
    public PageResult getTuBaMallUsersPage(PageQueryUtil pageUtil) {
        //pageUtil里面封装了page，start，limit，loginName等信息
        List<MallUser> mallUsers = mallUserMapper.findMallUserList(pageUtil);
        int total = mallUserMapper.getTotalMallUsers(pageUtil);
        PageResult pageResult = new PageResult(mallUsers, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    
    @Override
    public String register(String loginName, String password) {
        //先确保loginName之前未被别人注册过
        if (mallUserMapper.selectByLoginName(loginName) != null) {
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        //初始设置昵称就位loginName，密码要先加密，然后存储在数据库中去
        MallUser registerUser = new MallUser();
        registerUser.setLoginName(loginName);
        registerUser.setNickName(loginName);
        String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");
        registerUser.setPasswordMd5(passwordMD5);
        if (mallUserMapper.insertSelective(registerUser) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }
    
    @Override
    public String login(String loginName, String passwordMD5, HttpSession httpSession) {
        //根据登录名和密码先判断是是否有此人
        MallUser user = mallUserMapper.selectByLoginNameAndPasswd(loginName, passwordMD5);
        if (user != null && httpSession != null) {
            //确保该用户未被禁止登陆
            if (user.getLockedFlag() == 1) {
                return ServiceResultEnum.LOGIN_USER_LOCKED.getResult();
            }
            //昵称太长 影响页面展示
            if (user.getNickName() != null && user.getNickName().length() > 7) {
                String tempNickName = user.getNickName().substring(0, 7) + "..";
                user.setNickName(tempNickName);
            }
            TuBaMallUserVO tuBaMallUserVO = new TuBaMallUserVO();
            BeanUtil.copyProperties(user, tuBaMallUserVO);
            //走到这一步说明可以登录成功（前端已经处理各个字段为空的情况，controller中处理验证码的准确性，这里判断
            //用户名和密码的准确性，之后设置session中MALL_USER_SESSION_KEY，这里传success给controller，controller
            //了解了允许登录，于是返回ResultGenerator.genSuccessResult()，login.html得到result.resultCode == 200，于是
            //请求index首页，此时拦截器会拦截下来获取用户的id，进而根据用户id得到购物车中的商品数量，达到一种一进首页就有数据的
            //展示效果
            httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, tuBaMallUserVO);
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }
    
    @Override
    public TuBaMallUserVO updateUserInfo(MallUser mallUser, HttpSession httpSession) {
        TuBaMallUserVO userTemp = (TuBaMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        MallUser userFromDB = mallUserMapper.selectByPrimaryKey(userTemp.getUserId());
        if (userFromDB != null) {
            if (!StringUtils.isEmpty(mallUser.getNickName())) {
                userFromDB.setNickName(TuBaMallUtils.cleanString(mallUser.getNickName()));
            }
            if (!StringUtils.isEmpty(mallUser.getAddress())) {
                userFromDB.setAddress(TuBaMallUtils.cleanString(mallUser.getAddress()));
            }
            if (!StringUtils.isEmpty(mallUser.getIntroduceSign())) {
                userFromDB.setIntroduceSign(TuBaMallUtils.cleanString(mallUser.getIntroduceSign()));
            }
            if (mallUserMapper.updateByPrimaryKeySelective(userFromDB) > 0) {
                TuBaMallUserVO tuBaMallUserVO = new TuBaMallUserVO();
                userFromDB = mallUserMapper.selectByPrimaryKey(mallUser.getUserId());
                BeanUtil.copyProperties(userFromDB, tuBaMallUserVO);
                httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, tuBaMallUserVO);
                return tuBaMallUserVO;
            }
        }
        return null;//这里也可以不返回对象给controller，直接返回string告诉控制层是否update成功即可
    }
    
    @Override
    public Boolean lockUsers(Integer[] ids, int lockStatus) {
        if (ids.length < 1){
            return false;
        }
        //批量进制用户登录
        return mallUserMapper.lockUserBatch(ids, lockStatus) > 0;
    }
}
