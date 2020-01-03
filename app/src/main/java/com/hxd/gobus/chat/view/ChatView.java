package com.hxd.gobus.chat.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.chat.activity.ChatActivity;
import com.hxd.gobus.chat.adapter.ChattingListAdapter;
import com.hxd.gobus.chat.view.listview.DropDownListView;

import cn.jpush.im.android.api.model.Conversation;

/**
 * Created by ${chenyn} on 2017/3/28.
 */

public class ChatView extends RelativeLayout {
    Context mContext;
    private DropDownListView mChatListView;
    private Conversation mConv;
    private Button mAtMeBtn;

    private Toolbar mToolbar;
    private LinearLayout llBack;
    private TextView tvToolbarTitle;
    private TextView tvRightTitle;

    public ChatView(Context context) {
        super(context);
        this.mContext = context;
    }

    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initModule(AppCompatActivity activity, float density, int densityDpi) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        llBack = (LinearLayout) findViewById(R.id.ll_toolbar_back);
        tvToolbarTitle = (TextView) findViewById(R.id.tv_toolbar_center_title);
        tvRightTitle = (TextView) findViewById(R.id.tv_toolbar_right_title);
        llBack.setVisibility(View.VISIBLE);
        tvRightTitle.setVisibility(View.VISIBLE);
        mToolbar.setTitle("");// 标题的文字需在setSupportActionBar之前，不然会无效
        activity.setSupportActionBar(mToolbar);

        mAtMeBtn = (Button) findViewById(R.id.jmui_at_me_btn);
        if (densityDpi <= 160) {
            tvToolbarTitle.setMaxWidth((int)(180 * density + 0.5f));
        }else if (densityDpi <= 240) {
            tvToolbarTitle.setMaxWidth((int)(190 * density + 0.5f));
        }else {
            tvToolbarTitle.setMaxWidth((int)(200 * density + 0.5f));
        }
        mChatListView = (DropDownListView) findViewById(R.id.lv_chat);

    }

    public void setToPosition(int position) {
        mChatListView.smoothScrollToPosition(position);
        mAtMeBtn.setVisibility(GONE);
    }

    public void setChatListAdapter(ChattingListAdapter chatAdapter) {
        mChatListView.setAdapter(chatAdapter);
    }

    public DropDownListView getListView() {
        return mChatListView;
    }
    public void setToBottom() {
        mChatListView.clearFocus();
        mChatListView.post(new Runnable() {
            @Override
            public void run() {
                mChatListView.setSelection(mChatListView.getAdapter().getCount() - 1);
            }
        });
    }
    public void setConversation(Conversation conv) {
        this.mConv = conv;
    }

    public void setGroupIcon() {
        tvRightTitle.setBackgroundResource(R.mipmap.jmui_group_chat_detail);
    }

    public void setSingleIcon() {
        tvRightTitle.setBackgroundResource(R.mipmap.jmui_chat_detail);
    }

    public void setListeners(ChatActivity listeners) {
        llBack.setOnClickListener(listeners);
        tvRightTitle.setOnClickListener(listeners);
        mAtMeBtn.setOnClickListener(listeners);
    }

    public void dismissRightBtn() {
        tvRightTitle.setVisibility(View.GONE);
    }

    public void showRightBtn() {
        tvRightTitle.setVisibility(View.VISIBLE);
    }

    public void setChatTitle(int id, int count) {
        tvToolbarTitle.setText(id);
        tvToolbarTitle.setText("(" + count + ")");
        tvToolbarTitle.setVisibility(View.VISIBLE);
    }

    public void setChatTitle(int id) {
        tvToolbarTitle.setText(id);
    }
    public void showAtMeButton() {
        mAtMeBtn.setVisibility(VISIBLE);
    }


    //设置群聊名字
    public void setChatTitle(String name, int count) {
        tvToolbarTitle.setText(name+"(" + count + ")");
        tvToolbarTitle.setVisibility(View.VISIBLE);
    }

    public void setChatTitle(String title) {
        tvToolbarTitle.setText(title);
    }
    public void dismissGroupNum() {
        tvToolbarTitle.setVisibility(View.GONE);
    }
}
