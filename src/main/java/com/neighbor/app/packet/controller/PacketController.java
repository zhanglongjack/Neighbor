package com.neighbor.app.packet.controller;

import com.neighbor.app.packet.constants.PacketContainer;
import com.neighbor.app.packet.entity.Packet;
import com.neighbor.app.packet.service.PacketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.common.util.ResponseResult;

@Controller
@RequestMapping(value = "/packet")
@SessionAttributes("user")
public class PacketController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private PacketService packetService;
	@Autowired
	private PacketContainer packetContainer;
	@Autowired
	private UserWalletService userWalletService;
	
	@RequestMapping(value = "/sendPacket.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult sendPacket(@ModelAttribute("user") UserInfo user, Packet packet) throws Exception {
		packet.setUserId(user.getId());
		logger.info("sendPacket request : " + packet);
		Packet resultPacket = packetService.sendPacket(packet);
		UserWallet wallet = userWalletService.selectByPrimaryUserId(user.getId());
		ResponseResult result = new ResponseResult();
		result.addBody("packet", resultPacket);
		result.addBody("wallet", wallet);
		return result;
	}
	
	@RequestMapping(value = "/grabPacekt.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult grabPacekt(@ModelAttribute("user") UserInfo user, Packet packet) throws Exception {
		logger.info("grabPacekt request :packet== {},user =={}" , packet,user);
		return packetService.grabPacekt(packet,user);
	}
	
	@RequestMapping(value = "/queryNewestPacketInfo.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult queryNewestPacketInfo(Packet packet) throws Exception {
		logger.info("queryNewestPacketInfo request : " + packet);
		
		Packet cachePacket = packetContainer.get(packet.getId());
		
		return packetService.checLeftoverPacket(cachePacket.getStatus(), cachePacket);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}