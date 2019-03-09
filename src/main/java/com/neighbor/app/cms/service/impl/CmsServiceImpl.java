package com.neighbor.app.cms.service.impl;

import com.neighbor.app.cms.dao.CmsMapper;
import com.neighbor.app.cms.entity.Cms;
import com.neighbor.app.cms.service.CmsService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CmsServiceImpl implements CmsService {

    private static final Logger logger = LoggerFactory.getLogger(CmsServiceImpl.class);

    @Autowired
    private CmsMapper cmsMapper;

    public ResponseResult list(UserInfo user, Cms cms) throws Exception {

        logger.info("广告列表...");
        ResponseResult result = new ResponseResult();
        Long total = cmsMapper.selectPageTotalCount(cms);
        List<Cms> pageList = cmsMapper.selectByParams(cms);
        PageTools pageTools = cms.getPageTools();
        pageTools.setTotal(total);
        result.addBody("resultList", pageList);
        result.addBody("pageTools", pageTools);
        return result;

    }

    @Override
    public int insertSelective(Cms record) throws Exception {
        Date currentTime = new Date();

        record.setCreateTime(currentTime);
        record.setUpdateTime(currentTime);
        record.setCmsType("1");
        int count = cmsMapper.insertSelective(record);

        return count;

    }

    public Cms viewCms(Cms cms) throws Exception {
        cms = cmsMapper.selectByPrimaryKey(cms.getId());
        return cms;
    }

    @Override
    public int updateCms(Cms record) throws Exception {
        Date currentTime = new Date();

        record.setUpdateTime(currentTime);
        int count = cmsMapper.updateByPrimaryKeySelective(record);

        return count;

    }

    public void deleteCms(Long id) throws Exception {
        cmsMapper.deleteByPrimaryKey(id);
    }
}
