package com.neighbor.app.packet.service;

import com.neighbor.app.packet.entity.Packet;

public interface PacketService{

    int insertSelective(Packet record);

    Packet selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Packet record);

	public Packet sendPacket(Packet record) throws Exception;
}