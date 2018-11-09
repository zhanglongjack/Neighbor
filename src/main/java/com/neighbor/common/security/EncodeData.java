package com.neighbor.common.security;

import org.springframework.util.DigestUtils;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncodeData {
//	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//	
//	
//	public static String encode(CharSequence data){
//		return encoder.encode(data);
//	}
//	
//	
//    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
//        return encoder.matches(rawPassword,encodedPassword);
//    }
//	
//	public static void main(String[] args) {
//		System.out.println(EncodeData.encode("123"));
//	}
	
//	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//	
//	
	/**
	 * 加密
	 * @param data
	 * @return
	 */
	public static String encode(CharSequence data){
		//对密码进行 md5 加密
		return DigestUtils.md5DigestAsHex(data.toString().getBytes());
	}
//	
//	
    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
    	return encode(rawPassword).equals(encodedPassword);
    }
//	
//	public static void main(String[] args) {
//		System.out.println(EncodeData.encode("123"));
//	}
	
	
	
}
