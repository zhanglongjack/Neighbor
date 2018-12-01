package com.neighbor.app.packet.dao;

import org.apache.ibatis.annotations.Mapper;

import com.neighbor.app.packet.entity.PacketDetail;

@Mapper
public interface PacketDetailMapper {
    int insertSelective(PacketDetail record);

    PacketDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PacketDetail record);

}