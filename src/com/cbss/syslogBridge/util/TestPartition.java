package com.cbss.syslogBridge.util;

import java.util.Random;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class TestPartition implements Partitioner {

	final String item = "partitioner.key";

    Random random = new Random();


	public TestPartition(VerifiableProperties props) {
		if (props.containsKey(item)) {
			String v = props.getProperty(item);
			try {
				System.out.println("init patition,,,");
			} catch (Exception e) {
				System.err.println("SimplePartitioner parse props int error:"
						+ v);
			}
		}
	}

	public int partition(Object mkey, int totalPartitions) {
		
        System.out.print("partitions number is "+totalPartitions+"   ");
        if (mkey == null) {
            System.out.println("key is null ");
            return random.nextInt(totalPartitions);
        }
        else {
        	String host = mkey.toString();
        	
        	
        	
            int result = Math.abs(mkey.hashCode())%totalPartitions; //很奇怪，
                     //hashCode 会生成负数，奇葩，所以加绝对值
            System.out.println("key is "+ mkey+ " partitions is "+ result);
            return result;
        }

	}
	
	public static void main(String[] args){
		
		String host1= "102.58";
		String host2= "132.35.102.53"; 
		int dd = host1.hashCode();
		
		double gg = Double.parseDouble(host1);
		
		System.out.println("host1.hashCode="+host1.hashCode());
		System.out.println("host2.hashCode="+host2.hashCode());
		System.out.println(gg);
		
	}
	
	


}