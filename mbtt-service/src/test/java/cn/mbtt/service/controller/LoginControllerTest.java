package cn.mbtt.service.controller;

import cn.mbtt.common.result.Result;
import cn.mbtt.service.domain.po.LoginInfo;
import cn.mbtt.service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(LoginController.class)
//@ComponentScan("cn.mbtt.service")
@SpringBootTest(classes = LoginController.class)
@AutoConfigureMockMvc

public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private LoginController loginController;

    @Autowired
    private ObjectMapper objectMapper;

    private String validUsername = "testuser";
    private String validPassword = "password123";
    private String validVerifiCode = "1234";  // 假设验证码为1234

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin_Success() throws Exception {
        LoginInfo mockUserInfo = new LoginInfo();
        mockUserInfo.setUsername(validUsername);

        // 模拟 userService 登录方法
        when(userService.login(validUsername, validPassword)).thenReturn(mockUserInfo);

        mockMvc.perform(post("/login")
                        .param("username", validUsername)
                        .param("password", validPassword)
                        .param("verifiCode", validVerifiCode)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value(validUsername));

        // 验证 userService 的 login 方法是否被调用
        verify(userService, times(1)).login(validUsername, validPassword);
    }
    @Test
    public void testLogin_Fail_InvalidVerifiCode() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", validUsername)
                        .param("password", validPassword)
                        .param("verifiCode", "wrongCode")  // 错误的验证码
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("验证码错误，请重新输入"));
    }

    @Test
    public void testLogin_Fail_InvalidCredentials() throws Exception {
        // 模拟 userService 返回 null，表示用户名或密码错误
        when(userService.login(validUsername, validPassword)).thenReturn(null);

        mockMvc.perform(post("/login")
                        .param("username", validUsername)
                        .param("password", validPassword)
                        .param("verifiCode", validVerifiCode)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("用户名或密码错误"));
    }

    @Test
    public void testGetVerifiCodeImage() throws Exception {
        mockMvc.perform(get("/getVerifiCodeImage"))
                .andExpect(status().isOk());

        // 可以检查是否正确生成验证码图片，如果需要还可以通过 mock HttpServletResponse 来验证图片内容
    }

    @Autowired
    private DataSource dataSource;

    @Test
    public void testDatabaseConnection() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Database connected: " + (connection != null));
        }
    }

}
