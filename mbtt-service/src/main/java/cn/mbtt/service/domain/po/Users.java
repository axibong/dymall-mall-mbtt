package cn.mbtt.service.domain.po;

import cn.mbtt.service.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户表实体类，用于映射数据库中的 users 表。
 * <p>
 * 该类包含了用户的基本信息、角色信息、状态信息以及时间戳字段。
 * </p>
 *
 * @author axi
 * @version 1.0
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID，唯一标识用户的自增主键。
     */
    private Long id;

    /**
     * 用户名，唯一，最大长度为50字符。
     */
    @NotNull(message = "用户名不能为空")
    private String username;

    /**
     * 用户密码，使用哈希存储。
     */
    @NotNull(message = "密码不能为空")
    private String password;

    /**
     * 用户电子邮件地址，唯一，最大长度为100字符。
     */
    private String email;

    /**
     * 用户手机号，可选字段。
     */
    private String phone;

    /**
     * 用户头像的URL，可选字段。
     */
    private String avatarUrl;

    /**
     * 用户角色，默认为普通用户（user）。
     * <p>
     * 可选值为：
     * <ul>
     *     <li>user - 普通用户</li>
     *     <li>admin - 管理员</li>
     * </ul>
     */
    private String role;

    /**
     * 用户状态：
     * <ul>
     *     <li>1 - 活跃</li>
     *     <li>0 - 停用</li>
     *     <li>-1 - 已删除</li>
     * </ul>
     */
    private Integer status;

    /**
     * 记录创建时间，默认为当前时间。
     */
    private LocalDateTime createdAt;

    /**
     * 记录最近更新时间，默认为当前时间，并在更新时自动刷新。
     */
    private LocalDateTime updatedAt;

    /**
     * 记录删除时间，若为NULL表示未删除。
     */
    private LocalDateTime deletedAt;
}