package com.neighbor.app.notice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.neighbor.app.notice.entity.SysNotice;

@Mapper
public interface SysNoticeMapper {
    int insert(SysNotice record);

    SysNotice selectByPrimaryKey(Long noticeId);

    int updateByPrimaryKeySelective(SysNotice record);
    
    Long selectPageTotalCount(SysNotice record);

    List<SysNotice> selectPageByObjectForList(SysNotice record);
    
    List<SysNotice> selectBySelective(SysNotice record);
}