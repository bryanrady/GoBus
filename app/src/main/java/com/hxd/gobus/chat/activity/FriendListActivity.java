package com.hxd.gobus.chat.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.chat.entity.Event;
import com.hxd.gobus.chat.entity.EventType;
import com.hxd.gobus.chat.utils.DialogCreator;
import com.hxd.gobus.chat.utils.HandleResponseCode;
import com.hxd.gobus.chat.utils.SharePreferenceManager;
import com.hxd.gobus.chat.utils.ToastUtil;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.mvp.adapter.ContactsAdapter;
import com.hxd.gobus.utils.ContactsSingleton;
import com.hxd.gobus.utils.PinyinUtil;
import com.hxd.gobus.utils.SearchOperation;
import com.hxd.gobus.utils.SqliteUtils;
import com.hxd.gobus.utils.ToastUtils;
import com.hxd.gobus.views.RecycleViewDivider;
import com.hxd.gobus.views.SearchBox;
import com.hxd.gobus.views.SideBar;
import com.hxd.gobus.views.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.options.MessageSendingOptions;
import cn.jpush.im.android.eventbus.EventBus;
import cn.jpush.im.api.BasicCallback;

/**
 * 发送名片
 * Created by ${chenyn} on 2017/9/21.
 */

public class FriendListActivity extends BaseActivity implements ContactsAdapter.OnItemClickListener, com.hxd.gobus.views.SideBar.OnSideBarListener,View.OnClickListener{
    private Context mContext;
    private Toolbar mToolbar;
    private LinearLayout llBack;//返回
    private TextView tvToolbarTitle;//标题
    private TextView tvRightTitle;
    private SearchBox mSearchBox;
    private TextView tvLetterTip;//字母提示
    private RecyclerView rvContacts;
    private SideBar mSideBar;
    private List<Contact> mList;
    private ContactsAdapter mAdapter;
    private PinyinUtil pinyinUtil;
    private HashMap<Integer,Boolean> isSelected;
    private ArrayList<Contact> groupList;
    private SearchOperation mSearchOperation;

    private SweetAlertDialog sDialog;

    private Dialog mDialog;

    protected int mWidth;
    protected int mHeight;
    protected float mDensity;
    protected int mDensityDpi;

    private String mTargetAppKey;

    @Override
    public int bindLayout() {
        return R.layout.aty_contacts;
    }

    @Override
    public void initView() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mDensity = dm.density;
        mDensityDpi = dm.densityDpi;
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;

