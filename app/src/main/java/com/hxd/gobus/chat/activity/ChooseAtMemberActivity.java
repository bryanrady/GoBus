package com.hxd.gobus.chat.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.bean.User;
import com.hxd.gobus.chat.model.ParentLinkedHolder;
import com.hxd.gobus.chat.utils.keyboard.widget.EmoticonsEditText;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.mvp.adapter.ContactsAdapter;
import com.hxd.gobus.utils.CharacterParser;
import com.hxd.gobus.utils.PinyinUtil;
import com.hxd.gobus.utils.SqliteUtils;
import com.hxd.gobus.views.RecycleViewDivider;
import com.hxd.gobus.views.SearchBox;
import com.hxd.gobus.views.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;


/**
 * @ 功能, 选择群组中成员
 */

public class ChooseAtMemberActivity extends com.hxd.gobus.chat.activity.JBaseActivity implements SideBar.OnSideBarListener,ContactsAdapter.OnItemClickListener {

    private Context mContext;
    private SearchBox mSearchBox;
    private TextView tvLetterTip;//字母提示
    private RecyclerView rvContacts;
    private SideBar mSideBar;
    private List<Contact> mList;
    private ContactsAdapter mAdapter;
    private CharacterParser mParser;
    private PinyinUtil pinyinUtil;

    private List<UserInfo> mMemberInfoList;
    private LinearLayout mLl_groupAll;
    private static ParentLinkedHolder<EmoticonsEditText> textParentLinkedHolder;

    private String appKey;

    public static void show(ChatActivity textWatcher, EmoticonsEditText editText, String targetId) {
        synchronized (ChooseAtMemberActivity.class) {
            ParentLinkedHolder<EmoticonsEditText> holder = new ParentLinkedHolder<>(editText);
            textParentLinkedHolder = holder.addParent(textParentLinkedHolder);
        }

        Intent intent = new Intent(textWatcher, ChooseAtMemberActivity.class);
        intent.putExtra(BaseConfig.GROUP_ID, Long.parseLong(targetId));
        textWatcher.startActivityForResult(intent, BaseConfig
                .REQUEST_CODE_AT_MEMBER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_at_member);

        initView();
        initData();

    }

    private void initData() {
        mContext =  getApplicationContext();
        if(mList == null) mList = new ArrayList<>();
        mAdapter = new ContactsAdapter(mList,mContext);
        rvContacts.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mParser = CharacterParser.getInstance();
        pinyinUtil = new PinyinUtil();
        //设置RecycleView的布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvContacts.setLayoutManager(layoutManager);
        //设置RecycleView的Item间的分割线
        rvContacts.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, R.drawable.recycleview_divider));
        // 设置RecycleView的动画ItemAnimator
        rvContacts.setItemAnimator(new DefaultItemAnimator());

        long groupId = getIntent().getLongExtra(BaseConfig.GROUP_ID, 0);
        if (0 != groupId) {
            Conversation conv = JMessageClient.getGroupConversation(groupId);
            GroupInfo groupInfo = (GroupInfo) conv.getTargetInfo();
            mMemberInfoList = groupInfo.getGroupMembers();
        }
        for(UserInfo userInfo : mMemberInfoList){
            appKey = userInfo.getAppKey();
            Contact contacts = SqliteUtils.getDaoByWorkNo(mContext,userInfo.getUserName());
            if(!contacts.getWorkNo().equals(User.getInstance().getWorkNo())){
                mList.add(contacts);
            }
        }
        getContacts(mList);
        mAdapter = new ContactsAdapter(mList,getApplicationContext());
        rvContacts.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        Collections.sort(mList, pinyinUtil);// 根据a-z进行排序源数据
        mAdapter.updateData(mList);

        //根据输入框输入值的改变来过滤搜索
        mSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView() {
        mLl_groupAll = (LinearLayout) findViewById(R.id.ll_groupAll);
        mSearchBox = (SearchBox) findViewById(R.id.searchBox_contacts);
        rvContacts = (RecyclerView) findViewById(R.id.rv_contacts);
        tvLetterTip = (TextView) findViewById(R.id.tv_contacts_letter_tip);
        mSideBar = (SideBar) findViewById(R.id.sidebar_contacts);
        mSideBar.setTextView(tvLetterTip);
        mSideBar.setOnSideBarListener(this);

        initTitle(true, true, "选择成员", "", false, "");

        mLl_groupAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //@all
                Intent intent = new Intent();
                intent.putExtra(BaseConfig.ATALL, true);
                setResult(BaseConfig.RESULT_CODE_AT_ALL, intent);
                finish();
            }
        });
    }

    private void getContacts(List<Contact> list) {
        for (int i = 0; i < list.size(); i++) {
            //汉字转换成拼音
            String pinyin = mParser.getSelling(list.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                list.get(i).setFirstLetter(sortString.toUpperCase());
            } else {
                list.get(i).setFirstLetter("#");
            }
        }
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<Contact> filterDateList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mList;
        } else {
            filterDateList.clear();
            for (Contact sortModel : mList) {
                String name = sortModel.getName();
                String dept = sortModel.getUnitName();
                if (name.indexOf(filterStr.toString()) != -1 || mParser.getSelling(name).startsWith(filterStr.toString())
                        || dept.indexOf(filterStr.toString()) != -1 || mParser.getSelling(dept).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }
        Collections.sort(filterDateList, pinyinUtil); // 根据a-z进行排序
        mAdapter.updateData(filterDateList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 33 && data != null) {
            Intent intent = new Intent();
            intent.putExtra(BaseConfig.NAME, data.getStringExtra(BaseConfig.SEARCH_AT_MEMBER_NAME));
            intent.putExtra(BaseConfig.TARGET_ID, data.getStringExtra(BaseConfig.SEARCH_AT_MEMBER_USERNAME));
            intent.putExtra(BaseConfig.TARGET_APP_KEY, data.getStringExtra(BaseConfig.SEARCH_AT_APPKEY));
            setResult(BaseConfig.RESULT_CODE_AT_MEMBER, intent);
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        synchronized (ChooseAtMemberActivity.class) {
            if (textParentLinkedHolder != null) {
                textParentLinkedHolder = textParentLinkedHolder.putParent();
            }
        }
    }


    @Override
    public void onLetterChanged(String letter) {
        int position = mAdapter.getPositionForSection(letter.charAt(0));
        if (position != -1) {
            rvContacts.scrollToPosition(position);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Contact contacts = mAdapter.getItem(position);
        Intent intent = new Intent();
        String atName = contacts.getName();

        synchronized (ChooseAtMemberActivity.class) {
            if (textParentLinkedHolder != null) {
                EmoticonsEditText editText = textParentLinkedHolder.item;
                if (editText != null)
                    intent.putExtra(BaseConfig.NAME, atName);
            }
        }

        intent.putExtra(BaseConfig.TARGET_ID, contacts.getWorkNo());
        intent.putExtra(BaseConfig.TARGET_APP_KEY, appKey);
        setResult(BaseConfig.RESULT_CODE_AT_MEMBER, intent);
        finish();
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
