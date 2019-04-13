package com.test;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.neighbor.StartNeighbor;
import com.neighbor.app.wallet.entity.UserWallet;
import com.neighbor.app.wallet.service.UserWalletService;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = StartNeighbor.class)
public class ExecutorsTest {

//    @Autowired
//	private UserWalletService userWalletService;
    
	@Test
	public void test(){
		
		CountDownLatch count = new CountDownLatch(10);
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(300);
		for(int i =0;i<300;i++){
			fixedThreadPool.execute(new Runnable(){
				UserWallet record = new UserWallet();
				@Override
				public void run() {
					record.setAvailableAmount(new BigDecimal("1"));
					try {
						
						count.countDown();
						System.out.println("计数减:"+count.getCount());
						count.await();
						System.out.println("计数到达数值:"+count.getCount());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
//					int count = userWalletService.updateAvailable(record);
				}
				
			});
		}
		ExecutorService fixedThreadPool1 = Executors.newFixedThreadPool(200);
		for(int i =0;i<200;i++){
			fixedThreadPool1.execute(new Runnable(){
				UserWallet record = new UserWallet();
				
				@Override
				public void run() {
					record.setAvailableAmount(new BigDecimal("-1"));
					try {
						
						count.countDown();
						System.out.println("计数减:"+count.getCount());
						count.await();
						System.out.println("计数到达数值:"+count.getCount());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
//					int count = userWalletService.updateAvailable(record);
				}
				
			});
		}
		
		try {
			System.out.println("开始睡眠等待");
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("结束");
	}
	
}
