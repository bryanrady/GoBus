package com.hxd.gobus.utils;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

/**
 * @author 作者:pengyu
 * @version 版本号:1.0
 * @date 创建时间:2015年5月4日 上午11:15:29
 * @description 类描述:SDCard操作工具类
 */
public class SDCardUtil {

	private SDCardUtil() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 判断SDCard是否可用
	 * 
	 * @return true为可用，否则为不可用
	 */
	public static boolean isSDCardEnable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取SD卡路径
	 * 
	 * @return
	 */
	public static String getSDCardPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	}

	/**
	 * 获取SD卡的剩余容量 单位byte
	 * 
	 * @return
	 */
	public static long getSDCardAllSize() {
		if (isSDCardEnable()) {
			StatFs stat = new StatFs(getSDCardPath());
			// 获取空闲的数据块的数量
			long availableBlocks = (long) stat.getAvailableBlocks() - 4;
			// 获取单个数据块的大小（byte）
			long freeBlocks = stat.getAvailableBlocks();
			return freeBlocks * availableBlocks;
		}
		return 0;
	}

	/**
	 * 获取指定路径所在空间的剩余可用容量字节数，单位byte
	 * 
	 * @param filePath
	 * @return 容量字节 SDCard可用空间，内部存储可用空间
	 */
	public static long getFreeBytes(String filePath) {
		// 如果是sd卡的下的路径，则获取sd卡可用容量
		if (filePath.startsWith(getSDCardPath())) {
			filePath = getSDCardPath();
		} else {// 如果是内部存储的路径，则获取内存存储的可用容量
			filePath = Environment.getDataDirectory().getAbsolutePath();
		}
		StatFs stat = new StatFs(filePath);
		long availableBlocks = (long) stat.getAvailableBlocks() - 4;
		return stat.getBlockSize() * availableBlocks;
	}

	/**
	 * 获取系统存储路径
	 * 
	 * @return
	 */
	public static String getRootDirectoryPath() {
		return Environment.getRootDirectory().getAbsolutePath();
	}

	/**
	 * 获取自定义存储路径
	 * 
	 * @return
	 */
	public static String getSDCardPath(String path) {
		String SDCardDir = "";
		if (isSDCardEnable()) {
			SDCardDir = Environment.getExternalStorageDirectory().getPath() + "/" + path;
		}
		File tempFile = new File(SDCardDir);
		if (!tempFile.exists()) {
			if (tempFile.mkdirs()) {
				Log.v("exception", "tempFile=" + SDCardDir + " mkdir success!");
			}
		} else {
			Log.v("exception", "tempFile=" + SDCardDir + " is exist!");
		}
		return SDCardDir;
	}

	/**
	 * 根据文件名删除文件
	 * 
	 * @param filePath
	 *            SDCard卡上完整路径
	 * @return void
	 */
	public static void deleteFile(String filePath) {
		if (filePath!=null&&!filePath.equals(""))
			return;
		File file = new File(filePath);
		if (file.exists())
			file.delete();
	}
}
