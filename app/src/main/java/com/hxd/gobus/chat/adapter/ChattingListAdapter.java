package com.hxd.gobus.chat.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.chat.activity.receiptmessage.ReceiptMessageListActivity;
import com.hxd.gobus.chat.controller.ChatItemController;
import com.hxd.gobus.chat.utils.DialogCreator;
import com.hxd.gobus.chat.utils.HandleResponseCode;
import com.hxd.gobus.chat.utils.TimeFormat;
import com.hxd.gobus.chat.utils.keyboard.utils.imageloader.ImageLoader;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.mvp.activity.PersonalDataActivity;
import com.hxd.gobus.utils.SqliteUtils;
import com.hxd.gobus.views.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetReceiptDetailsCallback;
import cn.jpush.im.android.api.callback.ProgressUpdateCallback;
import cn.jpush.im.android.api.content.FileContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.enums.MessageStatus;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.api.options.MessageSendingOptions;
import cn.jpush.im.api.BasicCallback;

public class ChattingListAdapter extends BaseAdapter {

    public static final int PAGE_MESSAGE_COUNT = 18;
    private long mGroupId;

    //文本
    private final int TYPE_SEND_TXT = 0;
    private final int TYPE_RECEIVE_TXT = 1;

    // 图片
    private final int TYPE_SEND_IMAGE = 2;
    private final int TYPE_RECEIVER_IMAGE = 3;

    //文件
    private final int TYPE_SEND_FILE = 4;
    private final int TYPE_RECEIVE_FILE = 5;
    // 语音
    private final int TYPE_SEND_VOICE = 6;
    private final int TYPE_RECEIVER_VOICE = 7;
    // 位置
    private final int TYPE_SEND_LOCATION = 8;
    private final int TYPE_RECEIVER_LOCATION = 9;
    //群成员变动
    private final int TYPE_GROUP_CHANGE = 10;
    //视频
    private final int TYPE_SEND_VIDEO = 11;
    private final int TYPE_RECEIVE_VIDEO = 12;
    //自定义消息
    private final int TYPE_CUSTOM_TXT = 13;

    private Activity mActivity;
    private LayoutInflater mInflater;
    private Context mContext;
    private int mWidth;
    private Conversation mConv;
    private List<Message> mMsgList = new ArrayList<Message>();//所有消息列表
    private int mOffset = PAGE_MESSAGE_COUNT;
    private ContentLongClickListener mLongClickListener;
    //当前第0项消息的位置
    private int mStart;
    //发送图片消息的队列
    private Queue<Message> mMsgQueue = new LinkedList<Message>();
    private ChatItemController mController;
    private Dialog mDialog;
    private boolean mHasLastPage = false;

