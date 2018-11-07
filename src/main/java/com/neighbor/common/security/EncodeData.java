//package com.neighbor.common.security;
//
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//public class EncodeData {
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
//}
