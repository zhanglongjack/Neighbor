package com.neighbor.app.pay.common;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class AesUtil {



    public static  byte[] encrypt(String input, String key){
        byte[] crypted = null;
        try{
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
        }catch(Exception e){
           e.printStackTrace();
        }
        return crypted;
    }

    public static String decrypt(byte[] input, String key){
        byte[] output = null;
        try{
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(input);
        }catch(Exception e){
           e.printStackTrace();
        }
        return new String(output,StandardCharsets.UTF_8);
    }



    public static String encryptData(String content, String password){
        if(password!=null&&password.length()>16){
            password = password.substring(0,16);
        }
        byte[] encryptResult = encrypt(content, password);
        return new String(HexStringUtils.encodeHex(encryptResult));
    }

    public static String decryptData(String content, String password){
        if(password!=null&&password.length()>16){
            password = password.substring(0,16);
        }
        byte[] decode = HexStringUtils.decodeHex(content.toCharArray());
        return decrypt(decode, password);
    }

    public static void main(String[] args)  {

        String password = "81ff0c7ca96f4727123456";

        // 解密
        String decryptResult = decryptData("CDE2064F0AAC0BFB2214D1B241046FB3497D8B737A42238A18A90F14D734E6957A68807ED472663B5D8C9C2736F1362B73A281B7B758DA17218544F728FBE94CAC87771D7C6FC4394FEBDCEA64DE3BFD98289100C0E2EE1BC07DC6FFAF147A5838EC351310B848EC71C53B2B6E2969EFEE03573C92610499D6727419B9BCBFFB06C0B7CC68681B474FB6234CF5A6E4E7", password);
        System.out.println("解密后：" + decryptResult); //不转码会乱码

        System.out.println("解密后：" + password); //不转码会乱码

    }
}
