package com.neighbor.schedule.util;

import com.neighbor.app.packet.entity.Packet;

public class GrapPacketData {
	private Long groupId;
	private Long gameId;
	private Packet packet;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public Packet getPacket() {
		return packet;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}

	@Override
	public String toString() {
		return String.format("GrapPacketData [groupId=%s, gameId=%s, packet=%s]", groupId, gameId, packet);
	}

}
