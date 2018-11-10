package com.neighbor.common.util;

import com.neighbor.app.api.common.BaseApiResp;

import java.util.List;

public class PageResp {
    private Long totalNum;
    private List pageList;
    private Long totalPage;
    private boolean hasNext = true;

    private Long pageIndex;
    private Long pageSize;

    public Long getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Long pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalPage() {
        if(pageSize!=null&&pageSize>0&&totalNum!=null&&totalNum>0){
            totalPage = totalNum%pageSize==0?(totalNum/pageSize):(totalNum/pageSize)+1;
        }
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public boolean getHasNext() {
        if(pageIndex!=null&&pageIndex>0
                &&pageSize!=null&&pageSize>0&&totalNum!=null&&totalNum>0){
            if(pageIndex*pageSize>=totalNum){
                return false;
            }
        }
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public List getPageList() {
        return pageList;
    }

    public void setPageList(List pageList) {
        this.pageList = pageList;
    }
}
