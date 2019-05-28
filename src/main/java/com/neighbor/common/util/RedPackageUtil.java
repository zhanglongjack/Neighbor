package com.neighbor.common.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.neighbor.app.common.util.RandomUtil;
import com.neighbor.app.packet.entity.Packet;
import com.neighbor.app.robot.entity.RobotConfig;

public class RedPackageUtil {
//	
//	public static double generateSingleNum(double total, int num, int i) {
//		double t = total;
//		double min = 0.01;
//		DecimalFormat df = new DecimalFormat("0.00");
//		double safe_total = (total - (num - i) * min) / (num - i);
//		double money = min + (double) (Math.random() * (safe_total - min + 1));
//		String tem = df.format(money);
//		double f1 = Double.parseDouble(tem);
//		total = total - f1;
//		if (total < 0) {
//			return generateSingleNum(t, num, i);
//		}
//		return Double.parseDouble(tem);
//	}
//
//	public static String generate(double total, int num) {
//		StringBuffer sb = new StringBuffer();
//		double t = total;
//		double min = 0.01;
//		DecimalFormat df = new DecimalFormat("0.00");
//		for (int i = 1; i < num; i++) {
//			double safe_total = (total - (num - i) * min) / (num - i);
//			double money = min + (double) (Math.random() * (safe_total - min + 1));
//			String tem = df.format(money);
//			double f1 = Double.parseDouble(tem);
//			total = total - f1;
//			if (total < 0) {
//				return generate(t, num);
//			}
//			sb.append(tem + ",");
//		}
//		String tem = df.format(total);
//		sb.append(tem);
//		return sb.toString();
//	}
	
	public static double getRandomMoney(Packet packet,boolean isHit) {
		if(packet.remainSize ==0){
			return 0 ;
		}
	    // remainSize 剩余的红包数量
	    // remainMoney 剩余的钱
//		Long lock = Long.parseLong(packet.getId().toString());
		double money = 0;
//		synchronized (lock) {
			if (packet.remainSize > 1) {
			    Random r     = new Random();
			    double min   = 0.01; 
			    double max   = packet.remainMoney / packet.remainSize * 2;
			    money = r.nextDouble() * max;
			    money = money <= min ? 0.01: money;
			    money = Math.floor(money * 100) / 100.0;
			    
				money = generateMoneyByChance(packet, isHit, money);
		    }else{
		    	money = packet.remainMoney;
		    	//money = (double) Math.round(packet.remainMoney * 100) / 100;
		    }
//		}
	    
	    if(packet.remainSize==1 && !isHit && checkLastNum(packet,money)){
	    	return 0;
	    }
	    
	    packet.remainSize--;
	    packet.remainMoney -= money;
	    
	    return money;
	}

	private static double generateMoneyByChance(Packet packet, boolean isHit, double money) {
		boolean isBomb = packet.isHit();
		if(packet.getHitChance()>0&&isBomb&&packet.remainSize>1){
			// 如果中雷概率>0并且生成的中雷概率是中雷,并且红包剩余数量大于1的,将尾数直接改成中雷尾数
			
			// 以免尾数替换后金额超出剩余金额,再做此替换前检查剩余金额是否比剩余红包数量的10倍还多,多则替换尾数
			if((((packet.remainMoney - money) * 100) / (packet.remainSize * 10)) >=1){
				money =  (Math.floor(money*10) * 10 + packet.getHitNum()) / 100;
			}
			//System.err.println(money);
		}else if(packet.getHitChance()>0&&packet.remainSize>1&&!isBomb ){
			// 设置了中雷概率的,并且没中雷的和红包大于1的 或者没设置中雷概率的,重新生成为无雷尾数金额
			if(checkLastNum(packet,money))
			money = regenerate(money);
			//System.out.println(money);
		}else if(packet.getHitChance() == 0 && !isHit && packet.remainSize>1){
			// 非最后一个,且没设置中雷概率的,且中雷标识false的,重新生成为无雷尾数金额
			if(checkLastNum(packet,money))
			money = regenerate(money);
			//System.err.println(money);
		}
		return money;
	}

