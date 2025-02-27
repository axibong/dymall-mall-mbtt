package cn.mbtt.service.service.impl;

import cn.mbtt.service.domain.dto.ProductQuery;
import cn.mbtt.service.domain.dto.ProductUpdate;
import cn.mbtt.service.domain.po.Products;
import cn.mbtt.service.mapper.ProductMapper;
import cn.mbtt.service.repository.ProductElasticsearchRepository;
import cn.mbtt.service.service.ProductService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;  // 用于收集流结果
import java.util.stream.Stream;     // 用于 stream 操作（尽管通常 Stream 是自动导入的）

import lombok.var;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.elasticsearch.client.util.RequestConverters.search;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private ProductElasticsearchRepository productElasticsearchRepository;
    @Autowired
    private RestClient elasticsearchRestClient;

    @Override
    public Products createProduct(Products product) {
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productMapper.insertProduct(product);

        // 同步到 Elasticsearch
        //??
        productElasticsearchRepository.save(product);

        return product;
    }

    @Override
    public Products updateProduct(Products product) {
        ProductUpdate productUpdate=new ProductUpdate();
        QueryWrapper<Products>wrapper=new QueryWrapper<>();
        if(product.getName()!=null){
            productUpdate.setName(product.getName());
        }
        if(product.getImages()!=null){
            productUpdate.setImages(product.getImages());
        }
        if(product.getCategoryId()!=null){
            productUpdate.setCategoryId(product.getCategoryId());
        }
        if(product.getDescription()!=null){
            productUpdate.setDescription(product.getDescription());
        }
        if(product.getPrice()!=null){
            productUpdate.setPrice(product.getPrice());
        }
        if(product.getStatus()!=null){
            productUpdate.setStatus(product.getStatus());
        }
        if(product.getStock()!=null){
            productUpdate.setStock(product.getStock());
        }
        if(product.getOriginalPrice()!=null){
            productUpdate.setOriginalPrice(product.getOriginalPrice());

        }
        if(product.getSalesCount()!=null){
            productUpdate.setSalesCount(product.getSalesCount());
        }
        product.setUpdatedAt(LocalDateTime.now());
        productUpdate.setUpdatedAt(LocalDateTime.now());
        //          productMapper.update(productUpdate,wrapper);
        wrapper.eq("id",product.getId());
        productMapper.update(productUpdate,wrapper);
        //id
        product=productMapper.selectById(product.getId());


        // 同步到 Elasticsearch
        productElasticsearchRepository.save(product);

        return product;
    }

    @Override
    public boolean deleteProduct(Long productId) {
        // 删除数据库商品
        boolean isDeleted = productMapper.deleteById(productId)>0 ;

        if (isDeleted) {
            // 同步到 Elasticsearch，删除商品
            productElasticsearchRepository.deleteById(productId);
        }

        return isDeleted;
    }//ruan

    @Override
    public Optional<Products> getProductById(Long productId) {
        return Optional.ofNullable(productMapper.selectById(productId));
    }

    @Override
    public Optional<Products> getNoProductById(Long productId) {
        // 查询商品，忽略已软删除的商品（假设软删除通过 status 字段表示，0 表示未删除，-1 表示已删除）
        QueryWrapper<Products> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", productId);  // 按照商品 ID 查询
        queryWrapper.ne("status", -1);     // 排除已删除的商品（假设 -1 表示已删除）

        // 获取未软删除的商品
        return Optional.ofNullable(productMapper.selectOne(queryWrapper));
    }

    @Override
    public List<Products> getAllProducts() {
        return productMapper.selectList(null);
    }

    @Override
    public Page<Products> getProductsByPage(int page, int pageSize) {
        Page<Products> productPage = new Page<>(page, pageSize);
        return productMapper.selectPage(productPage, null);
    }

