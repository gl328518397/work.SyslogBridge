package com.cbss.syslogBridge.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

  static Properties ps;
	
	static{
		ps = new Properties();
		InputStream in = PropertiesUtil.class.getResourceAsStream("config.properties");
		try {
			ps.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getValue(String key){
		return ps.getProperty(key);
	}
	
	public static void main(String[] args){
		System.out.println(PropertiesUtil.getValue("queuesize"));
		System.out.println(PropertiesUtil.getValue("poolsize"));
		
	}
	
	/**
	 * 初始化高结算国家
	 * key nation:86:prov:79:areacode:0971:na	
	 * value 1
	 */
	public static void initHighFeeNations(){
		StringBuffer sb = new StringBuffer();
		
		
		
		String HighFeeNations = PropertiesUtil.getValue("HighFeeNations");
		
		String[] nationList = HighFeeNations.split(",");
		
		for(String temp : nationList){
			if(temp==null){
				continue;
			}
			sb.append("nation:").append(temp)
			.append(":prov:00")
			.append(":areacode:0000")
			.append(":na");

			String key = sb.toString();
			//client.set(key, "1");
			System.out.println(key);
			
			sb.setLength(0);
		}
		
	}
	
	
}

	


