package cn.mbtt.service.controller;

import cn.mbtt.service.domain.dto.ProductQuery;
import cn.mbtt.service.domain.dto.ProductUpdate;
import cn.mbtt.service.domain.po.Products;
import cn.mbtt.service.service.ProductService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Api(tags = "商品相关接口")  // Swagger 标签
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    @ApiOperation(value = "创建商品", notes = "根据商品信息创建商品")
    public Products createProduct(@RequestBody Products product) {
        return productService.createProduct(product);
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改商品", notes = "修改商品信息")
    public Products updateProduct(@RequestBody Products product) {
        return productService.updateProduct(product);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除商品", notes = "根据商品ID删除商品")
    public boolean deleteProduct(@PathVariable Long id) {
        return productService.softDeleteProduct(id);
    }

    @GetMapping("/get/{id}")
    @ApiOperation(value = "获取商品", notes = "根据商品ID获取商品信息")
    public Optional<Products> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取所有商品", notes = "获取所有未删除的商品信息")
    public List<Products> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询商品", notes = "分页查询商品")
    public Page<Products> getProductsByPage(@RequestParam int page, @RequestParam int pageSize) {
        return productService.getProductsByPage(page, pageSize);
    }

    @PostMapping("/query")
    @ApiOperation(value = "根据条件查询商品", notes = "根据查询条件分页查询商品")
    public Page<Products> queryProducts(
            @RequestBody ProductQuery productQuery,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return productService.queryProducts(productQuery, page, pageSize);
    }

    @DeleteMapping("/softdelete/{id}")
    @ApiOperation(value = "软删除商品", notes = "根据商品ID软删除商品")
    public boolean softDeleteProduct(@PathVariable Long id) {
        return productService.softDeleteProduct(id);
    }

    @PutMapping("/update/details/{id}")
    @ApiOperation(value = "更新商品信息", notes = "根据商品ID更新商品信息")
    public Products updateProductDetails(@RequestBody ProductUpdate productUpdate,
                                         @PathVariable Long id) {
        return productService.updateProductDetails(productUpdate, id);
    }

    @PutMapping("/restore/{id}")
    @ApiOperation(value = "恢复商品", notes = "根据商品ID恢复软删除的商品")
    public boolean restoreProduct(@PathVariable Long id) {
        return productService.restoreProduct(id);
    }

    @GetMapping("/search/name")
    @ApiOperation(value = "根据名称查询商品", notes = "通过商品名称搜索商品")
    public List<Products> searchProductsByName(@RequestParam String name) {
        return productService.searchProductsByName(name);

    }
    @GetMapping("/search/id")
    @ApiOperation(value = "根据ID查询商品", notes = "通过商品ID搜索商品")
    public Products searchProductById(@RequestParam Long id) {
        return productService.searchProductById(id);
    }
    @GetMapping("/search/category")
    @ApiOperation(value = "根据分类ID查询商品", notes = "通过商品分类ID搜索商品")
    public List<Products> searchProductsByCategoryId(@RequestParam Long categoryId) {
        return productService.searchProductsByCategoryId(categoryId);
    }


}
