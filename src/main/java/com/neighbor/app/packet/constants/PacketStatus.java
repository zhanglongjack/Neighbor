package com.neighbor.app.packet.constants;

public enum PacketStatus {
	uncollected("未领完"),
	collected("已领完"),
	part_back("部分退回"),
	all_back("全部退回");
	
	private String des;

	private PacketStatus(String des) {
		this.des = des;
	}

	public String getDes() {
		return des;
	}
	
}
