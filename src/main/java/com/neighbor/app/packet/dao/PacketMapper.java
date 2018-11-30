package com.neighbor.app.packet.dao;

import org.apache.ibatis.annotations.Mapper;

import com.neighbor.app.packet.entity.Packet;

@Mapper
public interface PacketMapper {

    int insertSelective(Packet record);

    Packet selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Packet record);
    
    Packet lockPacketByPrimaryKey(Long id);
    
    int updateCollectedNumBy(Packet record);
    
}