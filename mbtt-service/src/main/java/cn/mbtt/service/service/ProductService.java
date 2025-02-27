package cn.mbtt.service.service;

import cn.mbtt.service.domain.dto.ProductQuery;
import cn.mbtt.service.domain.dto.ProductUpdate;
import cn.mbtt.service.domain.po.Products;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductService{

    /**
     * 创建商品，并同步到 Elasticsearch
     * @param product 商品对象
     * @return 创建成功的商品
     */
    Products createProduct(Products product);

    /**
     * 修改商品信息，并同步到 Elasticsearch
     * @param product 商品对象，包含需要更新的信息
     * @return 更新后的商品
     */
    Products updateProduct(Products product);

    /**
     * 删除商品，并从 Elasticsearch 中删除
     * @param productId 商品ID
     * @return 删除是否成功
     */
    boolean deleteProduct(Long productId);

    /**
     * 获取单个商品信息（查找未被删除的商品）
     * @param productId 商品ID
     * @return 商品对象
     */
    Optional<Products> getProductById(Long productId);

    /**
     * 获取单个商品信息（不限制状态）
     * @param productId 商品ID
     * @return 商品对象
     */
    Optional<Products> getNoProductById(Long productId);

    /**
     * 获取所有商品信息
     * @return 商品列表
     */
    List<Products> getAllProducts();

    /**
     * 分页查询商品
     * @param page 当前页
     * @param pageSize 每页数量
     * @return 分页后的商品列表
     */
    Page<Products> getProductsByPage(int page, int pageSize);

    /**
     * 根据多个查询条件查询商品列表
     * @param productQuery 查询条件
     * @param page 当前页
     * @param pageSize 每页数量
     * @return 查询到的商品列表
     */
    Page<Products> queryProducts(ProductQuery productQuery, int page, int pageSize);

    /**
     * 根据条件查询商品（批量查询）
     * @param productQuery 查询条件
     * @return 商品列表
     */
    List<Products> queryProductsByCondition(ProductQuery productQuery);

    /**
     * 执行软删除商品，并同步到 Elasticsearch
     * @param productId 商品ID
     * @return 是否成功
     */
    boolean softDeleteProduct(Long productId);

    /**
     * 更新商品信息（支持部分字段更新），并同步到 Elasticsearch
     * @param productUpdate 包含更新信息的对象
     * @param productId 商品ID
     * @return 更新后的商品
     */
    Products updateProductDetails(ProductUpdate productUpdate, Long productId);

    /**
     * 恢复软删除的商品，并同步到 Elasticsearch
     * @param productId 商品ID
     * @return 是否恢复成功
     */
    boolean restoreProduct(Long productId);

    // 新增方法：根据名称查询商品（通过 Elasticsearch）
    List<Products> searchProductsByName(String name);

    List<Products> searchProductsByCategoryId(Long categoryId);

    Products searchProductById(Long id);
}
