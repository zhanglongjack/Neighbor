package com.neighbor.app.api.po;

import com.neighbor.app.api.common.BaseApiResp;

import java.util.List;

public class PageResp<T> extends BaseApiResp {
    private Long totalNum;
    private List<T> pageList;

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public List<T> getPageList() {
        return pageList;
    }

    public void setPageList(List<T> pageList) {
        this.pageList = pageList;
    }
}