    public ChattingListAdapter(Activity context, Conversation conv, ContentLongClickListener longClickListener) {
        this.mContext = context;
        mActivity = context;
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWidth = dm.widthPixels;
        mInflater = LayoutInflater.from(mContext);
        this.mConv = conv;
        this.mMsgList = mConv.getMessagesFromNewest(0, mOffset);
        reverse(mMsgList);
        mLongClickListener = longClickListener;
        this.mController = new ChatItemController(this, mActivity, conv, mMsgList, dm.density,
                longClickListener);
        mStart = mOffset;
        if (mConv.getType() == ConversationType.single) {
            UserInfo userInfo = (UserInfo) mConv.getTargetInfo();
            if (!TextUtils.isEmpty(userInfo.getAvatar())) {
                userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                    @Override
                    public void gotResult(int status, String desc, Bitmap bitmap) {
                        if (status == 0) {
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        } else {
            //群聊
            GroupInfo groupInfo = (GroupInfo) mConv.getTargetInfo();
            mGroupId = groupInfo.getGroupID();
        }
        checkSendingImgMsg();
    }

    public ChattingListAdapter(Context context, Conversation conv, ContentLongClickListener longClickListener,
                               int msgId) {
        this.mContext = context;
        mActivity = (Activity) context;
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWidth = dm.widthPixels;

        mInflater = LayoutInflater.from(mContext);
        this.mConv = conv;
        if (mConv.getUnReadMsgCnt() > PAGE_MESSAGE_COUNT) {
            this.mMsgList = mConv.getMessagesFromNewest(0, mConv.getUnReadMsgCnt());
            mStart = mConv.getUnReadMsgCnt();
        } else {
            this.mMsgList = mConv.getMessagesFromNewest(0, mOffset);
            mStart = mOffset;
        }
        reverse(mMsgList);
        mLongClickListener = longClickListener;
        this.mController = new ChatItemController(this, mActivity, conv, mMsgList, dm.density,
                longClickListener);
        GroupInfo groupInfo = (GroupInfo) mConv.getTargetInfo();
        mGroupId = groupInfo.getGroupID();
        checkSendingImgMsg();

    }

    private void reverse(List<Message> list) {
        if (list.size() > 0) {
            Collections.reverse(list);
        }
    }

    public void dropDownToRefresh() {
        if (mConv != null) {
            List<Message> msgList = mConv.getMessagesFromNewest(mMsgList.size(), PAGE_MESSAGE_COUNT);
            if (msgList != null) {
                for (Message msg : msgList) {
                    mMsgList.add(0, msg);
                }
                if (msgList.size() > 0) {
                    checkSendingImgMsg();
                    mOffset = msgList.size();
                    mHasLastPage = true;
                } else {
                    mOffset = 0;
                    mHasLastPage = false;
                }
                notifyDataSetChanged();
            }
        }
    }

    public boolean isHasLastPage() {
        return mHasLastPage;
    }

    public int getOffset() {
        return mOffset;
    }

    public void refreshStartPosition() {
        mStart += mOffset;
    }

    //当有新消息加到MsgList，自增mStart
    private void incrementStartPosition() {
        ++mStart;
    }

    /**
     * 检查图片是否处于创建状态，如果是，则加入发送队列
     */
    private void checkSendingImgMsg() {
        for (Message msg : mMsgList) {
            if (msg.getStatus() == MessageStatus.created
                    && msg.getContentType() == ContentType.image) {
                mMsgQueue.offer(msg);
            }
        }

        if (mMsgQueue.size() > 0) {
            Message message = mMsgQueue.element();
            if (mConv.getType() == ConversationType.single) {
                sendNextImgMsg(message);
            } else {
                sendNextImgMsg(message);
            }

            notifyDataSetChanged();
        }
    }

    public void setSendMsgs(int msgIds) {
        Message msg = mConv.getMessage(msgIds);
        if (msg != null) {
            if(msg.getContentType() == ContentType.file){   //如果发送是文件
                mMsgList.add(msg);
                incrementStartPosition();
                MessageSendingOptions options = new MessageSendingOptions();
                options.setNeedReadReceipt(true);
                JMessageClient.sendMessage(msg, options);
                msg.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        notifyDataSetChanged();
                    }
                });
            }else{
                mMsgList.add(msg);
                incrementStartPosition();
                mMsgQueue.offer(msg);
            }
        }
        if (mMsgQueue.size() > 0) {
            Message message = mMsgQueue.element();
//            if (mConv.getType() == ConversationType.single) {
//                sendNextImgMsg(message);
//                UserInfo userInfo = (UserInfo) message.getTargetInfo();
//                if (userInfo.isFriend()) {
//                    sendNextImgMsg(message);
//                } else {
//                    CustomContent customContent = new CustomContent();
//                    customContent.setBooleanValue("notFriend", true);
//                    Message customMsg = mConv.createSendMessage(customContent);
//                    addMsgToList(customMsg);
//                }
//            } else {
//                sendNextImgMsg(message);
//            }

            sendNextImgMsg(message);
            notifyDataSetChanged();
        }
    }

