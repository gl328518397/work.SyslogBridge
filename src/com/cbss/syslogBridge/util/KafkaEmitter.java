package com.cbss.syslogBridge.util;

import org.apache.log4j.Logger;


/**
 * 
 *
 */
public class KafkaEmitter {
	
private static Logger logger = Logger.getLogger(KafkaEmitter.class);
	
	private KafkaProducer kafkaproducer;
	private KafkaProducer kafkaproducerNorth6;
	
	
	public KafkaEmitter(){
         String brokers = PropertiesUtil.getValue("brokers");
         //String topic = PropertiesUtil.getValue("topic_ods");
        //ESS日志
         String topic = PropertiesUtil.getValue("topic_syslog");
		 kafkaproducer = new KafkaProducer(brokers, topic);
		 //北六日志
		 String topicNorth6 = PropertiesUtil.getValue("topic_syslog_north6");
		 kafkaproducerNorth6 = new KafkaProducer(brokers, topicNorth6);
		 
		 logger.info("brokers["+brokers+"],topic["+topic+"],topicNorth6["+topicNorth6+"].");
	}
	
	/**
	 * 只有一个kafka 生产者的情况
	 * @param message
	 */
	public void send(String message){
		
	    try {
			kafkaproducer.send(message);
			 //logger.info("向Kafka发送:\n"+message);
		} catch (Exception e) {
			 logger.error("向Kafka发送出错:\n"+message,e);
		}
	}
	
	/**
	 * 根据日志类型，用不同的producer发,ESS日志和北六的日志发往不同的topic
	 */
	public void send(String message,int messageType){
		
	    try {
	    	if(MessageType.SYSLOG_ESS==messageType){
	    		kafkaproducer.send(message);
	    		logger.info("发ESS:\n"+message);
	    	}else if(MessageType.SYSLOG_NORTH6==messageType){
	    		kafkaproducerNorth6.send(message);

	    		logger.info("发North6:\n"+message);
	    	}else{
	    		logger.info("类型错误不发kafka,message="+message+",messageType="+messageType);
	    	}
	    	
			//kafkaproducer.send(message);
			//logger.info("向发送:\n"+message);
		} catch (Exception e) {
			 logger.error("向Kafka发送出错:\n"+message,e);
		}
	}
	
	/**
	 * 根据日志类型，用不同的producer发,ESS日志和北六的日志发往不同的topic
	 * 根据key值进行分区，保证相同主句的日志发往相同的partition
	 * @param message
	 * @param messageType
	 * @param key
	 */
	public void send(String message,int messageType,String key){
		
	    try {
	    	if(MessageType.SYSLOG_ESS==messageType){
	    		kafkaproducer.send(message);
	    		logger.info("发ESS:\n"+message);
	    	}else if(MessageType.SYSLOG_NORTH6==messageType){

	    		//2015.09.17修改 根据主机地址发往不同的
	    		kafkaproducerNorth6.send(key,message);
	    		logger.info("发North6:\n"+message);
	    	}else{
	    		logger.info("类型错误不发kafka,message="+message+",messageType="+messageType);
	    	}
	    	
			//kafkaproducer.send(message);
			//logger.info("向发送:\n"+message);
		} catch (Exception e) {
			 logger.error("向Kafka发送出错:\n"+message,e);
		}
	}
	public void close(){
		kafkaproducer.close();
	}
}
