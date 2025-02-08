package cn.mbtt.service.controller;

import cn.mbtt.service.domain.po.Products;
import cn.mbtt.service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 创建商品
    @PostMapping("/create")
    public Products createProduct(@RequestBody Products product) {
        return productService.createProduct(product);
    }

    // 修改商品
    @PutMapping("/update")
    public Products updateProduct(@RequestBody Products product) {
        return productService.updateProduct(product);
    }

    // 删除商品
    @DeleteMapping("/delete/{id}")
    public boolean deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    // 获取单个商品
    @GetMapping("/get/{id}")
    public Optional<Products> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // 获取所有商品
    @GetMapping("/list")
    public List<Products> getAllProducts() {
        return productService.getAllProducts();
    }
}
