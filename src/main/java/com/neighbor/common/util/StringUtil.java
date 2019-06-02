package com.neighbor.common.util;

import javafx.scene.chart.Chart;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static String split_ = "-";
    public static String split = ",";
    public static String spot = ".";
    public static int[] continuityArray={0,1,2,3,4,5,6,7,8,9};//顺子数组
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]+.*[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }


    //生成顺子数 subType 1=顺子,2=同数,
    public static String generateGameRuleNum(String s1,String s2,int subType){
        if(!checkGameRuleNum(s1,s2,subType))return null;
        StringBuffer sb = new StringBuffer();
        sb.append(s1).append(split);
        int spotIdx = s1.indexOf(".");//小数点的位置
        String begin =s1.replace(".","");
        String end = s2.replace(".","");
        int len = begin.length();//长度
        int beginNum = Integer.valueOf(begin.substring(0,1));
        int endNum = Integer.valueOf(end.substring(0,1));
        if(Integer.valueOf(begin)<Integer.valueOf(end)){
            //正序
            for(int i=beginNum+1;i<endNum;i++){
                StringBuffer temp = new StringBuffer();
                for(int z=0;z<len;z++){
                    if(subType==1){
                        temp.append(continuityArray[i+z]);
                    }else{
                        temp.append(continuityArray[i]);
                    }

                }
                String tempStr = temp.toString();
                if(end.equals(tempStr))break;
                if(spotIdx!=-1) tempStr = tempStr.substring(0,spotIdx)+spot+tempStr.substring(spotIdx);
                sb.append(tempStr).append(split);
            }
        }else{
            //反序
            for(int i=beginNum-1;i>endNum;i--){
                StringBuffer temp = new StringBuffer();
                for(int z=0;z<len;z++){
                    if(subType==1){
                        temp.append(continuityArray[i-z]);
                    }else{
                        temp.append(continuityArray[i]);
                    }
                }
                String tempStr = temp.toString();
                if(end.equals(tempStr))break;
                if(spotIdx!=-1) tempStr = tempStr.substring(0,spotIdx)+spot+tempStr.substring(spotIdx);
                sb.append(tempStr).append(split);
            }
        }
        sb.append(s2);
        return sb.toString();
    }


    public static boolean isNumeric2(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public static boolean checkContinuity(String s1){
        char[] arr = s1.toCharArray();
        int c = arr[0]-'0';
        for(int i=1; i<arr.length; i++){
            int t = arr[i]-'0';
            if(t-c==1||t-c==-1){
                c = t;
            }else{
                return false;
            }
        }
        return true;
    }

    public static boolean checkSame(String s1){
        char[] arr = s1.toCharArray();
        int c = arr[0]-'0';
        for(int i=1; i<arr.length; i++){
            int t = arr[i]-'0';
            if(t-c!=0){
                return false;
            }
        }
        return true;
    }


    //判断顺子数 不能相等的 subType 1=顺子,2=同数
    public static boolean checkGameRuleNum(String s1,String s2,int subType){
        if(StringUtils.isEmpty(s1)||StringUtils.isEmpty(s2)){
            return false;
        }
        if(s1.equals(s2)){
            return false;
        }
        if(s1.length()!=s2.length()){
            return false;
        }
        if(s1.indexOf(".") != s2.indexOf(".")){
            return false;
        }
        String begin =s1.replace(".","");
        String end = s2.replace(".","");
        if(!isNumeric2(begin)||!isNumeric2(end)){
            return false;
        }
        if(subType==1){
            if(!checkContinuity(begin)||!checkContinuity(end)){
                return false;
            }
        }else{
            if(!checkSame(begin)||!checkSame(end)){
                return false;
            }
        }

        return true;
    }

    public static int checkLeopard(String matchingParam){
        if(!StringUtil.isEmpty(matchingParam)){
            char[] charArray = matchingParam.replace(".","").toCharArray();
            int size = 1;
            int len = charArray.length;
            char end = charArray[len-1];
            for(int i = len-2;i>=0;i--){
                char temp = charArray[i];
                if(temp!=end){
                    break;
                }
                size++;
            }
            return size;
        }
        return 0;
    }



    public static void main(String[] args) {
        System.out.println(checkLeopard("12.23"));


       /* System.out.println(generateGameRuleNum("1.23","7.89",1));
        System.out.println(generateGameRuleNum("9.87","3.21",1));
        System.out.println(generateGameRuleNum("77.7","11.1",2));
        System.out.println(generateGameRuleNum("1.11","9.99",2));
        System.out.println(checkGameRuleNum("1.23","7..",1));
        System.out.println(checkGameRuleNum("77.7","11.1",2));*/
    }


}
