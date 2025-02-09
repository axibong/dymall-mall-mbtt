package cn.mbtt.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("cn.mbtt.service.mapper")
@ComponentScan(basePackages = {"cn.mbtt.service","cn.mbtt.service.controller", "cn.mbtt.service.service","cn.mbtt.service.config"})
@SpringBootApplication
public class MbttApplication {
    public static void main(String[] args) {
        SpringApplication.run(MbttApplication.class, args);
    }
}