package com.neighbor.common.websoket.dao;

import org.apache.ibatis.annotations.Mapper;

import com.neighbor.common.websoket.po.SocketMessage;
@Mapper
public interface SocketMessageMapper {
    int deleteByPrimaryKey(Long msgId);

    int insertSelective(SocketMessage record);

    SocketMessage selectByPrimaryKey(Long msgId);

    int updateByPrimaryKeySelective(SocketMessage record);

}