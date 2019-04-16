package com.neighbor.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.neighbor.app.common.util.RandomUtil;
import com.neighbor.app.packet.entity.Packet;
import com.neighbor.app.robot.entity.RobotConfig;

public class RedPackageUtil {
	public static double getRandomMoney(Packet packet,boolean isHit) {
	    // remainSize 剩余的红包数量
	    // remainMoney 剩余的钱
//		Long lock = Long.parseLong(packet.getId().toString());
		double money = 0;
//		synchronized (lock) {
			if (packet.remainSize > 1) {
			    Random r     = new Random();
			    double min   = 0.01; //
			    double max   = packet.remainMoney / packet.remainSize * 2;
			    money = r.nextDouble() * max;
			    money = money <= min ? 0.01: money;
			    money = Math.floor(money * 100) / 100;
		    }else{
		    	money = packet.remainMoney;
//		    	money = (double) Math.round(packet.remainMoney * 100) / 100;
		    }
//		}
	    
	    money = BigDecimalUtil.rounding(money).doubleValue();
	    
	    if(!isHit){
	    	String temp = BigDecimalUtil.rounding(money).toPlainString();
	    	int lastNum =Integer.parseInt(temp.substring(temp.length()-1)) ;
	    	if(lastNum==packet.getHitNum()){
//	    		System.out.println("lastNum="+lastNum+",hitNum="+packet.getHitNum());
	    		if(packet.remainSize==1)return 0;
	    		return getRandomMoney(packet, isHit);
	    	}
	    }
	    packet.remainSize--;
	    packet.remainMoney -= money;
	    return money;
	}
	
	public static double generateSingleNum(double total, int num, int i) {
		double t = total;
		double min = 0.01;
		DecimalFormat df = new DecimalFormat("0.00");
		double safe_total = (total - (num - i) * min) / (num - i);
		double money = min + (double) (Math.random() * (safe_total - min + 1));
		String tem = df.format(money);
		double f1 = Double.parseDouble(tem);
		total = total - f1;
		if (total < 0) {
			return generateSingleNum(t, num, i);
		}
		return Double.parseDouble(tem);
	}

	public static String generate(double total, int num) {
		StringBuffer sb = new StringBuffer();
		double t = total;
		double min = 0.01;
		DecimalFormat df = new DecimalFormat("0.00");
		for (int i = 1; i < num; i++) {
			double safe_total = (total - (num - i) * min) / (num - i);
			double money = min + (double) (Math.random() * (safe_total - min + 1));
			String tem = df.format(money);
			double f1 = Double.parseDouble(tem);
			total = total - f1;
			if (total < 0) {
				return generate(t, num);
			}
			sb.append(tem + ",");
		}
		String tem = df.format(total);
		sb.append(tem);
		return sb.toString();
	}


	
	public static void main(String[] args) { 
		//System.out.println(RedPackageUtil.generate(100, 7));
		ExecutorService executor = Executors.newFixedThreadPool(16);
		for(int i=0;i<15000;i++){
			executor.execute(new Runnable() {
				public void run() {
					List<Double> numList = new ArrayList<Double>();
					Packet packet = new Packet();
					packet.setId(Long.parseLong(RandomUtil.getRandomBy(2)+""));
					packet.setAmount(new BigDecimal("64.00"));
					packet.setHitNum(3);
					packet.setPacketNum(7);
					for(int i=0;i<10;i++){
						Double num = RedPackageUtil.getRandomMoney(packet,new RobotConfig().isHit());
						if(num>0)
						numList.add(num);
					}
					
					double sum = 0;
					for(Double num : numList){
						sum+=num;
					}
					BigDecimal result = BigDecimalUtil.rounding(sum);
					if(result.doubleValue()>packet.getAmount().doubleValue()){
						System.out.println(packet.getAmount().doubleValue()+"检查结果:"+(result.doubleValue())+",与预期结果不一致"+numList);
					}else if(numList.size()==7 &&result.doubleValue()!=packet.getAmount().doubleValue()){
						System.out.println(packet.getAmount().doubleValue()+"检查结果:"+(result.doubleValue())+",与预期结果不一致"+numList);
					}
					
				}
			});
		}
		
		System.out.println("主线程开始休眠");
		try {
			executor.shutdown();
			while(!executor.isTerminated())
			Thread.sleep(1000);
			System.out.println(executor);
			System.out.println("结束");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
//	public static synchronized void add(Packet packet,boolean isHit){
//		if(numList.size()<32){
//			Double num = RedPackageUtil.getRandomMoney(packet,isHit);
//			System.out.println("剩余数字:"+packet.remainSize);
//			if(num==0){
//				System.out.println("这个是机器人,中雷概率不中,且是最后一个红包,结束");
//				return;
//			}
//			numList.add(num);
//		}else{
//			Double num = RedPackageUtil.getRandomMoney(packet,isHit);
//			System.out.println(num);
//		}
//		System.out.println(numList);
//	}
	
	
}