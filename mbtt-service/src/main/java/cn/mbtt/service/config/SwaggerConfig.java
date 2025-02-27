package cn.mbtt.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;

@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(true)
                //通过.select()方法，去配置扫描接口
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.mbtt.service.controller"))
                // 配置如何通过path过滤
                .paths(PathSelectors.any())
                .build();
    }
    Contact contact = new Contact("xx","xxx","xxxxxx");

    //配置Swagger 信息 = ApiInfo
    private ApiInfo apiInfo() {
        return new ApiInfo("Api文档",
                "备注",
                "1.0",
                "123",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>());
    }
}


