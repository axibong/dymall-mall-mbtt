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
//    public CommonResult<Integer> importAllList() {
//        int count = esProductService.importAll();
//        return CommonResult.success(count);
//    }
//
//    @ApiOperation(value = "根据id删除商品")
//    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult<Object> delete(@PathVariable Long id) {
//        esProductService.delete(id);
//        return CommonResult.success(null);
//    }
//
//    @ApiOperation(value = "简单搜索")
//    @RequestMapping(value = "/search/simple", method = RequestMethod.GET)
//    @ResponseBody
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
//     * @param keyword   搜索关键词
//     * @param sortType  排序类型（1：按ID降序，2：按价格升序）
//     * @param pageNum   当前页码
//     * @param pageSize  每页条数
//     * @return 商品的分页结果
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