	public static boolean checkLastNum(Packet packet, double money) {
		money = BigDecimalUtil.rounding(money).doubleValue();
		int lastNum = new BigDecimal(money+"").multiply(new BigDecimal(100)).remainder(new BigDecimal(10)).intValue();
		return lastNum==packet.getHitNum();
	}

	private static double regenerate(double money) {
		// 如果金额大于等于0.02,则-0.01返回,否则设置为0.01,因为可能最后两个包之和可能就是0.02
		if(money>=0.02){
			money = (Math.floor(money*100) - 1.0) / 100.0;
//			money = (((int) (money*100))-1) / 100.0;
		}
		return money;
	}
	
	public static void main(String[] args) {
		//System.out.println(RedPackageUtil.generate(100, 7));
		ExecutorService executor = Executors.newFixedThreadPool(16);
		AtomicInteger count = new AtomicInteger();
		for(int i=0;i<1000;i++){
			executor.execute(new Runnable() {
				public void run() {
					List<String> numList = new ArrayList<String>();
					List<String> isHitList = new ArrayList<String>();
					Packet packet = new Packet();
					packet.setId(Long.parseLong(RandomUtil.getRandomBy(22222222)+""));
					packet.setAmount(new BigDecimal("100.00"));
					packet.setHitNum(3);
					packet.setPacketNum(9);
					packet.setHitChance(0.5d);
					RobotConfig robot = new RobotConfig();
					robot.setHitChance(0.1);
					for(int i=0;i<20;i++){
						boolean isHit = robot.isHit();
						Double num = RedPackageUtil.getRandomMoney(packet,true);
						if(num>0){
							isHitList.add(isHit+"");							
							numList.add( BigDecimalUtil.rounding(num).toPlainString());
						}
					}
					
					BigDecimal sum = new BigDecimal(0);
					int bombNum = 0;
					List<Integer> lastNumList = new ArrayList<Integer>();
					for(String money : numList){
						sum = sum.add(new BigDecimal(money));
						int lastNum = new BigDecimal(money).multiply(new BigDecimal(100)).remainder(new BigDecimal(10)).intValue();
					    lastNumList.add(lastNum);
					    if(lastNum==packet.getHitNum()){
					    	count.incrementAndGet();
					    	bombNum++;
					    } 
					}
					if(sum.doubleValue()<100 || sum.doubleValue()>100){
						System.err.println("总数:"+sum.toPlainString()+",剩余金额:"+packet.remainMoney+",雷数:"+bombNum+","+numList+",尾数对应:"+lastNumList+",isHitList:"+isHitList);
					}
					System.out.println(packet.getId()+"总数:"+sum.toPlainString()+",雷数:"+bombNum+","+numList+",尾数对应:"+lastNumList+",isHitList:"+isHitList);
					
//					BigDecimal result = BigDecimalUtil.rounding(sum);
//					if(result.doubleValue()>packet.getAmount().doubleValue()){
//						System.out.println(packet.getAmount().doubleValue()+"检查结果:"+(result.doubleValue())+",与预期结果不一致"+numList);
//					}else if(numList.size()==packet.getPacketNum() &&result.doubleValue()!=packet.getAmount().doubleValue()){
//						System.out.println(packet.getAmount().doubleValue()+"检查结果:"+(result.doubleValue())+",与预期结果不一致"+numList);
//					}else if(numList.size()==packet.getPacketNum() && result.doubleValue()==packet.getAmount().doubleValue()){
//						System.out.println("雷数:"+bombNum+","+packet.getAmount().doubleValue()+"检查结果:"+(result.doubleValue())+",预期结果一致"+numList);
//					}
				}
			});
		}
		
		System.out.println("主线程开始休眠");
		try {
			executor.shutdown();
			while(!executor.isTerminated())
			Thread.sleep(1000);
			System.out.println(executor);
			System.out.println("结束:"+count.get());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	  
}