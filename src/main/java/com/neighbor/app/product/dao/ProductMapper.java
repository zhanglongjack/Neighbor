package com.neighbor.app.product.dao;

import com.neighbor.app.product.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ProductMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Long id);

    List<Product> selectByParams(Product record);

    int updateByPrimaryKeySelective(Product record);

    Long selectPageTotalCount(Product record);
}