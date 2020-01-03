package com.hxd.gobus.mvp.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseLazyFragment;
import com.hxd.gobus.bean.User;
import com.hxd.gobus.chat.activity.ChatActivity;
import com.hxd.gobus.chat.entity.Event;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.mvp.activity.TodoActivity;
import com.hxd.gobus.mvp.adapter.ConversationListAdapter;
import com.hxd.gobus.mvp.contract.IMessageContract;
import com.hxd.gobus.mvp.presenter.MessagePresenter;
import com.hxd.gobus.utils.DateUtil;
import com.hxd.gobus.utils.DialogCreator;
import com.hxd.gobus.utils.ScreenUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.MessageReceiptStatusChangeEvent;
import cn.jpush.im.android.api.event.MessageRetractEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * @author: wangqingbin
 * @date: 2019/7/25 10:23
 */

public class MessageFragment extends BaseLazyFragment<MessagePresenter> implements IMessageContract.View
        ,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.rl_message_todo)
    RelativeLayout rl_message_todo;

    @BindView(R.id.iv_message_todo_icon)
    ImageView iv_message_todo_icon;

    @BindView(R.id.tv_message_todo_title)
    TextView tv_message_todo_title;

    @BindView(R.id.tv_message_todo_notice)
    TextView tv_message_todo_notice;

    @BindView(R.id.tv_message_todo_date)
    TextView tv_message_todo_date;

    @BindView(R.id.tv_message_todo_count)
    TextView tv_message_todo_count;

    @BindView(R.id.list_conversation)
    ListView mListView;

    @OnClick({R.id.rl_message_todo})
    void clickTodo(){
        Intent intent = new Intent(mContext, TodoActivity.class);
        startActivity(intent);
    }

    private static List<Conversation> mConversationList = new ArrayList<Conversation>();
    private ConversationListAdapter mListAdapter;
    private Dialog mDialog;
    private BackgroundHandler mBackgroundHandler;

    private static final int REFRESH_CONVERSATION_LIST = 0x3000;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBackgroundHandler = new BackgroundHandler(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBackgroundHandler.removeCallbacksAndMessages(null);
        if (mConversationList != null){
            mConversationList = null;
        }
    }

    @Override
    protected void lazyLoad(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_main_message;
    }

    @Override
    protected void initView(View view) {
        initToolbar();

        mListView.setOnItemClickListener(this);
        //mListView.setOnItemLongClickListener(this);
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.GONE);
        mTvCenterTitle.setText("消 息");
        mTvCenterTitle.setTextColor(getResources().getColor(R.color.grey_6));
        mTvRightTitle.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getTodoCount();
        if(!"admin".equals(User.getInstance().getLoginName())){
            mPresenter.getConversationList();
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    public void showTodoMessage(int total) {
        tv_message_todo_title.setText("待办事项");
        if (total > 0){
            tv_message_todo_notice.setText(total + "个待办事项需要处理");
            tv_message_todo_count.setText(String.valueOf(total));
            tv_message_todo_count.setBackgroundResource(R.mipmap.unread_msg_red);
        }else{
            tv_message_todo_notice.setText("暂时没有待办事项需要处理");
        }
        tv_message_todo_date.setText(DateUtil.getCurTimeFormat(DateUtil.FORMAT_2));
    }

    @Override
    public void showConversationList(List<Conversation> conversationList) {
        this.mConversationList = conversationList;
        if (conversationList != null && conversationList.size() > 0){
            mListAdapter = new ConversationListAdapter(getContext(), conversationList);
            mListView.setAdapter(mListAdapter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        //点击会话条目
        Intent intent = new Intent();
        Conversation conv = mConversationList.get(position);
        intent.putExtra(BaseConfig.CONV_TITLE, conv.getTitle());
        //群聊
        if (conv.getType() == ConversationType.group) {
            if (mListAdapter.includeAtMsg(conv)) {
                intent.putExtra("atMsgId", mListAdapter.getAtMsgId(conv));
            }

            if (mListAdapter.includeAtAllMsg(conv)) {
                intent.putExtra("atAllMsgId", mListAdapter.getatAllMsgId(conv));
            }
            long groupId = ((GroupInfo) conv.getTargetInfo()).getGroupID();
            intent.putExtra(BaseConfig.GROUP_ID, groupId);
            intent.putExtra(BaseConfig.DRAFT, conv.getId());
        } else {
            UserInfo targetInfo = (UserInfo) conv.getTargetInfo();
            if(targetInfo != null){
                String targetId = ((UserInfo) conv.getTargetInfo()).getUserName();
                intent.putExtra(BaseConfig.TARGET_ID, targetId);
                intent.putExtra(BaseConfig.TARGET_APP_KEY, conv.getTargetAppKey());
                intent.putExtra(BaseConfig.DRAFT, conv.getId());
                if(targetId.equals("admin")){
                    intent.putExtra(BaseConfig.CONV_TITLE, ((UserInfo) conv.getTargetInfo()).getNickname());
                }
            }
        }
        intent.setClass(mContext, ChatActivity.class);
        mContext.startActivity(intent);

//        badgeCount = badgeCount - conv.getUnReadMsgCnt();
//        if(badgeCount > 0){
//            ShortcutBadger.applyCount(mContext, badgeCount);
//        }else{
//            ShortcutBadger.removeCount(mContext);
//        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final Conversation conv = mConversationList.get(position);
        if (conv != null) {
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        //会话置顶
                        case R.id.jmui_top_conv_ll:
                            if(!"admin".equals(conv.getTitle()) && !"系统通知".equals(conv.getTitle())){
                                //已经置顶,去取消
                                if (!TextUtils.isEmpty(conv.getExtra())) {
                                    mListAdapter.setCancelConvTop(conv);
                                    //没有置顶,去置顶
                                } else {
                                    mListAdapter.setConvTop(conv);
                                }
                            }
                            mDialog.dismiss();
                            break;
                        //删除会话
                        case R.id.jmui_delete_conv_ll:
                            if (conv.getType() == ConversationType.group) {
                                JMessageClient.deleteGroupConversation(((GroupInfo) conv.getTargetInfo()).getGroupID());
                            } else {
                                JMessageClient.deleteSingleConversation(((cn.jpush.im.android.api.model.UserInfo) conv.getTargetInfo()).getUserName());
                            }
                            mConversationList.remove(position);
                            mListAdapter.notifyDataSetChanged();

//                            badgeCount = badgeCount - conv.getUnReadMsgCnt();
//                            if(badgeCount > 0){
//                                ShortcutBadger.applyCount(mContext, badgeCount);
//                            }else{
//                                ShortcutBadger.removeCount(mContext);
//                            }

                            mDialog.dismiss();
                            break;
                        default:
                            break;
                    }

                }
            };
            mDialog = DialogCreator.createDelConversationDialog(mContext, listener, TextUtils.isEmpty(conv.getExtra()));
            mDialog.show();
            mDialog.getWindow().setLayout((int) (0.8 * ScreenUtils.getScreenWidth(mContext)), WindowManager.LayoutParams.WRAP_CONTENT);
        }
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshConversation(Conversation conv){
        mConversationList.add(conv);
        mListAdapter = new ConversationListAdapter(mContext, mConversationList);
        mListView.setAdapter(mListAdapter);
        if(!"admin".equals(conv.getTitle()) && !"系统通知".equals(conv.getTitle())){
            mListAdapter.setToTop(conv);
        }
        mListAdapter.notifyDataSetChanged();
    }

    private static class BackgroundHandler extends Handler {

        private final WeakReference<MessageFragment> mRefFragment;

        public BackgroundHandler(MessageFragment fragment){
            this.mRefFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (mRefFragment != null){
                MessageFragment fragment = mRefFragment.get();
                if (fragment == null || fragment.isDetached()) {
                    return;
                }
                switch (msg.what){
                    case REFRESH_CONVERSATION_LIST:
                        final Conversation conv = (Conversation) msg.obj;
                        EventBus.getDefault().post(conv);
                        break;
                }
            }
        }
    }

    /**
     * 收到消息
     */
    public void onEvent(MessageEvent event) {
        //    mConvListView.setUnReadMsg(JMessageClient.getAllUnReadMsgCount());
        Message msg = event.getMessage();
        if (msg.getTargetType() == ConversationType.group) {
            long groupId = ((GroupInfo) msg.getTargetInfo()).getGroupID();
            Conversation conv = JMessageClient.getGroupConversation(groupId);
            if (conv != null) {
                if (msg.isAtMe()) {
                    BaseConfig.isAtMe.put(groupId, true);
                    mListAdapter.putAtConv(conv, msg.getId());
                }
                if (msg.isAtAll()) {
                    BaseConfig.isAtall.put(groupId, true);
                    mListAdapter.putAtAllConv(conv, msg.getId());
                }
                mBackgroundHandler.sendMessage(mBackgroundHandler.obtainMessage(REFRESH_CONVERSATION_LIST,
                        conv));
            }
        } else {
            final UserInfo userInfo = (UserInfo) msg.getTargetInfo();
            String targetId = userInfo.getUserName();
            Conversation conv = JMessageClient.getSingleConversation(targetId, userInfo.getAppKey());
            if (conv != null) {
                mBackgroundHandler.sendMessage(mBackgroundHandler.obtainMessage(REFRESH_CONVERSATION_LIST, conv));
            }
        }
    }

    /**
     * 接收离线消息
     *
     * @param event 离线消息事件
     */
    public void onEvent(OfflineMessageEvent event) {
        Conversation conv = event.getConversation();
        if (!conv.getTargetId().equals("feedback_Android")) {
            mBackgroundHandler.sendMessage(mBackgroundHandler.obtainMessage(REFRESH_CONVERSATION_LIST, conv));
        }
    }

    /**
     * 消息撤回
     */
    public void onEvent(MessageRetractEvent event) {
        Conversation conversation = event.getConversation();
        mBackgroundHandler.sendMessage(mBackgroundHandler.obtainMessage(REFRESH_CONVERSATION_LIST, conversation));
    }

    /**
     * 消息已读事件
     */
    public void onEventMainThread(MessageReceiptStatusChangeEvent event) {
        mListAdapter.notifyDataSetChanged();
    }

    /**
     * 消息漫游完成事件
     *
     * @param event 漫游完成后， 刷新会话事件
     */
    public void onEvent(ConversationRefreshEvent event) {
        Conversation conv = event.getConversation();
        if (!conv.getTargetId().equals("feedback_Android")) {
            mBackgroundHandler.sendMessage(mBackgroundHandler.obtainMessage(REFRESH_CONVERSATION_LIST, conv));
            //多端在线未读数改变时刷新
            if (event.getReason().equals(ConversationRefreshEvent.Reason.UNREAD_CNT_UPDATED)) {
                mBackgroundHandler.sendMessage(mBackgroundHandler.obtainMessage(REFRESH_CONVERSATION_LIST, conv));
            }
        }
    }

    public void onEventMainThread(Event event) {
        switch (event.getType()) {
            case createConversation:
                Conversation conv = event.getConversation();
                if (conv != null) {
                    mListAdapter.addNewConversation(conv);
                }
                break;
            case deleteConversation:
                conv = event.getConversation();
                if (null != conv) {
                    mListAdapter.deleteConversation(conv);
                }
                break;
            //收到保存为草稿事件
            case draft:
                conv = event.getConversation();
                String draft = event.getDraft();
                //如果草稿内容不为空，保存，并且置顶该会话
                if (!TextUtils.isEmpty(draft)) {
                    mListAdapter.putDraftToMap(conv, draft);
                    if(!"admin".equals(conv.getTitle()) && !"系统通知".equals(conv.getTitle())){
                        mListAdapter.setToTop(conv);
                    }
                    //否则删除
                } else {
                    mListAdapter.delDraftFromMap(conv);
                }
                break;
            case addFriend:
                break;
        }
    }

}
