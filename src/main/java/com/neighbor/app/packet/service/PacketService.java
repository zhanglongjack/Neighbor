package com.neighbor.app.packet.service;

import com.neighbor.app.packet.entity.Packet;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.common.util.ResponseResult;

public interface PacketService{

    int insertSelective(Packet record);

    Packet selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Packet record);

	public Packet sendPacket(Packet record, UserWallet wallet) throws Exception;
	
    Packet lockPacketByPrimaryKey(Long id);

	ResponseResult grabPacekt(Packet packet, UserInfo user, Long gameId);

	ResponseResult checLeftoverPacket(String statusStr,Packet packet,Long userId);


}