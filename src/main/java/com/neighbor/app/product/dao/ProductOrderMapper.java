package com.neighbor.app.product.dao;

import com.neighbor.app.product.entity.ProductOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductOrder record);

    int insertSelective(ProductOrder record);

    ProductOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductOrder record);

    int updateByPrimaryKey(ProductOrder record);
}