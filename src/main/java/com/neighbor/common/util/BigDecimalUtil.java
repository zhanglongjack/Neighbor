package com.neighbor.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.neighbor.app.robot.util.RandomUtil;

public class BigDecimalUtil {
	private static DecimalFormat df = new DecimalFormat("0.00");
	/**
	 * 四舍五入保留两位小数
	 * @param bigDecimal
	 * @return BigDecimal
	 */
    public static BigDecimal rounding(BigDecimal bigDecimal){
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    public static BigDecimal rounding(double number){
        return new BigDecimal(df.format(number));
    }
    
    
    public static void main(String[] args) {
    	double a = BigDecimalUtil.rounding(9.10).doubleValue();
		System.out.println(a);
		System.out.println((double) Math.round(9.10666d* 100) / 100);
		ExecutorService executor = Executors.newFixedThreadPool(16);
		for(int i=0;i<4;i++){
			executor.execute(new Runnable() {
				public void run() {
					Long id = Long.parseLong(RandomUtil.getRandomBy(2)+"");
					System.out.println("我生成的"+id);
					test(id);
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
    
    
    public static void test(Long id){
    	System.out.println("我准备进同步块了:"+id);
    	synchronized (id) {
			System.out.println("我尽量开始休眠了,别人不许进来id==="+id);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
}
