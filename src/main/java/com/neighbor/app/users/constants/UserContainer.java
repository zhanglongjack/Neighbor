package com.neighbor.app.users.constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;

@Component
public class UserContainer  implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger logger = LoggerFactory.getLogger(UserContainer.class);
	public Map<Long,String> userMap = new HashMap<Long,String>();
	@Autowired
	private UserService userService;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(userMap.size()==0){
			buildUserInfo();
		}
	}

	public void buildUserInfo() {
//		List<UserInfo> userList = userService.selectAllForMap();
//		logger.debug("userList:{}",userList);
//		for(UserInfo user : userList){
////			userMap.put(user.getuId(), user.getName());
//		}
		 
		logger.debug("userMap:{}",userMap);
	}
	
	public String get(Long key){
		return userMap.get(key);
	}

}