@Override
public Page<Products> queryProducts(ProductQuery productQuery, int page, int pageSize) {
    // 1. 参数校验
    if (page < 1) throw new IllegalArgumentException("页码必须 >= 1");
    if (pageSize < 1 || pageSize > 1000) throw new IllegalArgumentException("页大小需在1-1000之间");

    // 2. 构建分页参数（ES从0开始）
    Pageable pageable = PageRequest.of(page - 1, pageSize);

    // 3. 构建复合查询
    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

    // 精确查询
    addTermQuery(boolQuery, "id", productQuery.getId());
    addTermQuery(boolQuery, "categoryId", productQuery.getCategoryId());
    addTermQuery(boolQuery, "status", productQuery.getStatus());

    // 数值范围查询
    addRangeQuery(boolQuery, "price", productQuery.getMinPrice(), productQuery.getMaxPrice());
    addRangeQuery(boolQuery, "stock", productQuery.getMinStock(), productQuery.getMaxStock());
    addRangeQuery(boolQuery, "salesCount", productQuery.getMinSalesCount(), productQuery.getMaxSalesCount());

    // 时间范围查询
    addDateRangeQuery(boolQuery, "createdAt", productQuery.getStartCreatedAt(), productQuery.getEndCreatedAt());
    addDateRangeQuery(boolQuery, "updatedAt", productQuery.getStartUpdatedAt(), productQuery.getEndUpdatedAt());

    // 模糊查询
    addWildcardQuery(boolQuery, "name", productQuery.getName());
    addWildcardQuery(boolQuery, "description", productQuery.getDescription());

    // 4. 构建排序
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
            .withQuery(boolQuery)
            .withPageable(pageable);

    addSort(queryBuilder, productQuery.getSortField(), productQuery.getSortOrder());

    // 5. 执行查询
    SearchHits<Products> searchHits = elasticsearchRestTemplate.search(queryBuilder.build(), Products.class);

    // 6. 转换结果
    return convertToPage(searchHits, page, pageSize);
}



    // 增强版时间范围查询
    private void addDateRangeQuery(BoolQueryBuilder boolQuery,
                                   String field,
                                   LocalDateTime start,
                                   LocalDateTime end) {
        if (start == null && end == null) return;

        // 参数校验
        if (start != null && end != null && start.isAfter(end)) {
            throw new IllegalArgumentException(field + "时间范围参数非法");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.of("Asia/Shanghai"));

        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(field)
                .timeZone("+08:00")
                .format("yyyy-MM-dd HH:mm:ss");

        if (start != null) {
            rangeQuery.gte(formatter.format(start.atZone(ZoneId.systemDefault())));
        }
        if (end != null) {
            rangeQuery.lte(formatter.format(end.atZone(ZoneId.systemDefault())));
        }

        boolQuery.must(rangeQuery);
    }

    // 其他工具方法保持不变...
    // ==================== 工具方法 ====================
    private void addTermQuery(BoolQueryBuilder boolQuery, String field, Object value) {
        if (value != null) {
            boolQuery.must(QueryBuilders.termQuery(field, value));
        }
    }

    private void addRangeQuery(BoolQueryBuilder boolQuery, String field, Number min, Number max) {
        if (min != null || max != null) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(field);
            if (min != null) rangeQuery.gte(min);
            if (max != null) rangeQuery.lte(max);
            boolQuery.must(rangeQuery);
        }
    }

    private void addDateRangeQuery(BoolQueryBuilder boolQuery, String field, String start, String end) {
        if (start != null || end != null) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(field);
            if (start != null) rangeQuery.gte(start);
            if (end != null) rangeQuery.lte(end);
            boolQuery.must(rangeQuery);
        }
    }

    private void addWildcardQuery(BoolQueryBuilder boolQuery, String field, String value) {
        if (StringUtils.isNotBlank(value)) {
            boolQuery.must(QueryBuilders.wildcardQuery(field, "*" + value + "*"));
        }
    }

    private void addSort(NativeSearchQueryBuilder queryBuilder, String sortField, String sortOrder) {
        if (StringUtils.isNotBlank(sortField) && StringUtils.isNotBlank(sortOrder)) {
            SortOrder order = "desc".equalsIgnoreCase(sortOrder) ? SortOrder.DESC : SortOrder.ASC;
            queryBuilder.withSort(SortBuilders.fieldSort(sortField).order(order));
        }
    }

    private Page<Products> convertToPage(SearchHits<Products> searchHits, int page, int pageSize) {
        Page<Products> resultPage = new Page<>(page, pageSize);

        // 设置分页数据
        resultPage.setRecords(searchHits.getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList()));

        // 设置总记录数
        resultPage.setTotal(searchHits.getTotalHits());

        // 设置分页参数
        resultPage.setCurrent(page);
        resultPage.setSize(pageSize);

        // 计算总页数
        resultPage.setPages((int) Math.ceil((double) searchHits.getTotalHits() / pageSize));

        return resultPage;
    }

    @Override
    public List<Products> queryProductsByCondition(ProductQuery productQuery) {
        return productMapper.querybatchProducts(productQuery, null);
    }

    @Override
    public boolean softDeleteProduct(Long productId) {

        Products product = new Products();
        product.setId(productId);
        product.setStatus(-1); // -1表示已删除
        product.setUpdatedAt(LocalDateTime.now());
        boolean isUpdated = productMapper.updateById(product) > 0;

        if (isUpdated) {
            // 同步到 Elasticsearch，进行软删除,
            productElasticsearchRepository.deleteById(productId);
            //productElasticsearchRepository.deleteById(productId);
        }

        return isUpdated;
    }

    @Override
    public Products updateProductDetails(ProductUpdate productUpdate, Long productId) {
        QueryWrapper<Products> wrapper = new QueryWrapper<>();
        wrapper.eq("id", productId);  // id = productId 作为更新条件
        productMapper.update(productUpdate, wrapper);
        productElasticsearchRepository.save(productMapper.selectById(productId));
        return productMapper.selectById(productId);
    }

    @Override
    public boolean restoreProduct(Long productId) {
        Products product = new Products();
        product.setId(productId);
        product.setStatus(1); // 1表示恢复
        product.setUpdatedAt(LocalDateTime.now());
        boolean isUpdated = productMapper.updateById(product) > 0;
        product=productMapper.selectById(productId);

        if (isUpdated) {
            // 同步到 Elasticsearch，恢复商品
            productElasticsearchRepository.save(product);
        }

        return isUpdated;
    }

    @Override
    public List<Products> searchProductsByName(String name) {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("name", name)) // Example: search by product name
                .build();

        // Execute the search query
        return elasticsearchRestTemplate.search(query, Products.class)
                .stream()
                .map(searchHit -> searchHit.getContent()) // Extract the content (Products object) from each search hit
                .collect(Collectors.toList());

    }
    public Products searchProductById(Long id) {
        // 假设你使用的是 ElasticsearchRestTemplate，执行查询操作
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.termQuery("id", id))
                .build();
        return elasticsearchRestTemplate.search(query, Products.class)
                .stream()
                .map(SearchHit::getContent)
                .findFirst()
                .orElse(null); // 如果没有找到，返回null
    }
    public List<Products> searchProductsByCategoryId(Long categoryId) {
        // 查询所有与 categoryId 匹配的商品
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.termQuery("categoryId", categoryId))
                .build();
        return elasticsearchRestTemplate.search(query, Products.class)
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }


}
