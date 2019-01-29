package com.neighbor.app.sys.service.impl;

import com.neighbor.app.sys.dao.SysVersionMapper;
import com.neighbor.app.sys.entity.SysVersion;
import com.neighbor.app.sys.service.SysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysServiceImpl implements SysService {

    @Autowired
    private SysVersionMapper sysVersionMapper;

    public SysVersion viewLastVersion() throws Exception {
        return sysVersionMapper.selectLast();
    }

}
