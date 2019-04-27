package com.neighbor.app.notice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neighbor.common.util.DateUtils;
import com.neighbor.common.util.PageTools;

public class SysNotice {
    private Long noticeId;

    private String noticeTitle;

    private String noticeContent;

    private String beginTime;
    
    private String beginDate;

    private String overTime;
    
    private String overDate;

    private String forceOfflineTime;
    
    private String forceOfflineDate;
    
    private Integer status;

    private String createdTime;

    private String updatedTime;
    
	@JsonIgnore
	private PageTools pageTools;

	public Long getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(Long noticeId) {
		this.noticeId = noticeId;
	}

	public String getNoticeTitle() {
		return noticeTitle;
	}

	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getOverTime() {
		return overTime;
	}

	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}

	public String getOverDate() {
		return overDate;
	}

	public void setOverDate(String overDate) {
		this.overDate = overDate;
	}

	public String getForceOfflineTime() {
		return forceOfflineTime;
	}

	public void setForceOfflineTime(String forceOfflineTime) {
		this.forceOfflineTime = forceOfflineTime;
	}

	public String getForceOfflineDate() {
		return forceOfflineDate;
	}

	public void setForceOfflineDate(String forceOfflineDate) {
		this.forceOfflineDate = forceOfflineDate;
	}

	/**
	 * true-允许登录,false-不允许登录
	 * @return
	 */
	public boolean isForceOffline() {
		if(this.forceOfflineTime==null||"".equals(this.forceOfflineTime)) return true;
		return DateUtils.compareCurrentDateTime(this.forceOfflineTime);
	}
 
	/**
	 * true-显示公告,false-不显示公告
	 * @return
	 */
	public boolean isShowNotice() {
		return !DateUtils.compareCurrentDateTime(this.beginTime);
	}

	/**
	 * false-公告未到结束时间 ,true-公告已过结束时间
	 * @return
	 */
	public boolean isCloseNotice() {
		return !DateUtils.compareCurrentDateTime(this.overTime);
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	public PageTools getPageTools() {
		return pageTools;
	}

	public void setPageTools(PageTools pageTools) {
		this.pageTools = pageTools;
	}

	@Override
	public String toString() {
		return String.format(
				"SysNotice [noticeId=%s, noticeTitle=%s, noticeContent=%s, beginTime=%s, beginDate=%s, overTime=%s, overDate=%s, forceOfflineTime=%s, forceOfflineDate=%s, status=%s, createdTime=%s, updatedTime=%s, pageTools=%s]",
				noticeId, noticeTitle, noticeContent, beginTime, beginDate, overTime, overDate, forceOfflineTime,
				forceOfflineDate, status, createdTime, updatedTime, pageTools);
	}
}