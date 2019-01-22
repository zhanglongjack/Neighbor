package com.neighbor.app.packet.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.neighbor.app.packet.entity.Packet;
import com.neighbor.app.packet.entity.PacketDetail;

@Mapper
public interface PacketMapper {

    int insertSelective(Packet record);

    Packet selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Packet record);
    
    Packet lockPacketByPrimaryKey(Long id);
    
    int updateCollectedNumBy(Packet record);

	Map<String, Object> querySendPacketSumAmount(Packet packet);

	Long selectPageTotalCount(Packet packet);

	List<PacketDetail> selectPageByObjectForList(Packet packet);
    
}