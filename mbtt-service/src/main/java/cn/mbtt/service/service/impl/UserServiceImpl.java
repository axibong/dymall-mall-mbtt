package cn.mbtt.service.service.impl;

import cn.mbtt.common.exception.ForbiddenException;
import cn.mbtt.common.exception.UnauthorizedException;
import cn.mbtt.common.result.Result;
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
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setRole(String.valueOf(Role.user));
        user.setStatus(1);

        userMapper.insert(user);
    }


    @Override
    public LoginInfo login(String username, String password) {
        String token = null;
        Users u = userMapper.selectByUsernameAndPassword(username);

        // 校验用户是否存在
        if (u == null) {
            throw new BadCredentialsException("用户名不存在");
        }

        try {
            // TODO 测试密码并不是加密的密码，不用工具类验证  1. 校验密码

//            if (!passwordEncoder.matches(password, u.getPassword())) {
//                throw new BadCredentialsException("密码不正确");
//            }

            // 2. 校验用户是否被冻结
            if (u.getStatus() != 1) {
                throw new ForbiddenException("用户被冻结");
            }

            // 3. 创建认证对象并将其设置到上下文中
            // 这里没有传递权限信息，可以传 null
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(u, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 4. 生成 JWT Token
            token = jwtTokenUtil.generateToken(u);

            LOGGER.info("登陆成功，员工信息：{}", u);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
            throw e;  // 抛出异常，交给全局异常处理
        }

        return new LoginInfo(u.getId(), u.getUsername(), token);
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
        // 查询用户信息
        Users users = userMapper.findByIdAndPassword(userId.intValue(), oldPwd);

        // 如果 不存在，返回失败
        if (users == null) {
            return Result.error("原密码有误!");
        }

        // 执行密码更新
        int rows = userMapper.updatePassword(userId.intValue(), oldPwd,newPwd);

        // 判断更新是否成功
        if (rows > 0) {
            return Result.success();
        } else {
            return Result.error("密码更新失败!");
        }
    }

}

