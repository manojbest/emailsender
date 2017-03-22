package com.emailer.util;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtil {

	private JsonUtil() {
		
	}
	
	public static <T> T readJson(String jsonString, Class<T> valueType){		
		T t = null;
		try {
			t = new ObjectMapper().readValue(jsonString,valueType);
		} catch (IOException ex) {
			System.out.println("Could not parse the json string : " + ex);		
		}
		return t;
	}
	
	public static String writeJson(Object obj, Class<?> valueType){
		String strJson = null;
		try {
			strJson = new ObjectMapper().writeValueAsString(valueType.cast(obj));
		} catch (Exception e) {
			System.out.println("Could not write the json string : "+ e);
		}
		return strJson;
	}
}
