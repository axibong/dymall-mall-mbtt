//package cn.mbtt.service.service.impl;
//
//
//import cn.hutool.core.collection.ListUtil;
//import cn.mbtt.service.domain.po.Products;
//import cn.mbtt.service.mapper.ProductMapper;
//import cn.mbtt.service.repository.EsProductRepository;
//import cn.mbtt.service.service.EsProductService;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.sort.SortBuilders;
//import org.elasticsearch.search.sort.SortOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.core.SearchHit;
//import org.springframework.data.elasticsearch.core.SearchHits;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.stereotype.Service;
//
//import java.util.Iterator;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class EsProductServiceImpl implements EsProductService {
//
//    @Autowired
//    private ProductMapper productMapper;
//    @Autowired
//    private EsProductRepository productRepository;
//    @Autowired
//    private ElasticsearchRestTemplate elasticsearchRestTemplate;
//    @Override
//    public int importAll() {
//        List<Products> esProductList = productMapper.findAll();
//        Iterable<Products> esProductIterable = productRepository.saveAll(esProductList);
//        Iterator<Products> iterator = esProductIterable.iterator();
//        int result = 0;
//        while (iterator.hasNext()) {
//            result++;
//            iterator.next();
//        }
//        return result;
//    }
//
//    @Override
//    public void delete(Long id) {
//        productRepository.deleteById(id);
//    }
//
//    @Override
//    public Page<Products> search(String keyword, Integer pageNum, Integer pageSize) {
//        Pageable pageable = PageRequest.of(pageNum, pageSize);
//        return productRepository.findByNameOrSubTitleOrKeywords(keyword, keyword, keyword, pageable);
//    }
//
//    @Override
//    public Page<Products> preciseSearch(String keyword, Integer sortType, Integer pageNum, Integer pageSize) {
//        // 构建分页参数
//        Pageable pageable = PageRequest.of(pageNum, pageSize);
//
//        // 创建组合查询
//        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
//                .should(QueryBuilders.matchQuery("name", keyword).boost(2.0f))    // name权重2倍
//                .should(QueryBuilders.matchQuery("description", keyword).boost(1.0f)); // description权重1倍
//
//        // 构建原生查询
//        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
//                .withQuery(boolQuery)
//                .withPageable(pageable);
//
//        // 动态添加排序规则
//        if (sortType != null) {
//            switch (sortType) {
//                case 1: // 按ID降序（从新到旧）
//                    queryBuilder.withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC));
//                    break;
//                case 2: // 按价格升序
//                    queryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.ASC));
//                    break;
//                default: // 默认按相关性评分排序
//                    queryBuilder.withSort(SortBuilders.scoreSort().order(SortOrder.DESC));
//            }
//        } else {
//            queryBuilder.withSort(SortBuilders.scoreSort().order(SortOrder.DESC));
//        }
//
//        NativeSearchQuery searchQuery = queryBuilder.build();
//
//        // 执行查询并返回分页结果
//        SearchHits<Products> searchHits = elasticsearchRestTemplate.search(searchQuery, Products.class);
//
//        if (searchHits.getTotalHits() <= 0) {
//            return new PageImpl<>(ListUtil.empty(), pageable, 0); // 如果没有匹配结果，返回空分页结果
//        }
//
//        // 将查询结果映射成列表
//        List<Products> productsList = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
//
//        // 返回分页结果
//        return new PageImpl<>(productsList, pageable, searchHits.getTotalHits());
//    }
//
//}
//
//
