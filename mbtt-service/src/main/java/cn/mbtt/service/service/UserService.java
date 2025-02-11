package cn.mbtt.service.service;

import cn.mbtt.service.pojo.LoginInfo;
import cn.mbtt.service.pojo.Result;
import cn.mbtt.service.pojo.User;

public interface UserService {

    //创建用户
    void save(User user, Boolean isAdmin);

    //用户登陆
    LoginInfo login(User user);

    //获取用户信息
    User getUserById(Integer id);

    //删除用户
    Result deleteUserById(Integer id);

    //修改密码
    boolean changePassword(Integer id, String oldPassword, String newPassword);
}
