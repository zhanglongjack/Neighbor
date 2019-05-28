package com.neighbor.common.websoket.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.neighbor.app.game.entity.GameRule;
import com.neighbor.app.packet.entity.Packet;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.DateUtils;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.constants.MessageDeleteStates;
import com.neighbor.common.websoket.constants.MessageStatus;
import com.neighbor.common.websoket.constants.WebSocketChatType;
import com.neighbor.common.websoket.constants.WebSocketMsgType;
import com.neighbor.common.websoket.dao.SocketMessageMapper;
import com.neighbor.common.websoket.po.GroupMsgRalation;
import com.neighbor.common.websoket.po.SocketMessage;
import com.neighbor.common.websoket.po.WebSocketHeader;
import com.neighbor.common.websoket.service.SocketMessageService;

@Service
public class SocketMessageServiceImpl implements SocketMessageService {

	@Autowired
	private SocketMessageMapper socketMessageMapper;

//	@Autowired
//	private ChatListService chatListService;
	
	@Override
	public int deleteByPrimaryKey(Long msgId) {
		return socketMessageMapper.deleteByPrimaryKey(msgId);
	}

	@Override
	public int insertSelective(SocketMessage record) {
		return socketMessageMapper.insertSelective(record);
		/*try {
			chatListService.modifyLastMessage(record);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	@Override
	public SocketMessage selectByPrimaryKey(Long msgId) {
		return socketMessageMapper.selectByPrimaryKey(msgId);
	}

	@Override
	public int updateByPrimaryKeySelective(SocketMessage record) {
		return socketMessageMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<SocketMessage> selectByStatus(String status) {
		return socketMessageMapper.selectByStatus(status);
	}

	@Override
	public List<SocketMessage> selectbySelective(SocketMessage msg) { 
		return socketMessageMapper.selectbySelective(msg);
	} 
	
	@Override
	public List<SocketMessage> selectByTargetUserIdStatus(Long targetUserId, String status, String chatType) {
		SocketMessage msg = new SocketMessage();
		msg.setTargetUserId(targetUserId);
		msg.setStatus(status);
		msg.setChatType(chatType);
		msg.setTargetUserDeleteFlag(MessageDeleteStates.normal.getDes());
		return selectbySelective(msg);
	}

	@Override
	public void insertRelationShipSelective(GroupMsgRalation ralation) {
		socketMessageMapper.insertRelationShipSelective(ralation);
	}


	@Override
	public ResponseResult unreadRecord(UserInfo user) {
		ResponseResult responseResult = new ResponseResult();
		SocketMessage socketMessage = new SocketMessage();
		socketMessage.setTargetUserId(user.getId());
		socketMessage.setTargetUserDeleteFlag(MessageDeleteStates.normal.getDes());
		socketMessage.setMasterMsgType("1");
		socketMessage.setStatus(MessageStatus.pushed_response.toString());//未推送状态
		List<SocketMessage> socketMessageList = selectbySelective(socketMessage);
		responseResult.addBody("messageList",socketMessageList);
		return responseResult;
	}

	@Override
	public ResponseResult pageRecord(UserInfo user, Long friendId,Long msgId, PageTools pageTools) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("userId",user.getId());
		map.put("friendId",friendId);
		map.put("msgId",msgId);
		map.put("rowIndex",pageTools.getRowIndex());
		map.put("pageSize",pageTools.getPageSize());
		Long size = selectPageTotalCount(map);
		pageTools.setTotal(size);
		List<SocketMessage> messageList = selectPageByObjectForList(map);
		ResponseResult result = new ResponseResult();
		result.addBody("messageList", messageList);
		result.addBody("pageTools", pageTools);
		return result;
	}

	@Override
	public ResponseResult changeRecord(UserInfo user, Long friendId, Long msgId, String status) {
		ResponseResult result = new ResponseResult();
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("userId",user.getId());
		map.put("friendId",friendId);
		map.put("msgId",msgId);
		map.put("masterMsgType","1");
		if(MessageStatus.complete.toString().equals(status)){
			map.put("status",MessageStatus.complete.toString());
			map.put("changeComplete","ok");
		}else if(MessageStatus.pushed.toString().equals(status)){
			map.put("status",MessageStatus.pushed.toString());
			map.put("changePushed","ok");
		}
		socketMessageMapper.changeRecord(map);
		return result;
	}

	@Override
	public Long selectPageTotalCount(HashMap<?,?> map) {
		return socketMessageMapper.selectPageTotalCount(map);
	}

	@Override
	public List<SocketMessage> selectPageByObjectForList(HashMap<?,?> map) {
		return socketMessageMapper.selectPageByObjectForList(map);
	}

	@Override
	public List<SocketMessage> selectForTargetUserMsgByStatus(String status, WebSocketChatType chatType) {
		SocketMessage msg = new SocketMessage();
		msg.setStatus(status);
		msg.setTargetUserNotNull(1);
		msg.setChatType(chatType+"");
//		msg.setMasterMsgType("1");
		return selectbySelective(msg);
	}

	@Override
	public int deleteMessage(SocketMessage record) {
		return socketMessageMapper.deleteMessage(record);
	}

	@Override
	public int jobDeleteMessage(SocketMessage record) {
		return socketMessageMapper.jobDeleteMessage(record);
	}

	@Override
	public List<SocketMessage> selectMsgByTargetGroupIdStatus(Long groupId, Long userId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("targetGroupId", groupId);
		map.put("userId", userId);
		return socketMessageMapper.selectMsgByTargetGroupIdStatus(map);
	}

	@Override
	public ResponseResult groupPageRecord(UserInfo user, Long groupId ,Long msgIdN, PageTools pageTools) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("userId",user.getId());
		map.put("msgId",msgIdN);
		map.put("targetGroupId",groupId);
		map.put("rowIndex",pageTools.getRowIndex());
		map.put("pageSize",pageTools.getPageSize());
		map.put("noGroupOpt","1");//不差进群通知和退群通知
		Long size = selectGroupPageTotalCount(map);
		pageTools.setTotal(size);
		List<SocketMessage> messageList = selectGroupPageByObjectForList(map);
		ResponseResult result = new ResponseResult();
		result.addBody("messageList", messageList);
		result.addBody("pageTools", pageTools);
		return result;
	}

	private List<SocketMessage> selectGroupPageByObjectForList(HashMap<String, Object> map) {
		return socketMessageMapper.selectGroupPageByObjectForList(map);
	}

	private Long selectGroupPageTotalCount(HashMap<String, Object> map) {
		return socketMessageMapper.selectGroupPageTotalCount(map);
	}

	@Override
	public void updateGroupMsgRalationStatusRecord(Long userId,Long msgId,Long groupId,MessageStatus status) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId); 
		map.put(MessageStatus.complete == status?"msgIdGreater":"msgId",msgId);
		map.put("targetGroupId", groupId);
		map.put("status", status+"");
		
		socketMessageMapper.updateGroupMsgRalationStatusRecord(map);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void insertMsgBackup(SocketMessage msg) {
		socketMessageMapper.insertMsgBackup(msg.getMsgId());
		socketMessageMapper.deleteByLessMsgId(msg.getMsgId());
		
	}
	
	private static String buildHeaderInfo(String uuid){
		WebSocketHeader header = new WebSocketHeader();
		header.setRequestId(uuid);
		header.setSign("sign");
		header.setToken("system");
		return JSON.toJSONString(header);
	}
	
	@Override
	public SocketMessage forceOfflineNoticeBuild(){
		SocketMessage socketMessage = new SocketMessage();
		socketMessage.setMsgType(WebSocketMsgType.FORCE_OFFLINE_NOTICE+""); 
		socketMessage.setChatType(WebSocketChatType.single+"");
		socketMessage.setSendNickName("system");
		return buildMessage(socketMessage, null, null);
	}
	
	@Override
	public void walletRefreshNotice(Long sendUserId,Long targetUserId,String sendNickName){
		SocketMessage socketMessage = new SocketMessage();
		socketMessage.setMsgType(WebSocketMsgType.WALLET_REFRESH+""); 
		socketMessage.setChatType(WebSocketChatType.single+"");
		socketMessage.setSendNickName(sendNickName);
		socketMessage.setMasterMsgType("2");
		Long count = socketMessageMapper.selectWalletRefreshCountBySelective(targetUserId);
		if(count==0){
			insertNoticeInfo(socketMessage,sendUserId,targetUserId);
		}
	}
	
	@Override
	public void groupRefreshNotice(Long sendUserId,Long targetUserId,String content){
		SocketMessage socketMessage = new SocketMessage();
		socketMessage.setMsgType(WebSocketMsgType.GROUP_REFRESH+"");
		socketMessage.setChatType(WebSocketChatType.single+"");
		socketMessage.setContent(content);
		socketMessage.setMasterMsgType("2");
		insertNoticeInfo(socketMessage,sendUserId,targetUserId);
	}
	
	@Override
	public void groupPacketLotteryNotcie(Packet packet, GameRule luckGot, UserInfo luckyUser){
		
		String content = buildLotteryMsg(packet, luckGot, luckyUser);
		
		String uuid = UUID.randomUUID().toString().replaceAll("-","");
		
		SocketMessage socketMessage = new SocketMessage();
		socketMessage.setMsgType(WebSocketMsgType.GROUP_PACKET_LOTTERY_NOTICE+"");
		socketMessage.setChatType(WebSocketChatType.multiple+"");
		socketMessage.setTargetGroupId(packet.getGroupId());
		socketMessage.setMasterMsgType("1");
		socketMessage.setContent(content);
		socketMessage.setHeader(buildHeaderInfo(uuid));
		socketMessage.setRequestId(uuid);
		socketMessage.setStatus(MessageStatus.pushed_response+"");
		socketMessage.setDate(DateUtils.getStringDateShort());
		socketMessage.setTime(DateUtils.getTimeShort());
		socketMessage.setTargetUserDeleteFlag(MessageDeleteStates.normal.getDes());
		socketMessage.setSendUserDeleteFlag(MessageDeleteStates.normal.getDes());
		
		insertSelective(socketMessage);
	}

	private String buildLotteryMsg(Packet packet, GameRule luckGot, UserInfo luckyUser) {
		String userNickName = luckyUser.getNickName();
		userNickName = userNickName.length()<=5?userNickName:userNickName.substring(0, 5)+"***";
		
		StringBuilder sb = new StringBuilder();
		sb.append("恭喜 ("+userNickName+") ");
		sb.append(packet.getAmount().toPlainString());
		sb.append("-");
		if(luckGot.getRuleType().equals("2")){
			sb.append(packet.getHitNum()+"/中奖,");
			sb.append("奖励 ("+luckGot.getMatchingParam()+") +"+luckGot.getRuleValue());
		}else{
			sb.append("多雷中奖,奖励 (中"+luckGot.getRuleCode()+"雷) +"+luckGot.getRuleValue());
		}
		
		return sb.toString();
	}

	private void insertNoticeInfo(SocketMessage socketMessage,Long sendUserId,Long targetUserId) {
		buildMessage(socketMessage, sendUserId, targetUserId);
		insertSelective(socketMessage);
	}

	private SocketMessage buildMessage(SocketMessage socketMessage, Long sendUserId, Long targetUserId) {
		String uuid = UUID.randomUUID().toString().replaceAll("-","");
		socketMessage.setSendUserId(sendUserId);
		socketMessage.setTargetUserId(targetUserId);
		socketMessage.setMasterMsgType("2");
		socketMessage.setContent(socketMessage.getContent()==null?"系统通知:"+socketMessage.getMsgType():socketMessage.getContent());
		socketMessage.setHeader(buildHeaderInfo(uuid));
		socketMessage.setRequestId(uuid);
		socketMessage.setStatus(MessageStatus.pushed_response+"");
		socketMessage.setDate(DateUtils.getStringDateShort());
		socketMessage.setTime(DateUtils.getTimeShort());
		socketMessage.setTargetUserDeleteFlag(MessageDeleteStates.normal.getDes());
		socketMessage.setSendUserDeleteFlag(MessageDeleteStates.normal.getDes());
		
		return socketMessage;
	}
	
	@Override
	public void updateWalletRefreshMsg(Long targetUserId) {
		socketMessageMapper.updateWalletRefreshMsg(targetUserId);
	}
	
}