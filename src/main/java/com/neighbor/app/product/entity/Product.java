package com.neighbor.app.product.entity;

import com.neighbor.app.common.entity.PageEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//商品表
public class Product extends PageEntity {
    private Long id;

    private Date createTime;

    private Date updateTime;

    private String contactDate;

    private String contactTime;

    private String name;

    //商品价格，使用的是积分
    private BigDecimal score;

    private String productDesc;

    //==============下面是扩展属性=====================
    private String imgType;
    private List<ProductImg> productImgList=new ArrayList<>();
    private List<String> imgType1;
    private List<String> imgType2;
    private List<String> imgType3;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getContactDate() {
        return contactDate;
    }

    public void setContactDate(String contactDate) {
        this.contactDate = contactDate;
    }

    public String getContactTime() {
        return contactTime;
    }

    public void setContactTime(String contactTime) {
        this.contactTime = contactTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public List<ProductImg> getProductImgList() {
        return productImgList;
    }

    public void setProductImgList(List<ProductImg> productImgList) {
        this.productImgList = productImgList;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public List<String> getImgType1() {
        return imgType1;
    }

    public void setImgType1(List<String> imgType1) {
        this.imgType1 = imgType1;
    }

    public List<String> getImgType2() {
        return imgType2;
    }

    public void setImgType2(List<String> imgType2) {
        this.imgType2 = imgType2;
    }

    public List<String> getImgType3() {
        return imgType3;
    }

    public void setImgType3(List<String> imgType3) {
        this.imgType3 = imgType3;
    }
}