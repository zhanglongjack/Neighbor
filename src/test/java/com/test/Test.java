package com.test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
	private static Map<String,String> map  = new ConcurrentHashMap<String,String>();
	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
//	private static CountDownLatch countDown = null;
	private static ReentrantLock lock = new ReentrantLock(true);
	public static void main(String[] args) throws InterruptedException {
//		countDown = new CountDownLatch(11);
		for(int i=0;i<11;i++){
			fixedThreadPool.execute(new First());
			fixedThreadPool.execute(new Second());
//			fixedThreadPool.execute(new Third()); 
		}
		
//		countDown.await();
//		System.out.println("*************");
//		countDown = new CountDownLatch(11);
		for(int i=0;i<11;i++){
			fixedThreadPool.execute(new First());
//			fixedThreadPool.execute(new Second());
//			fixedThreadPool.execute(new Third()); 
		}
		
//		countDown.await();
		System.out.println("===========");
//		for(int i=0;i<300;i++){
////			fixedThreadPool.execute(new First());
////			fixedThreadPool.execute(new Second());
//			fixedThreadPool.execute(new Third()); 
//		}
		System.err.println("当前线程池任务数量约:"+fixedThreadPool);
		System.out.println("主线程休眠");
		Thread.sleep(10000);
		fixedThreadPool.shutdown();
		System.err.println("当前线程池任务数量约:"+fixedThreadPool);
		Thread.sleep(200000);
	}
	
	static class First implements  Runnable {
		public void run() {
//			countDown.countDown();
			System.out.println("first:"+Thread.currentThread().getName()); 
			sleep(1l);
		}
	}
	
	static class Second implements Runnable {
		public void run() {
			System.out.println("second:"+Thread.currentThread().getName()); 
			sleep1(1l);
		}
	}
	
	static class Third implements Runnable {
		public void run() {
			System.out.println("third:"+Thread.currentThread().getName()); 
		}
	}
	
	public static void sleep(Long id){
		lock.lock();
		System.out.println("sleep我进来碎一觉" + Thread.currentThread().getName());
		try {
			Thread.sleep(3000);

			System.out.println("sleep睡醒了换人" + Thread.currentThread().getName());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		
//		ThreadPoolExecutor executor = (ThreadPoolExecutor) fixedThreadPool;
//		System.out.println("当前线程池任务数量约:"+executor);
//		int seconds = RandomUtil.getRandomBy(2)+1*1000;
//		System.out.println("线程:"+Thread.currentThread().getName()+"开始睡眠"+seconds+"毫秒");
//		try {
//			Thread.sleep(seconds);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public static void sleep1(Long id){
		lock.lock();
		System.out.println("sleep111111我进来碎一觉" + Thread.currentThread().getName());
		try {
			Thread.sleep(3000);

			System.out.println("sleep11111睡醒了换人" + Thread.currentThread().getName());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}
