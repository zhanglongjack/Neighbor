package com.neighbor.security.permission.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;
import com.neighbor.common.security.EncodeData;
import com.neighbor.common.security.WebSecurityConfig;
import com.neighbor.common.sms.TencentSms;
import com.neighbor.security.role.entity.SysRole;
import com.neighbor.security.role.entity.SysUserRoleKey;
import com.neighbor.security.role.service.SysRoleService;
import com.neighbor.security.role.service.SysUserRoleService;

/**
 * 自定义userDetailsService
 * @author jitwxs
 * @since 2018/5/9 9:36
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    @Autowired
    private UserService userService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysUserRoleService userRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	logger.debug("登录验证 username:{}",username);
    	String infos[] = username.split(",");
    	
        // 从数据库中取出用户信息
        UserInfo user = userService.selectByUserPhone(Long.parseLong(infos[0]));
        // 判断用户是否存在
        if(user == null && infos.length==1) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        
        // 添加权限
        Collection<GrantedAuthority> authorities = new ArrayList<>();
       
        logger.debug("登录验证成功 username:{}",username);
        if(infos.length==2){
        	
        	// 返回UserDetails实现类
            return new User(infos[0], EncodeData.encode(TencentSms.smsCache.get(infos[0])), authorities);
        }else{
        	 List<SysUserRoleKey> userRoles = userRoleService.listByUserId(user.getuId());
             for (SysUserRoleKey userRole : userRoles) {
                 SysRole role = roleService.selectByPrimaryKey(userRole.getRoleId());
                 authorities.add(new SimpleGrantedAuthority(role.getName()));
             }
            // 返回UserDetails实现类
            return new User(user.getPhone()+"", user.getPassword(), authorities);
        }
        

    }
}
