package com.hxd.gobus.mvp.presenter;

import android.text.TextUtils;

import com.hxd.gobus.R;
import com.hxd.gobus.bean.ApkInfo;
import com.hxd.gobus.bean.User;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.ActivityScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IMainContract;
import com.hxd.gobus.mvp.contract.IModifyPasswordContract;
import com.hxd.gobus.mvp.model.MainModel;
import com.hxd.gobus.mvp.model.ModifyPasswordModel;
import com.hxd.gobus.utils.AppUtil;
import com.hxd.gobus.utils.GsonUtils;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * @author: wangqingbin
 * @date: 2019/7/16 16:38
 */
@ActivityScope
public class ModifyPasswordPresenter extends BasePresenter<IModifyPasswordContract.View,ModifyPasswordModel> {

    @Inject
    public ModifyPasswordPresenter() {}

    public void commitNewPassword(String oldPwd,String newPwd,String confirmPwd){
        if (!TextUtils.isEmpty(oldPwd)){
            if (!TextUtils.isEmpty(newPwd) && newPwd.length()>=6 && newPwd.length()<=18){
                if (newPwd.equals(confirmPwd)){
                    if (!newPwd.equals(oldPwd)){
                        commitRequest(oldPwd,newPwd);
                    }else {
                        getView().showToast("新密码不能和原密码一样^_^");
                    }
                }else {
                    getView().showToast("两次输入密码不一致,请重新输入^_^");
                }
            }else {
                getView().showToast("请输入6~18位的字符密码^_^");
            }
        }else {
            getView().showToast("旧密码不能为空^_^");
        }
    }

    private void commitRequest(String oldPwd,String newPwd){
        getView().showLoading();
        getModel().modifyPassword(oldPwd,newPwd).subscribe(new ObserverImpl() {
            @Override
            protected void handleResponse(String response) {
                if (!TextUtils.isEmpty(response)){
                    getModel().clearUserInfo();
                    getModel().setNotLogin();

                    getView().showToast(response);
                    getView().commitNewPasswordSuccess();
                }
            }

            @Override
            protected void handleError(String errorMsg) {
                getView().showToast(errorMsg);
            }

            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onComplete() {
                getView().dismissLoading();
            }
        });
    }

}
