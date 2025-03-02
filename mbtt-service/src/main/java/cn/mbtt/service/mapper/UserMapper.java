package cn.mbtt.service.mapper;

import cn.mbtt.service.domain.po.Users;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    /**
     * 新增用户基本信息
     *
     * @return
     */
    @Insert("insert into users(username, password, email, phone, avatar_url, deleted_at)" +
            "values (#{username},#{password},#{email},#{phone},#{avatarUrl},#{deletedAt})")
    int insert(Users user);

    /**
     * 根据id查询员工信息
     */
    @Select("SELECT id, username, password , email, phone, role, created_at FROM users WHERE id = #{id}")
    Users getUserById(Long id);

    /**
     * 根据id和password查询用户信息
     */
    @Select("SELECT id, username, password FROM users WHERE id = #{id} AND password = #{password}")
    Users findByIdAndPassword(@Param("id") int id, @Param("password") String password);

    /**
     * 更新用户密码
     */
    @Update("UPDATE users SET password = #{newPwd} WHERE id = #{id}")
    int updatePassword(@Param("id") int id, @Param("newPwd") String newPwd);

    /**
         * 根据用户名查询用户信息
         * @param username 用户名
         * @return 返回 Users 对象
         */
    @Select("SELECT * FROM users WHERE username = #{username}")
    Users selectByUsername(String username);

}

