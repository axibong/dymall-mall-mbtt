package cn.mbtt.service.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户信息实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String avatarUrl;
    private Role role;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    //封装用户地址信息
    private List<UserAddresses> userAddressesList;

    public User(Integer id, String username, String email, String phone, Role role, LocalDateTime createdAt) {
    }
}
