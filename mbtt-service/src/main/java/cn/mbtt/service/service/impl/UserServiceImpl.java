package cn.mbtt.service.service.impl;

import cn.mbtt.service.mapper.UserMapper;
import cn.mbtt.service.pojo.LoginInfo;
import cn.mbtt.service.pojo.Result;
import cn.mbtt.service.pojo.Role;
import cn.mbtt.service.pojo.User;
import cn.mbtt.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 新增用户
     */
    @Override
    public void save(User user, Boolean isAdmin) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setStatus(1);
        //判断创建的是普通用户还是管理员
        if (isAdmin) {
            user.setRole(Role.admin);
        } else {
            user.setRole(Role.user);
        }
        userMapper.insert(user);
    }

    /**
     * 用户登录
     */
    @Override
    public LoginInfo login(User user) {
        //1. 调用 mapper 接口，根据用户名和密码查询员工信息
        User u = userMapper.selectByUsernameAndPassword(user);
        //2. 判断：判断是否存在这个员工，如果存在，组装登录成功信息
        if (u != null) {
            log.info("登陆成功，员工信息：{}", u);
            return new LoginInfo(u.getId(), u.getUsername(), "");
        }
        //3. 不存在，返回 null
        return null;
    }

    /**
     * 查询用户信息
     */
    @Override
    public User getUserById(Integer id) {
        //参数id为包装类型，有可能为null，所以需要排除id为null的情况
        if (id == null) {
            return null;
        }
        //1. 调用 mapper 接口，根据id查询员工信息，包括：id、username、email、phone、role和创建时间
        User user = userMapper.getUserById(id);
        //2. 判断：判断是否存在这个员工，如果存在，组装查询结果
        if (user != null) {
            log.info("查询的员工信息为：{}", user);
            return new User(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(), user.getRole(), user.getCreatedAt());
        }
        //3. 不存在，返回 null
        return null;
    }
    /**
     * 删除用户
     */
    @Override
    public Result deleteUserById(Integer id) {
        //参数id为包装类型，有可能为null，所以需要排除id为null的情况
        if (id == null) {
            return Result.error("用户ID不能为空，请重新输入");
        }
        //1.查询用户信息
        User user = userMapper.getUserById(id);
        //2. 判断是否存在这个员工，存在执行删除
        if (user != null) {
            userMapper.deleteUserById(id);
            return Result.success("ID为" + id + "的用户已删除");
        }
        //2. 不存在，返回 null
        return Result.error("用户不存在");
    }

    /**
     * 修改密码
     */
    @Override
    public boolean changePassword(Integer id, String oldPassword, String newPassword) {
        User user = getUserById(id);
        if (    user != null
                && StringUtils.equals(user.getPassword(), oldPassword)
                && !StringUtils.equals(oldPassword, newPassword)){
            userMapper.changePassword(id, newPassword);
            return true;
        }
        return false;
    }
}




