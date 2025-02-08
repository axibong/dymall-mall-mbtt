package cn.mbtt.service.service.impl;

import cn.mbtt.service.mapper.ProductMapper;
import cn.mbtt.service.service.ProductService;
import cn.mbtt.service.domain.po.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public Products createProduct(Products product) {
        // 插入新商品
        productMapper.insert(product);
        return product;  // 返回创建的商品
    }

    @Override
    public Products updateProduct(Products product) {
        // 检查商品是否存在
        Optional<Products> existingProduct = Optional.ofNullable(productMapper.findById(product.getId()));
        
        if (existingProduct.isPresent()) {
            productMapper.update(product);  // 更新商品
            return product;
        } else {
            // 商品不存在，抛出异常
            throw new RuntimeException("Product not found");
        }
    }

    @Override
public boolean deleteProduct(Long productId) {
    // 检查商品是否存在
    Optional<Products> product = Optional.ofNullable(productMapper.findById(productId));

    if (product.isPresent()) {
        // 软删除商品
        productMapper.delete(productId, LocalDateTime.now());
        return true;
    } else {
        return false;  // 商品不存在，返回 false
    }
}


    @Override
    public Optional<Products> getProductById(Long productId) {
        // 查询单个商品
        return Optional.ofNullable(productMapper.findById(productId));
    }

    @Override
    public Optional<Products> getNoProductById(Long productId) {
        // 查询单个商品
        return Optional.ofNullable(productMapper.getStatusById(productId));
    }


    @Override
    public List<Products> getAllProducts() {
        // 查询所有商品
        return productMapper.findAll();
    }
}
