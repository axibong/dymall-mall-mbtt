package cn.mbtt.service;

//import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//@MapperScan("cn.mbtt.service.mapper")
@EnableWebMvc
@SpringBootApplication
public class MbttApplication {
    public static void main(String[] args) {
        SpringApplication.run(MbttApplication.class, args);
    }
}