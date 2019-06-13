package com.neighbor.app.packet.controller;

import com.neighbor.app.commission.entity.CommissionHandleTask;
import com.neighbor.app.game.entity.GameRule;
import com.neighbor.app.group.entity.Group;
import com.neighbor.app.group.service.GroupService;
import com.neighbor.app.packet.constants.PacketContainer;
import com.neighbor.app.packet.entity.Packet;
import com.neighbor.app.packet.entity.PacketDetail;
import com.neighbor.app.packet.service.PacketService;
import com.neighbor.app.robot.entity.GrapPacketData;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.common.constants.CommonConstants;
import com.neighbor.common.constants.EnvConstants;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.BlockingQueue;

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
	@Autowired
	private GroupService groupService;
	@Autowired
	private BlockingQueue<GrapPacketData> taskQueue;
	@Autowired
	private BlockingQueue<CommissionHandleTask> commisionHandleTaskQueue;
	@Autowired
	private CommonConstants commonConstants;
	
	
	@RequestMapping(value = "/sendPacket.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult sendPacket(@ModelAttribute("user") UserInfo user, Packet packet) throws Exception {
		packet.setUserId(user.getId());
		packet.setNickName(user.getNickName());
		logger.info("sendPacket request : " + packet);
		UserWallet wallet = userWalletService.selectByPrimaryUserId(user.getId());
		Packet resultPacket = packetService.sendPacket(packet,wallet,user);
		ResponseResult result = new ResponseResult();
		result.addBody("wallet", wallet);
		if(resultPacket!=null){
			result.addBody("packet", resultPacket);
			Group group = new Group();
			group.setId(packet.getGroupId());
			Group groupResult = groupService.selectByPrimeryId(packet.getGroupId());
			
			addGrapPacketTask(groupResult.getId(), groupResult.getGameId(), resultPacket);
			
		}else{
			result.setErrorCode(1);
			result.setErrorMessage("余额不足");
		}
		
		return result;
	}
	
	private void addGrapPacketTask(Long groupId, Long gameId, Packet packet) {
		GrapPacketData data = new GrapPacketData();
		data.setGameId(gameId);
		data.setGroupId(packet.getGroupId());
		data.setPacket(packet);
		try {
			logger.info("开始尝试加人机器人抢红包队列任务");
			boolean isOk = taskQueue.offer(data);
			logger.info("是否加入机器人抢红包队列任务中?{}",isOk);
		} catch (Exception e) {
			logger.error("线程启动",e);
		}
	}
	
	@RequestMapping(value = "/grabPacekt.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult grabPacekt(@ModelAttribute("user") UserInfo user, Packet packet,Long gameId) throws Exception {
		logger.info("grabPacekt request :packet== {},user =={}" , packet,user);
		ResponseResult result = packetService.grabPacekt(packet,user,gameId);
		CommissionHandleTask task = (CommissionHandleTask) result.getBody().get("task");
		if(task!=null){
			logger.info("添加分佣任务:{}",task);
			commisionHandleTaskQueue.offer(task);
		}
		GameRule luckGot = (GameRule) result.getBody().get("luckGot");
		GameRule thunderGot = (GameRule) result.getBody().get("thunderGot");
		Packet cachePacket = (Packet) result.getBody().get("packet");
		if(result.getErrorCode()==0){
			packetService.grapPacketNotice(packet,user);
		}
		packetService.sendPacketMessage(luckGot,cachePacket,user);
		packetService.sendPacketMessage(thunderGot,cachePacket,user);
		
		return result;
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
	
	@RequestMapping(value = "/packetBaseInitInfo.req",method=RequestMethod.POST)
	@ResponseBody
	public ResponseResult packetBaseInitInfo() {
		logger.info("packetBaseInitInfo request "); 
		String packetNumber = commonConstants.getDictionarysBy(EnvConstants.PACKET_CONF, EnvConstants.PACEKT_BASE_NUM);
		String paidRate = commonConstants.getDictionarysBy(EnvConstants.PACKET_CONF, EnvConstants.PACKET_HIT_RATE);
		ResponseResult result = new ResponseResult();
		result.addBody("packetNumber", packetNumber);
		result.addBody("paidRate", paidRate);
		logger.info("packetBaseInitInfo response : " +result);
		return result;
	}

}
