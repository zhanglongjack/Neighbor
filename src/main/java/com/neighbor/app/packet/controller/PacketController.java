package com.neighbor.app.packet.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.neighbor.app.packet.constants.PacketContainer;
import com.neighbor.app.packet.entity.Packet;
import com.neighbor.app.packet.entity.PacketDetail;
import com.neighbor.app.packet.service.PacketService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.common.util.PageTools;
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
		UserWallet wallet = userWalletService.selectByPrimaryUserId(user.getId());
		Packet resultPacket = packetService.sendPacket(packet,wallet);
		ResponseResult result = new ResponseResult();
		result.addBody("wallet", wallet);
		if(resultPacket!=null){
			result.addBody("packet", resultPacket);
		}else{
			result.setErrorCode(1);
			result.setErrorMessage("余额不足");
		}
		
		return result;
	}
	
	@RequestMapping(value = "/grabPacekt.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult grabPacekt(@ModelAttribute("user") UserInfo user, Packet packet,Long gameId) throws Exception {
		logger.info("grabPacekt request :packet== {},user =={}" , packet,user);
		return packetService.grabPacekt(packet,user,gameId);
	}
	
	@RequestMapping(value = "/queryNewestPacketInfo.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult queryNewestPacketInfo(Packet packet,@ModelAttribute("user") UserInfo user) throws Exception {
		logger.info("queryNewestPacketInfo request : " + packet);
		
		Packet cachePacket = packetContainer.get(packet.getId());
		
		return packetService.checLeftoverPacket(cachePacket.getStatus(), cachePacket,user.getId());
	}
	
	@RequestMapping(value = "/gotPacketList.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult gotPacketList(@ModelAttribute("user") UserInfo user,PageTools pageTools,String year) {
		logger.info("gotPacketList request : " +pageTools); 
		PacketDetail packetDetail = new PacketDetail();
		packetDetail.setGotUserId(user.getId());
		packetDetail.setCreateYear(year);
		packetDetail.setPageTools(pageTools); 
		ResponseResult result = packetService.packetDetailPage(packetDetail);
		logger.info("gotPacketList response : " +result);
		return result;
	}
	
	@RequestMapping(value = "/sendPacketList.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult sendPacketList(@ModelAttribute("user") UserInfo user,PageTools pageTools,String year) {
		logger.info("sendPacketList request : " +pageTools); 
		Packet packet = new Packet();
		packet.setUserId(user.getId());
		packet.setCreateYear(year);
		packet.setPageTools(pageTools); 
		ResponseResult result = packetService.packetPage(packet);
		logger.info("sendPacketList response : " +result);
		return result;
	}
	
	
	
	
	
	
	
	

}
