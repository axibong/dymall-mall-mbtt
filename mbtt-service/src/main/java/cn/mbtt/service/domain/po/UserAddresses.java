package cn.mbtt.service.domain.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户地址实体类，用于映射数据库中的 user_addresses 表。
 * <p>
 * 该类包含了用户地址的基本信息、用户ID以及时间戳字段。
 * </p>
 *
 * @author axi
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddresses implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 地址ID，唯一标识地址的自增主键。
     */
    private Long id;

    /**
     * 用户ID，指向用户表的id字段。
     */
    private Long userId;

    /**
     * 收件人姓名。
     */
    private String recipientName;

    /**
     * 收件人手机号。
     */
    private String recipientPhone;

    /**
     * 省份。
     */
    private String province;

    /**
     * 城市。
     */
    private String city;

    /**
     * 区县。
     */
    private String district;

    /**
     * 详细地址。
     */
    private String detailAddress;

    /**
     * 邮政编码。
     */
    private String postalCode;

    /**
     * 是否为默认地址：
     * <ul>
     *     <li>1 - 默认地址</li>
     *     <li>0 - 非默认地址</li>
     * </ul>
     */
    private Integer isDefault;

    /**
     * 地址状态：
     * <ul>
     *     <li>1 - 有效</li>
     *     <li>0 - 无效</li>
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