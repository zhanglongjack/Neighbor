package com.neighbor.app.packet.constants;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.neighbor.app.packet.entity.Packet;
import com.neighbor.app.packet.service.PacketService;

@Component
public class PacketContainer  implements ApplicationListener<ContextRefreshedEvent> {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	public Map<Long,Packet> packetMap = new HashMap<Long,Packet>();
	@Autowired
	private PacketService packetService;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(packetMap.size()==0){
			buildPacketMap();
		}
	}

	public void buildPacketMap() {
//		List<UserInfo> userList = packetService.selectByStuats
//		logger.debug("userList:{}",userList);
//		for(UserInfo user : userList){
////			userMap.put(user.getuId(), user.getName());
//		}
		 
		logger.debug("packetMap:{}",packetMap);
	}
	
	public Packet get(Long key){
		if(packetMap.containsKey(key)){
			return packetMap.get(key);
		}
		Packet packet = null;
		synchronized(packetMap){
			if(packetMap.containsKey(key)){
				return packetMap.get(key);
			}
			packet = packetService.selectByPrimaryKey(key);
			put(key, packet);
		}
		
		return packet;
	}
	
	public void put(Long key,Packet packet){
		packetMap.put(key, packet);
	}

}
