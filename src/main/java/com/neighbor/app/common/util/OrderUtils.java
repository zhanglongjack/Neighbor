package com.neighbor.app.common.util;

import com.neighbor.common.util.DateFormateType;
import com.neighbor.common.util.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.util.*;

/**
 * Created by Administrator on 14-10-25.
 */
public class OrderUtils {
    private static Log logger = LogFactory.getLog(OrderUtils.class);
    public static String TRANSFER="1";
    public static String RECHARGE="2";
    public static String WITHDRAW="3";
    /**
     * 专门为高并发获取唯一ID使用
     *
     * @return
     * @throws Exception
     */
    public static String getOrderNo(String logic) throws Exception {
        StringBuffer orderNo = new StringBuffer();
        try {
            orderNo.append(logic)
                    .append(DateUtils.getUserDate(DateFormateType.MOST_TIGHT_LONG_S_FORMAT)).
                    append(fixStringToLengthNew
                            (String.valueOf(Thread.currentThread().getId()), 4, "0") +
                            getRandomChar(3));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return orderNo.toString();

    }

    /**
     * 专门为高并发获取唯一ID使用
     * 定长20位
     * @return
     * @throws Exception
     */
    public synchronized static String getOrderNo(String logic,Long uid) throws Exception {
        StringBuffer orderNo = new StringBuffer();
        try {
            Thread.sleep(1);
            String str = uid+"";
            orderNo.append(DateUtils.getUserDate(DateFormateType.MOST_TIGHT_LONG_S_FORMAT)).append(logic).append(str.substring(str.length()-2));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return orderNo.toString();

    }
    /**
     * 获得0-9,a-z,A-Z范围的随机字符串
     *
     * @param length 字符串长度
     * @return String
     */
    public static String getRandomChar(int length) {
        char[] chr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
                'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
                'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z'};

        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buffer.append(chr[random.nextInt(62)]);
        }

        return buffer.toString();
    }
    public static String fixStringToLengthNew(String numStr, int length, String addChar) {
        if (length <= 0) {
            return "";
        } else if (numStr.length() >= length) {
            return numStr;
        }

        int remainLength = length - numStr.length();
        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < remainLength; i++) {
            stringBuffer.append(addChar);
        }

        numStr = stringBuffer.toString() + numStr;

        return numStr;
    }


}
