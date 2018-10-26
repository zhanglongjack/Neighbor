package com.neighbor.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FileReaderUtil {

	public static Map<String, String> readFile(String filePath) {
		Map<String, String> resultMap = new HashMap<String, String>();
		File file = new File(filePath);
		if (file.isFile() && file.exists()) { // 判断文件是否存在
			InputStreamReader read = null;
			try {
				read = new InputStreamReader(new FileInputStream(file), "UTF-8");
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					System.out.println(lineTxt);
					resultMap.put(lineTxt, lineTxt);
				}
				return null;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (read != null) {
					try {
						read.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return null;
	}

	public static Properties readProperties(String fileName) {
		Properties prop = new Properties();
		try {
			// 读取属性文件a.properties
			InputStream in = FileReaderUtil.class.getResourceAsStream( "/users.properties" );    
//			InputStream in = new BufferedInputStream(new FileInputStream("users.properties"));
			prop.load(in); /// 加载属性列表
//			Iterator<String> it = prop.stringPropertyNames().iterator();
//			while (it.hasNext()) {
//				String key = it.next();
//				System.out.println(key + ":" + prop.getProperty(key));
//			}
//			in.close();

			/// 保存属性到b.properties文件
//			FileOutputStream oFile = new FileOutputStream("b.properties", true);// true表示追加打开
//			prop.setProperty("phone", "10086");
//			prop.store(oFile, "The New properties file");
//			oFile.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return prop;
	}
}
