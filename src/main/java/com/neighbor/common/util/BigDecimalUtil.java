package com.neighbor.common.util;

import java.math.BigDecimal;

public class BigDecimalUtil {
    public static BigDecimal rounding(BigDecimal bigDecimal){
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
