package cn.mbtt.service.service.impl;

import cn.mbtt.service.mapper.UserMapper;
import cn.mbtt.service.pojo.LoginInfo;
import cn.mbtt.service.pojo.Role;
import cn.mbtt.service.pojo.User;
import cn.mbtt.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void save(User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setRole(Role.user);
        user.setStatus(1);

        userMapper.insert(user);
    }

    @Override
    public LoginInfo login(User user) {
//1. 调用 mapper 接口，根据用户名和密码查询员工信息
        User u = userMapper.selectByUsernameAndPassword(user);
//2. 判断：判断是否存在这个员工，如果存在，组装登录成功信息
       if (u != null) {
           log.info("登陆成功，员工信息：{}", u);
           return new LoginInfo(u.getId(),u.getUsername(),"");
       }
//3. 不存在，返回 null
        return null;
    }
}

