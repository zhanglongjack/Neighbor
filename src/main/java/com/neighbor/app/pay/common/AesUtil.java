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
        byte[] encryptResult = encrypt(content, password);
        return new String(HexStringUtils.encodeHex(encryptResult));
    }

    public static void main(String[] args)  {

        String password = "81ff0c7ca96f4727";

        byte[] decode = HexStringUtils.decodeHex("98B2FC4EC70A314327C157C68B3BF1BD384F5DC149468AF225643F9518D3F613FAF50FCCCEC86A96B313BFE4024CF77D2122F232E06DD35813BC1B424FFC6CB76F7C31B8D7CBD218FFAD0CBA4730DAE724C3AD2EADB26ACB366C4D01D26A05B79863BF60348BD193F3B6E43EEB184AFB6813962D3C0CEA4AD2D7B30E8FF8CF161D8AB7086414C3695748A52B393E03B94443E9434AFE8E0D04B83450B17F45DB9C27146692D09B0F39C1ACE1027541BF1048C66CC28A754E9C5F1E7AF00722B9".toCharArray());
        // 解密
        String decryptResult = decrypt(decode, password);
        System.out.println("解密后：" + decryptResult); //不转码会乱码

    }
}
