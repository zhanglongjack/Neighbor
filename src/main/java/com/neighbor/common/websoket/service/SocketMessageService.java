package com.neighbor.common.websoket.service;

import com.neighbor.common.websoket.po.SocketMessage;

public interface SocketMessageService {
    int deleteByPrimaryKey(Long msgId);

    int insertSelective(SocketMessage record);

    SocketMessage selectByPrimaryKey(Long msgId);

    int updateByPrimaryKeySelective(SocketMessage record);

}