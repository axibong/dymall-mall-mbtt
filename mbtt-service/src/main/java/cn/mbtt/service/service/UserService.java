package cn.mbtt.service.service;

import cn.mbtt.common.result.Result;
import cn.mbtt.service.domain.dto.UserSaveDTO;
import cn.mbtt.service.domain.po.LoginInfo;
import cn.mbtt.service.domain.po.Users;

public interface UserService {
    Users getCurrentUser() ;


    //创建用户
    void save(UserSaveDTO user);

    //用户登陆
    LoginInfo login(String username, String password);

    Users getUserById(Long id);

//    String refreshToken(String token);

    Result updatePassword(Long userId, String oldPwd, String newPwd);
}

