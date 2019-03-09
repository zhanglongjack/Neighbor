package com.neighbor.app.cms.service;

import com.neighbor.app.cms.entity.Cms;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.ResponseResult;

public interface CmsService {

    public ResponseResult list(UserInfo user, Cms cms) throws Exception;

    public int insertSelective(Cms record) throws Exception;

    public Cms viewCms(Cms cms) throws Exception;

    public int updateCms(Cms record) throws Exception;

    public void deleteCms(Long id) throws Exception;

}