        mToolbar = findViewById(R.id.toolbar);
        llBack = findViewById(R.id.ll_toolbar_back);
        tvToolbarTitle = findViewById(R.id.tv_toolbar_center_title);
        tvRightTitle = findViewById(R.id.tv_toolbar_right_title);
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("发送名片");
        mToolbar.setTitle("");// 标题的文字需在setSupportActionBar之前，不然会无效
        setSupportActionBar(mToolbar);
        llBack.setOnClickListener(this);
        tvRightTitle.setOnClickListener(this);
        mSearchBox = findViewById(R.id.searchBox_contacts);
        rvContacts = findViewById(R.id.rv_contacts);
        tvLetterTip = findViewById(R.id.tv_contacts_letter_tip);
        mSideBar = findViewById(R.id.sidebar_contacts);
        mSideBar.setTextView(tvLetterTip);
        mSideBar.setOnSideBarListener(this);
    }

    @Override
    public void doBusiness() {

        mTargetAppKey = getIntent().getStringExtra(BaseConfig.TARGET_APP_KEY);

        groupList = new ArrayList<>();
        mList = new ArrayList<>();
        mAdapter = new ContactsAdapter(mList,mContext);
        rvContacts.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        pinyinUtil = new PinyinUtil();
        //设置RecycleView的布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvContacts.setLayoutManager(layoutManager);
        //设置RecycleView的Item间的分割线
        rvContacts.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, R.drawable.recycleview_divider));
        // 设置RecycleView的动画ItemAnimator
        rvContacts.setItemAnimator(new DefaultItemAnimator());


        ContactsSingleton.getInstance().setOnContactsListListener(new ContactsSingleton.OnContactsListListener() {
            @Override
            public void onContactsList(List<Contact> list) {
                mList = list;
                mAdapter.updateData(mList);
                isSelected = new HashMap<>();
                for (int i = 0; i < mList.size(); i++) {
                    isSelected.put(mList.get(i).getPersonId(), false);
                }
                mAdapter.setIsSelected(isSelected);
            }

            @Override
            public void onListError(String error) {
                ToastUtils.showShortToast(mContext,error);
            }
        });
        ContactsSingleton.getInstance().startSyncQuery(mContext);

        mSearchOperation = new SearchOperation(Looper.myLooper());
        mSearchOperation.setCanSearchListener(new SearchOperation.ICanSearchListener() {
            @Override
            public void canSearch(String keyword) {
                startSearch(keyword);
            }
        });

        //根据输入框输入值的改变来过滤搜索
        mSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String filterStr = s.toString().trim();
                if(TextUtils.isEmpty(filterStr)){
                    filterData(filterStr);
                }else{
                    mSearchOperation.optionSearch(filterStr);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void startSearch(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            return;
        }
        //我们可以把每次输入框改变的字符串传给一个工具类，并让它来判断是否进行搜索
        filterData(mSearchBox.getText().toString().trim());
        mSearchBox.clearFocus();
    }

    private void filterData(String filterStr) {
        if(mList != null){
            List<Contact> filterDateList = new ArrayList<>();
            if (TextUtils.isEmpty(filterStr)) {
                filterDateList = mList;
            } else {
                filterDateList.clear();
                for (Contact sortModel : mList) {
                    String name = sortModel.getName();
                    String dept = sortModel.getUnitName();
                    String namePinyin = sortModel.getNamePinyin();
                    String deptPinyin = sortModel.getDeptPinyin();
                    if(name != null && dept != null){
                        if(namePinyin != null && deptPinyin != null){
                            if (name.indexOf(filterStr.toString()) != -1 || namePinyin.startsWith(filterStr.toString())
                                    || dept.indexOf(filterStr.toString()) != -1 || deptPinyin.startsWith(filterStr.toString())) {
                                filterDateList.add(sortModel);
                            }
                        }else{
                            if (name.indexOf(filterStr.toString()) != -1 || dept.indexOf(filterStr.toString()) != -1) {
                                filterDateList.add(sortModel);
                            }
                        }
                    }
                }
            }
            Collections.sort(filterDateList, pinyinUtil); // 根据a-z进行排序
            mAdapter.updateData(filterDateList);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Contact contacts = mAdapter.getItem(position);
        if (getIntent().getBooleanExtra("isSingle", false)) {
            setBusinessCard(contacts, JMessageClient.getSingleConversation(getIntent().getStringExtra("userId")));
        } else {
            setBusinessCard(contacts, JMessageClient.getGroupConversation(getIntent().getLongExtra("groupId", 0)));
        }
    }

    private void setBusinessCard(final Contact contacts, final Conversation conversation) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_cancel:
                        mDialog.dismiss();
                        break;
                    case R.id.btn_sure:
                        mDialog.dismiss();
                        //把名片的userName和appKey通过extra发送给对方
                        TextContent content = new TextContent("推荐了一张名片");
                        content.setStringExtra("userName", contacts.getWorkNo());
                        content.setStringExtra("appKey", mTargetAppKey);
                        content.setStringExtra("businessCard", "businessCard");

                        Message textMessage = conversation.createSendMessage(content);
                        MessageSendingOptions options = new MessageSendingOptions();
                        options.setNeedReadReceipt(true);
                        JMessageClient.sendMessage(textMessage, options);
                        textMessage.setOnSendCompleteCallback(new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    SharePreferenceManager.setIsOpen(true);
                                    Toast.makeText(FriendListActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                    defaultFinish();
                                } else {
                                    HandleResponseCode.onHandle(FriendListActivity.this, i, false);
                                }
                            }
                        });
                        break;
                }
            }
        };

        String title = null;
        if (getIntent().getBooleanExtra("isSingle", false)) {
            title = SqliteUtils.getNameByWorkNo(mContext,conversation.getTitle());
        } else {
            title = conversation.getTitle();
//            if(conversation.getTitle().contains(",")){
//                StringBuffer buffer = new StringBuffer();
//                String[] split = conversation.getTitle().split(",");
//                for(int i=0;i<split.length;i++){
//                    title = buffer.append(SqliteUtils.getNameByWorkNo(mContext,split[i])+",").toString();
//                }
//                title = title.substring(0,title.length()-1);
//            }
        }

        mDialog = DialogCreator.createBusinessCardDialog(FriendListActivity.this, listener,title,
                contacts.getName(), contacts.getPhotoUrl());
        mDialog.getWindow().setLayout((int) (0.8 * mWidth), WindowManager.LayoutParams.WRAP_CONTENT);
        mDialog.show();
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onLetterChanged(String letter) {
        int position = mAdapter.getPositionForSection(letter.charAt(0));
        if (position != -1) {
            rvContacts.scrollToPosition(position);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.tv_toolbar_right_title:
                final ArrayList<String> selectedUser = new ArrayList<>();
                for(Contact contacts : groupList){
                    selectedUser.add(contacts.getWorkNo());
                }
                JMessageClient.createGroup("", "", new CreateGroupCallback() {
                    @Override
                    public void gotResult(int responseCode, String responseMsg, final long groupId) {
                        if (responseCode == 0) {
                            if (selectedUser.size() > 0) {
                                JMessageClient.addGroupMembers(groupId, selectedUser, new BasicCallback() {
                                    @Override
                                    public void gotResult(int responseCode, String responseMessage) {
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
                                //如果创建群组时候没有选择人,那么size就是1
                                createGroup(groupId, 1);
                            }
                        } else {
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
}