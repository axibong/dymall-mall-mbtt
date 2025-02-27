package cn.mbtt.service.repository;

import cn.mbtt.service.domain.po.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 商品 Elasticsearch 数据访问接口
 * 用于与 Elasticsearch 进行商品数据的操作
 * 继承自 Spring Data Elasticsearch 提供的 ElasticsearchRepository 接口，简化了与 Elasticsearch 的交互。
 */
@Repository
public interface ProductElasticsearchRepository extends ElasticsearchRepository<Products, Long> {

    /**
     * 根据商品名称查找商品
     * @param name 商品名称
     * @return 商品列表
     */
    List<Products> findByName(String name);

    /**
     * 根据商品分类ID查找商品
     * @param categoryId 商品分类ID
     * @return 商品列表
     */
    List<Products> findByCategoryId(Long categoryId);

    /**
     * 根据商品状态查找商品（例如：在售、下架）
     * @param status 商品状态
     * @return 商品列表
     */
    List<Products> findByStatus(Integer status);

    /**
     * 根据商品名称模糊查询商品（使用 Elasticsearch 的查询功能）
     * @param name 商品名称的关键字
     * @return 商品列表
     */
    @Query("{\"match\": {\"name\": \"?0\"}}")
    List<Products> findByNameLike(String name);

    /**
     * 根据商品价格区间查询商品（例如：价格在某个范围内）
     * @param minPrice 最小价格
     * @param maxPrice 最大价格
     * @return 商品列表
     */
    List<Products> findByPriceBetween(Double minPrice, Double maxPrice);

    /**
     * 根据商品的创建时间范围查询商品
     * @param startTime 起始时间
     * @param endTime 结束时间
     * @return 商品列表
     */
    List<Products> findByCreatedAtBetween(String startTime, String endTime);

    /**
     * 根据商品的名称和分类ID查询商品
     * @param name 商品名称
     * @param categoryId 商品分类ID
     * @return 商品列表
     */
    List<Products> findByNameAndCategoryId(String name, Long categoryId);

    /**
     * 分页查询商品（根据商品的名称进行分页查询）
     * @param name 商品名称
     * @param pageable 分页参数
     * @return 分页商品列表
     */
    Page<Products> findByName(String name, Pageable pageable);

    /**
     * 根据商品名称和状态查询商品（分页查询）
     * @param name 商品名称
     * @param status 商品状态
     * @param pageable 分页参数
     * @return 分页商品列表
     */
    Page<Products> findByNameAndStatus(String name, Integer status, Pageable pageable);

    /**
     * 根据商品名称查询商品的数量
     * @param name 商品名称
     * @return 商品数量
     */
    long countByName(String name);

    /**
     * 删除商品（根据商品ID）
     * @param productId 商品ID
     */
    void deleteById(Long productId);

    /**
     * 根据商品ID查找商品，返回可选值（如果商品存在返回商品，如果不存在返回 Optional.empty()）
     * @param productId 商品ID
     * @return 商品对象的 Optional
     */
    Optional<Products> findById(Long productId);

    // 如果需要更多自定义查询，可以在这里定义
}

