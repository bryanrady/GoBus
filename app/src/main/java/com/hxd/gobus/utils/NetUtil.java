package com.hxd.gobus.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by pengyu520 on 2016/7/26.
 * 网络操作工具类
 */
public class NetUtil {
    /**
     * 上下文内容
     */
    private Context mContext;

    private ConnectivityManager cManager;

    public static enum netType {
        wifi, CMNET, CMWAP, noneNet
    }

    public NetUtil(Context context) {
        this.mContext = context;
        this.cManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 网络是否连接可用
     */
    public boolean isConnected() {
        if (mContext != null) {
            if (cManager != null) {
                NetworkInfo[] networkInfo = cManager.getAllNetworkInfo();
                if (networkInfo != null) {
                    for (NetworkInfo info : networkInfo) {
                        if (info.getState() == NetworkInfo.State.CONNECTED && info.isAvailable()) {
                            return true;
                        }
                    }
                }
            }

        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     */
    public boolean isWifiConnected() {
        if (mContext != null) {
            NetworkInfo mWiFiNetworkInfo = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     */
    public boolean isMobileConnected() {
        if (mContext != null) {
            NetworkInfo mMobileNetworkInfo = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取当前网络连接的类型信息
     */
    public int getConnectedType() {
        if (mContext != null) {
            NetworkInfo mNetworkInfo = cManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * 监测URL地址是否有效
     * @param url
     * @return
     */
    public boolean checkURL(String url) {
        try {
            URL u = new URL(url);
            HttpURLConnection urlConn = (HttpURLConnection) u.openConnection();
            urlConn.connect();

            if (urlConn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap 网络3：net网络
     */
    public netType getAPNType() {
        NetworkInfo networkInfo = cManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType.noneNet;
        }
        int nType = networkInfo.getType();

        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                return netType.CMNET;
            } else {
                return netType.CMWAP;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return netType.wifi;
        }
        return netType.noneNet;

    }

    /**
     * 检查当前环境网络是否可用，不可用跳转至开启网络界面,不设置网络强制关闭当前Activity
     */
    public void setNetWork() {
        if (!isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("网络设置");
            builder.setMessage("网络不可用，是否现在设置网络？");
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ((Activity) mContext).startActivityForResult(new Intent(Settings.ACTION_SETTINGS), which);
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.create();
            builder.show();
        }
    }
}
