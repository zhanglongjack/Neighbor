package com.neighbor.common.websoket.dao;

import com.neighbor.common.websoket.po.SocketMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Mapper
public interface SocketMessageMapper {
    int deleteByPrimaryKey(Long msgId);

    int insertSelective(SocketMessage record);

    SocketMessage selectByPrimaryKey(Long msgId);

    int updateByPrimaryKeySelective(SocketMessage record);

	List<SocketMessage> selectByStatus(String status);

	List<SocketMessage> selectbySelective(SocketMessage msg);

	void insertRelationShipSelective(Map<String, Long> relationShip);

    Long selectPageTotalCount(HashMap map);

    List<SocketMessage> selectPageByObjectForList(HashMap map);

    void changeRecord(HashMap map);
    int deleteMessage(SocketMessage record);
    int jobDeleteMessage(SocketMessage record);
}