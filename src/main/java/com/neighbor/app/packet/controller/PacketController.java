package com.neighbor.app.packet.controller;

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
import com.neighbor.common.util.ResponseResult;

@Controller
@RequestMapping(value = "/packet")
@SessionAttributes("user")
public class PacketController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private PacketService packetService;

	@RequestMapping(value = "/sendPacket.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult sendPacket(@ModelAttribute("user") UserInfo user, Packet packet) throws Exception {
		packet.setUserId(user.getId());
		logger.info("sendPacket request : " + packet);
		Packet resultPacket = packetService.sendPacket(packet);
		ResponseResult result = new ResponseResult();
		result.addBody("packet", resultPacket);
		return result;
	}

}
