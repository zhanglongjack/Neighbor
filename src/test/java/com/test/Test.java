package com.test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.neighbor.app.robot.util.RandomUtil;

public class Test {
	private static Map<String,String> map  = new ConcurrentHashMap<String,String>();
	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(30);
	public static void main(String[] args) throws InterruptedException {
		for(int i=0;i<29;i++){
			fixedThreadPool.execute(new First());
//			fixedThreadPool.execute(new Second());
//			fixedThreadPool.execute(new Third()); 
		}
		for(int i=0;i<300;i++){
//			fixedThreadPool.execute(new First());
//			fixedThreadPool.execute(new Second());
			fixedThreadPool.execute(new Third()); 
		}
		System.err.println("当前线程池任务数量约:"+fixedThreadPool);
		System.out.println("主线程休眠");
		Thread.sleep(10000);
		System.err.println("当前线程池任务数量约:"+fixedThreadPool);
		Thread.sleep(200000);
	}
	
	static class First implements  Runnable {
		public void run() {
			System.out.println("first:"+Thread.currentThread().getName()); 
			sleep();
		}
	}
	
	static class Second implements Runnable {
		public void run() {
			System.out.println("second:"+Thread.currentThread().getName()); 
			sleep();
		}
	}
	
	static class Third implements Runnable {
		public void run() {
			System.out.println("third:"+Thread.currentThread().getName()); 
		}
	}
	
	public static void sleep(){
		ThreadPoolExecutor executor = (ThreadPoolExecutor) fixedThreadPool;
		System.out.println("当前线程池任务数量约:"+executor);
		int seconds = RandomUtil.getRandomBy(2)+1*100000;
		System.out.println("线程:"+Thread.currentThread().getName()+"开始睡眠"+seconds+"毫秒");
		try {
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
