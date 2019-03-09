package com.neighbor.app.robot.util;

import java.util.Random;

public class RandomUtil {
	private static Random random = new Random();
	
	public static int getRandomBy(int bound){
		return random.nextInt(bound);
	}
	
}
