package com.cbss.syslogBridge.util;

import java.util.Random;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class SimplePartitioner implements Partitioner {

	final String item = "partitioner.key";

	int key = -1;

	Random random = new Random();

	public SimplePartitioner(VerifiableProperties props) {
		if (props.containsKey(item)) {
			String v = props.getProperty(item);
			try {
				key = Integer.parseInt(v);
			} catch (Exception e) {
				System.err.println("SimplePartitioner parse props int error:"
						+ v);
			}
		}
	}

	public int partition(Object mkey, int totalPartitions) {
		int ret = key < 0 ? random.nextInt(totalPartitions) : key % totalPartitions;
//		System.out.println("me partition called:key" + key + ":" + ret);
		return ret;
	}

}