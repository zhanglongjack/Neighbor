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

import java.util.List;

@Service
public class CmsServiceImpl implements CmsService {

    private static final Logger logger = LoggerFactory.getLogger(CmsServiceImpl.class);

    @Autowired
    private CmsMapper cmsMapper;

    public ResponseResult list(UserInfo user, Cms cms) throws Exception{

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

}
