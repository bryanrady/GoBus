package com.hxd.gobus.chat.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.hxd.gobus.R;
import com.hxd.gobus.chat.controller.SendFileController;
import com.hxd.gobus.chat.view.SendFileView;

public class SendFileActivity extends JBaseActivity {

    private SendFileView mView;
    private SendFileController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_file);
        mView = (SendFileView) findViewById(R.id.send_file_view);
        mView.initModule(SendFileActivity.this);
        mController = new SendFileController(this, mView);
        mView.setOnClickListener(mController);
        mView.setOnPageChangeListener(mController);

        //设置文件选择界面viewpager能左右滑动..在 会话 通讯录 我  主界面是不能左右滑动的
        mView.setScroll(true);
    }

    public FragmentManager getSupportFragmentManger() {
        // TODO Auto-generated method stub
        return getSupportFragmentManager();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mView.setScroll(false);
    }
}
