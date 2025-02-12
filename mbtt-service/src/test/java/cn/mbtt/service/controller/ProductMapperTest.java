  package cn.mbtt.service.controller;

// import cn.mbtt.service.domain.po.Products;
// import cn.mbtt.service.mapper.ProductMapper;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;

// import java.util.List;

// import static org.junit.jupiter.api.Assertions.assertNotNull;

// @SpringBootTest
// public class ProductMapperTest {

//     @Autowired
//     private ProductMapper productMapper;

//     @Test
//     public void testFindAll() {
//         List<Products> products = productMapper.findAll();
//         assertNotNull(products);
        
//         // 遍历输出并验证 images 是否正确映射
//         for (Products product : products) {
//             System.out.println("Product ID: " + product.getId());
//             System.out.println("Images: " + product.getImages());  // 输出 images 字段
//             // 可以增加更具体的验证逻辑，确保 images 是 List<String> 类型，并且包含期望的内容
//         }
//     }
// }
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import cn.mbtt.service.domain.po.Products;
import cn.mbtt.service.mapper.ProductMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Test
    public void testFindAll() {
        List<Products> products = productMapper.findAll();
        assertNotNull(products);

        for (Products product : products) {
            assertNotNull(product.getId());
            assertNotNull(product.getName());
            assertNotNull(product.getImages());
            assertTrue(product.getImages() instanceof List<?>);
            assertTrue(((List<?>) product.getImages()).stream().allMatch(item -> item instanceof String));
            System.out.println("Product ID: " + product.getId());
            System.out.println("Images: " + product.getImages());
        }
    }

    @Test
    public void testInsertProduct() {
        // 创建产品对象
        Products product = new Products();
        product.setName("Test Product");
        product.setCategoryId(1L);
        product.setDescription("This is a test product.");
        product.setPrice(new BigDecimal("9.99"));
        product.setOriginalPrice(new BigDecimal("19.99"));
        product.setStock(10);
        product.setImages(Arrays.asList("image1.jpg", "image2.jpg"));
        product.setSalesCount(0);
        product.setStatus(1);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        // 插入产品
        productMapper.insert(product);

        // 调试：检查插入后的 ID
        System.out.println("Product ID after insert: " + product.getId());  // 打印插入后的 ID

        // 验证插入是否成功
        assertNotNull(product.getId());  // 确保 ID 不为 null

        // 查询并验证插入的数据
        Products insertedProduct = productMapper.findById(product.getId());
        assertNotNull(insertedProduct);
        assertTrue(insertedProduct.getName().equals(product.getName()));
        assertTrue(insertedProduct.getImages().equals(product.getImages()));
        assertNotNull(insertedProduct.getCreatedAt());
        assertNotNull(insertedProduct.getUpdatedAt());
    }
    @Test
public void testUpdateProduct() {
    // 假设已插入的产品 ID
    Long productId = 1L;  // 请根据实际插入的 ID 替换

    // 先查询一下已插入的产品
    Products product = productMapper.findById(productId);
    assertNotNull(product);

    // 修改产品的一些字段
    product.setName("Updated Product");
    product.setStock(50);
    product.setUpdatedAt(LocalDateTime.now());

    // 执行更新
    productMapper.update(product);

    // 查询更新后的商品
    Products updatedProduct = productMapper.findById(productId);
    assertNotNull(updatedProduct);
    assertEquals("Updated Product", updatedProduct.getName());
    assertEquals(50, updatedProduct.getStock());
}
@Test
public void testSoftDeleteProduct() {
    // 1. 插入一个新的产品对象，进行测试
    Products product = new Products();
    product.setName("Soft Delete Product");
    product.setCategoryId(1L);
    product.setDescription("This is a product to be soft deleted.");
    product.setPrice(new BigDecimal("10.00"));
    product.setOriginalPrice(new BigDecimal("15.00"));
    product.setStock(10);
    product.setImages(Arrays.asList("delete_image.jpg"));
    product.setSalesCount(0);
    product.setStatus(1);  // 初始状态为在售
    product.setCreatedAt(LocalDateTime.now());
    product.setUpdatedAt(LocalDateTime.now());

    // 插入商品
    productMapper.insert(product);
    Long productId = product.getId();
    
    // 确保插入操作成功
    assertNotNull(productId);

    // 2. 执行软删除操作
    LocalDateTime deletedAt = LocalDateTime.now();
    productMapper.delete(productId, deletedAt);

    // 3. 查询商品，验证软删除是否成功
    Products deletedProduct = productMapper.getStatusById(productId);
    //findByTd只能返回没被删除的，所以没办法得到id
    // 验证软删除后的状态和删除时间
    //assertNotNull(deletedProduct);
    assertEquals(-1, deletedProduct.getStatus());  // 验证 status 是否变为 -1
    //assertNotNull(product.getDeletedAt());  // 验证 deleted_at 是否被赋值
    //assertEquals(deletedAt, product.getDeletedAt());  // 验证删除时间是否正确
}

