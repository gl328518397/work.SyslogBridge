package com.cbss.syslogBridge.start;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.cbss.syslogBridge.clientdemo.Syslog4jServerDemo;
import com.cbss.syslogBridge.util.KafkaEmitter;
import com.cbss.syslogBridge.util.MessageType;
import com.cbss.syslogBridge.util.PropertiesUtil;

public class StartSyslogBridge implements Runnable {
	private static Logger logger = Logger.getLogger(StartSyslogBridge.class);
	private KafkaEmitter kafkaEmitter;
	private Syslog4jServerDemo demoServer;
	private boolean flag = false;
	private Thread thread;
	private String isWriteFile;
	private String isSendKafka;
	File file;// = new File(DIR+File.separatorChar+file);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * 初始化
	 */
	public void start() {
		isWriteFile = PropertiesUtil.getValue("isWriteFile");
		isSendKafka = PropertiesUtil.getValue("isSendKafka");
		logger.info("syslogBridge begin init,isWriteFile[" + isWriteFile
				+ "],isSendKafka[" + isSendKafka + "].");

		// 写文件
		if (isWriteFile.equals("1")) {
			String fileDir = PropertiesUtil.getValue("fileDir");
			String fileName = sdf.format(new Date()) + ".syslog";
			file = new File(fileDir + File.separatorChar + fileName);
			logger.info("fileDir[" + fileDir + "],fileName[" + fileName + "].");
		}

		// 发kafka
		if (isSendKafka.equals("1")) {
			kafkaEmitter = new KafkaEmitter();
		}

		demoServer = new Syslog4jServerDemo();
		flag = true;

		thread = new Thread(this, "syslogBridge");
		thread.start();
		logger.info("syslogBridge finish init...");

	}

	@Override
	public void run() {

		while (flag) {
			//logger.info("get meaasge");
			String message = demoServer.emit();
			
			int messageType = getType(message);//
			// logger.info(message);
			if (isWriteFile.equals("1")) {
				writeFile(message);
			}

			if (isSendKafka.equals("1")) {
				//kafkaEmitter.send(message);
				
				kafkaEmitter.send(message,messageType);
			}
		}

	}

	
	/**
	 * 根据日志的开头判断是ESS日志还是北六的日志
	 * @param message
	 * @return
	 */
	private int getType(String message) {
		if(message.startsWith(MessageType.SYSLOG_ESS_HOST)){
			return MessageType.SYSLOG_ESS;
		}else if(message.startsWith(MessageType.SYSLOG_NORTH6_HOST)){
			return MessageType.SYSLOG_NORTH6;
		}else{
			return MessageType.SYSLOG_NULL;
		}
		
	}

	/**
	 * 写文件
	 * 
	 * @param message
	 */
	private void writeFile(String message) {

		try {
			FileUtils.writeStringToFile(file, message, true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}
