package com.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.neighbor.StartNeighbor;
import com.neighbor.app.common.util.RandomUtil;
import com.neighbor.app.group.entity.GroupMember;
import com.neighbor.app.group.service.GroupService;
import com.neighbor.app.packet.dao.PacketDetailMapper;
import com.neighbor.app.packet.dao.PacketMapper;
import com.neighbor.app.packet.entity.Packet;
import com.neighbor.app.packet.entity.PacketDetail;
import com.neighbor.app.packet.service.PacketService;
import com.neighbor.app.recharge.constants.ChannelTypeDesc;
import com.neighbor.app.recharge.controller.RechargeController;
import com.neighbor.app.recharge.entity.Recharge;
import com.neighbor.app.users.controller.LoginController;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.dao.SocketMessageMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartNeighbor.class)
public class PacketServiceTest { 
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private PacketMapper packetMapper;
	@Autowired
	private PacketService packetService;
	@Autowired
	private PacketDetailMapper packetDetailService;
	@Autowired
	private LoginController loginController;
	@Autowired
	private RechargeController rechargeController;
	@Autowired
	private UserService userService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private UserWalletService userWalletService;
	@Autowired
	private SocketMessageMapper socketMessageMapper;

	@Test
	public void testSocketSelect(){
		Long count = socketMessageMapper.selectWalletRefreshCountBySelective(600021l);
		System.out.println(count);
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public static void main(String[] args) {
    	double bb = (((int) (1.04*10))*10+1) / 100.0;
    	double aa = (8.04*100)/100;
    	
    	BigDecimal tt = new BigDecimal("20.83").multiply(new BigDecimal(100)).remainder(new BigDecimal(10));
    	System.out.println(bb);
    	System.out.println(aa);
    	System.out.println(tt.toEngineeringString());
	}
	
	public void testSocketMsgUpdate() throws Exception{
		List<PacketDetail> a = packetDetailService.selectPacketDetailByPacketId(37L);
		
		System.err.println(a);
		
		Packet packet = packetService.selectByPrimaryKey(37L);
		
		System.out.println(packet);
		
		//socketMessageService.updateWalletRefreshMsg(600018l);
		
		//Thread.sleep(30000);
	}
	
	public void testRobotGrapPacket() throws Exception{
		Packet packet = new Packet();
		packet.setAmount(new BigDecimal(100));
		packet.setPacketNum(7);
		packet.setHitNum(1);
		packet.setUserId(600023L);
		packet.setGroupId(100001L);
		UserInfo user = userService.selectByPrimaryKey(packet.getUserId());
		UserWallet wallet = userWalletService.selectByPrimaryUserId(packet.getUserId());
		packetService.sendPacket(packet, wallet,user);
		
		Thread.sleep(100000);
	}
	
	
	
	public void test1() throws Exception{
		Long gameId = 1L;
		Packet packet = new Packet();
		packet.setAmount(new BigDecimal(100));
		packet.setPacketNum(7);
		packet.setHitNum(1);
		packet.setUserId(600023L);
		packet.setGroupId(100000L);
		UserInfo user = userService.selectByPrimaryKey(packet.getUserId());
		UserWallet wallet = userWalletService.selectByPrimaryUserId(packet.getUserId());
		final Packet packet1 = packetService.sendPacket(packet, wallet,user);
		PageTools pageTool =new PageTools();
		pageTool.setPageSize(200);
		GroupMember groupMember = new GroupMember();
		groupMember.setPageTools(pageTool);
		groupMember.setGroupId(packet1.getGroupId());
		ResponseResult result =  groupService.memberList(user, groupMember);
		List<GroupMember> memList = (List<GroupMember>) result.getBody().get("resultList");
		
		ExecutorService fixedThreadPool = null;
		try {
			logger.info("开始并发抢红包");
			CyclicBarrier cyclicBarrier = new CyclicBarrier(memList.size());
//			CountDownLatch countDown = new CountDownLatch(memList.size());
			fixedThreadPool = Executors.newFixedThreadPool(memList.size());
			List<Future<Long>> futureList = new ArrayList<Future<Long>>();
			for (int i=0;i<memList.size();i++) {
				final GroupMember member = memList.get(i);
				futureList.add(fixedThreadPool.submit(new Callable<Long>() {
					@Override
					public Long call() throws Exception {
//						countDown.countDown();
//						countDown.await();
						cyclicBarrier.await();
						UserInfo user = new UserInfo();
						try {
							
							user.setUserID(member.getUserId());
							ResponseResult result = packetService.grabPacekt(packet1, user,gameId);
							if(result.getErrorCode()==0){
								return member.getUserId();
							}
							return null;
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("异常的用户:"+user);
							return null;
						}
					}
				}));
			}
			
			for(Future<Long> future : futureList){
				try {
					Long groupMemberId = future.get(100, TimeUnit.SECONDS);
					if(groupMemberId!=null){
						logger.info("抢到红包的ID[{}]",groupMemberId);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(fixedThreadPool!=null){
				fixedThreadPool.shutdown();
			}
		}
		Thread.sleep(10000);
		
		
	}
	
	@Test
	public void testRegister() throws Exception{
		Long phone = 15999585920L;
		for(int i=0;i<100;i++){
			loginController.sendSMS(phone +"");
			loginController.registerLogin(phone+"", "123456","", "xxxxx");
			phone++;
		}
	}
	@Test
	public void testGroupService() throws Exception{
		Long phone = 15999585920L;
		for(int i=0;i<100;i++){
			UserInfo user = userService.selectByUserPhone(phone+"");
			groupService.addGroupMember(100000L, user.getId(), null);
			phone++;
		}
	}
	@Test
	public void testRecharge() throws Exception{
		Long phone = 15999585920L;
		BigDecimal amount = new BigDecimal(1000);
		for(int i=0;i<100;i++){
			UserInfo user = userService.selectByUserPhone(phone+"");
			Recharge recharge = new Recharge();
			recharge.setAmount(amount);
			recharge.setRemarks(ChannelTypeDesc.alipay.getDes());
			recharge.setChannelType(ChannelTypeDesc.alipay.toString());
			rechargeController.recharge(user, recharge);
			phone++;
		}
	}
	@Test
	public void test(){
		try {
			Packet aaa =packetMapper.lockPacketByPrimaryKey(23L);
			logger.info("最佳明细List:",JSON.toJSON(aaa.getDetailList()));
			logger.info("test:"+JSONObject.toJSON(aaa.getDetailList()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
