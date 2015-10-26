package com.cbss.syslogBridge.util;

import java.io.UnsupportedEncodingException;

/**
 * 字节缓冲
 * @author zb
 *
 */
public class ByteArray {
	private static final int DEFAULTSIZE = 4096;
	private int size = 0;
	private byte[] data = new byte[DEFAULTSIZE];
	
	public ByteArray(){
		data = new byte[DEFAULTSIZE];
	}
	
	public ByteArray(int BUFFER_SIZE){
		data = new byte[BUFFER_SIZE];
	}
	
	public ByteArray(byte[] data){
		setData(data,data.length);
	}
	
	public void setData(byte[] data,int length){
		this.data = data;
		size = length;
	}
	
	/**
	 * 重新分配缓中区大小
	 * @param newLength
	 */
	private void reallot(int newLength){
		if(data.length<newLength){
			byte[] bufTemp = data;
			int length = (newLength/DEFAULTSIZE+1)*DEFAULTSIZE;
			data = new byte[length];
			System.arraycopy(bufTemp,0,data,0,bufTemp.length);
			bufTemp = null;
		}
	}
	
	/**
	 * 追加字节
	 * @param value
	 */
	public void append(byte[] value){
		if(value!=null&& value.length>0){
			reallot(size+value.length);
			System.arraycopy(value,0,data,size,value.length);
			size += value.length;
		}
	}
	
	public void append(byte[] value,int length){
		append(value,0,length);
	}
	
	public void append(byte[] value,int start ,int length){
		if(value!=null&& length>0){
			reallot(size+length);
			System.arraycopy(value,start,data,size,length);
			size += length;
		}
	}
	
	/**
	 * 追加一个字节
	 * @param b
	 */
	public void append(byte b){
		reallot(size+1);
		data[size] = b;
		size ++;
	}
	
	/**
	 * 数据的长度
	 * @return
	 */
	public int size(){
		return size;
	}
	
	/**
	 * 得到字节
	 * @param index
	 * @return
	 */
	public byte get(int index){
		return data[index];
	}
	
	public void set(int index,byte b){
		reallot(index+1);
		data[index] = b;
		if(index+1>size)
			size = index+1;
	}
	
	/**
	 * 从缓冲区中指定的位置查找
	 * @param start
	 * @param value
	 * @return
	 */
	public int find(int start,byte[] value){
		
		return find(start,size-start,value);
	}
	
	/**
	 * 从缓冲区中指定的位置查找
	 * @param start
	 * @param length
	 * @param value
	 * @return
	 */
	public int find(int start,int length,byte[] value){
		if(value!=null){
			int j;
			if(length+start>size)length = size-start;
			if(length>=value.length){
				int step = start+length-value.length;
				for(int i=start;i<=step;i++){
					for(j=0;j<value.length;j++){
						if(data[i+j]!=value[j])
							break;
					}
					if(j==value.length)return i;
				}
			}
		}
		return -1;
	}
	
	public void clear(){
		size=0;
	}
	
	/**
	 * 删除缓冲区中的后半截
	 * @param start
	 */
	public void remove(int start){
		int i;
		for( i=0;i<size-start;i++){
			data[i] = data[start+i];
		}
		size = i;
	}
	
	public void removeTest(int start,int length){
		
		int end = start+length;
		end = end>size? size : end;
		int len = size - end;
		
		byte[] dst = new byte[len];
		for(int i = end;i<len;i++){
			dst[i-end] = data[i];
		}
		data = dst;
		size = len;
	}
	
	/**
	 * 删除缓冲区的数据
	 * @param start
	 * @param length
	 */
	public void remove(int start,int length){
		int end = start+length;
		end = end>size? size : end;
		int len = size - end;
		if(len>0){
			int i;
			for( i=0;i<len;i++){
				data[start+i] = data[end+i];
			}
		}
		size -= end-start;
	}
	
	public int compareTo(ByteArray byteArray){
		int flag = size>byteArray.size?1:0;
		flag = size<byteArray.size?-1:0;
		int length = flag>0?byteArray.size:size;
		for(int i=0;i<length;i++){
			if(data[i]>byteArray.data[i])
				return 1;
			if(data[i]<byteArray.data[i])
				return -1;
		}
		return flag;
	}
	
	public int compareTo(byte[] bytes){
		int flag = size>bytes.length?1:0;
		flag = size<bytes.length?-1:0;
		int length = flag>0?bytes.length:size;
		for(int i=0;i<length;i++){
			if(data[i]>bytes[i])
				return 1;
			if(data[i]<bytes[i])
				return -1;
		}
		return flag;
	}
	
	public byte[] getBuffer(){
		return data;
	}
	
	/**
	 * 得到字节数组
	 * @return
	 */
	public byte[] toArray(){
		byte[] array = new byte[size];
		System.arraycopy(data,0,array,0,size);
		return array;
	}
	
	/**
	 * gb2312为编码转换或字符集
	 */
	public String toString(){
		try {
			return new String(toArray(),"gb2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new String(toArray());
	}
}
