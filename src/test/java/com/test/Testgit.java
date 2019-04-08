package com.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Testgit {
	private static final Logger logger = LoggerFactory.getLogger(Testgit.class);
	public static void main(String[] args) {
		logger.info("tetttttt");
		for (int i = 0; i < 300; i++) {
			new Thread() {
				@Override
				public void run() {
					System.out.println("线程:" + Thread.currentThread().getName());
					try {
						Thread.sleep(100000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};

			}.start();
		}
		
		System.out.println("主线程准备休眠");
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
