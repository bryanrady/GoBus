package com.hxd.gobus.chat.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxd.gobus.BusApp;
import com.hxd.gobus.R;
import com.hxd.gobus.chat.adapter.CreateGroupAdapter;
import com.hxd.gobus.chat.adapter.StickyListAdapter;
import com.hxd.gobus.chat.database.FriendEntry;
import com.hxd.gobus.chat.database.UserEntry;
import com.hxd.gobus.chat.entity.Event;
import com.hxd.gobus.chat.entity.EventType;
import com.hxd.gobus.chat.utils.DialogCreator;
import com.hxd.gobus.chat.utils.ToastUtil;
import com.hxd.gobus.chat.utils.keyboard.utils.EmoticonsKeyboardUtils;
import com.hxd.gobus.chat.utils.pinyin.PinyinComparator;
import com.hxd.gobus.chat.utils.sidebar.SideBar;
import com.hxd.gobus.chat.view.listview.StickyListHeadersListView;
import com.hxd.gobus.config.BaseConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.eventbus.EventBus;
import cn.jpush.im.api.BasicCallback;


/**
 * Created by ${chenyn} on 2017/5/3.
 */

public class CreateGroupActivity extends com.hxd.gobus.chat.activity.JBaseActivity implements View.OnClickListener, TextWatcher {

