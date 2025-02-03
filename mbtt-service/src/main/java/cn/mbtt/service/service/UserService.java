package cn.mbtt.service.service;

import cn.mbtt.service.pojo.LoginInfo;
import cn.mbtt.service.pojo.User;

public interface UserService {
    //创建用户
    void save(User user);

    //用户登陆
    LoginInfo login(User user);
}
