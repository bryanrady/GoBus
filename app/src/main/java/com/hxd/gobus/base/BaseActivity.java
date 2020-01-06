package com.hxd.gobus.base;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bryanrady.lib_permission.annotation.NeedPermission;
import com.hxd.gobus.BuildConfig;
import com.hxd.gobus.BusApp;
import com.hxd.gobus.R;
import com.hxd.gobus.bean.event.StringEvent;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.core.ioc.IocInjectUtils;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.IBaseView;
import com.hxd.gobus.mvp.activity.LoginActivity;
import com.hxd.gobus.mvp.activity.MainActivity;
import com.hxd.gobus.utils.AppManager;
import com.hxd.gobus.utils.FileHelper;
import com.hxd.gobus.utils.LeakedUtils;
import com.hxd.gobus.utils.LogUtils;
import com.hxd.gobus.utils.SharePreferenceManager;
import com.hxd.gobus.utils.StatusBarUtils;
import com.hxd.gobus.views.ButtonDialog;
import com.hxd.gobus.views.CommonDialog;
import com.hxd.gobus.views.SweetAlert.SweetAlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import dagger.android.AndroidInjection;

/**
 * @author: wangqingbin
 * @date: 2019/7/11 20:32
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements IBaseView {

    private static final String TAG = BaseActivity.class.getName();
    private File mApkFile;
    private static final int UNKNOW_APP_SOURCE_CODE = 10086;

    @Inject
    @Nullable
    protected P mPresenter; // 如果用不到Presenter,在Activity中直接不要泛型即可
    protected Context mContext;
    private Unbinder mUnbinder;
    private SweetAlertDialog mSweetAlertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        mContext = this;

        //设置所有的Activity竖屏显示   这个ActivityInfo是Activity的存档信息 插件化里面有详细分析
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppManager.getInstance().addActivity(this);

        setContentView(bindLayout());

        mUnbinder = ButterKnife.bind(this);

        initLoadingDialog();
        initView();
        doBusiness();

        StatusBarUtils.setToolBarStyle(this, true, getResources().getColor(R.color.colorPrimary));

        //IocInjectUtils.injectNetwork(this,this);
        JMessageClient.registerEventReceiver(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LeakedUtils.fixLeak(this);
        AppManager.getInstance().finishActivity(this);
        mUnbinder.unbind();
        JMessageClient.unRegisterEventReceiver(this);
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
        if (mSweetAlertDialog != null){
            mSweetAlertDialog.dismiss();
        }
    //    BusApp.getRefWatcher().watch(this);
    }

    protected abstract int bindLayout();

    protected abstract void initView();

    protected abstract void doBusiness();

    private void initLoadingDialog(){
        mSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mSweetAlertDialog.setTitleText("数据加载中,请稍后...");
        mSweetAlertDialog.setCancelable(false);
    }

    @Override
    public void showLoading() {
        mSweetAlertDialog.show();
    }

    @Override
    public void dismissLoading() {
        mSweetAlertDialog.dismiss();
    }

    @Override
    public void showToast(String toast) {
        showShortToast(toast);
    }

    protected void defaultFinish() {
        super.finish();
    }

    protected void showShortToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    protected void showShortToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
    }

    protected void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else{
            LogUtils.e(TAG, "there is no activity can handle this intent: "+intent.getAction().toString());
        }
    }

    protected void startActivity(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else{
            LogUtils.e(TAG, "there is no activity can handle this intent: "+intent.getAction().toString());
        }
    }

    protected void startActivity(String action,Uri data) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.setData(data);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else{
            LogUtils.e(TAG, "there is no activity can handle this intent: "+intent.getAction().toString());
        }
    }

    protected void startActivity(String action, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else{
            LogUtils.e(TAG, "there is no activity can handle this intent: "+intent.getAction().toString());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receive(StringEvent event){
        showConversationTimeoutDialog(event.getMessage());
    }

    private void showConversationTimeoutDialog(String msg) {
        CommonDialog commonDialog = new CommonDialog(mContext, R.style.dialog, msg, new CommonDialog.OnCloseListener() {
            @Override
            public void
            onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    startActivity(LoginActivity.class);
                }
                dialog.dismiss();
            }
        });
        commonDialog.setTitle("提 示");
        commonDialog.setPositiveButton("确 定");
        commonDialog.setNegativeButton("取 消");
        commonDialog.setNegativeButtonVisible(false);
        commonDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveApkFile(File apkFile){
        this.mApkFile = apkFile;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先获取是否有安装未知来源应用的权限
            boolean haveInstallPermission = getPackageManager().canRequestPackageInstalls();
            //如果没有权限
            if (!haveInstallPermission) {
                startInstallPermissionSettingActivity();
            }else{
                installApk();
            }
        }else{
            installApk();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        Uri packageURI = Uri.parse("package:" + getPackageName());
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        startActivityForResult(intent, UNKNOW_APP_SOURCE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UNKNOW_APP_SOURCE_CODE && resultCode == RESULT_OK) {
            installApk();
        }
    }

    @NeedPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private void installApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri contentUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            contentUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID +".provider", mApkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            contentUri = Uri.fromFile(mApkFile);
        }
        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onEventMainThread(LoginStateChangeEvent event) {
        LoginStateChangeEvent.Reason reason = event.getReason();
        UserInfo myInfo = event.getMyInfo();
        if (myInfo != null) {
            String path;
            File avatar = myInfo.getAvatarFile();
            if (avatar != null && avatar.exists()) {
                path = avatar.getAbsolutePath();
            } else {
                path = FileHelper.getUserAvatarPath(myInfo.getUserName());
            }
            SharePreferenceManager.setCachedUsername(myInfo.getUserName());
            SharePreferenceManager.setCachedAvatarPath(path);
            JMessageClient.logout();
        }
        switch (reason) {
            case user_logout:
                CommonDialog commonDialog = new CommonDialog(mContext, R.style.dialog, "您的账号已经在其他设备上登陆", new CommonDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            JMessageClient.login(SharePreferenceManager.getCachedUsername(), SharePreferenceManager.getCachedPsw(),
                                    new BasicCallback() {
                                        @Override
                                        public void gotResult(int responseCode, String responseMessage) {
                                            if (responseCode == 0) {
                                                startActivity(MainActivity.class);
                                            }
                                        }
                                    });
                        } else {
                            JMessageClient.logout();
                            startActivity(LoginActivity.class);
                        }
                        dialog.dismiss();
                    }
                });
                commonDialog.setTitle("提 示");
                commonDialog.setPositiveButton("重新登录");
                commonDialog.setNegativeButton("退 出");
                commonDialog.setNegativeButtonVisible(true);
                commonDialog.show();
                break;
            case user_password_change:
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

}
