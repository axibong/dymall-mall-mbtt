package cn.mbtt.service.mapper;

import cn.mbtt.service.pojo.Role;
import cn.mbtt.service.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {

    /**
     * 新增员工基本信息
     */
    @Insert("insert into users(username, password, email, phone, avatar_url, created_at, updated_at)" +
            "values (#{username},#{password},#{email},#{phone},#{avatarUrl},#{createdAt}, #{updatedAt})")
    void insert(User user);

    /**
     * 根据用户名和密码查询员工信息
     */
    @Select("select id, username, password from users where username = #{username} and password = #{password}")
    User selectByUsernameAndPassword(User user);

    /**
     * 根据id查询员工信息
     */
    @Select("SELECT id, username, email, phone, role, created_at FROM users WHERE id = #{id}")
    User getUserById(Integer id);
}

