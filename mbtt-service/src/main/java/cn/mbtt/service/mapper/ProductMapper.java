package cn.mbtt.service.mapper;

import cn.mbtt.service.domain.dto.ProductQuery;
import cn.mbtt.service.domain.dto.ProductUpdate;
import cn.mbtt.service.domain.po.Products;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Products> {
    /**
     *
     * @param product
     */
    @Insert("INSERT INTO products(category_id, name, description, price, original_price, stock, images, sales_count, status, created_at, updated_at) " +
            "VALUES(#{categoryId}, #{name}, #{description}, #{price}, #{originalPrice}, #{stock}, #{images, typeHandler=cn.mbtt.service.handler.JsonListTypeHandler, jdbcType=VARCHAR}, #{salesCount}, #{status}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")  // 添加 @Options 注解
    void insertProduct(Products product);

    /**
     *
     * @param productUpdate
     * @param wrapper
     */

    @Update("<script>"
            + "UPDATE products "
            + "<set>"
            + "<if test='productUpdate.categoryId != null'>category_id = #{productUpdate.categoryId},</if>"
            + "<if test='productUpdate.name != null'>name = #{productUpdate.name},</if>"
            + "<if test='productUpdate.description != null'>description = #{productUpdate.description},</if>"
            + "<if test='productUpdate.price != null'>price = #{productUpdate.price},</if>"
            + "<if test='productUpdate.originalPrice != null'>original_price = #{productUpdate.originalPrice},</if>"
            + "<if test='productUpdate.stock != null'>stock = #{productUpdate.stock},</if>"
            + "<if test='productUpdate.images != null'>images = #{productUpdate.images,typeHandler=cn.mbtt.service.handler.JsonListTypeHandler, jdbcType=VARCHAR},</if>"
            + "<if test='productUpdate.status != null'>status = #{productUpdate.status},</if>"
            + "<if test='productUpdate.salesCount != null'>sales_count = #{productUpdate.salesCount},</if>"
            + "<if test='productUpdate.updatedAt != null'>updated_at = #{productUpdate.updatedAt},</if>"
            + "</set>"

            + "<if test='ew != null'>${ew.customSqlSegment}</if>"
            + "</script>")
    void update(@Param("productUpdate") ProductUpdate productUpdate,
                @Param("ew") QueryWrapper<Products> wrapper);

    /**
     *
     * @param id
     * @param categoryId
     * @param name
     * @param status
     * @param deletedAt
     * @param wrapper
     */
    @Update("<script>"
            + "UPDATE products "
            + "SET deleted_at = #{deletedAt}, "
            + "status = -1 "
            + "WHERE 1 = 1 "
            + "<if test='id != null'>AND id = #{id}</if>"
            + "<if test='categoryId != null'>AND category_id = #{categoryId}</if>"
            + "<if test='name != null'>AND name = #{name}</if>"
            + "<if test='status != null'>AND status = #{status}</if>"
            + "<if test='wrapper != null'>${wrapper.sqlSegment}</if>"
            + "</script>")
    void softDeleteProduct(@Param("id") Long id,
                           @Param("categoryId") Long categoryId,
                           @Param("name") String name,
                           @Param("status") Integer status,
                           @Param("deletedAt") LocalDateTime deletedAt,
                           @Param("wrapper") QueryWrapper<Products> wrapper);

    /**
     *
     * @param productQuery
     * @param wrapper
     * @return
     */
    @Select("<script>"
            + "SELECT * FROM products "
            + "WHERE 1 = 1 "
            + "<if test='productQuery.name != null'>AND name LIKE CONCAT('%', #{productQuery.name}, '%')</if>"
            + "<if test='productQuery.description != null'>AND description LIKE CONCAT('%', #{productQuery.description}, '%')</if>"
            + "<if test='productQuery.categoryId != null'>AND category_id = #{productQuery.categoryId}</if>"
            + "<if test='productQuery.status != null'>AND status = #{productQuery.status}</if>"
            + "<if test='productQuery.minPrice != null'>AND price &gt;= #{productQuery.minPrice}</if>"
            + "<if test='productQuery.maxPrice != null'>AND price &lt;= #{productQuery.maxPrice}</if>"
            + "<if test='wrapper != null'>${wrapper.sqlSegment}</if>"
            + "LIMIT 1"
            + "</script>")
    Products querySingleProduct(
            @Param("productQuery") ProductQuery productQuery,
            @Param("wrapper") QueryWrapper<Products> wrapper);


    /**
     *
     * @param productQuery
     * @param wrapper
     * @return
     */
    @Select("<script>"
            + "SELECT * FROM products "
            + "WHERE 1 = 1 "
            + "<if test='productQuery.name != null'>AND name LIKE CONCAT('%', #{productQuery.name}, '%')</if>"
            + "<if test='productQuery.description != null'>AND description LIKE CONCAT('%', #{productQuery.description}, '%')</if>"
            + "<if test='productQuery.categoryId != null'>AND category_id = #{productQuery.categoryId}</if>"
            + "<if test='productQuery.status != null'>AND status = #{productQuery.status}</if>"
            + "<if test='productQuery.minPrice != null'>AND price &gt;= #{productQuery.minPrice}</if>"
            + "<if test='productQuery.maxPrice !=null'>AND price &lt;= #{productQuery.maxPrice}</if>"
            + "<if test='wrapper != null'>${wrapper.sqlSegment}</if>"
            + "</script>")
    List<Products> querybatchProducts(
            @Param("productQuery") ProductQuery productQuery,
            @Param("wrapper") QueryWrapper<Products> wrapper);

    /**
     *
     * @param productQuery
     * @param page
     * @return
     */

    @Select("<script>"
            + "SELECT * FROM products "
            + "WHERE 1 = 1 "
            + "<if test='productQuery.name != null'>AND name LIKE CONCAT('%', #{productQuery.name}, '%')</if>"
            + "<if test='productQuery.description != null'>AND description LIKE CONCAT('%', #{productQuery.description}, '%')</if>"
            + "<if test='productQuery.categoryId != null'>AND category_id = #{productQuery.categoryId}</if>"
            + "<if test='productQuery.status != null'>AND status = #{productQuery.status}</if>"
            + "<if test='productQuery.minPrice != null'>AND price &gt;= #{productQuery.minPrice}</if>"
            + "<if test='productQuery.maxPrice != null'>AND price &lt;= #{productQuery.maxPrice}</if>"
            + "</script>")
    List<Products> queryProductsWithPagination(
            @Param("productQuery") ProductQuery productQuery,
            Page<Products> page);  // 分页参数

}








