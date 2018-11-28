package com.neighbor.common.websoket.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.neighbor.common.websoket.po.SocketMessage;
@Mapper
public interface SocketMessageMapper {
    int deleteByPrimaryKey(Long msgId);

    int insertSelective(SocketMessage record);

    SocketMessage selectByPrimaryKey(Long msgId);

    int updateByPrimaryKeySelective(SocketMessage record);

	List<SocketMessage> selectByStatus(String status);

	List<SocketMessage> selectbySelective(SocketMessage msg);

	void insertRelationShipSelective(Map<String, Long> relationShip);

}