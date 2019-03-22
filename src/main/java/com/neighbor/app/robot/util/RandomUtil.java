package com.neighbor.app.robot.util;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomUtil {
	private final static Logger logger = LoggerFactory.getLogger(RandomUtil.class);
	private static Random random = new Random();
	
	public static int getRandomBy(int bound){
		int result =  random.nextInt(bound);
		logger.info("生成随机数:"+result);
		return result;
	}
	
}
