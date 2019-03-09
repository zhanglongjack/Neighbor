package com.neighbor.app.product.service;

import com.neighbor.app.product.entity.Product;
import com.neighbor.app.product.entity.ProductImg;
import com.neighbor.app.product.entity.ProductOrder;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.ResponseResult;

import java.util.List;

public interface ProductService {

    public ResponseResult listProduct(UserInfo user, Product product) throws Exception;

    public Product viewProduct(Product product);

    public void payAction(UserInfo userInfo, ProductOrder productOrder,ResponseResult result);

    public int insertSelective(Product record)throws Exception;

    public List<ProductImg> listProductImg(ProductImg productImg)throws Exception;

    public int updateProduct(Product record) throws Exception;

    public void deleteProduct(Long productId) throws Exception;

}
