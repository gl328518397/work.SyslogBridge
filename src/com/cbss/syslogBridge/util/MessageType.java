package com.cbss.syslogBridge.util;

public class MessageType {

	public static int SYSLOG_NULL = 0;//nothing
	
	public static int SYSLOG_ESS = 1;//ESS的日志
	public static String SYSLOG_ESS_HOST = "10.161";//以这个地址开头的都是ESS的日志
	
	public static int SYSLOG_NORTH6 = 2;//北六的日志
	public static String SYSLOG_NORTH6_HOST = "132.35";
}
