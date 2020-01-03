package com.hxd.gobus.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hxd.gobus.BusApp;
import com.hxd.gobus.chat.entity.Event;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.IBaseView;
import com.hxd.gobus.views.SweetAlert.SweetAlertDialog;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.MessageReceiptStatusChangeEvent;
import cn.jpush.im.android.api.event.MessageRetractEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import dagger.android.support.AndroidSupportInjection;

/**
 * @author: wangqingbin
 * @date: 2019/7/15 20:20
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements IBaseView {

    @Inject
    @Nullable
    protected P mPresenter;
    protected Context mContext;
    private Unbinder mUnbinder;

    private SweetAlertDialog mSweetAlertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(bindLayout(), container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initLoadingDialog();
        initView(view);
        initData();
        JMessageClient.registerEventReceiver(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        JMessageClient.unRegisterEventReceiver(this);
//        if (mPresenter != null) {
//            mPresenter.onDestroy();
//            mPresenter = null;
//        }
//        if (mUnbinder != null){
//            mUnbinder.unbind();
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    //    BusApp.getRefWatcher().watch(this);
    }

    protected abstract int bindLayout();

    protected abstract void initView(View view);

    protected abstract void initData();

    private void initLoadingDialog(){
        mSweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
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
        if (!TextUtils.isEmpty(toast)) {
            Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 收到消息
     */
    public void onEvent(MessageEvent event) {
    }

    /**
     * 接收离线消息
     *
     * @param event 离线消息事件
     */
    public void onEvent(OfflineMessageEvent event) {

    }

    /**
     * 消息撤回
     */
    public void onEvent(MessageRetractEvent event) {

    }

    /**
     * 消息已读事件
     */
    public void onEventMainThread(MessageReceiptStatusChangeEvent event) {

    }

    /**
     * 消息漫游完成事件
     *
     * @param event 漫游完成后， 刷新会话事件
     */
    public void onEvent(ConversationRefreshEvent event) {

    }

    public void onEventMainThread(Event event) {

    }
}
