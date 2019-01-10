package com.neighbor.app.product.service.impl;

import com.neighbor.app.product.dao.ProductImgMapper;
import com.neighbor.app.product.dao.ProductMapper;
import com.neighbor.app.product.dao.ProductOrderItemMapper;
import com.neighbor.app.product.dao.ProductOrderMapper;
import com.neighbor.app.product.entity.Product;
import com.neighbor.app.product.entity.ProductImg;
import com.neighbor.app.product.entity.ProductOrder;
import com.neighbor.app.product.entity.ProductOrderItem;
import com.neighbor.app.product.service.ProductService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.common.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
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

    @Autowired
    private UserWalletService userWalletService;

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

    public void payAction(UserInfo userInfo, ProductOrder productOrder,ResponseResult result) {

        Date currentTime = new Date();
        Long userId = userInfo.getId();

        productOrder.setCreateTime(currentTime);
        productOrder.setUpdateTime(currentTime);
        productOrder.setContactDate(DateUtils.formatDateStr(currentTime, DateFormateType.SHORT_FORMAT));
        productOrder.setContactTime(DateUtils.formatDateStr(currentTime, DateFormateType.TIME_FORMAT));
        productOrder.setUserId(userId);
        productOrder.setState("1");
        productOrderMapper.insertSelective(productOrder);

        ProductOrderItem orderItem = new ProductOrderItem();
        orderItem.setCreateTime(currentTime);
        orderItem.setUpdateTime(currentTime);
        orderItem.setContactDate(DateUtils.formatDateStr(currentTime, DateFormateType.SHORT_FORMAT));
        orderItem.setContactTime(DateUtils.formatDateStr(currentTime, DateFormateType.TIME_FORMAT));
        orderItem.setOrderId(productOrder.getId());
        orderItem.setProductId(Long.parseLong(productOrder.getProductId()));
        orderItem.setNum(1);
        productOrderItemMapper.insertSelective(orderItem);

        UserWallet userWallet = new UserWallet();
        userWallet.setScore(productOrder.getScore().negate());
        userWallet.setuId(userInfo.getId());
        userWalletService.updateWalletAmount(userWallet);

        userWallet = userWalletService.selectByPrimaryUserId(userInfo.getId());

        result.addBody("userWallet",userWallet);
    }

}
