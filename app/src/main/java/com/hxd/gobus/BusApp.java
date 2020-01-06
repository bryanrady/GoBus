package com.hxd.gobus;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.amap.api.maps2d.MapsInitializer;
import com.bumptech.glide.request.target.ViewTarget;
import com.hxd.gobus.chat.database.ConversationEntry;
import com.hxd.gobus.chat.database.FriendEntry;
import com.hxd.gobus.chat.database.FriendRecommendEntry;
import com.hxd.gobus.chat.database.UserEntry;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.RetrofitInit;
import com.hxd.gobus.di.components.DaggerAppComponent;
import com.hxd.gobus.greendao.DaoMaster;
import com.hxd.gobus.greendao.DaoSession;
import com.hxd.gobus.service.NotificationClickEventReceiver;
import com.hxd.gobus.utils.CrashHandler;
import com.hxd.gobus.utils.LogUtils;
import com.hxd.gobus.utils.SharePreferenceManager;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import javax.inject.Inject;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * @description:    应用入口
 * @author: wangqingbin
 * @date: 2019/7/10 16:17
 */

public class BusApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> mActivityInjector;

    private static DaoSession mDaoSession;
    private RefWatcher mRefWatcher;
    private static BusApp mInstance;

    public static BusApp getContext(){
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initThirdSdk();
    }

    private void initThirdSdk() {
        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this);
        mRefWatcher = setupLeakCanary();
    //    CrashHandler.getInstance().init(this);
        initRetrofit();
        initGreenDao();
        initActiveAndroid();
        //高德地图初始化工作
        try {
            MapsInitializer.initialize(getApplicationContext());
            MapsInitializer.loadWorldGridMap(true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        LogUtils.isDebug = true;//测试时使用调试模式,打印Log
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(mInstance);     		// 初始化 JPush
        JMessageClient.init(this, true);
        JMessageClient.setDebugMode(true);
        SharePreferenceManager.init(this, BaseConfig.JCHAT_CONFIGS);
        //设置Notification的模式
        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_WITH_SOUND | JMessageClient.FLAG_NOTIFY_WITH_LED | JMessageClient.FLAG_NOTIFY_WITH_VIBRATE);
        //注册Notification点击的接收器
        new NotificationClickEventReceiver(this);

        ViewTarget.setTagId(R.id.glide_tag);
    }

    private RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher() {
        return getContext().mRefWatcher;
    }

    private void initRetrofit() {
        RetrofitInit.init(this)
                .withApiHost(Constant.ROOT_URL)
                .configure();
    }

    private void initActiveAndroid(){
        //ActiveAndroid
        Configuration.Builder builder = new Configuration.Builder(this);
        //手动的添加模型类
        builder.addModelClass(UserEntry.class);
        builder.addModelClass(FriendEntry.class);
        builder.addModelClass(FriendRecommendEntry.class);
        builder.addModelClass(ConversationEntry.class);
        ActiveAndroid.initialize(builder.create());
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, BaseConfig.DATABASE_NAME);
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        mDaoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession(){
        return mDaoSession;
    }

    public static UserEntry getUserEntry() {
        return UserEntry.getUser(JMessageClient.getMyInfo().getUserName(), JMessageClient.getMyInfo().getAppKey());
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return mActivityInjector;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
