package com.neighbor.common.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @use 利用Java代码给图片加水印
 */
public class WaterMarkUtils {

    /**
     * @param srcImgPath 源图片路径
     * @param tarImgPath 保存的图片路径
     * @param waterMarkContent 水印内容
     * @param markContentColor 水印颜色
     * @param font 水印字体
     */
    public void addWaterMark(String srcImgPath, String tarImgPath, String waterMarkContent,Color markContentColor ) {

        try {
            // 读取原图片信息
            File srcImgFile = new File(srcImgPath);//得到文件
            Image srcImg = ImageIO.read(srcImgFile);//文件转化为图片
            // 加水印
            Font font1 = new Font("微软雅黑", Font.PLAIN, 135);
            BufferedImage bufImg1 = writeWatermark(waterMarkContent, markContentColor, font1, srcImg);  
            Font font2 = new Font("创艺简", Font.PLAIN, 135);
            BufferedImage bufImg2 = writeWatermark(waterMarkContent, markContentColor, font2, srcImg);  
            Font font3 = new Font("华文彩云", Font.PLAIN, 135);
            BufferedImage bufImg3 = writeWatermark(waterMarkContent, markContentColor, font3, srcImg);  
            Font font4 = new Font("华文隶书", Font.PLAIN, 135);
            BufferedImage bufImg4 = writeWatermark(waterMarkContent, markContentColor, font4, srcImg);  
            int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
            int srcImgHeight = srcImg.getHeight(null);//获取图片的高
            
            
            BufferedImage bufImg = new BufferedImage(srcImgWidth*2, srcImgHeight*2, BufferedImage.TYPE_INT_RGB);
            
            
            modifyImagetogeter(bufImg2, bufImg,0,0);
            modifyImagetogeter(bufImg1, bufImg,srcImgWidth,0);
            modifyImagetogeter(bufImg3, bufImg,0,srcImgHeight);
            modifyImagetogeter(bufImg4, bufImg,srcImgWidth,srcImgHeight);
            
            writeImage(tarImgPath,bufImg);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

	public BufferedImage writeWatermark(String waterMarkContent, Color markContentColor, Font font, Image srcImg) {
        int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
        int srcImgHeight = srcImg.getHeight(null);//获取图片的高
		BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bufImg.createGraphics();
		g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
		g.setColor(markContentColor); //根据图片的背景设置水印颜色
		g.setFont(font);              //设置字体

		//设置水印的坐标 
		int x = (int) Math.floor(srcImgWidth/2 );  
		int y = (int) Math.floor(srcImgHeight/2 );  
		g.drawString(waterMarkContent, 0, y);  //画出水印
		g.dispose();
		return bufImg;
	}
    
    public void writeImage(String tarImgPath,BufferedImage bufImg) throws IOException{
    	
    	  FileOutputStream outImgStream = new FileOutputStream(tarImgPath);  
          ImageIO.write(bufImg, "jpg", outImgStream);
          System.out.println("添加水印完成");  
          outImgStream.flush();  
          outImgStream.close();  
    }
    
    
    
    public Graphics2D modifyImagetogeter(BufferedImage b, BufferedImage d,int x,int y) {  
        
        try {  
            int w = b.getWidth();  
            int h = b.getHeight();  
  
            Graphics2D g = d.createGraphics();  
            g.drawImage(b, x, y, w, h, null);  
            g.dispose();
            return g;
        } catch (Exception e) {  
            System.out.println(e.getMessage());  
        }  
  
        return null;  
    } 
    
    public int getWatermarkLength(String waterMarkContent, Graphics2D g) {  
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());  
    }  
    public static void main(String[] args) {
        Font font = new Font("微软雅黑", Font.PLAIN, 35);                     //水印字体
        String srcImgPath="D:/学习相关/百度推广/我的项目/微信图片_20180928172337.jpg"; //源图片地址
        String tarImgPath="D:/学习相关/百度推广/我的项目/t.jpg"; //待存储的地址
		String waterMarkContent = "测试痔疮";  //水印内容
        Color color=new Color(255,255,255,128);                               //水印图片色彩以及透明度
        new WaterMarkUtils().addWaterMark(srcImgPath, tarImgPath,waterMarkContent, Color.black);

    }
}