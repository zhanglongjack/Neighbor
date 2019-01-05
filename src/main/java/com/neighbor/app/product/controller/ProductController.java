package com.neighbor.app.product.controller;

import com.neighbor.app.cms.controller.CmsController;
import com.neighbor.app.cms.entity.Cms;
import com.neighbor.app.cms.service.CmsService;
import com.neighbor.app.product.entity.Product;
import com.neighbor.app.product.service.ProductService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/product")
@SessionAttributes("user")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/listProduct.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult listProduct(@ModelAttribute("user") UserInfo user, PageTools pageTools,Product product) throws Exception {
        logger.info("listProduct request user >>>> " + user);
        product.setPageTools(pageTools);
        ResponseResult result = productService.listProduct(user, product);
        return result;
    }

}
