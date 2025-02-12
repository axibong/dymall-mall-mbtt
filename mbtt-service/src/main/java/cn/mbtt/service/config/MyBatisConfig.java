package cn.mbtt.service.config;

import cn.mbtt.service.handler.JsonListTypeHandler;  // 引入你自定义的 TypeHandler
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan("cn.mbtt.service.mapper")
public class MyBatisConfig {

    // 注册 SqlSessionFactory Bean
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();

        // 设置数据源
        factoryBean.setDataSource(dataSource);
        //factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("src/main/java/service/mapper/*.xml"));
        // 注册自定义的 TypeHandler
        //factoryBean.setTypeHandlers(new TypeHandler[]{new JsonListTypeHandler()});
        System.out.println("Registering JsonListTypeHandler");

        return factoryBean.getObject();
    }
}
