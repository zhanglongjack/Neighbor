package com.neighbor.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by zhangyk on 2015/4/28.
 */
public class RedPackageUtil {
    public static String generate(double total,int num){
        StringBuffer sb = new StringBuffer();
        double t = total;
        double min = 0.01;
        DecimalFormat df = new DecimalFormat("0.00");
        for(int i = 1 ;i<num ; i++){
            double safe_total = (total-(num-i)*min)/(num-i);
            double money = min+(double)(Math.random()*(safe_total-min+1));
            String tem = df.format(money);
            double   f1   =   Double.parseDouble(tem);
            total=total-f1;
            if(total<0){
                return generate(t,num);
            }
            sb.append(tem+",");
        }
        String tem = df.format(total);
        sb.append(tem);
        return sb.toString();
    }

    public static String format(BigDecimal bigDecimal){
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(bigDecimal.doubleValue());
    }

    public static void main(String[] args) {
        System.out.println(RedPackageUtil.generate(100,7));
    }
}