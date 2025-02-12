package cn.mbtt.service.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装登录结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfo {
    private Long id;
    private String username;
    private String token;

}
