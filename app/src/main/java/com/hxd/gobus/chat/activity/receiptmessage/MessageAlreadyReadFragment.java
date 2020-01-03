package com.hxd.gobus.chat.activity.receiptmessage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hxd.gobus.R;
import com.hxd.gobus.chat.activity.fragment.BaseFragment;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.mvp.activity.PersonalDataActivity;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by ${chenyn} on 2017/9/5.
 */

public class MessageAlreadyReadFragment extends BaseFragment {
    private Activity mContext;
    private View mRootView;
    private ListView mReceipt_alreadyRead;
    private AlreadyReadAdapter mAdapter;
    private long mGroupId;

    @SuppressLint({"NewApi", "ValidFragment"})
    public MessageAlreadyReadFragment(long groupIdForReceipt) {
        this.mGroupId = groupIdForReceipt;
    }
    public MessageAlreadyReadFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_receipt_already_read,
                container, false);
        mReceipt_alreadyRead = (ListView) mRootView.findViewById(R.id.receipt_alreadyRead);
        mAdapter = new AlreadyReadAdapter(this);
        mReceipt_alreadyRead.setAdapter(mAdapter);
    }

    private void initListViewClick() {
        mReceipt_alreadyRead.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserInfo userInfo = (UserInfo) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.setClass(mContext, PersonalDataActivity.class);
                intent.putExtra("TAG","MessageAlreadyReadFragment");
                intent.putExtra(BaseConfig.TARGET_ID, userInfo.getUserName());
                intent.putExtra(BaseConfig.TARGET_APP_KEY, userInfo.getAppKey());
        //        intent.putExtra(BaseApplication.GROUP_ID, mGroupId);
                startActivity(intent);
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initView(inflater,container);
        initListViewClick();
        ViewGroup p = (ViewGroup) mRootView.getParent();
        if (p != null) {
            p.removeAllViewsInLayout();
        }
        return mRootView;
    }
}
