package cn.mbtt.service.mapper;

import cn.mbtt.service.domain.po.Products;
import cn.mbtt.service.handler.JsonListTypeHandler;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ProductMapper {

    // 查询所有产品
    @Select("SELECT * FROM products WHERE status != -1")  // 排除已删除的商品
    @Results(id = "productResultMap", value = {
        @Result(property = "id", column = "id"),
        @Result(property = "categoryId", column = "category_id"),
        @Result(property = "name", column = "name"),
        @Result(property = "description", column = "description"),
        @Result(property = "price", column = "price"),
        @Result(property = "originalPrice", column = "original_price"),
        @Result(property = "stock", column = "stock"),
        @Result(property = "salesCount", column = "sales_count"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at"),
        @Result(property = "images", column = "images", typeHandler = JsonListTypeHandler.class, jdbcType = JdbcType.VARCHAR)  // 使用 JsonListTypeHandler
    })
    List<Products> findAll();

    // 根据ID查询单个产品
    @Select("SELECT * FROM products WHERE id = #{id} AND status != -1")
    @ResultMap("productResultMap")  // 使用上面定义的映射
    Products findById(Long id);

    // 插入新商品
    @Insert("INSERT INTO products(category_id, name, description, price, original_price, stock, images, sales_count, status, created_at, updated_at) " +
            "VALUES(#{categoryId}, #{name}, #{description}, #{price}, #{originalPrice}, #{stock}, #{images, typeHandler=cn.mbtt.service.handler.JsonListTypeHandler, jdbcType=VARCHAR}, #{salesCount}, #{status}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")  // 添加 @Options 注解
    void insert(Products product);

    // 更新商品信息
    @Update("UPDATE products SET category_id = #{categoryId}, name = #{name}, description = #{description}, price = #{price}, " +
            "original_price = #{originalPrice}, stock = #{stock}, images = #{images, typeHandler=cn.mbtt.service.handler.JsonListTypeHandler, jdbcType=VARCHAR}, sales_count = #{salesCount}, status = #{status}, " +
            "updated_at = #{updatedAt} WHERE id = #{id}")
    void update(Products product);

    // 删除商品（软删除）
//     @Update("UPDATE products SET status = -1, deleted_at = #{deletedAt} WHERE id = #{id}")
//     void delete(Long id, LocalDateTime deletedAt);
    @Update("UPDATE products SET status = -1, deleted_at = #{deletedAt} WHERE id = #{id}")
    int delete(@Param("id") Long id, @Param("deletedAt") LocalDateTime deletedAt);
//查询经过软删除的商品
    @Select("SELECT status FROM products WHERE id = #{id}")
    Products getStatusById(Long id);


     //查询没有做复杂实现，仅仅实现了通过id查询
    // 批量查询商品
    @Select("<script>" +
            "SELECT * FROM products WHERE id IN " +
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            " AND status != -1" +
            "</script>")
    List<Products> findByIds(@Param("ids") List<Long> ids);
}
