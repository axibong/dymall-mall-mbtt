package cn.mbtt.service.controller;
import cn.mbtt.service.pojo.Result;
import cn.mbtt.service.pojo.User;
import cn.mbtt.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;


@RequestMapping("/api")
@RestController
@Slf4j
public class UserController  {

    @Autowired
    private UserService userService;

    //新增用户
    @PostMapping("/users")
    public Result save(@RequestBody User user, boolean isAdmin){
        log.info("请求参数user：{}", user, "请求参数isAdmin：{}", isAdmin);
        userService.save(user, isAdmin);
        return Result.success();
    }

    //获取用户信息
    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("请求参数id：{}", id);
        return userService.getUserById(id);
    }
}

/*
用ResponseEntity<User>返回响应体和HTTP状态的方法，返回值类型为Optional<User>
但暂时不知道如何只返回部分成员变量
public ResponseEntity<User> getUserById(@PathVariable int id) {
    log.info("请求参数id：{}", id);
    Optional<User> userOptional = userService.getUserById(id);

    if (userOptional.isPresent()) {
        return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
    } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
*/
