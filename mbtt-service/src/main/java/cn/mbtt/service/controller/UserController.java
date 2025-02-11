package cn.mbtt.service.controller;

import cn.mbtt.service.pojo.Result;
import cn.mbtt.service.pojo.User;
import cn.mbtt.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    //新增用户
    @PostMapping("/save")
    public Result save(@RequestBody User user, boolean isAdmin) {
        log.info("请求参数user：{}", user, "请求参数isAdmin：{}", isAdmin);
        userService.save(user, isAdmin);
        return Result.success();
    }

    //获取用户信息
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        log.info("请求参数id：{}", id);
        return userService.getUserById(id);
    }

    //删除用户
    @DeleteMapping("{/delete}")
    public Result deleteUserById(@PathVariable Integer id) {
        log.info("请求参数id：{}", id);
        userService.deleteUserById(id);
        return Result.success();
    }

    //修改密码
    @PostMapping("{changepassword}")
    public Result changePassword(@RequestParam Integer id,
                                 String oldPassword,
                                 String newPassword) {
        boolean isSuccess = userService.changePassword(id, oldPassword, newPassword);
        if (isSuccess) {
            return Result.success("密码修改成功！");
        }
        return Result.error("密码修改失败！");
    }

}




