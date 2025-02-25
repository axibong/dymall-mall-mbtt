package cn.mbtt.service;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.oas.annotations.EnableOpenApi;

@MapperScan("cn.mbtt.service.mapper")
@EnableWebMvc
@SpringBootApplication(scanBasePackages = "cn.mbtt.service")
@EnableKnife4j
@EnableScheduling  // 启用定时任务
@EnableOpenApi  // 让 Knife4j 正确加载
@EnableElasticsearchRepositories(basePackages = "cn.mbtt.service.repository")
public class MbttApplication {
    public static void main(String[] args) {
        SpringApplication.run(MbttApplication.class, args);
    }
}
