package com.neighbor.app.product.service.impl;

import com.neighbor.app.product.dao.ProductImgMapper;
import com.neighbor.app.product.dao.ProductMapper;
import com.neighbor.app.product.dao.ProductOrderItemMapper;
import com.neighbor.app.product.dao.ProductOrderMapper;
import com.neighbor.app.product.entity.Product;
import com.neighbor.app.product.entity.ProductImg;
import com.neighbor.app.product.service.ProductService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);


    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductImgMapper productImgMapper;
    @Autowired
    private ProductOrderMapper productOrderMapper;
    @Autowired
    private ProductOrderItemMapper productOrderItemMapper;

    public ResponseResult listProduct(UserInfo user, Product product) throws Exception {

        logger.info("商品列表...");
        ResponseResult result = new ResponseResult();
        Long total = productMapper.selectPageTotalCount(product);
        List<Product> pageList = productMapper.selectByParams(product);
        List<ProductImg> imgList = productImgMapper.selectByParams(null);
        String imgType = product.getImgType();
        for (Product productThis : pageList) {
            productThis.setImgType(imgType);
            makeProductImgInfo(imgList, productThis);
        }

        PageTools pageTools = product.getPageTools();
        pageTools.setTotal(total);
        result.addBody("resultList", pageList);
        result.addBody("pageTools", pageTools);
        return result;

    }

    private void makeProductImgInfo(List<ProductImg> imgList, Product productThis) {
        for (ProductImg productImg : imgList) {
            if (StringUtil.isNotEmpty(productThis.getImgType())) {
                if (productImg.getProductId().longValue() == productThis.getId()
                        && productThis.getImgType().equals(productImg.getImgType())) {
                    productThis.getProductImgList().add(productImg);
                }
            } else {
                if (productImg.getProductId().longValue() == productThis.getId()) {
                    productThis.getProductImgList().add(productImg);
                }
            }
        }
    }

    public Product viewProduct(Product product) {
        product = productMapper.selectByPrimaryKey(product.getId());
        List<ProductImg> imgList = productImgMapper.selectByParams(null);
        makeProductImgInfo(imgList, product);
        return product;
    }

}
