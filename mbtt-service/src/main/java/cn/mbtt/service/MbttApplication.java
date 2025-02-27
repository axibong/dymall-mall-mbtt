package cn.mbtt.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("cn.mbtt.service.mapper")  // 扫描 Mapper 接口包

@SpringBootApplication  // 启动 Spring Boot 应用

public class MbttApplication {
    public static void main(String[] args) {
        SpringApplication.run(MbttApplication.class, args);
    }
}