    /**
     * 从发送队列中出列，并发送图片
     *
     * @param msg 图片消息
     */
    private void sendNextImgMsg(Message msg) {
        MessageSendingOptions options = new MessageSendingOptions();
        options.setNeedReadReceipt(true);
        JMessageClient.sendMessage(msg, options);
        msg.setOnSendCompleteCallback(new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                //出列
                mMsgQueue.poll();
                //如果队列不为空，则继续发送下一张
                if (!mMsgQueue.isEmpty()) {
                    sendNextImgMsg(mMsgQueue.element());
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addMsgToList(Message msg) {
        mMsgList.add(msg);
        incrementStartPosition();
        notifyDataSetChanged();
    }

    public void addMsgListToList(List<Message> singleOfflineMsgList) {
        mMsgList.addAll(singleOfflineMsgList);
        notifyDataSetChanged();
    }

    public void addMsgFromReceiptToList(Message msg) {
        mMsgList.add(msg);
        msg.setOnSendCompleteCallback(new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    incrementStartPosition();
                    notifyDataSetChanged();
                } else {
                    HandleResponseCode.onHandle(mContext, i, false);
                    notifyDataSetChanged();
                }
            }
        });
    }


    //找到撤回的那一条消息,并且用撤回后event下发的去替换掉这条消息在集合中的原位置
    List<Message> forDel;
    int i;

    public void delMsgRetract(Message msg) {
        forDel = new ArrayList<>();
        i = 0;
        for (Message message : mMsgList) {
            if (msg.getServerMessageId().equals(message.getServerMessageId())) {
                i = mMsgList.indexOf(message);
                forDel.add(message);
            }
        }
        mMsgList.removeAll(forDel);
        mMsgList.add(i, msg);
        notifyDataSetChanged();
    }

    public Message getLastMsg() {
        if (mMsgList.size() > 0) {
            return mMsgList.get(mMsgList.size() - 1);
        } else {
            return null;
        }
    }

    public Message getMessage(int position) {
        return mMsgList.get(position);
    }

    List<Message> del = new ArrayList<>();

    public void removeMessage(Message message) {
        for (Message msg : mMsgList) {
            if (msg.getServerMessageId().equals(message.getServerMessageId())) {
                del.add(msg);
            }
        }
        mMsgList.removeAll(del);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMsgList.size();
    }


