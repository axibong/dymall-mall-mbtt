package cn.mbtt.service.service;

import cn.mbtt.service.domain.po.Products;
import cn.mbtt.service.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    private Products product;

    @BeforeEach
    void setUp() {
        // 设置一个商品对象用于测试
        product = Products.builder()
                .categoryId(1L)
                .name("测试商品")
                .description("这是一个测试商品")
                .price(new BigDecimal("10.99"))
                .originalPrice(new BigDecimal("15.99"))
                .stock(100)
                .salesCount(0)
                .status(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @Transactional
    void testCreateProduct() {
        // 创建商品
        Products createdProduct = productService.createProduct(product);

        // 验证商品是否成功创建
        assertNotNull(createdProduct);
        assertNotNull(createdProduct.getId());  // 确保ID被自动生成
        assertEquals("测试商品", createdProduct.getName());
    }

    @Test
    @Transactional
    void testUpdateProduct() {
        // 首先创建一个商品
        Products createdProduct = productService.createProduct(product);

        // 更新商品信息
        createdProduct.setName("更新后的商品");
        createdProduct.setPrice(new BigDecimal("12.99"));
        Products updatedProduct = productService.updateProduct(createdProduct);

        // 验证商品是否成功更新
        assertNotNull(updatedProduct);
        assertEquals("更新后的商品", updatedProduct.getName());
        assertEquals(new BigDecimal("12.99"), updatedProduct.getPrice());
    }

    @Test
    @Transactional
    void testDeleteProduct() {
        // 创建商品对象
        Products product = new Products();
        product.setName("Test Product");
        product.setCategoryId(1L);
        product.setDescription("This is a test product.");
        product.setPrice(new BigDecimal("9.99"));
        product.setOriginalPrice(new BigDecimal("19.99"));
        product.setStock(10);
        product.setImages(Arrays.asList("image1.jpg", "image2.jpg"));
        product.setSalesCount(0);
        product.setStatus(1);  // 在售状态
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        // 创建商品
        Products createdProduct = productService.createProduct(product);

        // 删除商品
        boolean isDeleted = productService.deleteProduct(createdProduct.getId());  // 调用软删除方法

        // 验证商品是否已软删除
        assertTrue(isDeleted);  // 确保删除成功

        // 确保商品的状态已更新为 -1
        Optional<Products> deletedProduct = productService.getNoProductById(createdProduct.getId());
        assertTrue(deletedProduct.isPresent());
        assertEquals(-1, deletedProduct.get().getStatus());  // 商品状态应为 -1（已删除）
    }
  

    @Test
    @Transactional
    void testGetProductById() {
        // 创建商品
        Products createdProduct = productService.createProduct(product);

        // 根据 ID 获取商品
        Optional<Products> retrievedProduct = productService.getProductById(createdProduct.getId());

        // 验证商品是否成功获取
        assertTrue(retrievedProduct.isPresent());
        assertEquals("测试商品", retrievedProduct.get().getName());
    }

    @Test
    @Transactional
    void testGetAllProducts() {
        // 创建多个商品
        productService.createProduct(product);
        Products anotherProduct = Products.builder()
                .categoryId(2L)
                .name("另一个商品")
                .description("第二个测试商品")
                .price(new BigDecimal("20.99"))
                .originalPrice(new BigDecimal("25.99"))
                .stock(50)
                .salesCount(10)
                .status(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        productService.createProduct(anotherProduct);

        // 获取所有商品
        List<Products> products = productService.getAllProducts();

        // 验证商品列表的数量
        assertNotNull(products);
        assertTrue(products.size() > 1);  // 应该返回多个商品
    }
}
