package com.neighbor.app.product.dao;

import com.neighbor.app.product.entity.ProductOrderItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductOrderItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductOrderItem record);

    int insertSelective(ProductOrderItem record);

    ProductOrderItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductOrderItem record);

    int updateByPrimaryKey(ProductOrderItem record);
}