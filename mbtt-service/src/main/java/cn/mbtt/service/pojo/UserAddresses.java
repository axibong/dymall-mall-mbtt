package cn.mbtt.service.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddresses {
    private Integer id;
    private Integer userId;
    private String recipientName;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detailedAddress;
    private Boolean isDefault;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

}

