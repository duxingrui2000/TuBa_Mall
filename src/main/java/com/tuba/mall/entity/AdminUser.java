package com.tuba.mall.entity;

/**
 * @Author dxr
 * @Description 管理员实体表,目前没用lombok插件，所有get与set生成一下
 * @Date 17:19 6.21
 * 建表sql
 * CREATE TABLE `tuba_mall_admin_user` (
 * `admin_user_id` int NOT NULL AUTO_INCREMENT COMMENT '管理员id',
 * `login_user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员登陆名称',
 * `login_password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员登陆密码',
 * `nick_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员显示昵称',
 * `locked` tinyint DEFAULT '0' COMMENT '是否锁定 0未锁定 1已锁定无法登陆',
 * PRIMARY KEY (`admin_user_id`) USING BTREE
 * ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
 **/
public class AdminUser {
    private Integer adminUserId;
    
    private String loginUserName;
    
    private String loginPassword;
    
    private String nickName;
    
    private Byte locked;
    
    public Integer getAdminUserId() {
        return adminUserId;
    }
    
    public void setAdminUserId(Integer adminUserId) {
        this.adminUserId = adminUserId;
    }
    
    public String getLoginUserName() {
        return loginUserName;
    }
    
    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName == null ? null : loginUserName.trim();
    }
    
    public String getLoginPassword() {
        return loginPassword;
    }
    
    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword == null ? null : loginPassword.trim();
    }
    
    public String getNickName() {
        return nickName;
    }
    
    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }
    
    public Byte getLocked() {
        return locked;
    }
    
    public void setLocked(Byte locked) {
        this.locked = locked;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", adminUserId=").append(adminUserId);
        sb.append(", loginUserName=").append(loginUserName);
        sb.append(", loginPassword=").append(loginPassword);
        sb.append(", nickName=").append(nickName);
        sb.append(", locked=").append(locked);
        sb.append("]");
        return sb.toString();
    }
}