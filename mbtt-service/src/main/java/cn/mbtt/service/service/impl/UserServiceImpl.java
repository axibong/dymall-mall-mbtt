package cn.mbtt.service.service.impl;

import cn.mbtt.common.exception.ForbiddenException;
import cn.mbtt.common.exception.UnauthorizedException;
import cn.mbtt.common.result.Result;
import cn.mbtt.service.domain.dto.UserSaveDTO;
import cn.mbtt.service.domain.po.LoginInfo;
import cn.mbtt.service.domain.po.Users;
import cn.mbtt.service.enums.Role;
import cn.mbtt.service.mapper.UserMapper;
import cn.mbtt.service.service.UserService;
import cn.mbtt.service.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Override
    public void save(Users user) {
        // 参数校验
        if (user == null) {
            throw new IllegalArgumentException("用户信息不能为空");
        }
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setRole(String.valueOf(Role.user));
        user.setStatus(1);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 插入数据库
        int rows = userMapper.insert(user);
        if (rows != 1) {
            throw new RuntimeException("用户插入失败");
        }
    }


    @Override
    public LoginInfo login(String username, String password) {
        Users u = userMapper.selectByUsername(username);
        if (u == null) {
            throw new BadCredentialsException("用户名不存在");
        }
        LOGGER.info("Login user before token generation: {}", u); // 调试
        try {
            if (!passwordEncoder.matches(password, u.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            if (u.getStatus() != 1) {
                throw new ForbiddenException("用户被冻结");
            }
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(u, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenUtil.generateToken(u);
            LOGGER.info("Generated token: {}", token);
            LOGGER.info("登陆成功，员工信息：{}", u);
            return new LoginInfo(u.getId(), u.getUsername(), token);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
            throw e;
        }
    }
//    @Override
//    public String refreshToken(String token) {
//        return jwtTokenUtil.refreshHeadToken(token);
//    }


    @Override
    public Users getUserById(Long id) {
        //参数id为包装类型，有可能为null，所以需要排除id为null的情况
        if (id == null) {
            return null;
        }
        //1. 调用 mapper 接口，根据id查询员工信息，包括：id、username、email、phone、role和创建时间
        Users user = userMapper.getUserById(id);
        //2. 判断：判断是否存在这个员工，如果存在，组装查询结果
        if (user != null) {
            log.info("查询的员工信息为：{}", user);
            return user;
        }
        //3. 不存在，返回 null
        return null;
    }



    @Override
    public Users getCurrentUser() {
        // 获取当前的安全上下文
        SecurityContext ctx = SecurityContextHolder.getContext();

        // 获取当前的身份认证信息
        Authentication auth = ctx.getAuthentication();
        System.out.println("Auth: " + auth); // 调试输出
        // 检查身份认证信息是否存在以及用户是否已认证
        if (auth == null || !auth.isAuthenticated()) {
            // 如果没有认证或未认证，抛出未认证异常
            throw new UnauthorizedException("用户未认证");
        }

        // 从 Authentication 对象中获取当前用户的主体（principal），并返回
        // principal 通常是一个包含用户信息的对象，这里是 Users 对象
        return (Users) auth.getPrincipal();
    }

    @Override
    public Result updatePassword(Long userId, String oldPwd, String newPwd) {
        if (userId == null || oldPwd == null || newPwd == null) {
            return Result.error("参数不能为空");
        }

        // 查询用户信息
        Users users = userMapper.getUserById(userId);
        if (users == null) {
            return Result.error("用户不存在");
        }
        LOGGER.info("User password from DB: {}", users.getPassword());
        if (!passwordEncoder.matches(oldPwd, users.getPassword())) {
            return Result.error("原密码有误!");
        }

        // 加密新密码并更新
        String encodedNewPwd = passwordEncoder.encode(newPwd);
        int rows = userMapper.updatePassword(userId.intValue(), encodedNewPwd);

        if (rows > 0) {
            return Result.success();
        } else {
            return Result.error("密码更新失败!");
        }
    }

}

