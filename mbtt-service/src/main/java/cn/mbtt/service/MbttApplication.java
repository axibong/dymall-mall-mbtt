
package cn.mbtt.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("cn.mbtt.service.mapper")
@ComponentScan(basePackages = {"cn.mbtt.service","cn.mbtt.service.controller", "cn.mbtt.service.service","cn.mbtt.service.handler","cn.mbtt.service.config"})
@SpringBootApplication
public class MbttApplication {
    public static void main(String[] args) {
        SpringApplication.run(MbttApplication.class, args);
    }
}

// package cn.mbtt.service;

// import cn.mbtt.service.domain.po.Products;
// import cn.mbtt.service.mapper.ProductMapper;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;

// import java.util.List;

// @SpringBootApplication
// public class MbttApplication implements CommandLineRunner {

//     @Autowired
//     private ProductMapper productMapper;

//     public static void main(String[] args) {
//         SpringApplication.run(MbttApplication.class, args);
//     }

//     @Override
//     public void run(String... args) throws Exception {
//         // 调用 ProductMapper 查询所有商品
//         List<Products> products = productMapper.findAll();

//         if (products == null || products.isEmpty()) {
//             System.out.println("No products found");
//             return;
//         }

//         // 遍历输出并验证 images 是否正确映射
//         for (Products product : products) {
//             System.out.println("Product ID: " + product.getId());
//             System.out.println("Images: " + product.getImages());  // 输出 images 字段
//         }
//     }
// }
