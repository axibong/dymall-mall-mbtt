package cn.mbtt.service.controller;

import cn.mbtt.service.domain.po.Products;
import cn.mbtt.service.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
/**
 * 商品控制器
 * 提供商品的增删改查操作
 */
@Api(tags = "商品管理接口")
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 创建商品
     *
     * @param product 商品对象
     * @return 创建后的商品对象
     */
    @ApiOperation("创建商品")    @PostMapping("/create")
    public Products createProduct(@RequestBody Products product) {
        return productService.createProduct(product);
    }

    /**
     * 修改商品
     *
     * @param product 商品对象（包含需要修改的信息）
     * @return 修改后的商品对象
     */
    @ApiOperation("修改商品信息")    @PutMapping("/update")
    public Products updateProduct(@RequestBody Products product) {
        return productService.updateProduct(product);
    }

    /**
     * 根据ID删除商品
     *
     * @param id 商品ID
     * @return 是否删除成功
     */
    @ApiOperation("删除商品")    @DeleteMapping("/delete/{id}")
    public boolean deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    /**
     * 根据ID获取商品信息
     *
     * @param id 商品ID
     * @return 对应的商品对象（如果存在）
     */
    @ApiOperation("获取单个商品信息")    @GetMapping("/get/{id}")
    public Optional<Products> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    /**
     * 获取所有商品列表
     *
     * @return 商品列表
     */
    @ApiOperation("获取所有商品列表")    @GetMapping("/list")
    public List<Products> getAllProducts() {
        return productService.getAllProducts();
    }
}
