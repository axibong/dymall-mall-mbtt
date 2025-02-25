//package cn.mbtt.service.utils;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@SpringBootTest
//public class AuthTest {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Autowired
//    private JwtTokenUtil jwtUtil;  // 注入 JWT 工具类
//
//    @Test
//    public void testAuthentication() {
//        // 模拟一个用户
//        String username = "testUser";
//        String password = "testPassword";
//
//        // 获取用户信息
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//        // 验证密码是否匹配
//        assert passwordEncoder.matches(password, userDetails.getPassword());
//
//        // 进行身份验证
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(username, password)
//        );
//
//        // 确保用户被成功认证
//        assert authentication.isAuthenticated();
//
//        // 生成 JWT 令牌
//        String token = jwtUtil.generateToken(userDetails);
//
//        // 打印 JWT 令牌
//        System.out.println("Generated JWT Token: " + token);
//    }
//}
