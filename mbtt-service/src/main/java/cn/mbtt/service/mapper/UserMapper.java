package cn.mbtt.service.mapper;

import cn.mbtt.service.pojo.Role;
import cn.mbtt.service.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    /**
     * 新增员工基本信息
     */
    @Insert("INSERT INTO users(username, password, email, phone, avatar_url, created_at, updated_at)" +
            "VALUES (#{username},#{password},#{email},#{phone},#{avatarUrl},#{createdAt}, #{updatedAt})")
    void insert(User user);

    /**
     * 根据用户名和密码查询员工信息
     */
    @Select("SELECT id, username, password FROM users WHERE username = #{username} and password = #{password}")
    User selectByUsernameAndPassword(User user);

    /**
     * 根据id查询员工信息
     */
    @Select("SELECT id, username, email, phone, role, created_at FROM users WHERE id = #{id}")
    User getUserById(Integer id);

    /**
     * 根据id删除员工信息
     */
    @Delete("DELETE FROM users WHERE id = #{id};")
    void deleteUserById(Integer id);

    @Update("UPDATE users SET password = #{newPassword} WHERE id =#{id} ")
    void changePassword(Integer id, String newPassword);
}

