package cn.mbtt.service.service;

import cn.mbtt.service.domain.po.Products;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    /**
     * 创建商品
     * @param product 商品对象
     * @return 创建成功的商品
     */
    Products createProduct(Products product);

    /**
     * 修改商品信息
     * @param product 商品对象，包含需要更新的信息
     * @return 更新后的商品
     */
    Products updateProduct(Products product);

    /**
     * 删除商品
     * @param productId 商品ID
     * @return 删除是否成功
     */
    boolean deleteProduct(Long productId);

    /**
     * 获取单个商品信息
     * @param productId 商品ID
     * @return 商品对象
     */
    //查找未被删除
    Optional<Products> getProductById(Long productId);
    //查找无限制
    Optional<Products> getNoProductById(Long productId);

    /**
     * 获取所有商品信息
     * @return 商品列表
     */
    List<Products> getAllProducts();
}
