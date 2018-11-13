package com.neighbor.common.util;

public class PageTools {
	private Integer index = 1;
	private Integer pageSize = 20;
	private Integer totalPage = 0;
	private Integer rowIndex;
	private Long total;
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	public Integer getRowIndex() {
		rowIndex =(index-1)*pageSize;
		return rowIndex;
	}
	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}
	public Long getTotal() {
		return total;
	}
	
	public Integer getTotalPage() {
		return totalPage;
	} 
	public void setTotal(Long total) {
		this.total = total;
		this.totalPage = (int) ((this.total-1)/this.pageSize+1);
//		if(this.index>totalPage){
//			this.index=totalPage;
//		}
	}
	@Override
	public String toString() {
		return String.format("PageTools [index=%s, pageSize=%s, totalPage=%s, rowIndex=%s, total=%s]", index, pageSize,
				totalPage, rowIndex, total);
	}



}
