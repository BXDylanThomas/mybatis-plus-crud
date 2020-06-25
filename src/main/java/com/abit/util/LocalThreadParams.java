package com.abit.util;

public class LocalThreadParams {

	private static ThreadLocal<Object> obj = new ThreadLocal<>();

	public static void setParams(Object obj) {
		LocalThreadParams.obj.set(obj);
	}

	public static Object getParams(){
		return LocalThreadParams.obj.get();
	}

	public static void clean(){
		LocalThreadParams.obj.set(null);
	}

}