    @Override
    public int getItemViewType(int position) {
        Message msg = mMsgList.get(position);
        //是文字类型或者自定义类型（用来显示群成员变化消息）
        switch (msg.getContentType()) {
            case text:
                return msg.getDirect() == MessageDirect.send ? TYPE_SEND_TXT
                        : TYPE_RECEIVE_TXT;
            case image:
                return msg.getDirect() == MessageDirect.send ? TYPE_SEND_IMAGE
                        : TYPE_RECEIVER_IMAGE;
            case file:
                String extra = msg.getContent().getStringExtra("video");
                if (!TextUtils.isEmpty(extra)) {
                    return msg.getDirect() == MessageDirect.send ? TYPE_SEND_VIDEO
                            : TYPE_RECEIVE_VIDEO;
                } else {
                    return msg.getDirect() == MessageDirect.send ? TYPE_SEND_FILE
                            : TYPE_RECEIVE_FILE;
                }
            case voice:
                return msg.getDirect() == MessageDirect.send ? TYPE_SEND_VOICE
                        : TYPE_RECEIVER_VOICE;
            case location:
                return msg.getDirect() == MessageDirect.send ? TYPE_SEND_LOCATION
                        : TYPE_RECEIVER_LOCATION;
            case eventNotification:
            case prompt:
                return TYPE_GROUP_CHANGE;
            default:
                return TYPE_CUSTOM_TXT;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 14;
    }


    private View createViewByType(Message msg, int position) {
        // 会话类型
        switch (msg.getContentType()) {
            case text:
                return getItemViewType(position) == TYPE_SEND_TXT ?
                        mInflater.inflate(R.layout.jmui_chat_item_send_text, null) :
                        mInflater.inflate(R.layout.jmui_chat_item_receive_text, null);
            case image:
                return getItemViewType(position) == TYPE_SEND_IMAGE ?
                        mInflater.inflate(R.layout.jmui_chat_item_send_image, null) :
                        mInflater.inflate(R.layout.jmui_chat_item_receive_image, null);
            case file:
                String extra = msg.getContent().getStringExtra("video");
                if (!TextUtils.isEmpty(extra)) {
                    return getItemViewType(position) == TYPE_SEND_VIDEO ?
                            mInflater.inflate(R.layout.jmui_chat_item_send_video, null) :
                            mInflater.inflate(R.layout.jmui_chat_item_receive_video, null);
                } else {
                    return getItemViewType(position) == TYPE_SEND_FILE ?
                            mInflater.inflate(R.layout.jmui_chat_item_send_file, null) :
                            mInflater.inflate(R.layout.jmui_chat_item_receive_file, null);
                }
            case voice:
                return getItemViewType(position) == TYPE_SEND_VOICE ?
                        mInflater.inflate(R.layout.jmui_chat_item_send_voice, null) :
                        mInflater.inflate(R.layout.jmui_chat_item_receive_voice, null);
            case location:
                return getItemViewType(position) == TYPE_SEND_LOCATION ?
                        mInflater.inflate(R.layout.jmui_chat_item_send_location, null) :
                        mInflater.inflate(R.layout.jmui_chat_item_receive_location, null);
            case eventNotification:
            case prompt:
                if (getItemViewType(position) == TYPE_GROUP_CHANGE) {
                    return mInflater.inflate(R.layout.jmui_chat_item_group_change, null);
                }
            default:
                return mInflater.inflate(R.layout.jmui_chat_item_group_change, null);
        }
    }

    @Override
    public Message getItem(int position) {
        return mMsgList.get(position);
    }

    public void clearMsgList() {
        mMsgList.clear();
        mStart = 0;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Message msg = mMsgList.get(position);
        //消息接收方发送已读回执
        if (msg.getDirect() == MessageDirect.receive && !msg.haveRead()) {
            msg.setHaveRead(new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                }
            });
        }
        final UserInfo userInfo = msg.getFromUser();
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = createViewByType(msg, position);
            holder.msgTime = (TextView) convertView.findViewById(R.id.jmui_send_time_txt);
            holder.headIcon = (CircleImageView) convertView.findViewById(R.id.jmui_avatar_iv);
            holder.displayName = (TextView) convertView.findViewById(R.id.jmui_display_name_tv);
            holder.txtContent = (TextView) convertView.findViewById(R.id.jmui_msg_content);
            holder.sendingIv = (ImageView) convertView.findViewById(R.id.jmui_sending_iv);
            holder.resend = (ImageButton) convertView.findViewById(R.id.jmui_fail_resend_ib);
            holder.ivDocument = (ImageView) convertView.findViewById(R.id.iv_document);
            holder.text_receipt = (TextView) convertView.findViewById(R.id.text_receipt);
            switch (msg.getContentType()) {
                case text:
                    holder.ll_businessCard = (LinearLayout) convertView.findViewById(R.id.ll_businessCard);
                    holder.business_head = (CircleImageView) convertView.findViewById(R.id.business_head);
                    holder.tv_nickUser = (TextView) convertView.findViewById(R.id.tv_nickUser);
                    holder.tv_userName = (TextView) convertView.findViewById(R.id.tv_userName);
                    break;
                case image:
                    holder.picture = (ImageView) convertView.findViewById(R.id.jmui_picture_iv);
                    holder.progressTv = (TextView) convertView.findViewById(R.id.jmui_progress_tv);
                    break;
                case file:
                    String extra = msg.getContent().getStringExtra("video");
                    if (!TextUtils.isEmpty(extra)) {
                        holder.picture = (ImageView) convertView.findViewById(R.id.jmui_picture_iv);
                        holder.progressTv = (TextView) convertView.findViewById(R.id.jmui_progress_tv);
                        holder.videoPlay = (LinearLayout) convertView.findViewById(R.id.message_item_video_play);
                    } else {
                        holder.progressTv = (TextView) convertView.findViewById(R.id.jmui_progress_tv);
                        holder.contentLl = (LinearLayout) convertView.findViewById(R.id.jmui_send_file_ll);
                        holder.sizeTv = (TextView) convertView.findViewById(R.id.jmui_send_file_size);
                        holder.alreadySend = (TextView) convertView.findViewById(R.id.file_already_send);
                    }
                    if (msg.getDirect().equals(MessageDirect.receive)) {
                        holder.fileLoad = (TextView) convertView.findViewById(R.id.jmui_send_file_load);
                    }
                    break;
                case voice:
                    holder.voice = (ImageView) convertView.findViewById(R.id.jmui_voice_iv);
                    holder.voiceLength = (TextView) convertView.findViewById(R.id.jmui_voice_length_tv);
                    holder.readStatus = (ImageView) convertView.findViewById(R.id.jmui_read_status_iv);
                    break;
                case location:
                    holder.location = (TextView) convertView.findViewById(R.id.jmui_loc_desc);
                    holder.picture = (ImageView) convertView.findViewById(R.id.jmui_picture_iv);
                    holder.locationView = convertView.findViewById(R.id.location_view);
                    break;
                case custom:
                case prompt:
                case eventNotification:
                    holder.groupChange = (TextView) convertView.findViewById(R.id.jmui_group_content);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        long nowDate = msg.getCreateTime();
        if (mOffset == 18) {
            if (position == 0 || position % 18 == 0) {
                TimeFormat timeFormat = new TimeFormat(mContext, nowDate);
                holder.msgTime.setText(timeFormat.getDetailTime());
                holder.msgTime.setVisibility(View.VISIBLE);
            } else {
                long lastDate = mMsgList.get(position - 1).getCreateTime();
                // 如果两条消息之间的间隔超过五分钟则显示时间
                if (nowDate - lastDate > 300000) {
                    TimeFormat timeFormat = new TimeFormat(mContext, nowDate);
                    holder.msgTime.setText(timeFormat.getDetailTime());
                    holder.msgTime.setVisibility(View.VISIBLE);
                } else {
                    holder.msgTime.setVisibility(View.GONE);
                }
            }
        } else {
            if (position == 0 || position == mOffset
                    || (position - mOffset) % 18 == 0) {
                TimeFormat timeFormat = new TimeFormat(mContext, nowDate);

                holder.msgTime.setText(timeFormat.getDetailTime());
                holder.msgTime.setVisibility(View.VISIBLE);
            } else {
                long lastDate = mMsgList.get(position - 1).getCreateTime();
                // 如果两条消息之间的间隔超过五分钟则显示时间
                if (nowDate - lastDate > 300000) {
                    TimeFormat timeFormat = new TimeFormat(mContext, nowDate);
                    holder.msgTime.setText(timeFormat.getDetailTime());
                    holder.msgTime.setVisibility(View.VISIBLE);
                } else {
                    holder.msgTime.setVisibility(View.GONE);
                }
            }
        }

        //显示头像
        if (holder.headIcon != null) {
//            if (userInfo != null && !TextUtils.isEmpty(userInfo.getAvatar())) {
//                userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
//                    @Override
//                    public void gotResult(int status, String desc, Bitmap bitmap) {
//                        if (status == 0) {
//                            holder.headIcon.setImageBitmap(bitmap);
//                        } else {
//                        //    holder.headIcon.setImageResource(R.drawable.jmui_head_icon);
//                            holder.headIcon.setImageResource(R.mipmap.jmui_conv_head_icon);
//                        }
//                    }
//                });
//            } else {
//            //    holder.headIcon.setImageResource(R.drawable.jmui_head_icon);
//                holder.headIcon.setImageResource(R.mipmap.jmui_conv_head_icon);
//            }

            if(userInfo != null){
                if(!TextUtils.isEmpty(userInfo.getUserName())){
                    if(!"admin".equals(userInfo.getUserName()) && !"系统通知".equals(userInfo.getUserName())){
                        Contact contacts = SqliteUtils.getDaoByWorkNo(mContext, userInfo.getUserName());
                        if(contacts != null){
                            String imageName = contacts.getPhotoUrl();
                            if(!TextUtils.isEmpty(imageName)){
                                String imageUrl = Constant.HEAD_PICTURE_DOWNLOAD_URL + imageName;
                                try {
                                    ImageLoader.getInstance(mContext).displayImage(imageUrl, holder.headIcon);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                holder.headIcon.setImageResource(R.mipmap.jmui_conv_head_icon);
                            }
                        }
                    }else{
                        //在这里设置系统通知头像
                        holder.headIcon.setImageResource(R.mipmap.jchat_conv_system_notification_icon);
                    }
                }
            }

            // 点击头像跳转到个人信息界面
            holder.headIcon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent();
                    intent.putExtra("TAG","chattingList");
                    if(mConv.getType() == ConversationType.single){ //单聊天室
                        intent.putExtra("TAG2","single");
                    }else{  //群聊天室
                        intent.putExtra("TAG2","group");
                    }
                    if (msg.getDirect() == MessageDirect.send) {
                        if( JMessageClient.getMyInfo().getUserName().equals("admin")){
                            return;
                        }
                        intent.putExtra(BaseConfig.TARGET_ID, JMessageClient.getMyInfo().getUserName());
                    }else{
                        if( userInfo.getUserName().equals("admin")){
                            return;
                        }
                        String targetID = userInfo.getUserName();
                        intent.putExtra(BaseConfig.TARGET_ID, targetID);
                    }

                    intent.setClass(mContext, PersonalDataActivity.class);
                    mContext.startActivity(intent);

//                    if (msg.getDirect() == MessageDirect.send) {
//                        intent.putExtra(BaseApplication.TARGET_ID, JMessageClient.getMyInfo().getUserName());
//                        intent.setClass(mContext, PersonalActivity.class);
//                        mContext.startActivity(intent);
//                    } else {
//                        String targetID = userInfo.getUserName();
//                        intent.putExtra(BaseApplication.TARGET_ID, targetID);
//                        intent.putExtra(BaseApplication.TARGET_APP_KEY, userInfo.getAppKey());
//                        intent.putExtra(BaseApplication.GROUP_ID, mGroupId);
//                        if (userInfo.isFriend()) {
//                            intent.setClass(mContext, FriendInfoActivity.class);
//                        } else {
//                            intent.setClass(mContext, GroupNotFriendActivity.class);
//                        }
//                        ((Activity) mContext).startActivityForResult(intent,
//                                BaseApplication.REQUEST_CODE_FRIEND_INFO);
//                    }
                }
            });

