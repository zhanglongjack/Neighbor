package com.neighbor.app.sys.service;

import com.neighbor.app.sys.entity.SysVersion;

public interface SysService {
    public SysVersion viewLastVersion() throws Exception;
}
