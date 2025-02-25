package cn.mbtt.service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenUtil jwtUtil;

    /**
     * 测试 JWT 登录成功
     */
//    @Test
//    public void testJwtLogin() throws Exception {
//        String username = "testUser";
//        String password = "testPassword";
//
//        mockMvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").exists()); // 预期返回 JWT
//    }
    @Test
    public void testLoginAndGetToken() throws Exception {
        String username = "testUser";
        String password = "testPassword";

        // 模拟登录请求
        mockMvc.perform(post("/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertNotNull(response);
                    assertTrue(response.contains("token")); // 假设返回的JSON中包含token字段
                });
        System.out.println("测试通过");
    }

    @Test
    public void testAccessProtectedResourceWithValidToken() throws Exception {
        String token = "your-valid-jwt-token"; // 替换为实际的JWT令牌

        // 使用有效的JWT令牌访问受保护的端点
        mockMvc.perform(MockMvcRequestBuilders.get("/protected")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
    @Test
    public void testLoginAndGetToken2() throws Exception {
        String username = "testUser";
        String password = "testPassword";

        // **Step 1: 发送登录请求，获取 JWT 令牌**
        MvcResult result = mockMvc.perform(post("/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andReturn();

        // **Step 2: 获取 JSON 响应中的 token**
        String responseJson = result.getResponse().getContentAsString();
        System.out.println("登录响应：" + responseJson);  // 打印完整响应，检查格式

        // 解析 JSON 获取 token（假设返回格式：{"token": "xxx.yyy.zzz"}）
        String token = new ObjectMapper().readTree(responseJson).get("token").asText();
        System.out.println("获取的 JWT 令牌：" + token);

        assertNotNull(token);
        assertTrue(token.length() > 10); // 确保 token 看起来是有效的
    }

    @Test
    public void testLoginAndGetToken3() throws Exception {
        String username = "testUser";
        String password = "testPassword";

        MvcResult result = mockMvc.perform(post("/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        System.out.println("登录响应: " + response);
    }

}