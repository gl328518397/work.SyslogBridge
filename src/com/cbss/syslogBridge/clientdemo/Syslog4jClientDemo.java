package com.cbss.syslogBridge.clientdemo;

import java.util.Date;

import org.productivity.java.syslog4j.Syslog;
import org.productivity.java.syslog4j.SyslogIF;

import com.cbss.syslogBridge.util.ByteArray;
import com.cbss.syslogBridge.util.PropertiesUtil;

public class Syslog4jClientDemo {
	private static byte delimiter = 1;
	public static void main(String[] args) {
		 SyslogIF syslog = Syslog.getInstance("udp");
		 syslog.getConfig().setHost("10.124.3.1");
		 //syslog.getConfig().setHost("localhost");
		 //syslog.getConfig().setHost("192.168.1.1");
		 
		 syslog.getConfig().setPort(514);
		int i=1;
			 while(true){
				 try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				 
				 String host = PropertiesUtil.getValue("send-test-syslog-host");
				 String message = PropertiesUtil.getValue("send-test-syslog");
				 ByteArray byteArray = new ByteArray();
					byteArray.append(host.getBytes());
					byteArray.append(delimiter);
					byteArray.append(new Date().toString().getBytes());
					byteArray.append(delimiter);
					byteArray.append(message.getBytes());
					String sendMessage = new String(byteArray.toArray());
				 
				 //syslog.info("Today is good day!,times="+i+"\n");
					syslog.info(sendMessage);
				 System.out.println("this is client,send["+sendMessage+"]");
				 i++;
			}

	}

}
