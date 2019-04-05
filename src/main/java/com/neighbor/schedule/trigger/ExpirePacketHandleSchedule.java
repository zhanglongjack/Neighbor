package com.neighbor.schedule.trigger;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.neighbor.app.packet.constants.PacketContainer;
import com.neighbor.app.packet.constants.PacketStatus;
import com.neighbor.app.packet.entity.Packet;
import com.neighbor.app.packet.service.PacketService;
import com.neighbor.common.util.DateUtils;

@Component
public class ExpirePacketHandleSchedule {
	private static final Logger logger = LoggerFactory.getLogger(ExpirePacketHandleSchedule.class);
	
	@Autowired
	private PacketService packetService; 
	@Autowired
	private PacketContainer packetContainer; 

	/**
	 * 个人消息定时推送任务
	 */
	@Scheduled(cron = "0 0/1 * * * *")
	public void userMsgPush() {
		 logger.info("开始执行红包过去检查任务");
		try {
			String today = DateUtils.getStringDateShort();
			Packet packetParam = new Packet();
			packetParam.setStatus(PacketStatus.uncollected.toString());
			packetParam.setGroupIdIsNotNull("1");
			packetParam.setSendDateLess(today);
			
			handle(packetParam);
			
			packetParam.setSendDateLess(null);
			packetParam.setSendDate(today);
			packetParam.setSendTimeLess(DateUtils.getTimeBy(-3));
			handle(packetParam);
			
			clearPacketCache(today);
			
		} catch (Exception e) {
			logger.error("处理一对一单人消息推送异常",e);
		} 
	}

	private void clearPacketCache(String today) {
		for(Long key : packetContainer.packetMap.keySet()){
			Packet packet = packetService.selectByPrimaryKey(key);
			
			if(today.equals(packet.getSendDate())){
				String tenMinite = DateUtils.getTimeBy(-10);
				Long beforeTen = Long.parseLong(tenMinite.replaceAll(":", "")) ;
				Long sendTie = Long.parseLong(packet.getSendTime().replaceAll(":", "")) ;
				if(sendTie<beforeTen){
					packetContainer.clearMap(key);
				}
			}else{
				packetContainer.clearMap(key);
			}
		}
	}
	
	public void handle(Packet packetParam) {
		List<Packet> packetList = packetService.selectPacketBySelective(packetParam);
		logger.info("本次处理的红包有[{}]条",packetList.size());
		for(Packet packet : packetList){
			packetService.expirePacketHandle(packet.getId()); 
		}
	}
	
}
