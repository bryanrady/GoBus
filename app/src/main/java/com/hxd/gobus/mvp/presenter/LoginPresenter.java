package com.hxd.gobus.mvp.presenter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hxd.gobus.BusApp;
import com.hxd.gobus.bean.User;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.ILoginContract;
import com.hxd.gobus.mvp.model.LoginModel;
import com.hxd.gobus.utils.GsonUtils;
import com.hxd.gobus.utils.NetUtil;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.lang.ref.WeakReference;
import java.util.Set;

import javax.inject.Inject;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import io.reactivex.disposables.Disposable;

/**
 * @author: wangqingbin
 * @date: 2019/7/16 16:38
 */
@ActivityScope
public class LoginPresenter extends BasePresenter<ILoginContract.View,LoginModel> {

    private static final int MSG_SET_ALIAS = 1001;
    private static final String LOGIN_JPUSH_PASSWORD = "123456";
    private static MyHandler mHandler;

    @Inject
    public LoginPresenter() {
        mHandler = new MyHandler(this);
    }

    public void showLoginInfo(){
        User user = getModel().getUser();
        boolean checkStatus = getModel().getCheckStatus();
        if(checkStatus){
            if(user != null){
                getView().showUserInfo(user);
            }
        }else{
            if(user != null){
                getView().showUsername(user);
            }
        }
    }

    public void getCheckedStatus(){
        boolean checkStatus = getModel().getCheckStatus();
        getView().isCheck(checkStatus);
    }

    public void login(final String loginName,final String loginPassword,final boolean isChecked){
        getView().showLoading();
        getModel().verifyUser(loginName,loginPassword).subscribe(new ObserverImpl() {

            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onComplete() {
            //    getView().dismissLoading();
            }

            @Override
            protected void handleResponse(String response) {
                User user = GsonUtils.jsonToObject(response, User.class);
                if(user != null){
                    MiPushClient.setAlias(BusApp.getContext(),user.getLoginName(),null);
                    mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, user.getLoginName()));

                    loginJpush(user,loginPassword,isChecked);
                }
            }

            @Override
            protected void handleError(String errorMsg) {
                getView().showToast(errorMsg);
                getView().dismissLoading();
            }
        });

    }

    private void loginJpush(final User user,final String loginPassword,final boolean isChecked) {
    //    getView().showLoading();
        JMessageClient.login(user.getLoginName(), LOGIN_JPUSH_PASSWORD, new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage) {
                if (responseCode == 0) {
                    getView().loginSuccess();

                    user.setLoginPassword(loginPassword);
                    User.setInstance(user);
                    getModel().setAlreadyLogin();
                    //设置自己不加密的密码 不用服务器返回来的加密串
                    getModel().saveCheckStatus(isChecked);
                    getModel().setCachedPsw(loginPassword);
                    getModel().saveUser(user);
                } else {
                    getView().showToast("登录即时通讯服务器失败!"+ responseMessage);
                }
                getView().dismissLoading();
            }
        });
    }

    private static class MyHandler extends Handler {

        private final WeakReference<LoginPresenter> mRefPresenter;

        public MyHandler(LoginPresenter presenter){
            this.mRefPresenter = new WeakReference<>(presenter);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mRefPresenter != null){
                LoginPresenter presenter = mRefPresenter.get();
                if (presenter == null) {
                    return;
                }
                switch (msg.what){
                    case MSG_SET_ALIAS:
                        Log.d("Alias", "Set alias in handler.");
                        JPushInterface.setAliasAndTags(BusApp.getContext(), (String) msg.obj, null, mAliasCallback);
                        break;
                }
            }
        }
    }

    private static final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
                case 0:
                    Log.d("gotResult", "Set tag and alias success");
                    break;
                case 6002:
                    Log.d("gotResult", "Failed to set alias and tags due to timeout. Try again after 60s.");
                    NetUtil util = new NetUtil(BusApp.getContext());
                    if (util.isConnected()) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.d("gotResult", "No network");
                    }
                    break;
                default:
                    Log.d("gotResult", "Failed with errorCode = " + code);
            }
        }
    };

}
