package com.hxd.gobus.utils;

import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * @author pengyu520
 * @date 2015年12月19日 上午11:21:27 
 * @Des:文件下载工具类
 */
public class DownloadFile {
	private static Handler mHandler;
	public final static int DOWN_UPDATE = 1;// 下载中
	public final static int DOWN_OVER = 2;// 下载完成
	public final static int DOWN_FAIL= 3;// 下载失败
	public final static String DOWN_MSG= "下载失败,请检查网络或服务器!";
	private DownloadFile(Handler handler) {
		super();
		mHandler = handler;
	}

	private static DownloadFile instance = null;

	public static DownloadFile getInstance(Handler handler) {
		if (instance == null) {
			instance = new DownloadFile(handler);
		}
		return instance;
	}
	public void updateApk(final String url, final File saveFile){
		new Thread(new Runnable() {

			@Override
			public void run() {
				downloadApkFile(url, saveFile);
			}
		}).start();
	}

	public void downloadApkFile(String url, File saveFile) {
		URL strUrl ;
		HttpURLConnection conn = null;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			strUrl = new URL(url);
			conn = (HttpURLConnection) strUrl.openConnection();// 创建URL及HttpURLConnection对象
			conn.setRequestProperty("User-Agent", "PacificHttpClient");// 设置
			conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			conn.setRequestProperty("Accept-Charset", "utf-8");
			conn.setRequestProperty("contentType", "utf-8");
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-type", "text/html");
			conn.setConnectTimeout(10 * 1000);
			conn.setReadTimeout(10 * 1000);
			conn.connect();// 连接到服务器
			int responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				int length = conn.getContentLength();// 得到文件的长度
				is = conn.getInputStream();// 得到输入流
				fos = new FileOutputStream(saveFile);
				byte buf[] = new byte[1024];
				int number = 0;
				int readSize = 0;
				while ((number = is.read(buf)) != -1) {
					fos.write(buf, 0, number);
					readSize += number;
					String progress = myPercent(readSize, length);// 计算现在的的百分比
					sendUpdateMessage(DOWN_UPDATE, progress);// 发送下载更新消息
				}
				sendEmpMessage(DOWN_OVER);// 发送URL为空的消息
			} else {
				sendEmpMessage(DOWN_FAIL);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			sendEmpMessage(DOWN_FAIL);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			sendEmpMessage(DOWN_FAIL);
		} catch (IOException e) {
			e.printStackTrace();
			sendEmpMessage(DOWN_FAIL);
		} finally {
			try {
				if (conn != null) {// 关闭相关连接和流，释放资源
					conn.disconnect();
				}
				if (fos != null) {
					fos.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				sendEmpMessage(DOWN_FAIL);
			}
		}
	}

	/**
	 * method desc：下载文件
	 * @param serverFilePath 网络文件路径
	 * @param localSavePath 传入的文件
	 */
	public void downloadFile(String serverFilePath, String localSavePath) {
		File localSaveFile = new File(localSavePath);
		File fileParent = localSaveFile.getParentFile();
		if(!fileParent.exists()){
			fileParent.mkdirs();
		}
		try {
			localSaveFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		URL strUrl ;
		HttpURLConnection conn = null;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			strUrl = new URL(serverFilePath);
			conn = (HttpURLConnection) strUrl.openConnection();// 创建URL及HttpURLConnection对象
			conn.setRequestProperty("User-Agent", "PacificHttpClient");// 设置
			conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			conn.setRequestProperty("Accept-Charset", "utf-8");
			conn.setRequestProperty("contentType", "utf-8");
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-type", "text/html");
			conn.setConnectTimeout(30 * 1000);
			conn.setReadTimeout(10 * 1000);
			conn.connect();// 连接到服务器
			int responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				int length = conn.getContentLength();// 得到文件的长度
				is = conn.getInputStream();// 得到输入流
				fos = new FileOutputStream(localSaveFile);
				byte buf[] = new byte[1024];
				int number = 0;
				int readSize = 0;
				while ((number = is.read(buf)) != -1) {
					fos.write(buf, 0, number);
					readSize += number;
					String progress = myPercent(readSize, length);// 计算现在的的百分比
					sendUpdateMessage(DOWN_UPDATE, progress);// 发送下载更新消息
				}
				sendEmpMessage(DOWN_OVER);// 发送URL为空的消息
			} else {
				sendEmpMessage(DOWN_FAIL);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			sendEmpMessage(DOWN_FAIL);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			sendEmpMessage(DOWN_FAIL);
		} catch (IOException e) {
			e.printStackTrace();
			sendEmpMessage(DOWN_FAIL);
		} finally {
			try {
				if (conn != null) {// 关闭相关连接和流，释放资源
					conn.disconnect();
				}
				if (fos != null) {
					fos.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				sendEmpMessage(DOWN_FAIL);
			}
		}
	}

	/**
	 * method desc：发空消息
	 * @param flag 标识
	 */
	private void sendEmpMessage(int flag) {
		Message message = mHandler.obtainMessage();
		message.what = flag;
		mHandler.sendMessage(message);
	}

	/**
	 * method desc：发送更新的消息
	 * @param flag 标识
	 * @param progress 消息进度
	 */
	private void sendUpdateMessage(int flag, String progress) {
		Message message = mHandler.obtainMessage();
		message.what = flag;
		message.obj = progress;
		mHandler.sendMessageDelayed(message, 500);
	}

	public String myPercent(int y, int z) {
		double baiy = y * 1.0;
		double baiz = z * 1.0;
		double fen = baiy / baiz;
		DecimalFormat df = new DecimalFormat("##%"); // ##.00% 百分比格式，后面不足2位的用0补齐
		return df.format(fen);
	}
}
