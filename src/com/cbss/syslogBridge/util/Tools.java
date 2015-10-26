package com.cbss.syslogBridge.util;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;

public class Tools {

	public static byte[] intToBytes(int num) {
		byte[] b = new byte[4];
		for (int i = 3; i >= 0; i--) {
			b[i] = (byte) (num & 0xFF);
			num = num >> 8;
		}
		return b;
	}

	public static int bytesToInt(byte[] b) {
		int tmp = 0;
		int v = 0;
		for (int i = 0; i < 4; i++) {
			v <<= 8;
			tmp = b[i] & 0xFF;
			v |= tmp;
		}
		return v;
	}

	// long转byte数组
	public static byte[] longToBytes(long x) {
		byte[] bb = new byte[8];
		bb[0] = (byte) (x >> 56);
		bb[1] = (byte) (x >> 48);
		bb[2] = (byte) (x >> 40);
		bb[3] = (byte) (x >> 32);
		bb[4] = (byte) (x >> 24);
		bb[5] = (byte) (x >> 16);
		bb[6] = (byte) (x >> 8);
		bb[7] = (byte) (x >> 0);
		return bb;
	}

	public static long bytesToLong(byte[] b) throws Exception {
		if (b.length > 8) {
			throw new Exception("byte 数组尺寸大于8，请检查，无法转换long型");
		}
		if (b.length < 8) {
			byte[] bb = new byte[8];
			System.arraycopy(b, 0, bb, 0, b.length);
			b = bb;
		}
		return ((((long) b[0] & 0xff) << 56) | (((long) b[1] & 0xff) << 48)
				| (((long) b[2] & 0xff) << 40) | (((long) b[3] & 0xff) << 32)
				| (((long) b[4] & 0xff) << 24) | (((long) b[5] & 0xff) << 16)
				| (((long) b[6] & 0xff) << 8) | (((long) b[7] & 0xff) << 0));
	}

	// public static void main(String[] args) throws Exception{
	// byte[] a=String.valueOf(1).getBytes();
	// byte[] b=new byte[]{Char1};
	// return;
	// }
	// public static List<byte[]> parseFields(String line) {
	// byte[] lineByte = line.getBytes();
	// if (lineByte.length == 0) {
	// return null;
	// }
	// int start = 0;
	// List<byte[]> list = new ArrayList<byte[]>();
	// for (int index = 0; index < lineByte.length; index++) {
	// byte b = lineByte[index];
	// if (b ==0x2c
	// || b == 0x2f) {
	// byte[] field = new byte[index - start];
	// System.arraycopy(lineByte, start, field, 0, index - start);
	// list.add(field);
	// if (b == 0x2c)
	// start = index + 1;
	// }
	// }
	// return list;
	// }
	public static void reverse(int[] array) {
		int len = array.length;
		int count = len / 2;
		for (int i = 0; i < count; i++) {
			int temp = array[i];
			array[i] = array[len - 1 - i];
			array[len - 1 - i] = temp;
		}
		return;
	}

	public static byte[] getReverse(byte[] a) {
		if (a == null)
			return null;
		byte[] b = new byte[a.length];
		for (int i = 0; i < b.length; i++) {
			b[i] = a[a.length - 1 - i];
		}
		return b;
	}

	/*
	 * 拼接两个一维bye数组
	 */
	public static byte[] uniArray(byte[] a, byte[] b) {
		if (a == null && b != null)
			return b;
		else if (a != null && b == null)
			return a;
		else if (a == null && b == null)
			return null;
		else {
			byte[] tmp = new byte[a.length + b.length];
			System.arraycopy(a, 0, tmp, 0, a.length);
			System.arraycopy(b, 0, tmp, a.length, b.length);
			return tmp;
		}
	}

	// short转换成byte数组

	public static byte[] shortToBytes(short s) {
		byte[] shortBuf = new byte[2];
		for (int i = 0; i < 2; i++) {
			int offset = (shortBuf.length - 1 - i) * 8;
			shortBuf[i] = (byte) ((s >>> offset) & 0xff);
		}
		return shortBuf;
	}

	// byte数组转换成short

	public static short bytesToShort(byte[] b) {
		return (short) ((b[0] << 8) + (b[1] & 0xFF));
	}
	
	public static byte[][] split(byte[] bb, byte c){
		if( bb == null){
			return null;
		}
		LinkedList<byte[]> list = new LinkedList<byte[]>();
		ByteBuffer buf = ByteBuffer.allocate(bb.length);
		for(byte b : bb){
			if ( b == c ){
				list.add( Arrays.copyOf(buf.array(), buf.position() ) );
				buf.clear();
			} else {
				buf.put(b);
			}
		}
		
		list.add( Arrays.copyOf(buf.array(), buf.position() ) );
		
		byte[][] ret = new byte[list.size()][];
		int i = 0 ;
		for(byte[] _ : list){
			ret[i] = _ ;
			i ++ ;
		}
		return  ret;
	}

}
