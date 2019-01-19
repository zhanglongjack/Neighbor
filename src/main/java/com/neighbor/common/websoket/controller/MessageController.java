package com.neighbor.common.websoket.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alipay.api.internal.util.StringUtils;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.constants.MessageStatus;
import com.neighbor.common.websoket.service.SocketMessageService;

@Controller
@RequestMapping(value = "/message")
@SessionAttributes("user")
public class MessageController {
	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
	@Autowired
	private SocketMessageService socketMessageService;

	// 查询出所有未读消息
	@RequestMapping(value = "/unreadRecord.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult unreadRecord(@ModelAttribute("user") UserInfo user) throws Exception {
		logger.info("unreadRecord request user >>>> " + user);
		ResponseResult result = socketMessageService.unreadRecord(user);
		return result;
	}

	// 分页展示好友发送已完成和自己发送的消息
	@RequestMapping(value = "/pageRecord.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult pageRecord(@ModelAttribute("user") UserInfo user, Long friendId, String msgId,
			PageTools pageTools) throws Exception {
		logger.info("pageRecord request user >>>> " + user + " | targetUserId >>" + friendId + " | msgId >> " + msgId);
		logger.info("pageRecord request pageTools >>>> " + pageTools);
		Long msgIdN = null;
		if (StringUtils.isNumeric(msgId)) {
			msgIdN = Long.valueOf(msgId);
		}
		ResponseResult result = socketMessageService.pageRecord(user, friendId, msgIdN, pageTools);
		return result;
	}

	// 通知后端变更完成记录状态
	@RequestMapping(value = "/changeCompleteRecord.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult changeCompleteRecord(@ModelAttribute("user") UserInfo user, Long friendId, Long msgId)
			throws Exception {
		logger.info("pageRecord request user >>>> " + user + " | targetUserId >>" + friendId + " | msgId >> " + msgId + "| status >>" + MessageStatus.complete);
		ResponseResult result = socketMessageService.changeRecord(user, friendId, msgId, MessageStatus.complete.toString());
		return result;
	}

	// 通知后端变更已推送记录状态
	@RequestMapping(value = "/changePushedRecord.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult changePushedRecord(@ModelAttribute("user") UserInfo user, Long friendId, Long msgId)
			throws Exception {
		logger.info("pageRecord request user >>>> " + user + " | targetUserId >>" + friendId + " | msgId >> " + msgId + "| status >>" + MessageStatus.pushed.toString());
		ResponseResult result = socketMessageService.changeRecord(user, friendId, msgId, MessageStatus.pushed.toString());
		return result;
	}

	// 分页展示好友发送已完成和自己发送的消息
	@RequestMapping(value = "/groupPageRecord.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult groupPageRecord(@ModelAttribute("user") UserInfo user, Long groupId,String msgId, PageTools pageTools)
			throws Exception {
		logger.info("groupPageRecord request user >>>> " + user + " | targetUserId >>" + groupId);
		logger.info("groupPageRecord request pageTools >>>> " + pageTools);
		Long msgIdN = null;
		if (StringUtils.isNumeric(msgId)) {
			msgIdN = Long.valueOf(msgId);
		}
		ResponseResult result = socketMessageService.groupPageRecord(user, groupId,msgIdN, pageTools);
		return result;
	}

	// 通知后端变更已推送记录状态
	@RequestMapping(value = "/changeGroupMsgPushedRecord.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult changeGroupMsgPushedRecord(@ModelAttribute("user") UserInfo user, Long groupId, Long msgId)
			throws Exception {
		logger.info("changeGroupMsgPushedRecord request user >>>> " + user + " | targetUserId >>" + groupId + " | msgId >> " + msgId + "| status >>" + MessageStatus.pushed.toString());
		
		socketMessageService.updateGroupMsgRalationStatusRecord(user.getId(), msgId, groupId, MessageStatus.pushed);
		return new ResponseResult();
	}
	
	// 通知后端变更已推送记录状态
	@RequestMapping(value = "/changeGroupMsgCompleteRecord.req", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult changeGroupMsgCompleteRecord(@ModelAttribute("user") UserInfo user, Long groupId, Long msgId)
			throws Exception {
		logger.info("changeGroupPushedRecord request user >>>> " + user + " | targetUserId >>" + groupId + " | msgId >> " + msgId + "| status >>" + MessageStatus.complete.toString());
		
		socketMessageService.updateGroupMsgRalationStatusRecord(user.getId(), msgId, groupId, MessageStatus.complete);
		return new ResponseResult();
	}

}
