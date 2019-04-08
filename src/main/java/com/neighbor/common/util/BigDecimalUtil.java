package com.neighbor.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

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
}
