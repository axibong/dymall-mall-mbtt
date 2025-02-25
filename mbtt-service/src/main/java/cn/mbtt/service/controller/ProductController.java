package cn.mbtt.service.controller;

import cn.mbtt.common.result.CommonPage;
import cn.mbtt.common.result.CommonResult;
import cn.mbtt.service.domain.po.Products;
import cn.mbtt.service.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

//    @Autowired
//    private EsProductService esProductService;

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


//    @ApiOperation(value = "导入所有数据库中商品到ES")
//    @RequestMapping(value = "/importAll", method = RequestMethod.POST)
//    @ResponseBody
//    /**
//     * 导入数据库中所有商品到 Elasticsearch。
//     *
//     * 该接口会将所有数据库中的商品数据导入到 Elasticsearch 索引中。返回导入商品的数量。
//     *
//     * @return CommonResult<Integer> 导入的商品数量
//     */
//    public CommonResult<Integer> importAllList() {
//        int count = esProductService.importAll();
//        return CommonResult.success(count);
//    }
//
//    @ApiOperation(value = "根据id删除商品")
//    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
//    @ResponseBody
//    /**
//     * 根据商品ID删除商品。
//     *
//     * 该接口用于删除 Elasticsearch 中指定商品ID的商品数据。删除成功后返回成功响应。
//     *
//     * @param id 商品的唯一标识
//     * @return CommonResult<Object> 删除操作结果
//     */
//    public CommonResult<Object> delete(@PathVariable Long id) {
//        esProductService.delete(id);
//        return CommonResult.success(null);
//    }
//
//    @ApiOperation(value = "简单搜索")
//    @RequestMapping(value = "/search/simple", method = RequestMethod.GET)
//    @ResponseBody
//    /**
//     * 简单搜索商品。
//     *
//     * 该接口支持根据关键词搜索商品，并返回匹配商品的分页数据。分页支持，可以通过指定页面号和每页条数进行控制。
//     *
//     * @param keyword 搜索关键词
//     * @param pageNum 当前页码，默认为0
//     * @param pageSize 每页显示的商品数，默认为5
//     * @return CommonResult<CommonPage<Products>> 商品的分页结果
//     */
//    public CommonResult<CommonPage<Products>> search(@RequestParam(required = false) String keyword,
//                                                     @RequestParam(required = false, defaultValue = "0") Integer pageNum,
//                                                     @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
//        Page<Products> esProductPage = esProductService.search(keyword, pageNum, pageSize);
//        return CommonResult.success(CommonPage.restPage(esProductPage));
//    }
//
//    /**
//     * 精确搜索商品
//     *
//     * 该接口根据给定的关键词精确查找商品，可以选择排序方式（如按ID降序或按价格升序）。
//     * 支持分页功能，可以获取特定页数和每页商品的数量。
//     *
//     * @param keyword 搜索关键词
//     * @param sortType 排序类型（1：按ID降序，2：按价格升序）
//     * @param pageNum 当前页码
//     * @param pageSize 每页商品数量
//     * @return Page<Products> 分页后的商品数据
//     */
//    @GetMapping("/search")
//    public Page<Products> preciseSearch(
//            @RequestParam String keyword,
//            @RequestParam(required = false) Integer sortType,
//            @RequestParam Integer pageNum,
//            @RequestParam Integer pageSize) {
//
//        // 调用 service 层的精确搜索方法
//        return esProductService.preciseSearch(keyword, sortType, pageNum, pageSize);
//    }
}