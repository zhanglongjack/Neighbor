package com.neighbor.app.product.controller;

import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.product.entity.Product;
import com.neighbor.app.product.entity.ProductImg;
import com.neighbor.app.product.entity.ProductOrder;
import com.neighbor.app.product.service.ProductService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.constants.EnvConstants;
import com.neighbor.common.security.EncodeData;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/product")
@SessionAttributes("user")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private Environment env;

    @RequestMapping(value = "/listProduct.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult listProduct(@ModelAttribute("user") UserInfo user, PageTools pageTools, Product product) throws Exception {
        logger.info("listProduct request user >>>> " + user);
        product.setPageTools(pageTools);
        ResponseResult result = productService.listProduct(user, product);
        return result;
    }

    @RequestMapping(value = "/getProduct.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult getProduct(@ModelAttribute("user") UserInfo user, Product product) {
        logger.info("getProduct begin ......");

        ResponseResult result = new ResponseResult();
        try {

            product = productService.viewProduct(product);

            result.addBody("product", product);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(ErrorCodeDesc.failed.getValue());
        }

        return result;
    }

    @RequestMapping(value = "/payAction.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult payAction(@ModelAttribute("user") UserInfo user, ProductOrder productOrder) {
        logger.info("payAction begin ......");

        ResponseResult result = new ResponseResult();
        try {

            productService.payAction(user, productOrder, result);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(ErrorCodeDesc.failed.getValue());
        }

        return result;
    }

    @RequestMapping(value = "/pageView.ser")
    @ResponseBody
    public Map<String, Object> pageView(Product product, PageTools pageTools, @ModelAttribute("user") UserInfo user) throws Exception {
        logger.debug("ProductController pageView : " + user);

//        if (!user.isAdmin()) {
//            userInfo.setUserID(user.getId());
//        }
        Long size = ((PageTools) (productService.listProduct(null, product).getBody().get("pageTools"))).getTotal();
        pageTools.setTotal(size);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("pageTools", pageTools);
        return result;
    }

    @RequestMapping(value = "/loadPage.ser")
    public ModelAndView loadPage(Product product, PageTools pageTools,
                                 @ModelAttribute("user") UserInfo user) throws Exception {
        logger.debug("ProductController loadPage :" + product + " page info ===" + pageTools);

//        if (!user.isAdmin()) {
//            userInfo.setUserID(user.getId());
//        }

        Map<String, Object> body = productService.listProduct(null, product).getBody();

        product.setPageTools(pageTools);
        ModelAndView mv = new ModelAndView("page/product/Content :: container-fluid");
        Long size = ((PageTools) (body.get("pageTools"))).getTotal();
        pageTools.setTotal(size);

        mv.addObject("resultList", body.get("resultList"));
        mv.addObject("pageTools", pageTools);
        mv.addObject("queryObject", product);

        return mv;
    }

    @RequestMapping(value = "/addModalView.ser")
    public String addModalView(Long id, Model model) throws Exception {
        logger.debug("addModalView request:" + id + ",model:" + model);

        return "page/product/AddModal";
    }

    @RequestMapping(value = "/productAdd.ser")
    @ResponseBody
    public Map<String, Object> productAdd(UserInfo userInfo, Product product) throws Exception {
        logger.info("productAdd request:{}", product);
        int num = productService.insertSelective(product);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        map.put("addNumber", num);
        return map;
    }

    @RequestMapping(value = "/modifyModalView.ser")
    public String modifyModalView(Long id, Model model) throws Exception {
        logger.debug("modifyModalView request:" + id + ",model:" + model);
        if (id != null) {
            Product product = new Product();
            product.setId(id);
            product = productService.viewProduct(product);
            model.addAttribute("modifyInfo", product);
        }

        logger.debug("primaryModalView model : " + model);

        return "page/product/ModifyModal";
    }

    @RequestMapping(value = "/productModify.ser")
    @ResponseBody
    public Map<String, Object> productModify(UserInfo userInfo, Product product) throws Exception {
        logger.info("productModify request:{}", product);
        int num = productService.updateProduct(product);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        map.put("addNumber", num);
        return map;
    }

    @RequestMapping(value = "/loadImg.ser", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult loadImg(Long id) {
        logger.info("loadImg begin ......");

        ResponseResult result = new ResponseResult();
        try {

            ProductImg productImg=new ProductImg();
            productImg.setProductId(id);
            List<ProductImg> productImgList=productService.listProductImg(productImg);
            result.addBody("productImgList", productImgList);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(ErrorCodeDesc.failed.getValue());
        }

        return result;
    }

    @RequestMapping(value = "/deleteImg.ser", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult deleteImg(String imgUrl,Long id) {
        logger.info("deleteImg begin ......");

        ResponseResult result = new ResponseResult();
        try {

            String filepath = env.getProperty(EnvConstants.UPLOADER_FILEPATH) + imgUrl;
            File file = new File(filepath);
            file.delete();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(ErrorCodeDesc.failed.getValue());
        }

        return result;
    }

    @RequestMapping(value = "/deleteProduct.ser", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult deleteProduct(Long id) {
        logger.info("deleteProduct begin ......");

        ResponseResult result = new ResponseResult();
        try {

            productService.deleteProduct(id);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(ErrorCodeDesc.failed.getValue());
        }

        return result;
    }

}