            holder.headIcon.setTag(position);
            holder.headIcon.setOnLongClickListener(mLongClickListener);
        }

        switch (msg.getContentType()) {
            case text:
                //final String content = ((TextContent) msg.getContent()).getText();
                //SimpleCommonUtils.spannableEmoticonFilter(holder.txtContent, content);
                TextContent textContent = (TextContent) msg.getContent();
                String extraBusiness = textContent.getStringExtra("businessCard");
                if (extraBusiness != null) {
                    holder.txtContent.setVisibility(View.GONE);
                    holder.ll_businessCard.setVisibility(View.VISIBLE);
                    mController.handleBusinessCard(msg, holder, position);
                } else {
                    holder.ll_businessCard.setVisibility(View.GONE);
                    holder.txtContent.setVisibility(View.VISIBLE);
                    mController.handleTextMsg(msg, holder, position);
                }
                break;
            case image:
                mController.handleImgMsg(msg, holder, position);
                break;
            case file:
                FileContent fileContent = (FileContent) msg.getContent();
                String extra = fileContent.getStringExtra("video");
                if (!TextUtils.isEmpty(extra)) {
                    mController.handleVideo(msg, holder, position);
                } else {
                    mController.handleFileMsg(msg, holder, position);
                }
                break;
            case voice:
                mController.handleVoiceMsg(msg, holder, position);
                break;
            case location:
                mController.handleLocationMsg(msg, holder, position);
                break;
            case eventNotification:
                mController.handleGroupChangeMsg(msg, holder);
                break;
            case prompt:
                mController.handlePromptMsg(msg, holder);
                break;
            default:
                mController.handleCustomMsg(msg, holder);
        }
        if (msg.getDirect() == MessageDirect.send && !msg.getContentType().equals(ContentType.prompt) && msg.getContentType() != ContentType.custom) {
            if (msg.getUnreceiptCnt() == 0) {
                if (msg.getTargetType() == ConversationType.group) {
                    holder.text_receipt.setText("全部已读");
                } else if (!((UserInfo) msg.getTargetInfo()).getUserName().equals(JMessageClient.getMyInfo().getUserName())) {
                    holder.text_receipt.setText("已读");
                }
                holder.text_receipt.setTextColor(mContext.getResources().getColor(R.color.message_already_receipt));
            } else {
                holder.text_receipt.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                if (msg.getTargetType() == ConversationType.group) {
                    holder.text_receipt.setText(msg.getUnreceiptCnt() + "人未读");
                    //群聊未读消息数点击事件
                    holder.text_receipt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Intent intent = new Intent(mContext, ReceiptMessageListActivity.class);
                            intent.putExtra("groupIdForReceipt", mGroupId);
                            msg.getReceiptDetails(new GetReceiptDetailsCallback() {
                                @Override
                                public void gotResult(int i, String s, List<ReceiptDetails> list) {
                                    if (i == 0) {
                                        for (ReceiptDetails receipt : list) {
                                            BaseConfig.alreadyRead.clear();
                                            BaseConfig.unRead.clear();
                                            List<UserInfo> alreadyRead = receipt.getReceiptList();
                                            List<UserInfo> unRead = receipt.getUnreceiptList();
                                            BaseConfig.alreadyRead.addAll(alreadyRead);
                                            BaseConfig.unRead.addAll(unRead);
                                            intent.putExtra("noReadCount", unRead.size());
                                            intent.putExtra("alreadyReadCount", alreadyRead.size());
                                            mContext.startActivity(intent);
                                        }
                                    }
                                }
                            });
                        }
                    });
                } else if (msg.getTargetInfo() != null && !((UserInfo) msg.getTargetInfo()).getUserName().equals(JMessageClient.getMyInfo().getUserName())) {
                    holder.text_receipt.setText("未读");
                }
            }
        }
        return convertView;
    }

    //重发对话框
    public void showResendDialog(final ViewHolder holder, final Message msg) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.jmui_cancel_btn) {
                    mDialog.dismiss();
                } else {
                    mDialog.dismiss();
                    switch (msg.getContentType()) {
                        case text:
                        case location:
                        case voice:
                            resendTextOrVoiceOrLocation(holder, msg);
                            break;
                        case image:
                            resendImage(holder, msg);
                            break;
                        case file:
                            resendFile(holder, msg);
                            break;
                    }
                }
            }
        };
        mDialog = DialogCreator.createResendDialog(mContext, listener);
        mDialog.getWindow().setLayout((int) (0.8 * mWidth), WindowManager.LayoutParams.WRAP_CONTENT);
        mDialog.show();
    }

    private void resendTextOrVoiceOrLocation(final ViewHolder holder, Message msg) {
        holder.resend.setVisibility(View.GONE);
        holder.sendingIv.setVisibility(View.VISIBLE);
        holder.sendingIv.startAnimation(mController.mSendingAnim);

        if (!msg.isSendCompleteCallbackExists()) {
            msg.setOnSendCompleteCallback(new BasicCallback() {
                @Override
                public void gotResult(final int status, String desc) {
                    holder.sendingIv.clearAnimation();
                    holder.sendingIv.setVisibility(View.GONE);
                    if (status != 0) {
                        HandleResponseCode.onHandle(mContext, status, false);
                        holder.resend.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        MessageSendingOptions options = new MessageSendingOptions();
        options.setNeedReadReceipt(true);
        JMessageClient.sendMessage(msg, options);
    }

    private void resendImage(final ViewHolder holder, Message msg) {
        holder.sendingIv.setVisibility(View.VISIBLE);
        holder.sendingIv.startAnimation(mController.mSendingAnim);
        holder.picture.setAlpha(0.75f);
        holder.resend.setVisibility(View.GONE);
        holder.progressTv.setVisibility(View.VISIBLE);
        try {
            // 显示上传进度
            msg.setOnContentUploadProgressCallback(new ProgressUpdateCallback() {
                @Override
                public void onProgressUpdate(final double progress) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String progressStr = (int) (progress * 100) + "%";
                            holder.progressTv.setText(progressStr);
                        }
                    });
                }
            });
            if (!msg.isSendCompleteCallbackExists()) {
                msg.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(final int status, String desc) {
                        holder.sendingIv.clearAnimation();
                        holder.sendingIv.setVisibility(View.GONE);
                        holder.progressTv.setVisibility(View.GONE);
                        holder.picture.setAlpha(1.0f);
                        if (status != 0) {
                            HandleResponseCode.onHandle(mContext, status, false);
                            holder.resend.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
            MessageSendingOptions options = new MessageSendingOptions();
            options.setNeedReadReceipt(true);
            JMessageClient.sendMessage(msg, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resendFile(final ViewHolder holder, final Message msg) {
        if (holder.contentLl != null)
            holder.contentLl.setBackgroundColor(Color.parseColor("#86222222"));
        holder.resend.setVisibility(View.GONE);
        holder.progressTv.setVisibility(View.VISIBLE);
        try {
            msg.setOnContentUploadProgressCallback(new ProgressUpdateCallback() {
                @Override
                public void onProgressUpdate(final double progress) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String progressStr = (int) (progress * 100) + "%";
                            holder.progressTv.setText(progressStr);
                        }
                    });
                }
            });
            if (!msg.isSendCompleteCallbackExists()) {
                msg.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(final int status, String desc) {
                        holder.progressTv.setVisibility(View.GONE);
                        //此方法是api21才添加的如果低版本会报错找不到此方法.升级api或者使用ContextCompat.getDrawable
                        holder.contentLl.setBackground(mContext.getResources().getDrawable(R.drawable.jmui_msg_send_bg));
                        if (status != 0) {
                            HandleResponseCode.onHandle(mContext, status, false);
                            holder.resend.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
            MessageSendingOptions options = new MessageSendingOptions();
            options.setNeedReadReceipt(true);
            JMessageClient.sendMessage(msg, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUpdateReceiptCount(long serverMsgId, int unReceiptCnt) {
        for (Message message : mMsgList) {
            if (message.getServerMessageId() == serverMsgId) {
                message.setUnreceiptCnt(unReceiptCnt);
            }
        }
        notifyDataSetChanged();
    }


    public static class ViewHolder {
        public TextView msgTime;
        public CircleImageView headIcon;
        public ImageView ivDocument;
        public TextView displayName;
        public TextView txtContent;
        public ImageView picture;
        public TextView progressTv;
        public ImageButton resend;
        public TextView voiceLength;
        public ImageView voice;
        public ImageView readStatus;
        public TextView location;
        public TextView groupChange;
        public ImageView sendingIv;
        public LinearLayout contentLl;
        public TextView sizeTv;
        public LinearLayout videoPlay;
        public TextView alreadySend;
        public View locationView;
        public LinearLayout ll_businessCard;
        public CircleImageView business_head;
        public TextView tv_nickUser;
        public TextView tv_userName;
        public TextView text_receipt;
        public TextView fileLoad;
    }


    public static abstract class ContentLongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            onContentLongClick((Integer) v.getTag(), v);
            return true;
        }

        public abstract void onContentLongClick(int position, View view);
    }

    public void stopVoice(){
        if(mController != null){
            mController.stopVoice();
        }
    }

}