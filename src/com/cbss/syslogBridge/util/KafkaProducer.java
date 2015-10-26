package com.cbss.syslogBridge.util;

//import kafka.javaapi.producer.Producer;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class KafkaProducer {

	String brokers = null;
	String topic = null;

	Producer<String, String> producer;
	private final Properties props = new Properties();

	public int partitionMethod = -1; // random

	public KafkaProducer(String brokers) {
		this(brokers, null);
	}

	/**
	 * 
	 * @param brokers
	 *            "192.168.1.6:9092,192.168.1.7:9092,192.168.1.8:9092,192.168.1.8:9092,192.168.1.9:9092"
	 * @param topic
	 */
	public KafkaProducer(String brokers, String topic) {
		this.brokers = brokers;
		this.topic = topic;
		init();
	}
	
	public void init(){
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("metadata.broker.list", brokers);
		props.put("partitioner.key", String.valueOf(partitionMethod));
		//props.put("partitioner.class", "com.cucc.cdrmonitor.kafka.SimplePartitioner");
		props.put("partitioner.class", "com.cbss.syslogBridge.util.SimplePartitioner");
		
		producer = new Producer<String, String>(new ProducerConfig(props));
	}

	public void send(String content) throws Exception{
		KeyedMessage<String, String> message = new KeyedMessage<String, String>(
				topic, content);
		producer.send(message);
	
	}

	public void send(List<String> contents) throws Exception {
		List<KeyedMessage<String, String>> ms = new LinkedList<KeyedMessage<String, String>>();
		for (String i : contents) {
			ms.add(new KeyedMessage<String, String>(topic, i));
		}
		producer.send(ms);
	}
	
	/**
	 * 根据key值发送到固定的partition,
	 * @param key 我们取主机IP来定义key,这样保证每台主机产生的日志都发往相同的partition.
	 * @param content 发送的内容
	 * @throws Exception
	 */
	public void send(String key,String content) throws Exception{
		KeyedMessage<String, String> message = new KeyedMessage<String, String>(
				topic, key,content);
		
		producer.send(message);
	
	}
	
	public void close(){
		try{
			producer.close();
		} catch(Exception e){
			
		}
	}

}
