package com.neighbor.app.cms.service;

import com.neighbor.app.cms.entity.Cms;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.ResponseResult;

public interface CmsService {
    public ResponseResult list(UserInfo user, Cms cms) throws Exception;
}
