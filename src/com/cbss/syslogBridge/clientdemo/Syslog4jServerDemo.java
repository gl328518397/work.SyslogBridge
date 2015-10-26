package com.cbss.syslogBridge.clientdemo;

import java.net.SocketAddress;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;
import org.productivity.java.syslog4j.server.SyslogServer;
import org.productivity.java.syslog4j.server.SyslogServerConfigIF;
import org.productivity.java.syslog4j.server.SyslogServerEventHandlerIF;
import org.productivity.java.syslog4j.server.SyslogServerEventIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;
import org.productivity.java.syslog4j.server.SyslogServerSessionlessEventHandlerIF;

import com.cbss.syslogBridge.util.ByteArray;
import com.cbss.syslogBridge.util.PropertiesUtil;
import com.cbss.syslogBridge.util.SyslogUtility;

public class Syslog4jServerDemo implements SyslogServerSessionlessEventHandlerIF{
	//http://www.syslog4j.org/docs/javadoc/
	private static Logger logger = Logger.getLogger(Syslog4jServerDemo.class);
	private static byte delimiter = 1;//分隔符
	//private static String SEPARATOR = "#";
	//public static final String SYSLOG_HOST = "localhost";
	//public static final String SYSLOG_HOST = "10.124.3.1";
	//public static final String SYSLOG_HOST = "192.168.1.1";
	public static final String SYSLOG_HOST = PropertiesUtil.getValue("syslog_host");
	public static final int SYSLOG_PORT = Integer.parseInt(PropertiesUtil.getValue("syslog_port"));
	public static final String UDP_PROTOCOL = "udp";
	public static final int DEFAULT_QUEUE_SIZE = 1000;
	public static final int DEFAULT_BATCH_SIZE = 10;
	
	private transient ArrayBlockingQueue<String> syslog;
	//private transient HashMap<Long, List<String>> emittedButNonAcked;
	private transient SyslogServerIF server;
	
	public Syslog4jServerDemo(){
		syslog = new ArrayBlockingQueue<String>(DEFAULT_QUEUE_SIZE);
	    //emittedButNonAcked = Maps.newHashMapWithExpectedSize(DEFAULT_QUEUE_SIZE);
	    server = SyslogServer.getThreadedInstance(UDP_PROTOCOL.toLowerCase());
	    SyslogServerConfigIF config = server.getConfig();
	    config.setHost(SYSLOG_HOST);
		config.setPort(SYSLOG_PORT);
		config.addEventHandler(this);
		logger.info("SYSLOG_HOST["+SYSLOG_HOST+"],SYSLOG_PORT["+SYSLOG_PORT+"].");
		
	}

	public static void main(String[] args) {
		Syslog4jServerDemo demoServer = new Syslog4jServerDemo();
		System.out.println("begin ");
		while(true){
			
			demoServer.emit();
		}
	}
	
	public String emit(){
		 String message = null;
			try {
				message = syslog.take();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return message;
	}
	
	
	

	@Override
	public void destroy(SyslogServerIF server) {
		return;
	}

	@Override
	public void initialize(SyslogServerIF server) {
		return;
	}

	@Override
	public void event(SyslogServerIF server, SocketAddress socket,
			SyslogServerEventIF event) {
		String date = (event.getDate() == null ? new Date() : event.getDate()).toString();
		String facility = SyslogUtility.getFacilityString(event.getFacility());
		String level = SyslogUtility.getLevelString(event.getLevel());
		String host = event.getHost();
		String message = event.getMessage();
		
		ByteArray byteArray = new ByteArray();
		byteArray.append(host.getBytes());
		byteArray.append(delimiter);
		byteArray.append(date.getBytes());
		byteArray.append(delimiter);
		byteArray.append(message.getBytes());
		String sendMessage = new String(byteArray.toArray());
		
		boolean interrupted = false;
		do {		
			if (!syslog.offer(sendMessage)) {
				try {
					syslog.take();
					syslog.offer(sendMessage);
					interrupted = false;
					
				} catch (InterruptedException e) {
					// shouldnt happen, we take() if the queue full, so no waiting
					interrupted = true;
				}
			}
		} while (interrupted); // retry loop
		
		//logger.info(sendMessage);
		//logger.info("接收到SYSLOG消息：facility{" + facility + "},level{" + level + "},sendMessage{" + sendMessage+"}.");
	}

	@Override
	public void exception(SyslogServerIF server, SocketAddress socket,
			Exception exception) {
		return;
	}

}
