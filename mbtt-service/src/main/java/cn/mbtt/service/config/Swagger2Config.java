//package cn.mbtt.service.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.ParameterBuilder;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.schema.ModelRef;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.service.Parameter;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Swagger2配置信息
// */
//@Configuration
//public class Swagger2Config {
//
//    @Bean
//    public Docket webApiConfig() {
//        List<Parameter> pars = new ArrayList<>();
//
//        // 添加 Authorization 请求头参数
//        ParameterBuilder tokenPar = new ParameterBuilder();
//        tokenPar.name("Authorization")
//                .description("Authorization Token")
//                .modelRef(new ModelRef("string"))
//                .parameterType("header")
//                .required(true)
//                .build();
//        pars.add(tokenPar.build());
//
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("webApi")
//                .apiInfo(webApiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("cn.mbtt.service.controller"))  // 修改为自己的包名
//                .build();
//    }
//
//    private ApiInfo webApiInfo() {
//        return new ApiInfoBuilder()
//                .title("dymall项目API文档")
//                .description("本文档描述了项目相关接口")
//                .version("1.0.0")
//                .contact(new Contact("axi", " ", "axibong63@gmail.com"))
//                .build();
//    }
//}
