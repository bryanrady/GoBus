package com.hxd.gobus.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.hxd.gobus.BuildConfig;
import com.hxd.gobus.BusApp;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.core.http.download.DownloadListener;
import com.hxd.gobus.core.http.retrofit.RetrofitClient;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * 后台下载apk的服务
 * @author: wangqingbin
 * @date: 2019/7/17 9:27
 */

public class DownloadApkService extends Service {

    private static final String TAG = "DownloadApkService";
    private final int NotificationID = 0x10000;
    private NotificationManager mNotificationManager = null;
    private NotificationCompat.Builder builder;
    private int lastProgress = 0;
    private String apkUrl;
    private String fileName;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                apkUrl = bundle.getString("apkUrl");
                fileName = bundle.getString("fileName");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        downFile(apkUrl, fileName);
                    }
                }).start();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void downFile(String apkUrl, String fileName) {
        String downloadDir;
        if(TextUtils.isEmpty(Environment.getExternalStorageDirectory().getAbsolutePath())){
            downloadDir = BusApp.getContext().getCacheDir().getAbsolutePath() + "/gobus/apk";
        } else{
            downloadDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/gobus/apk";
        }
        RetrofitClient.create()
                .url(apkUrl)
                .dir(downloadDir)
                .filename(fileName)
                .extension("apk")
                .build()
                .download(new DownloadListener() {
                    @Override
                    public void onStart() {
                        String id = "my_channel_01";
                        String name = "我是渠道名字";
                        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        // 针对Android 8.0版本对于消息栏的限制，需要加入channel渠道这一概念
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {  //Android 8.0以上
                            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
                            mNotificationManager.createNotificationChannel(mChannel);
                            builder = new NotificationCompat.Builder(getApplicationContext());
                            builder.setSmallIcon(R.mipmap.ic_launcher);
                            builder.setTicker("正在下载新版本");
                            builder.setContentTitle(getApplicationName());
                            builder.setContentText("正在下载,请稍后...");
                            builder.setNumber(0);
                            builder.setChannelId(id);
                            builder.setAutoCancel(true);
                        } else {    //Android 8.0以下
                            builder = new NotificationCompat.Builder(getApplicationContext());
                            builder.setSmallIcon(R.mipmap.ic_launcher);
                            builder.setTicker("正在下载新版本");
                            builder.setContentTitle(getApplicationName());
                            builder.setContentText("正在下载,请稍后...");
                            builder.setNumber(0);
                            builder.setAutoCancel(true);
                        }
                        mNotificationManager.notify(NotificationID, builder.build());
                    }

                    @Override
                    public void onProgress(final int progress,long currentLength,long total) {
                        //这里进行一层优化 频繁调用notify会卡顿
                        if ((progress - lastProgress) > 2){
                            builder.setProgress(100, progress, false);
                        //    builder.setContentInfo(progress+"%");
                            mNotificationManager.notify(NotificationID, builder.build());
                            lastProgress = progress;
                        }
                    }

                    @Override
                    public void onFinish(String path) {
                        File file = new File(path);
                        Intent installIntent = new Intent(Intent.ACTION_VIEW);
                        Uri uri;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(DownloadApkService.this, BuildConfig.APPLICATION_ID +".provider", file);
                            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            uri = Uri.fromFile(file);
                        }
                        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
                        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        PendingIntent mPendingIntent = PendingIntent.getActivity(DownloadApkService.this, 0, installIntent, 0);
                        builder.setProgress(0, 0, false);
                        builder.setContentInfo("");
                        builder.setContentText("下载完成,请点击安装");
                        builder.setContentIntent(mPendingIntent);
                        mNotificationManager.notify(NotificationID, builder.build());
                        // 震动提示
                        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        assert vibrator != null;
                        vibrator.vibrate(250L);// 参数是震动时间(long类型)
                        stopSelf();
//                        startActivity(installIntent);// 下载完成之后自动弹出安装界面
//                        mNotificationManager.cancel(NotificationID);

                        EventBus.getDefault().post(file);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (mNotificationManager != null){
                            mNotificationManager.cancel(NotificationID);
                        }
                    }

                    @Override
                    public void onError(int errorCode, String message) {
                        if (mNotificationManager != null){
                            mNotificationManager.cancel(NotificationID);
                        }
                    }
                });
    }

    private String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo;
        try {
            packageManager = getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }

}