    private ImageButton mCancelBtn;
    private EditText mSearchEt;
    private StickyListHeadersListView mListView;
    private SideBar mSideBar;
    private TextView mLetterHintTv;
    private LinearLayout mFinishBtn;
    private StickyListAdapter mAdapter;
    private List<FriendEntry> mData;
    private HorizontalScrollView scrollViewSelected;
    private GridView imageSelectedGridView;
    private CreateGroupAdapter mGroupAdapter;
    private Context mContext;
    private Dialog mLoadingDialog;
    private FriendEntry mFriendEntry;
    private TextView mTv_noFriend;
    private TextView mTv_noFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_create_group);
        initView();
        initData();
    }

    private void initData() {
        UserEntry userEntry = BusApp.getUserEntry();
        mData = userEntry.getFriends();
        Collections.sort(mData, new PinyinComparator());
        if (mData.size() > 0) {
            mTv_noFriend.setVisibility(View.GONE);
        } else {
            mTv_noFriend.setVisibility(View.VISIBLE);
        }
        mGroupAdapter = new CreateGroupAdapter(CreateGroupActivity.this);
        imageSelectedGridView.setAdapter(mGroupAdapter);
        mAdapter = new StickyListAdapter(CreateGroupActivity.this, mData, true, scrollViewSelected, imageSelectedGridView, mGroupAdapter);
        mListView.setAdapter(mAdapter);

    }

    private void initView() {
        mTv_noFriend = (TextView) findViewById(R.id.tv_noFriend);
        mTv_noFilter = (TextView) findViewById(R.id.tv_noFilter);
        mCancelBtn = (ImageButton) findViewById(R.id.jmui_cancel_btn);
        mFinishBtn = (LinearLayout) findViewById(R.id.finish_btn);
        mSearchEt = (EditText) findViewById(R.id.search_et);
        mListView = (StickyListHeadersListView) findViewById(R.id.sticky_list_view);
        mSideBar = (SideBar) findViewById(R.id.sidebar);
        mLetterHintTv = (TextView) findViewById(R.id.letter_hint_tv);
        mSideBar.setTextView(mLetterHintTv);
        scrollViewSelected = (HorizontalScrollView) findViewById(R.id.contact_select_area);
        imageSelectedGridView = (GridView) findViewById(R.id.contact_select_area_grid);

        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = mAdapter.getSectionForLetter(s);
                if (position != -1 && position < mAdapter.getCount()) {
                    mListView.setSelection(position - 1);
                }
            }
        });

        mSearchEt.addTextChangedListener(this);

        mListView.setDrawingListUnderStickyHeader(true);
        mListView.setAreHeadersSticky(true);
        mListView.setStickyHeaderTopOffset(0);
        mFinishBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (forDelete != null && forDelete.size() > 0) {
            for (FriendEntry e : forDelete) {
                e.delete();
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jmui_cancel_btn:
                EmoticonsKeyboardUtils.closeSoftKeyboard(this);
                finish();
                break;
            case R.id.finish_btn:
                //拿到所选择的userName
                final ArrayList<String> selectedUser = mAdapter.getSelectedUser();
                mLoadingDialog = DialogCreator.createLoadingDialog(mContext,
                        mContext.getString(R.string.creating_hint));
                mLoadingDialog.show();
                JMessageClient.createGroup("", "", new CreateGroupCallback() {
                    @Override
                    public void gotResult(int responseCode, String responseMsg, final long groupId) {
                        if (responseCode == 0) {
                            if (selectedUser.size() > 0) {
                                JMessageClient.addGroupMembers(groupId, selectedUser, new BasicCallback() {
                                    @Override
                                    public void gotResult(int responseCode, String responseMessage) {
                                        mLoadingDialog.dismiss();
                                        if (responseCode == 0) {
                                            //如果创建群组时添加了人,那么就在size基础上加上自己
                                            createGroup(groupId, selectedUser.size() + 1);
                                        } else if (responseCode == 810007) {
                                            ToastUtil.shortToast(mContext, "不能添加自己");
                                        } else {
                                            ToastUtil.shortToast(mContext, "添加失败");
                                        }
                                    }
                                });
                            } else {
                                mLoadingDialog.dismiss();
                                //如果创建群组时候没有选择人,那么size就是1
                                createGroup(groupId, 1);
                            }
                        } else {
                            mLoadingDialog.dismiss();
                            ToastUtil.shortToast(mContext, responseMsg);
                        }
                    }
                });
                break;
        }
    }

    private void createGroup(long groupId, int groupMembersSize) {
        Conversation groupConversation = JMessageClient.getGroupConversation(groupId);
        if (groupConversation == null) {
            groupConversation = Conversation.createGroupConversation(groupId);
            EventBus.getDefault().post(new Event.Builder()
                    .setType(EventType.createConversation)
                    .setConversation(groupConversation)
                    .build());
        }

        Intent intent = new Intent();
        //设置跳转标志
        intent.putExtra("fromGroup", true);
        intent.putExtra(BaseConfig.CONV_TITLE, groupConversation.getTitle());
        intent.putExtra(BaseConfig.MEMBERS_COUNT, groupMembersSize);
        intent.putExtra(BaseConfig.GROUP_ID, groupId);
        intent.setClass(mContext, ChatActivity.class);
        mContext.startActivity(intent);
        finish();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        filterData(s.toString());
    }

    List<FriendEntry> filterDateList;

    private void filterData(final String filterStr) {
        filterDateList = new ArrayList<>();
        if (!TextUtils.isEmpty(filterStr)) {
            filterDateList.clear();
            //遍历好友集合进行匹配
            for (FriendEntry entry : mData) {
                String appKey = entry.appKey;

                String userName = entry.username;
                String noteName = entry.noteName;
                String nickName = entry.nickName;
                if (!userName.equals(filterStr) && userName.contains(filterStr) ||
                        !userName.equals(filterStr) && noteName.contains(filterStr) ||
                        !userName.equals(filterStr) && nickName.contains(filterStr) &&
                                appKey.equals(JMessageClient.getMyInfo().getAppKey())) {
                    filterDateList.add(entry);
                }
            }
        } else {
            if (mFriendEntry != null) {
                mFriendEntry.delete();
            }
            filterDateList = mData;
        }
        if (filterDateList.size() > 0) {
            mTv_noFilter.setVisibility(View.GONE);
        }
        // 根据a-z进行排序
        Collections.sort(filterDateList, new PinyinComparator());
        mAdapter.updateListView(filterDateList, true, filterStr);

        //当搜索的人不是好友时全局搜索
        //这个不能放在for中的else中,否则for循环了多少次就会添加多少次搜出来的user
        final UserEntry user = UserEntry.getUser(JMessageClient.getMyInfo().getUserName(),
                JMessageClient.getMyInfo().getAppKey());
        final List<FriendEntry> finalFilterDateList = filterDateList;
        JMessageClient.getUserInfo(filterStr, new GetUserInfoCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage, UserInfo info) {
                if (responseCode == 0) {
                    mFriendEntry = new FriendEntry(info.getUserID(), info.getUserName(), info.getNotename(), info.getNickname(), info.getAppKey(), info.getAvatar(),
                            info.getUserName(), filterStr.substring(0, 1).toUpperCase(), user);
                    mFriendEntry.save();
                    forDelete.add(mFriendEntry);
                    finalFilterDateList.add(mFriendEntry);
                    Collections.sort(finalFilterDateList, new PinyinComparator());
                    if (finalFilterDateList.size() > 0) {
                        mTv_noFilter.setVisibility(View.GONE);
                    }
                    mAdapter.updateListView(finalFilterDateList, true, filterStr);
                } else {
                    if (filterDateList.size() > 0) {
                        mTv_noFilter.setVisibility(View.GONE);
                    } else {
                        mTv_noFilter.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    List<FriendEntry> forDelete = new ArrayList<>();

    @Override
    public void afterTextChanged(Editable s) {

    }
}
