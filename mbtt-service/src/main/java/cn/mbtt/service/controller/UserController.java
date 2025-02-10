package cn.mbtt.service.controller;
import cn.mbtt.service.pojo.Result;
import cn.mbtt.service.pojo.User;
import cn.mbtt.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/users")
@RestController
@Slf4j
public class UserController  {

    @Autowired
    private UserService userService;

    //新增用户
    @PostMapping
    public Result save(@RequestBody User user){
        log.info("请求参数user：{}", user);
        userService.save(user);
        return Result.success();
    }

}
