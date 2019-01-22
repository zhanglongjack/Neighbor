package com.neighbor.app.packet.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import com.neighbor.app.packet.entity.PacketDetail;

@Mapper
public interface PacketDetailMapper {
    int insertSelective(PacketDetail record);

    PacketDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PacketDetail record);

	List<PacketDetail> selectPacketDetailByPacketId(Long packetId);

	Long selectPageTotalCount(PacketDetail packetDetail);

	List<PacketDetail> selectPageByObjectForList(PacketDetail packetDetail);

	Map<String, Object> queryGotPacketSumAmount(PacketDetail packetDetail);

}