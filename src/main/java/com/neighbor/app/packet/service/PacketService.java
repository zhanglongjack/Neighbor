package com.neighbor.app.packet.service;

import java.util.List;

import com.neighbor.app.game.entity.GameRule;
import com.neighbor.app.packet.entity.Packet;
import com.neighbor.app.packet.entity.PacketDetail;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.common.util.ResponseResult;

public interface PacketService{

    int insertSelective(Packet record);

    Packet selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Packet record);

    Packet sendPacket(Packet record, UserWallet wallet, UserInfo user) throws Exception;
	
    Packet lockPacketByPrimaryKey(Long id);

	ResponseResult grabPacekt(Packet packet, UserInfo user, Long gameId);

	ResponseResult checLeftoverPacket(String statusStr,Packet packet,Long userId);
 
	ResponseResult packetDetailPage(PacketDetail packetDetail);

	ResponseResult packetPage(Packet packet);

	List<Packet> selectPacketBySelective(Packet packet);

	void expirePacketHandle(Long packetId);

	void sendPacketMessage(GameRule gameRule, Packet cachePacket, UserInfo user);

	void grapPacketNotice(Packet packet, UserInfo grapUser);

}