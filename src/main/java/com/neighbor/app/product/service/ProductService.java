package com.neighbor.app.product.service;

import com.neighbor.app.product.entity.Product;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.ResponseResult;

public interface ProductService {

    public ResponseResult listProduct(UserInfo user, Product product) throws Exception;



}
