package com.tuba.mall.service;

import com.tuba.mall.controller.vo.TuBaMallUserVO;
import com.tuba.mall.entity.MallUser;
import com.tuba.mall.util.PageQueryUtil;
import com.tuba.mall.util.PageResult;

import javax.servlet.http.HttpSession;

public interface TuBaMallUserService {
    /**
     * 后台分页
     */
    PageResult getTuBaMallUsersPage(PageQueryUtil pageUtil);

    /**
     * 用户注册
     */
    String register(String loginName, String password);

    /**
     * 登录
     */
    String login(String loginName, String passwordMD5, HttpSession httpSession);

    /**
     * 用户信息修改并返回最新的用户信息
     */
    TuBaMallUserVO updateUserInfo(MallUser mallUser, HttpSession httpSession);

    /**
     * 用户禁用与解除禁用(0-未锁定 1-已锁定)
     */
    Boolean lockUsers(Integer[] ids, int lockStatus);
}
