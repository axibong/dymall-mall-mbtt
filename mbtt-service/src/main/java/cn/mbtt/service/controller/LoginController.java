package cn.mbtt.service.controller;

import cn.mbtt.common.result.CommonResult;
import cn.mbtt.common.result.Result;
import cn.mbtt.service.domain.po.LoginInfo;
import cn.mbtt.service.service.UserService;
import cn.mbtt.service.utils.CreateVerifiCodeImage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
* 登陆接口
*/
@Api(tags = "登录相关接口")
@RestController
public class LoginController {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private UserService userService;

    @Autowired
    private SqlSession sqlSession;

    @GetMapping("/test-db")
    public String testDbConnection() {
        try {
            // 执行简单查询
            sqlSession.selectOne("SELECT 1");
            return "Database connection successful!";
        } catch (Exception e) {
            return "Database connection failed: " + e.getMessage();
        }
    }

    /**
     * 登陆方法
     */
    @PostMapping("/login")
    @ApiOperation("登录的方法")
    public Result login(@RequestParam @RequestBody String username,
                        @RequestParam @RequestBody String password,
                        @RequestParam String verifiCode,  // 传入验证码参数
                        HttpServletRequest request) {

        // 获取当前会话中的验证码
        HttpSession session = request.getSession();
        String sessionVerifiCode = (String) session.getAttribute("verifiCode");
        // TODO: 直接写死验证码，避免从 session 里取
        sessionVerifiCode = "1234";
        // 校验验证码
        if (sessionVerifiCode == null || sessionVerifiCode.isEmpty()) {
            return Result.error("验证码已失效，请刷新后重试");
        }
        //TODO 验证码写死
        if (!sessionVerifiCode.equalsIgnoreCase("1234")) {
            return Result.error("验证码错误，请重新输入");
        }
//        if (!sessionVerifiCode.equalsIgnoreCase(verifiCode)) {
//            return Result.error("验证码错误，请重新输入");
//        }

        // 验证成功后，清除验证码
        session.removeAttribute("verifiCode");

        // 调用登录服务进行用户认证
        LoginInfo info = userService.login(username, password);
        if (info != null) {
            return Result.success(info);  // 登录成功，返回用户信息
        }

        // 用户名或密码错误
        return Result.error("用户名或密码错误");
    }

    //TODO 之后再加
//    @ApiOperation(value = "刷新token")
//    @RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult refreshToken(HttpServletRequest request) {
//        String token = request.getHeader(tokenHeader);
//        String refreshToken = userService.refreshToken(token);
//        if (refreshToken == null) {
//            return CommonResult.failed("token已经过期！");
//        }
//        Map<String, String> tokenMap = new HashMap<>();
//        tokenMap.put("token", refreshToken);
//        tokenMap.put("tokenHead", tokenHead);
//        return CommonResult.success(tokenMap);
//    }

    @ApiOperation("获取验证码图片")
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response){
        //TODO  **先写死验证码，之后有前端了再上传**
        String verifiCode = "1234";

        // 将验证码文本放入session，为下一次验证做准备
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode", verifiCode);

        // 获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
//        // 获取图片上的验证码
//        String verifiCode =new String( CreateVerifiCodeImage.getVerifiCode());
//        // 将验证码文本放入session域,为下一次验证做准备
//        HttpSession session = request.getSession();
//        session.setAttribute("verifiCode",verifiCode);
        // 将验证码图片响应给浏览器
        try {
            ImageIO.write(verifiCodeImage,"JPEG",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
