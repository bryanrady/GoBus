package com.hxd.gobus.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * Created by pengyu520 on 2016/6/7.
 * 对Gson解析json数据的进一步封装
 */
public class GsonUtils {

	private static Gson gson = null;

	static {
		if (gson == null) {
			gson = new Gson();
		}
	}

	private GsonUtils() {

	}

	/**
	 * 将对象转换成json字符串
	 * 
	 * @param object
	 * @return
	 */
	public static String objectToJson(Object object) {
		String jsonString = null;
		if (gson != null) {
			jsonString = gson.toJson(object);
		}
		return jsonString;
	}

	/**
	 * 将json字符串转换成实体对象
	 * 
	 * @param jsonString
	 * @param cls
	 * @return
	 */
	public static <T> T jsonToObject(String jsonString, Class<T> cls) {
		T t = null;
		if (gson != null) {
			t = gson.fromJson(jsonString, cls);
		}
		return t;
	}

	/**
	 * 将json数组转换成list(实体对象)
	 * 
	 * @param jsonString
	 * @param cls
	 * @return
	 */
	public static <T> List<T> jsonToList(String jsonString, Class<T> cls) {
		List<T> list = null;
		if (gson != null) {
			list = gson.fromJson(jsonString, new TypeToken<List<T>>() {}.getType());
		}
		return list;
	}

	/**
	 * 将json数组转换成list(map对象)
	 * 
	 * @param jsonString
	 * @return
	 */
	public static <T> List<Map<String, T>> jsonToListMaps(String jsonString) {
		List<Map<String, T>> list = null;
		if (gson != null) {
			list = gson.fromJson(jsonString, new TypeToken<List<Map<String, T>>>() {
			}.getType());
		}
		return list;
	}

	/**
	 * 将json数组转换成map
	 * 
	 * @param jsonString
	 * @return
	 */
	public static <T> Map<String, T> jsonToMaps(String jsonString) {
		Map<String, T> map = null;
		if (gson != null) {
			map = gson.fromJson(jsonString, new TypeToken<Map<String, T>>() {
			}.getType());
		}
		return map;
	}

	/**
	 * 将Map转化为Json
	 * 
	 * @param map
	 * @return String
	 */
	public static <T> String mapToJson(Map<String, T> map) {
		String jsonStr = null;
		if (gson != null) {
			jsonStr = gson.toJson(map);
		}
		return jsonStr;
	}
}
