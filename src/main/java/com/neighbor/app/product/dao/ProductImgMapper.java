package com.neighbor.app.product.dao;

import com.neighbor.app.product.entity.ProductImg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductImgMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductImg record);

    int insertSelective(ProductImg record);

    ProductImg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductImg record);

    int updateByPrimaryKey(ProductImg record);

    List<ProductImg> selectByParams(ProductImg record);

}