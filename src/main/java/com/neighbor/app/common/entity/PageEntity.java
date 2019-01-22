package com.neighbor.app.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neighbor.common.util.PageTools;

public class PageEntity {
	@JsonIgnore
	private PageTools pageTools = new PageTools();

	public PageTools getPageTools() {
		return pageTools;
	}

	public void setPageTools(PageTools pageTools) {
		this.pageTools = pageTools;
	}
	
}
