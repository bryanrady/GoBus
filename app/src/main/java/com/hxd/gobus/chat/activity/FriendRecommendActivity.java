package com.hxd.gobus.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.hxd.gobus.BusApp;
import com.hxd.gobus.R;
import com.hxd.gobus.chat.adapter.FriendRecommendAdapter;
import com.hxd.gobus.chat.database.FriendRecommendEntry;
import com.hxd.gobus.chat.database.UserEntry;
import com.hxd.gobus.chat.entity.FriendInvitation;
import com.hxd.gobus.config.BaseConfig;

import java.util.List;

/**
 * Created by ${chenyn} on 2017/3/17.
 *
 * 通讯录界面.验证消息
 */

public class FriendRecommendActivity extends com.hxd.gobus.chat.activity.JBaseActivity {

    private ListView mListView;
    private FriendRecommendAdapter mAdapter;
    private List<FriendRecommendEntry> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_recommend);

        initView();

        UserEntry user = BusApp.getUserEntry();
        if (null != user) {
            mList = user.getRecommends();
            mAdapter = new FriendRecommendAdapter(this, mList, mDensity, mWidth);
            mListView.setAdapter(mAdapter);
        } else {
            Log.e("FriendRecommendActivity", "Unexpected error: User table null");
        }
    }

    private void initView() {
        initTitle(true, true, "新的朋友", "", false, "");
        mListView = (ListView) findViewById(R.id.friend_recommend_list_view);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case BaseConfig.RESULT_BUTTON:
                int position = data.getIntExtra("position", -1);
                int btnState = data.getIntExtra("btn_state", -1);
                FriendRecommendEntry entry = mList.get(position);
                if (btnState == 2) {
                    entry.state = FriendInvitation.ACCEPTED.getValue();
                    entry.save();
                }else if (btnState == 1) {
                    entry.state = FriendInvitation.REFUSED.getValue();
                    entry.save();
                }
                break;
            default:
                break;
        }
    }

    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }
}
