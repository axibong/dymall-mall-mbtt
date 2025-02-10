package cn.mbtt.service.controller;

import cn.mbtt.service.pojo.LoginInfo;
import cn.mbtt.service.pojo.Result;
import cn.mbtt.service.pojo.User;
import cn.mbtt.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
* 登陆接口
*/
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 登陆方法
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user){

        LoginInfo info = userService.login(user);

        if (info != null) {
            return Result.success(info);
        }
        return Result.error("用户名或密码错误");
    }

}
