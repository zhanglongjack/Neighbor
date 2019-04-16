package com.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Testgit {
	private static final Logger logger = LoggerFactory.getLogger(Testgit.class);

	public static void main(String[] args) {
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10);
		for (int i = 0; i < 10; i++) {
			try {
				queue.put("i=" + i);
			} catch (InterruptedException e) {
			}
		}
		Thread t = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
						System.out.println(queue.take());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
		};
		t.start();
		try {
			System.out.println("阻塞了吗?");
			for (int i = 0; i < 20; i++) {
				try {
					queue.put("j=" + i);
					System.out.println("放了吗?");
				} catch (InterruptedException e) {
				}
			}
			System.out.println("到这了吗?");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
