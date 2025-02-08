package cn.mbtt.service.service.impl;

import cn.mbtt.service.domain.po.Products;
import cn.mbtt.service.mapper.ProductMapper;
import cn.mbtt.service.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceImplTest {

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Products product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 创建测试用的商品对象
        product = Products.builder()
                .id(1L)
                .categoryId(101L)
                .name("Test Product")
                .description("A test product description")
                .price(new BigDecimal("100.00"))
                .originalPrice(new BigDecimal("120.00"))
                .stock(10)
                .salesCount(0)
                .status(1)  // 在售
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreateProduct() {
        // 模拟 productMapper.insert(product) 调用
        Mockito.doNothing().when(productMapper).insert(Mockito.any(Products.class));

        // 调用 ProductService 的 createProduct 方法
        Products createdProduct = productService.createProduct(product);

        // 验证是否调用了插入方法
        Mockito.verify(productMapper, Mockito.times(1)).insert(Mockito.any(Products.class));

        // 验证返回的商品是否与原始商品相同
        assertEquals(product.getName(), createdProduct.getName());
        assertEquals(product.getPrice(), createdProduct.getPrice());
    }

    @Test
    void testUpdateProduct() {
        // 模拟商品存在
        Mockito.when(productMapper.findById(1L)).thenReturn(product);
        
        // 更新商品
        product.setPrice(new BigDecimal("110.00"));
        Mockito.doNothing().when(productMapper).update(Mockito.any(Products.class));

        // 调用 ProductService 的 updateProduct 方法
        Products updatedProduct = productService.updateProduct(product);

        // 验证是否调用了更新方法
        Mockito.verify(productMapper, Mockito.times(1)).update(Mockito.any(Products.class));

        // 验证返回的商品是否与更新后的商品一致
        assertEquals(product.getPrice(), updatedProduct.getPrice());
    }

    @Test
    void testUpdateProductNotFound() {
        // 模拟商品不存在
        Mockito.when(productMapper.findById(1L)).thenReturn(null);

        // 验证商品不存在时抛出异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(product);
        });

        // 验证异常信息
        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void testGetProductById() {
        // 模拟商品查询返回
        Mockito.when(productMapper.findById(1L)).thenReturn(product);

        // 调用查询方法
        Optional<Products> foundProduct = productService.getProductById(1L);

        // 验证商品是否被正确返回
        assertTrue(foundProduct.isPresent());
        assertEquals(product.getId(), foundProduct.get().getId());
    }

    @Test
    void testGetProductByIdNotFound() {
        // 模拟商品查询返回 null
        Mockito.when(productMapper.findById(1L)).thenReturn(null);

        // 调用查询方法
        Optional<Products> foundProduct = productService.getProductById(1L);

        // 验证返回值是否为空
        assertFalse(foundProduct.isPresent());
    }

    @Test
void testGetAllProducts() {
    // 使用 Arrays.asList() 来创建一个列表
    Mockito.when(productMapper.findAll()).thenReturn(Arrays.asList(product));

    List<Products> allProducts = productService.getAllProducts();

    assertEquals(1, allProducts.size());
    assertEquals(product.getId(), allProducts.get(0).getId());
}
}
