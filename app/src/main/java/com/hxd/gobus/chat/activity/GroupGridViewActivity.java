package com.hxd.gobus.chat.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hxd.gobus.R;
import com.hxd.gobus.chat.adapter.GroupGridViewAdapter;
import com.hxd.gobus.chat.utils.DialogCreator;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.mvp.activity.PersonalDataActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by ${chenyn} on 2017/5/8.
 */

public class GroupGridViewActivity extends JBaseActivity {
    private static final String TAG = "ChatDetailActivity";
    private static final int ADD_MEMBERS_TO_GRIDVIEW = 2048;

    private GridView mGroup_gridView;
    private boolean mIsCreator = false;
    private long mGroupId;
    private List<UserInfo> mMemberInfoList = new ArrayList<UserInfo>();
    private int mCurrentNum;
    private static final int ADD_FRIEND_REQUEST_CODE = 3;
    private Dialog mLoadingDialog = null;
    private final MyHandler myHandler = new MyHandler(this);
    private GroupGridViewAdapter mGridViewAdapter;
    private LinearLayout mSearch_title;

    private Toolbar mToolbar;
    private LinearLayout llBack;
    private TextView tvToolbarTitle;//标题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_gridview);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        llBack = (LinearLayout) findViewById(R.id.ll_toolbar_back);
        tvToolbarTitle = (TextView) findViewById(R.id.tv_toolbar_center_title);
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("群成员");
        mToolbar.setTitle("");// 标题的文字需在setSupportActionBar之前，不然会无效
        setSupportActionBar(mToolbar);

        mGroup_gridView = (GridView) findViewById(R.id.group_gridView);
        mSearch_title = (LinearLayout) findViewById(R.id.search_title);
        initData();
    }

    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }

    private void initData() {
        mGroupId = getIntent().getLongExtra(BaseConfig.GROUP_ID, 0);

        final Conversation conv = JMessageClient.getGroupConversation(mGroupId);
        GroupInfo groupInfo = (GroupInfo) conv.getTargetInfo();
        mMemberInfoList = groupInfo.getGroupMembers();
        String groupOwnerId = groupInfo.getGroupOwner();

        mCurrentNum = mMemberInfoList.size();
        String groupOwner = groupInfo.getGroupOwner();
        final String userName = JMessageClient.getMyInfo().getUserName();
        if (groupOwner.equals(userName)) {
            mIsCreator = true;
        }
        mGridViewAdapter = new GroupGridViewAdapter(this, mMemberInfoList, mIsCreator, mAvatarSize, groupOwnerId);
        mGroup_gridView.setAdapter(mGridViewAdapter);

        mGroup_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                if (position < mCurrentNum) {
                    UserInfo userInfo = mMemberInfoList.get(position);
                    intent.setClass(GroupGridViewActivity.this, PersonalDataActivity.class);
                    intent.putExtra("TAG", "group_gridview");
                    intent.putExtra(BaseConfig.TARGET_ID, userInfo.getUserName());
                    intent.putExtra(BaseConfig.TARGET_APP_KEY, userInfo.getAppKey());
                    startActivity(intent);
                    // 点击添加成员按钮
                } else if (position == mCurrentNum) {
                    showContacts();

                    // 是群主, 成员个数大于1并点击删除按钮
                } else if (position == mCurrentNum + 1 && mIsCreator && mCurrentNum > 1) {
                    intent.putExtra(BaseConfig.DELETE_MODE, true);
                    intent.putExtra(BaseConfig.GROUP_ID, mGroupId);
                    intent.setClass(GroupGridViewActivity.this, DeleteGroupPersonActivity.class);
                    startActivityForResult(intent, BaseConfig.REQUEST_CODE_ALL_MEMBER);
                }
            }
        });

        mSearch_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupGridViewActivity.this, com.hxd.gobus.chat.activity.SearchGroupActivity.class);
                BaseConfig.mSearchGroup = mMemberInfoList;
                startActivity(intent);
            }
        });

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void showContacts() {
        Intent intent = new Intent();
        intent.putExtra("tag", "add_group");
        //作用是已经在群组中的人默认勾选checkbox
        intent.putExtra("add_friend_group_id", mGroupId);
        intent.setClass(this, CreateGroup2Activity.class);
        startActivityForResult(intent, ADD_FRIEND_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_FRIEND_REQUEST_CODE && data != null) {
            ArrayList<String> list = data.getStringArrayListExtra("SelectedUser");
            if (null != list && list.size() != 0) {
                addMembersToGroup(list);
            }
        }
    }

    public void addMembersToGroup(ArrayList<String> users) {
        ArrayList<String> list = new ArrayList<>();
        for (String username : users) {
            if (checkIfNotContainUser(username)) {
                list.add(username);
            }
        }
        if (list.size() > 0) {
            mLoadingDialog = DialogCreator.createLoadingDialog(GroupGridViewActivity.this,
                    getString(R.string.adding_hint));
            mLoadingDialog.show();
            Message msg = myHandler.obtainMessage();
            msg.what = ADD_MEMBERS_TO_GRIDVIEW;
            msg.obj = list;
            msg.sendToTarget();
        }
    }

    private boolean checkIfNotContainUser(String targetID) {
        if (mMemberInfoList != null) {
            for (UserInfo userInfo : mMemberInfoList) {
                if (userInfo.getUserName().equals(targetID))
                    return false;
            }
            return true;
        }
        return true;
    }

    private static class MyHandler extends Handler {
        private final WeakReference<GroupGridViewActivity> mActivity;

        public MyHandler(GroupGridViewActivity controller) {
            mActivity = new WeakReference<>(controller);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            GroupGridViewActivity controller = mActivity.get();
            if (controller != null) {
                switch (msg.what) {
                    //好友模式从通讯录中添加好友
                    case ADD_MEMBERS_TO_GRIDVIEW:
                        Log.i(TAG, "Adding Group Members");
                        ArrayList<String> users = (ArrayList<String>) msg.obj;
                        controller.addMembers(users);
                        break;
                }
            }
        }
    }

    private void addMembers(ArrayList<String> users) {
        JMessageClient.addGroupMembers(mGroupId, users, new BasicCallback() {
            @Override
            public void gotResult(final int status, final String desc) {
                mLoadingDialog.dismiss();
                if (status == 0) {
                    initData();
                    mGridViewAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(GroupGridViewActivity.this, "添加失败" + desc, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
