package com.hxd.gobus.chat.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hxd.gobus.R;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.chat.controller.ActivityController;
import com.hxd.gobus.chat.entity.Event;
import com.hxd.gobus.chat.entity.EventType;
import com.hxd.gobus.chat.utils.DialogCreator;
import com.hxd.gobus.chat.utils.HandleResponseCode;
import com.hxd.gobus.mvp.adapter.ContactsAdapter;
import com.hxd.gobus.utils.ContactsSingleton;
import com.hxd.gobus.utils.PinyinUtil;
import com.hxd.gobus.utils.SearchOperation;
import com.hxd.gobus.utils.SqliteUtils;
import com.hxd.gobus.utils.ToastUtils;
import com.hxd.gobus.views.RecycleViewDivider;
import com.hxd.gobus.views.SearchBox;
import com.hxd.gobus.views.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.options.MessageSendingOptions;
import cn.jpush.im.android.eventbus.EventBus;
import cn.jpush.im.api.BasicCallback;


/**
 * Created by ${chenyn} on 2017/7/16.
 */

public class ForwardMsgActivity extends com.hxd.gobus.chat.activity.JBaseActivity implements SideBar.OnSideBarListener,ContactsAdapter.OnItemClickListener {

    private LinearLayout mLl_groupAll;
    private Dialog mDialog;
    private Context mContext;
    private SearchBox mSearchBox;
    private TextView tvLetterTip;//字母提示
    private RecyclerView rvContacts;
    private SideBar mSideBar;
    private List<Contact> mList;
    private PinyinUtil pinyinUtil;
    private ContactsAdapter mAdapter;
    private SearchOperation mSearchOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward_msg);
        ActivityController.addActivity(this);

        initView();
        initData();
    }

    private void initView() {
        if (getIntent().getFlags() == 1) {
            initTitle(true, true, "发送名片", "", false, "");
        } else {
            initTitle(true, true, "转发", "", false, "");
        }

        mLl_groupAll = (LinearLayout) findViewById(R.id.ll_groupAll);
        mSearchBox = (SearchBox) findViewById(R.id.searchBox_contacts);
        rvContacts = (RecyclerView) findViewById(R.id.rv_contacts);
        tvLetterTip = (TextView) findViewById(R.id.tv_contacts_letter_tip);
        mSideBar = (SideBar) findViewById(R.id.sidebar_contacts);
        mSideBar.setTextView(tvLetterTip);
        mSideBar.setOnSideBarListener(this);
    }

    private void initData() {
        mContext =  getApplicationContext();
        if(mList == null) mList = new ArrayList<>();
        mAdapter = new ContactsAdapter(mList,mContext);
        pinyinUtil = new PinyinUtil();
        rvContacts.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
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
            }

            @Override
            public void onListError(String error) {
                ToastUtils.showShortToast(mContext,error);
            }
        });
        ContactsSingleton.getInstance().startSyncQuery(mContext);

        //群组
        mLl_groupAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForwardMsgActivity.this, com.hxd.gobus.chat.activity.GroupActivity.class);
                setExtraIntent(intent);
            }
        });

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

    private void setExtraIntent(Intent intent) {
        //发送名片,ForwardMsgActivity跳转过来
        if (getIntent().getFlags() == 1) {
            intent.setFlags(2);
            intent.putExtra("userName", getIntent().getStringExtra("userName"));
            intent.putExtra("appKey", getIntent().getStringExtra("appKey"));
            intent.putExtra("avatar", getIntent().getStringExtra("avatar"));
        } else {
            //转发消息,启动群组界面,设置flag标识为1
            intent.setFlags(1);
        }
        startActivity(intent);
    }

    private void setBusinessCard(String name, final Intent intent, final Conversation conversation) {
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
                        content.setStringExtra("userName", intent.getStringExtra("userName"));
                        content.setStringExtra("appKey", intent.getStringExtra("appKey"));
                        content.setStringExtra("businessCard", "businessCard");

                        Message textMessage = conversation.createSendMessage(content);
                        MessageSendingOptions options = new MessageSendingOptions();
                        options.setNeedReadReceipt(true);
                        JMessageClient.sendMessage(textMessage, options);
                        textMessage.setOnSendCompleteCallback(new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    Toast.makeText(ForwardMsgActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    HandleResponseCode.onHandle(ForwardMsgActivity.this, i, false);
                                }
                            }
                        });
                        break;
                }
            }
        };
        mDialog = DialogCreator.createBusinessCardDialog(ForwardMsgActivity.this, listener, name,
                intent.getStringExtra("userName"), intent.getStringExtra("avatar"));
        mDialog.getWindow().setLayout((int) (0.8 * mWidth), WindowManager.LayoutParams.WRAP_CONTENT);
        mDialog.show();
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
        Conversation conversation  = JMessageClient.getSingleConversation(contacts.getWorkNo());
        if (conversation == null) { //如果会话为空，就创建新的会话
            conversation = Conversation.createSingleConversation(contacts.getWorkNo(), getIntent().getStringExtra("mTargetAppKey"));
            EventBus.getDefault().post(new Event.Builder()
                    .setType(EventType.createConversation)
                    .setConversation(conversation)
                    .build());
        }

        //发送名片
        final Intent intent = getIntent();
        if (intent.getFlags() == 1) {
            String toName = SqliteUtils.getNameByWorkNo(mContext,conversation.getTitle());
            setBusinessCard(toName, intent, conversation);
            //转发消息
        } else {
            DialogCreator.createForwardMsg(ForwardMsgActivity.this, mWidth, true, conversation, null, null, null);
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
