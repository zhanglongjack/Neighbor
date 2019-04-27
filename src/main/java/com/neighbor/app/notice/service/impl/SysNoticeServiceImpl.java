package com.neighbor.app.notice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neighbor.app.notice.dao.SysNoticeMapper;
import com.neighbor.app.notice.entity.SysNotice;
import com.neighbor.app.notice.service.SysNoticeService;

@Service
public class SysNoticeServiceImpl implements SysNoticeService {
	@Autowired
	private SysNoticeMapper sysNoticeMapper;

	@Override
	public int insert(SysNotice record) {
		return sysNoticeMapper.insert(record);
	}

	@Override
	public SysNotice selectByPrimaryKey(Long noticeId) {
		return sysNoticeMapper.selectByPrimaryKey(noticeId);
	}

	@Override
	public int updateByPrimaryKeySelective(SysNotice record) {
		return sysNoticeMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public Long selectPageTotalCount(SysNotice record) {
		return sysNoticeMapper.selectPageTotalCount(record);
	}

	@Override
	public List<SysNotice> selectPageByObjectForList(SysNotice record) {
		return sysNoticeMapper.selectPageByObjectForList(record);
	}

	@Override
	public List<SysNotice> selectBySelective(SysNotice record) {
		return sysNoticeMapper.selectBySelective(record);
	}

}