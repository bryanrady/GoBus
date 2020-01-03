package com.hxd.gobus.chat.activity.historyfile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.hxd.gobus.R;
import com.hxd.gobus.chat.activity.JBaseActivity;
import com.hxd.gobus.chat.activity.historyfile.controller.HistoryFileController;
import com.hxd.gobus.chat.activity.historyfile.view.HistoryFileView;

/**
 * Created by ${chenyn} on 2017/8/23.
 */

public class HistoryFileActivity extends JBaseActivity {
    private HistoryFileView mView;
    private HistoryFileController mController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_file);
        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        long groupId = intent.getLongExtra("groupId", 0);
        boolean isGroup = intent.getBooleanExtra("isGroup", false);

        mView = (HistoryFileView) findViewById(R.id.history_file_view);
        mView.initModule(HistoryFileActivity.this);
        mController = new HistoryFileController(this, mView, userName, groupId, isGroup);
        mView.setOnClickListener(mController);
        mView.setOnPageChangeListener(mController);
        mView.setScroll(true);
    }

    public FragmentManager getSupportFragmentManger() {
        return getSupportFragmentManager();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.trans_finish_in);
    }
}