@Test
public void testFindById() {
    // 插入商品数据
    Products product = new Products();
    product.setName("Test Product");
    product.setCategoryId(1L);
    product.setDescription("This is a test product.");
    product.setPrice(new BigDecimal("9.99"));
    product.setOriginalPrice(new BigDecimal("19.99"));
    product.setStock(10);
    product.setImages(Arrays.asList("image1.jpg", "image2.jpg"));
    product.setSalesCount(0);
    product.setStatus(1);  // 状态为在售
    product.setCreatedAt(LocalDateTime.now());
    product.setUpdatedAt(LocalDateTime.now());

    productMapper.insert(product);  // 插入商品数据
    Long productId = product.getId();  // 获取插入后的商品ID

    // 确保商品ID不为null
    assertNotNull(productId);

    // 使用插入后的 ID 查找商品
    Products fetchedProduct = productMapper.findById(productId);

    // 验证查询结果
    assertNotNull(fetchedProduct);  // 确保返回的商品不为 null
    assertEquals(productId, fetchedProduct.getId());  // 验证 ID 是否正确
    assertEquals("Test Product", fetchedProduct.getName());  // 验证名称是否正确
}
@Test
public void testFindByIds() {
    // 插入商品数据
    Products product1 = new Products();
    product1.setName("Product 1");
    product1.setCategoryId(1L);
    product1.setDescription("This is product 1.");
    product1.setPrice(new BigDecimal("9.99"));
    product1.setOriginalPrice(new BigDecimal("19.99"));
    product1.setStock(10);
    product1.setImages(Arrays.asList("image1.jpg"));
    product1.setSalesCount(0);
    product1.setStatus(1);  // 在售
    product1.setCreatedAt(LocalDateTime.now());
    product1.setUpdatedAt(LocalDateTime.now());

    Products product2 = new Products();
    product2.setName("Product 2");
    product2.setCategoryId(1L);
    product2.setDescription("This is product 2.");
    product2.setPrice(new BigDecimal("19.99"));
    product2.setOriginalPrice(new BigDecimal("29.99"));
    product2.setStock(15);
    product2.setImages(Arrays.asList("image2.jpg"));
    product2.setSalesCount(5);
    product2.setStatus(1);  // 在售
    product2.setCreatedAt(LocalDateTime.now());
    product2.setUpdatedAt(LocalDateTime.now());

    Products product3 = new Products();
    product3.setName("Product 3");
    product3.setCategoryId(1L);
    product3.setDescription("This is product 3.");
    product3.setPrice(new BigDecimal("15.99"));
    product3.setOriginalPrice(new BigDecimal("25.99"));
    product3.setStock(20);
    product3.setImages(Arrays.asList("image3.jpg"));
    product3.setSalesCount(3);
    product3.setStatus(-1);  // 已删除，状态为 -1
    product3.setCreatedAt(LocalDateTime.now());
    product3.setUpdatedAt(LocalDateTime.now());

    // 插入商品
    productMapper.insert(product1);
    productMapper.insert(product2);
    productMapper.insert(product3);

    // 获取商品的 ID 列表
    List<Long> ids = Arrays.asList(product1.getId(), product2.getId(), product3.getId());

    // 执行批量查询
    List<Products> productsList = productMapper.findByIds(ids);

    // 验证返回结果
    assertNotNull(productsList);  // 验证返回的列表不为空
    assertEquals(2, productsList.size());  // 因为 product3 的状态为 -1，应该被排除

    // 验证商品1和商品2是否在返回列表中
    assertTrue(productsList.stream().anyMatch(p -> p.getId().equals(product1.getId())));
    assertTrue(productsList.stream().anyMatch(p -> p.getId().equals(product2.getId())));

    // 验证商品3（已删除）不在返回列表中
    assertFalse(productsList.stream().anyMatch(p -> p.getId().equals(product3.getId())));
}


}
