package cn.mbtt.service.mapper;

import cn.mbtt.service.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    /**
     * 新增员工基本信息
     */
    @Insert("insert into users(username, password, email, phone, avatar_url, deleted_at)" +
            "values (#{username},#{password},#{email},#{phone},#{avatarUrl},#{deletedAt})")
    void insert(User user);

    /**
     * 根据用户名和密码查询员工信息
     */
    @Select("select id, username, password from users where username = #{username} and password = #{password}")
    User selectByUsernameAndPassword(User user);
}
