package cn.mbtt.service.controller;
import cn.mbtt.common.result.CommonResult;
import cn.mbtt.common.result.Result;
import cn.mbtt.service.domain.po.Users;
import cn.mbtt.service.service.UserService;
import cn.mbtt.service.service.impl.UserServiceImpl;
import cn.mbtt.service.utils.JwtTokenUtil;
import cn.mbtt.service.utils.MD5;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Api(tags = "用户相关接口")
@RequestMapping("/users")
@RestController
@Slf4j
public class UserController  {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserService userService;

    @ApiOperation("新增用户")
    @PostMapping("/save")
    public Result save(@RequestBody  @Valid @ApiParam("用户信息") Users user){
        log.info("请求参数user：{}", user);
        try {
            userService.save(user);
            return Result.success("用户新增成功");
        } catch (Exception e) {
            log.error("新增用户失败: {}", e.getMessage());
            return Result.error("新增用户失败: " + e.getMessage());
        }
    }

    @ApiOperation("根据ID获取用户信息")
    @GetMapping("/{id}")
    public Users getUserById(@PathVariable @ApiParam("用户ID") Long id) {
        log.info("请求参数id：{}", id);
        return userService.getUserById(id);
    }


    @ApiOperation("spring security获取用户信息信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult info(Principal principal) {
        if (principal == null) {
            return CommonResult.unauthorized(null);
        }
        Users users = userService.getCurrentUser();
        return CommonResult.success(users);
    }

    @ApiOperation("更新用户密码的处理器")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @ApiParam("token口令") @RequestHeader("Authorization") String authHeader,
            @ApiParam("旧密码") @PathVariable("oldPwd") String oldPwd,
            @ApiParam("新密码") @PathVariable("newPwd") String newPwd
    ){
        LOGGER.info("Received oldPwd: {}, newPwd: {}", oldPwd, newPwd);
        if (!authHeader.startsWith("Bearer ")) {
            return Result.error("无效的token格式，应为 Bearer <token>");
        }
        String token = authHeader.substring("Bearer ".length());
        boolean expiration = jwtTokenUtil.isTokenExpired(token);
        if (expiration) {
            // token过期
            return Result.error("token失效,请重新登录后修改密码");
        }
        // 获取用户ID和用类型
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        if (userId == null) {
            LOGGER.warn("无法从token解析userId: {}", token);
            return Result.error("无法获取用户ID，token无效");
        }
        return userService.updatePassword(userId, oldPwd, newPwd);
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
