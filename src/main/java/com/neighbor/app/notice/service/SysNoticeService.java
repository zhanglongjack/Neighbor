package com.neighbor.app.notice.service;

import java.util.List;

import com.neighbor.app.notice.entity.SysNotice;

public interface SysNoticeService {
    int insert(SysNotice record);

    SysNotice selectByPrimaryKey(Long noticeId);

    int updateByPrimaryKeySelective(SysNotice record);
    
    Long selectPageTotalCount(SysNotice record);

    List<SysNotice> selectPageByObjectForList(SysNotice record);
    
    List<SysNotice> selectBySelective(SysNotice record);
}