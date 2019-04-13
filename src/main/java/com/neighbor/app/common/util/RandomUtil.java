package com.neighbor.app.common.util;

import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomUtil {
	private final static Logger logger = LoggerFactory.getLogger(RandomUtil.class);
	private static Random random = new Random();
	
	public static int getRandomBy(int bound){
		int result =  random.nextInt(bound);
		//logger.info("生成随机数:"+result);
		return result;
	}
	public static String getNickName(){
		return "好领居玩家_"+getRandomBy(1000000);
	}

	public static synchronized String getReCode(){
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String code = UUID.randomUUID().toString().replaceAll("-","");
		return code.substring(24);
	}


	public static void main(String[] args) throws Exception{
		logger.info(getNickName());
		logger.info(getNickName());
		logger.info(getNickName());
		logger.info(getNickName());
		logger.info(getNickName());
	}
}
