package com.hxd.gobus.chat.activity;

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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.bean.User;
import com.hxd.gobus.chat.entity.Event;
import com.hxd.gobus.chat.entity.EventType;
import com.hxd.gobus.chat.utils.ToastUtil;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.mvp.activity.PersonalDataActivity;
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
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.eventbus.EventBus;
import cn.jpush.im.api.BasicCallback;

/**
 * 发起群聊
 * Created by wangqingbin on 2018/5/29.
 */

public class CreateGroup2Activity extends BaseActivity implements ContactsAdapter.OnItemClickListener,
        SideBar.OnSideBarListener,View.OnClickListener{
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
    private String tag;
    private long add_groupId;
    private HashMap<Integer,Boolean> isSelected;
    private ArrayList<Contact> groupList;

    private SearchOperation mSearchOperation;
    private SweetAlertDialog sDialog;

    @Override
    public int bindLayout() {
        return R.layout.aty_contacts;
    }

    @Override
    public void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        llBack = (LinearLayout) findViewById(R.id.ll_toolbar_back);
        tvToolbarTitle = (TextView) findViewById(R.id.tv_toolbar_center_title);
        tvRightTitle = (TextView) findViewById(R.id.tv_toolbar_right_title);
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("通讯录");
        mToolbar.setTitle("");// 标题的文字需在setSupportActionBar之前，不然会无效
        setSupportActionBar(mToolbar);
        llBack.setOnClickListener(this);
        tvRightTitle.setOnClickListener(this);
        mSearchBox = (SearchBox) findViewById(R.id.searchBox_contacts);
        rvContacts = (RecyclerView) findViewById(R.id.rv_contacts);
        tvLetterTip = (TextView) findViewById(R.id.tv_contacts_letter_tip);
        mSideBar = (SideBar) findViewById(R.id.sidebar_contacts);
        mSideBar.setTextView(tvLetterTip);
        mSideBar.setOnSideBarListener(this);
    }

    private GroupInfo mGroupInfo;
    private List<UserInfo> mMemberInfoList;
    private String mGroupName;
    private String mGroupDesc;
    private String groupOwnerId;

    @Override
    public void doBusiness() {
        Intent intent = getIntent();
        tag = intent.getStringExtra("tag");
        if(tag.equals("add_group")){
            add_groupId = intent.getLongExtra("add_friend_group_id",0);
            if(add_groupId != 0){
                Conversation conv = JMessageClient.getGroupConversation(add_groupId);
                mGroupInfo = (GroupInfo) conv.getTargetInfo();
                mMemberInfoList = mGroupInfo.getGroupMembers();
                groupOwnerId = mGroupInfo.getGroupOwner();
                mGroupName = mGroupInfo.getGroupName();
                mGroupDesc = mGroupInfo.getGroupDescription();
            }
        }
        groupList = new ArrayList<>();
        mList = new ArrayList<>();
        mAdapter = new ContactsAdapter(mList,getApplicationContext());
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
        if (tag.equals("single")){
            Intent intent = new Intent();
            intent.setClass(mContext, PersonalDataActivity.class);
            intent.putExtra("TAG", "single");
            intent.putExtra("contacts", contacts);
            intent.putExtra(BaseConfig.TARGET_ID, contacts.getWorkNo());
            startActivity(intent);
            defaultFinish();
        }else if (tag.equals("group")){
            if(isSelected.get(contacts.getPersonId())){   //如果之前是选中的，点击就要取消
                isSelected.put(contacts.getPersonId(),false);
                mAdapter.setIsSelected(isSelected);
                groupList.remove(contacts);
            }else{
                isSelected.put(contacts.getPersonId(),true);
                mAdapter.setIsSelected(isSelected);
                groupList.add(contacts);    //选中的参加群的
            }
            if(groupList.size()>0){
                tvRightTitle.setVisibility(View.VISIBLE);
                tvRightTitle.setText("确定("+groupList.size()+")");
            }else{
                tvRightTitle.setVisibility(View.GONE);
                tvRightTitle.setText("");
            }
            mAdapter.notifyDataSetChanged();
        }else if(tag.equals("add_group")){  //添加组员      默认和之前添加的方式一样
            if(isSelected.get(contacts.getPersonId())){   //如果之前是选中的，点击就要取消
                isSelected.put(contacts.getPersonId(),false);
                mAdapter.setIsSelected(isSelected);
                groupList.remove(contacts);
            }else{
                isSelected.put(contacts.getPersonId(),true);
                mAdapter.setIsSelected(isSelected);
                groupList.add(contacts);    //选中的参加群的
            }
            if(groupList.size()>0){
                tvRightTitle.setVisibility(View.VISIBLE);
                tvRightTitle.setText("确定("+groupList.size()+")");
            }else{
                tvRightTitle.setVisibility(View.GONE);
                tvRightTitle.setText("");
            }
            mAdapter.notifyDataSetChanged();
        }
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
                if(tag.equals("add_group")){    //如果是添加操作
                    if(mMemberInfoList != null){
                        StringBuffer stringBuffer = new StringBuffer();
                        String nameByWorkNo = null;
                        for(UserInfo userInfo : mMemberInfoList){
                            for(Contact contacts : groupList){
                                if(userInfo.getUserName().equals(contacts.getWorkNo())){
                                    nameByWorkNo = stringBuffer.append(SqliteUtils.getNameByWorkNo(mContext, contacts.getWorkNo())+",").toString();
                                }else{
                                    selectedUser.add(contacts.getWorkNo());
                                }
                            }
                        }
                        if(!TextUtils.isEmpty(nameByWorkNo)){
                            showShortToast(nameByWorkNo.substring(0,nameByWorkNo.length()-1)+"已经在群里!");
                        }

                    }else{
                        for(Contact contacts : groupList){
                            selectedUser.add(contacts.getWorkNo());
                        }
                    }
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("SelectedUser", selectedUser);
                    setResult(BaseConfig.RESULT_CODE_SELECT_FRIEND, intent);
                    finish();
                }else{  //创建群添加人时候的操作
                    StringBuffer stringBuffer = new StringBuffer();
                    for(Contact contacts : groupList){
                        if(!contacts.getWorkNo().equals(User.getInstance().getWorkNo())){
                            selectedUser.add(contacts.getWorkNo());
                            stringBuffer.append(contacts.getName()+",");
                        }
                    }
                    String groupName = stringBuffer.substring(0,stringBuffer.length()-1);
                    //防止名字太长无法创建群组
                    if(groupName.length()>25){
                        groupName = groupName.substring(0,25);
                    }
                    JMessageClient.createGroup(groupName, "", new CreateGroupCallback() {
                        @Override
                        public void gotResult(int responseCode, String responseMsg, final long groupId) {
                            if (responseCode == 0) {
                                if (selectedUser.size() > 0) {
                                    JMessageClient.addGroupMembers(groupId, selectedUser, new BasicCallback() {
                                        @Override
                                        public void gotResult(int responseCode, String responseMessage) {
                                            if (responseCode == 0) {
                                                //在选人的时候直接把自己过滤掉了
                                                //如果创建群组时添加了人,那么就在size基础上加上自己
                                                createGroup(groupId, selectedUser.size() + 1);
                                            } else if (responseCode == 810007) {
                                            //    ToastUtil.shortToast(mContext, "不能添加自己");
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
                }
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
