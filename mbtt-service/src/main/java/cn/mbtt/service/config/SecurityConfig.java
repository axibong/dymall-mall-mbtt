package cn.mbtt.service.config;

import cn.mbtt.service.component.JwtAuthenticationTokenFilter;
import cn.mbtt.service.component.RestAuthenticationEntryPoint;
import cn.mbtt.service.component.RestfulAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * SpringSecurity相关配置，仅用于配置SecurityFilterChain
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
//    @Autowired(required = false)
//    private DynamicSecurityService dynamicSecurityService;
//    @Autowired(required = false)
//    private DynamicSecurityFilter dynamicSecurityFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity
                .authorizeRequests();
        //不需要保护的资源路径允许访问
        for (String url : ignoreUrlsConfig.getUrls()) {
            registry.antMatchers(url).permitAll();
        }
        //允许跨域请求的OPTIONS请求
        registry.antMatchers(HttpMethod.OPTIONS)
                .permitAll();
        //任何请求都需要身份认证
        registry
                .anyRequest().authenticated()  // 直接在此处配置
                .and()
                .csrf().disable()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                //自定义权限拒绝处理类
                .and()
                .exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                //自定义权限拦截器JWT过滤器
                .and()
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
//        //有动态权限配置时添加动态权限校验过滤器
//        if(dynamicSecurityService!=null){
//            registry.and().addFilterBefore(dynamicSecurityFilter, FilterSecurityInterceptor.class);
//        }
        return httpSecurity.build();
    }

    //方便测试：修改 MallSecurityConfig，让 UserDetailsService 直接返回一个 硬编码的用户，不去查询数据库
//        @Bean
//        public UserDetailsService userDetailsService() {
//            return username -> {
//                if ("testUser".equals(username)) {
//                    return User.withUsername("testUser")
//                            .password(new BCryptPasswordEncoder().encode("testPassword"))
//                            .roles("USER")
//                            .build();
//                } else if ("admin".equals(username)) {
//                    return User.withUsername("admin")
//                            .password(new BCryptPasswordEncoder().encode("123456"))
//                            .roles("ADMIN")
//                            .build();
//                } else {
//                    throw new UsernameNotFoundException("用户不存在: " + username);
//                }
//            };
//        }

//        @Bean
//        public PasswordEncoder passwordEncoder() {
//            return new BCryptPasswordEncoder(); // 使用 BCrypt 进行密码加密
//        }
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            if (!"testUser".equals(username)) {
                throw new UsernameNotFoundException("用户不存在: " + username);
            }
            return User.withUsername("testUser")
                    .password(passwordEncoder().encode("testPassword")) // 密码加密
                    .roles("USER")
                    .build();
        };
    }
    //使用 BCryptPasswordEncoder 进行密码加密，提升安全性。
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 关键：手动配置 AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    }

