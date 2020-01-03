package com.hxd.gobus.mvp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hxd.gobus.R;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.utils.ContactUtils;
import com.hxd.gobus.utils.ConversationComparator;
import com.hxd.gobus.utils.ConversationTopComparator;
import com.hxd.gobus.utils.NumberValidationUtils;
import com.hxd.gobus.utils.SharePreferenceManager;
import com.hxd.gobus.utils.TimeFormat;
import com.hxd.gobus.utils.ViewHolderUtils;
import com.hxd.gobus.views.CircleImageView;
import com.hxd.gobus.views.SwipeLayoutConv;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.PromptContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by ${chenyn} on 2017/3/30.
 */

public class ConversationListAdapter extends BaseAdapter{

    private List<Conversation> mDatas;
    private Context mContext;
    private Map<String, String> mDraftMap = new HashMap<>();
    private UIHandler mUIHandler = new UIHandler(this);
    private static final int REFRESH_CONVERSATION_LIST = 0x3003;
    private SparseBooleanArray mArray = new SparseBooleanArray();
    private SparseBooleanArray mAtAll = new SparseBooleanArray();
    private HashMap<Conversation, Integer> mAtConvMap = new HashMap<>();
    private HashMap<Conversation, Integer> mAtAllConv = new HashMap<>();
    private UserInfo mUserInfo;
    private GroupInfo mGroupInfo;

    public ConversationListAdapter(Context context, List<Conversation> data) {
        this.mContext = context;
        this.mDatas = data;
    }

    /**
     * 收到消息后将会话置顶
     *
     * @param conv 要置顶的会话
     */
    public void setToTop(Conversation conv) {
        int oldCount = 0;
        int newCount = 0;

        if(mDatas != null && mDatas.size() > 0){
            //如果是旧的会话
            for (Conversation conversation : mDatas) {
                if (conv.getId().equals(conversation.getId())) {
                    //如果是置顶的,就直接把消息插入,会话在list中的顺序不变
                    if (!TextUtils.isEmpty(conv.getExtra())) {
                        mUIHandler.sendEmptyMessageDelayed(REFRESH_CONVERSATION_LIST, 200);
                        //这里一定要return掉,要不还会走到for循环之后的方法,就会再次添加会话
                        return;
                        //如果不是置顶的,就在集合中把原来的那条消息移出,然后去掉置顶的消息数量,插入到集合中
                    } else {
                        //因为后面要改变排序位置,这里要删除
                        mDatas.remove(conversation);
                        //这里要排序,因为第一次登录有漫游消息.离线消息(其中群组变化也是用这个事件下发的);所以有可能会话的最后一条消息
                        //时间比较早,但是事件下发比较晚,这就导致乱序.所以要重新排序.

                        //排序规则,每一个进来的会话去和倒叙list中的会话比较时间,如果进来的会话的最后一条消息就是最早创建的
                        //那么这个会话自然就是最后一个.所以直接跳出循环,否则就一个个向前比较.
                        for (int i = mDatas.size(); i > SharePreferenceManager.getCancelTopSize(); i--) {
                            if (conv.getLatestMessage() != null && mDatas.get(i - 1).getLatestMessage() != null) {
                                if (conv.getLatestMessage().getCreateTime() > mDatas.get(i - 1).getLatestMessage().getCreateTime()) {
                                    oldCount = i - 1;
                                } else {
                                    oldCount = i;
                                    break;
                                }
                            } else {
                                oldCount = i;
                            }
                        }
                        mDatas.add(oldCount, conv);
                        mUIHandler.sendEmptyMessageDelayed(REFRESH_CONVERSATION_LIST, 200);
                        return;
                    }
                }
            }
            if (mDatas.size() == 0) {
                mDatas.add(conv);
            } else {
                //如果是新的会话,直接去掉置顶的消息数之后就插入到list中
                for (int i = mDatas.size(); i > SharePreferenceManager.getCancelTopSize(); i--) {
                    if (conv.getLatestMessage() != null && mDatas.get(i - 1).getLatestMessage() != null) {
                        if (conv.getLatestMessage().getCreateTime() > mDatas.get(i - 1).getLatestMessage().getCreateTime()) {
                            newCount = i - 1;
                        } else {
                            newCount = i;
                            break;
                        }
                    } else {
                        newCount = i;
                    }
                }
                mDatas.add(newCount, conv);
            }
            mUIHandler.sendEmptyMessageDelayed(REFRESH_CONVERSATION_LIST, 200);
        }
    }

    //置顶会话
    public void setConvTop(Conversation conversation) {
        int count = 0;
        //遍历原有的会话,得到有几个会话是置顶的
        for (Conversation conv : mDatas) {
            if (!TextUtils.isEmpty(conv.getExtra())) {
                count++;
            }
        }
        conversation.updateConversationExtra(count + "");
        mDatas.remove(conversation);
        mDatas.add(count, conversation);
        mUIHandler.removeMessages(REFRESH_CONVERSATION_LIST);
        mUIHandler.sendEmptyMessageDelayed(REFRESH_CONVERSATION_LIST, 200);

    }

    //取消会话置顶
    public void setCancelConvTop(Conversation conversation) {
        forCurrent.clear();
        topConv.clear();
        int i = 0;
        for (Conversation oldConv : mDatas) {
            //在原来的会话中找到取消置顶的这个,添加到待删除中
            if (oldConv.getId().equals(conversation.getId())) {
                oldConv.updateConversationExtra("");
                break;
            }
        }
        //全部会话排序
        ConversationComparator comparator = new ConversationComparator();
        Collections.sort(mDatas, comparator);

        //遍历会话找到原来设置置顶的
        for (Conversation con : mDatas) {
            if (!TextUtils.isEmpty(con.getExtra())) {
                forCurrent.add(con);
            }
        }
        topConv.addAll(forCurrent);
        SharePreferenceManager.setCancelTopSize(topConv.size());
        mDatas.removeAll(forCurrent);
        if (topConv != null && topConv.size() > 0) {
            ConversationTopComparator topComparator = new ConversationTopComparator();
            Collections.sort(topConv, topComparator);
            for (Conversation conv : topConv) {
                mDatas.add(i, conv);
                i++;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }

    @Override
    public Conversation getItem(int position) {
        if (mDatas == null) {
            return null;
        }
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_conversation, null);
        }
        final CircleImageView headIcon = ViewHolderUtils.get(convertView, R.id.msg_item_head_icon);
        TextView convName = ViewHolderUtils.get(convertView, R.id.conv_item_name);
        TextView content = ViewHolderUtils.get(convertView, R.id.msg_item_content);
        TextView datetime = ViewHolderUtils.get(convertView, R.id.msg_item_date);
        TextView newMsgNumber = ViewHolderUtils.get(convertView, R.id.new_msg_number);
    //    ImageView groupBlocked = ViewHolderUtils.get(convertView, R.id.iv_groupBlocked);
    //    ImageView newMsgDisturb = ViewHolderUtils.get(convertView, R.id.new_group_msg_disturb);
    //    ImageView newGroupMsgDisturb = ViewHolderUtils.get(convertView, R.id.new_msg_disturb);

        final SwipeLayoutConv swipeLayout = ViewHolderUtils.get(convertView, R.id.swp_layout);
        final TextView delete = ViewHolderUtils.get(convertView, R.id.tv_delete);

        final Conversation convItem = mDatas.get(position);

        String draft = mDraftMap.get(convItem.getId());
//        if (!TextUtils.isEmpty(convItem.getExtra())) {
//            swipeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.conv_list_background));
//        } else {
//            swipeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//        }

        //如果会话草稿为空,显示最后一条消息
        if (TextUtils.isEmpty(draft)) {
            Message lastMsg = convItem.getLatestMessage();
            if (lastMsg != null) {
                TimeFormat timeFormat = new TimeFormat(mContext, lastMsg.getCreateTime());
//                //会话界面时间
                datetime.setText(timeFormat.getTime());
                String contentStr;
                switch (lastMsg.getContentType()) {
                    case image:
                        contentStr = mContext.getString(R.string.type_picture);
                        break;
                    case voice:
                        contentStr = mContext.getString(R.string.type_voice);
                        break;
                    case location:
                        contentStr = mContext.getString(R.string.type_location);
                        break;
                    case file:
                        String extra = lastMsg.getContent().getStringExtra("video");
                        if (!TextUtils.isEmpty(extra)) {
                            contentStr = mContext.getString(R.string.type_smallvideo);
                        } else {
                            contentStr = mContext.getString(R.string.type_file);
                        }
                        break;
                    case video:
                        contentStr = mContext.getString(R.string.type_video);
                        break;
                    case eventNotification:
                        contentStr = mContext.getString(R.string.group_notification);
                        break;
                    case custom:
                        CustomContent customContent = (CustomContent) lastMsg.getContent();
                        Boolean isBlackListHint = customContent.getBooleanValue("blackList");
                        if (isBlackListHint != null && isBlackListHint) {
                            contentStr = mContext.getString(R.string.jmui_server_803008);
                        } else {
                            contentStr = mContext.getString(R.string.type_custom);
                        }
                        break;
                    case prompt:
                        contentStr = ((PromptContent) lastMsg.getContent()).getPromptText();

                        List<String> stringList = getStringList(contentStr);
                        for(String s : stringList){
                            int index = contentStr.indexOf(s);
                            StringBuffer buffer = new StringBuffer(s);
                            Contact contact = ContactUtils.getInstance().queryContactFromDatabase(s);
                            if (contact != null){
                                String name = contact.getName();
                                if(name != null){
                                    StringBuffer replace = buffer.replace(0, 6, name);

                                    StringBuffer buf = new StringBuffer(contentStr);
                                    StringBuffer rep = buf.replace(index, index + 6, replace.toString());
                                    contentStr = rep.toString();
                                }
                            }
                        }
                        break;
                    default:
                        contentStr = ((TextContent) lastMsg.getContent()).getText();
                        break;
                }

                MessageContent msgContent = lastMsg.getContent();
                Boolean isRead = msgContent.getBooleanExtra("isRead");
                Boolean isReadAtAll = msgContent.getBooleanExtra("isReadAtAll");
                if (lastMsg.isAtMe()) {
                    if (null != isRead && isRead) {
                        mArray.delete(position);
                        mAtConvMap.remove(convItem);
                    } else {
                        mArray.put(position, true);
                    }
                }
                if (lastMsg.isAtAll()) {
                    if (null != isReadAtAll && isReadAtAll) {
                        mAtAll.delete(position);
                        mAtAllConv.remove(convItem);
                    } else {
                        mAtAll.put(position, true);
                    }

                }
                long gid = 0;
                if (convItem.getType().equals(ConversationType.group)) {
                    gid = Long.parseLong(convItem.getTargetId());
                }

                if (mAtAll.get(position) && BaseConfig.isAtall.get(gid) != null && BaseConfig.isAtall.get(gid)) {
                    contentStr = "[@所有人] " + contentStr;
                    SpannableStringBuilder builder = new SpannableStringBuilder(contentStr);
                    builder.setSpan(new ForegroundColorSpan(Color.RED), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    content.setText(builder);
                } else if (mArray.get(position) && BaseConfig.isAtMe.get(gid) != null && BaseConfig.isAtMe.get(gid)) {
                    //有人@我 文字提示
                    contentStr = mContext.getString(R.string.somebody_at_me) + contentStr;
                    SpannableStringBuilder builder = new SpannableStringBuilder(contentStr);
                    builder.setSpan(new ForegroundColorSpan(Color.RED), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    content.setText(builder);
                } else {
                    if (lastMsg.getTargetType() == ConversationType.group && !contentStr.equals("[群成员变动]")) {
                        UserInfo info = lastMsg.getFromUser();
                        if(info != null){
                            String fromName = info.getDisplayName();
                            Contact contact = ContactUtils.getInstance().queryContactFromDatabase(fromName);
                            if (contact != null){
                                fromName = contact.getName();
                            }
                            convName.setText(fromName);
                            if (BaseConfig.isAtall.get(gid) != null && BaseConfig.isAtall.get(gid)) {
                                content.setText("[@所有人] " + fromName + ": " + contentStr);
                            } else if (BaseConfig.isAtMe.get(gid) != null && BaseConfig.isAtMe.get(gid)) {
                                content.setText("[有人@我] " + fromName + ": " + contentStr);
                                //如果content是撤回的那么就不显示最后一条消息的发起人名字了
                            } else if (msgContent.getContentType() == ContentType.prompt) {
                                content.setText(contentStr);
                            } else if (info.getUserName().equals(JMessageClient.getMyInfo().getUserName())) {
                                content.setText(contentStr);
                            } else {
                                content.setText(fromName + ": " + contentStr);
                            }

                        }
                    } else {
                        if (BaseConfig.isAtall.get(gid) != null && BaseConfig.isAtall.get(gid)) {
                            content.setText("[@所有人] " + contentStr);
                        } else if (BaseConfig.isAtMe.get(gid) != null && BaseConfig.isAtMe.get(gid)) {
                            content.setText("[有人@我] " + contentStr);
                        } else {
                            if (lastMsg.getUnreceiptCnt() == 0) {
                                if (lastMsg.getTargetType().equals(ConversationType.single) &&
                                        lastMsg.getDirect().equals(MessageDirect.send) &&
                                        !lastMsg.getContentType().equals(ContentType.prompt) &&
                                        //排除自己给自己发送消息
                                        !((UserInfo) lastMsg.getTargetInfo()).getUserName().equals(JMessageClient.getMyInfo().getUserName())) {
                                    content.setText("[已读]" + contentStr);
                                } else {
                                    content.setText(contentStr);
                                }
                            } else {
                                if (lastMsg.getTargetType().equals(ConversationType.single) &&
                                        lastMsg.getDirect().equals(MessageDirect.send) &&
                                        !lastMsg.getContentType().equals(ContentType.prompt) &&
                                        !((UserInfo) lastMsg.getTargetInfo()).getUserName().equals(JMessageClient.getMyInfo().getUserName())) {
                                    contentStr = "[未读]" + contentStr;
                                    SpannableStringBuilder builder = new SpannableStringBuilder(contentStr);
                                    builder.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)),
                                            0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    content.setText(builder);
                                } else {
                                    content.setText(contentStr);
                                }

                            }
                        }
                    }
                }
            } else {
                if (convItem.getLastMsgDate() == 0) {
                    //会话列表时间展示的是最后一条会话,那么如果最后一条消息是空的话就不显示
                    datetime.setText("");
                    content.setText("");
                } else {
                    TimeFormat timeFormat = new TimeFormat(mContext, convItem.getLastMsgDate());
                    datetime.setText(timeFormat.getTime());
                    content.setText("");
                }
            }
        } else {
            draft = mContext.getString(R.string.draft) + draft;
            SpannableStringBuilder builder = new SpannableStringBuilder(draft);
            builder.setSpan(new ForegroundColorSpan(Color.RED), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            content.setText(builder);
        }

        if (convItem.getType().equals(ConversationType.single)) {
            if("admin".equals(convItem.getTitle()) || "系统通知".equals(convItem.getTitle())){
                UserInfo targetInfo = (UserInfo) convItem.getTargetInfo();
                if(targetInfo != null){
                    convName.setText(targetInfo.getNickname());
                }
                headIcon.setImageResource(R.mipmap.jchat_conv_system_notification_icon);
            }else{Contact contact = ContactUtils.getInstance().queryContactFromDatabase(convItem.getTitle());
                if(contact != null){
                    if(TextUtils.isEmpty(contact.getName())){
                        convName.setText(convItem.getTitle());
                    }else{
                        convName.setText(contact.getName());
                    }
                    String imageName = contact.getPhotoUrl();
                    if(!TextUtils.isEmpty(imageName)){
                        String imageUrl = Constant.HEAD_PICTURE_DOWNLOAD_URL + imageName;
                        headIcon.setTag(imageUrl);
                        final String tag = (String) headIcon.getTag();
                        if (imageUrl.equals(tag)) {
                            RequestOptions options = new RequestOptions()
                                    .placeholder(R.mipmap.ic_message_default_head)//图片加载出来前，显示的图片
                                    .fallback(R.mipmap.ic_message_default_head) //url为空的时候,显示的图片
                                    .error(R.mipmap.ic_message_default_head);//图片加载失败后，显示的图片
                            Glide.with(mContext)
                                    .load(imageUrl)
                                    .apply(options)
                                    .into(headIcon);
                        }else{
                            headIcon.setImageResource(R.mipmap.ic_message_default_head);
                        }
                    }else{
                        headIcon.setImageResource(R.mipmap.ic_message_default_head);
                    }
                }else{
                    convName.setText(convItem.getTitle());
                    headIcon.setImageResource(R.mipmap.ic_message_default_head);
                }
            }
        } else {
            mGroupInfo = (GroupInfo) convItem.getTargetInfo();
            if (mGroupInfo != null) {
                mGroupInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                    @Override
                    public void gotResult(int i, String s, Bitmap bitmap) {
                        if (i == 0) {
                            headIcon.setImageBitmap(bitmap);
                        } else {
                            headIcon.setImageResource(R.mipmap.ic_message_default_head);
                        }
                    }
                });
            }
            if(convItem.getTitle().contains(",")){
                String[] split = convItem.getTitle().split(",");
                StringBuffer buffer = new StringBuffer();
                for(int i=0;i<split.length;i++){
                    if(NumberValidationUtils.isNumeric(split[i])){
                        Contact contact = ContactUtils.getInstance().queryContactFromDatabase(convItem.getTitle());
                        if (contact != null){
                            if(!TextUtils.isEmpty(contact.getName())){
                                buffer.append(contact.getName()+",");
                            }else{
                                buffer.append(split[i]+",");
                            }
                        }
                    }
                }
                if(buffer.length()>0){
                    convName.setText(buffer.substring(0,buffer.length()-1));
                }
            }else{
                convName.setText(convItem.getTitle());
            }
        }

        if (convItem.getUnReadMsgCnt() > 0) {
//            newGroupMsgDisturb.setVisibility(View.GONE);
//            newMsgDisturb.setVisibility(View.GONE);
            newMsgNumber.setVisibility(View.GONE);
            if (convItem.getType().equals(ConversationType.single)) {
                if (mUserInfo != null && mUserInfo.getNoDisturb() == 1) {
                //    newMsgDisturb.setVisibility(View.VISIBLE);
                } else {
                    newMsgNumber.setVisibility(View.VISIBLE);
                }
                if (convItem.getUnReadMsgCnt() < 100) {
                    newMsgNumber.setText(String.valueOf(convItem.getUnReadMsgCnt()));
                } else {
                    newMsgNumber.setText("99+");
                }
            } else {
                if (mGroupInfo != null && mGroupInfo.getNoDisturb() == 1) {
                //    newGroupMsgDisturb.setVisibility(View.VISIBLE);
                } else {
                    newMsgNumber.setVisibility(View.VISIBLE);
                }
                if (convItem.getUnReadMsgCnt() < 100) {
                //    newGroupMsgNumber.setText(String.valueOf(convItem.getUnReadMsgCnt()));
                } else {
                    newMsgNumber.setText("99+");
                }
            }

        } else {
//            newGroupMsgDisturb.setVisibility(View.GONE);
//            newMsgDisturb.setVisibility(View.GONE);
            newMsgNumber.setVisibility(View.GONE);
        }

        //禁止使用侧滑功能.如果想要使用就设置为true
        swipeLayout.setSwipeEnabled(false);
        //侧滑删除会话
        swipeLayout.addSwipeListener(new SwipeLayoutConv.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayoutConv layout) {

            }

            @Override
            public void onOpen(SwipeLayoutConv layout) {
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (convItem.getType() == ConversationType.single) {
                            JMessageClient.deleteSingleConversation(((UserInfo) convItem.getTargetInfo()).getUserName());
                        } else {
                            JMessageClient.deleteGroupConversation(((GroupInfo) convItem.getTargetInfo()).getGroupID());
                        }
                        mDatas.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onStartClose(SwipeLayoutConv layout) {

            }

            @Override
            public void onClose(SwipeLayoutConv layout) {

            }

            @Override
            public void onUpdate(SwipeLayoutConv layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayoutConv layout, float xvel, float yvel) {

            }
        });


        return convertView;
    }

    /**
     * 从数字中分段提取数字集合
     * @param str
     * @return
     */
    public List<String> getStringList(String str){
        List<String> digitList = new ArrayList<String>();
        String regex = "\\d*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            if (!"".equals(m.group())){
                digitList.add(m.group());
            }
        }
        return digitList;
    }

    List<Conversation> topConv = new ArrayList<>();
    List<Conversation> forCurrent = new ArrayList<>();

    public void sortConvList() {
        forCurrent.clear();
        topConv.clear();
        int i = 0;
        ConversationComparator comparator = new ConversationComparator();
        Collections.sort(mDatas, comparator);
        for (Conversation con : mDatas) {
            if (!TextUtils.isEmpty(con.getExtra())) {
                forCurrent.add(con);
            }
        }
        topConv.addAll(forCurrent);
        mDatas.removeAll(forCurrent);
        if (topConv != null && topConv.size() > 0) {
            ConversationTopComparator topComparator = new ConversationTopComparator();
            Collections.sort(topConv, topComparator);
            for (Conversation conv : topConv) {
                mDatas.add(i, conv);
                i++;
            }
        }
        notifyDataSetChanged();
    }

    public void addNewConversation(Conversation conv) {
        mDatas.add(0, conv);
        notifyDataSetChanged();
    }

    public void addAndSort(Conversation conv) {
        mDatas.add(conv);
        ConversationComparator comparator = new ConversationComparator();
        Collections.sort(mDatas, comparator);
        notifyDataSetChanged();
    }

    public void deleteConversation(Conversation conversation) {
        mDatas.remove(conversation);
        notifyDataSetChanged();
    }

    public void putDraftToMap(Conversation conv, String draft) {
        mDraftMap.put(conv.getId(), draft);
    }

    public void delDraftFromMap(Conversation conv) {
        mArray.delete(mDatas.indexOf(conv));
        mAtConvMap.remove(conv);
        mAtAllConv.remove(conv);
        mDraftMap.remove(conv.getId());
        notifyDataSetChanged();
    }

    public void updateData(List<Conversation> list){
        this.mDatas=list;
        notifyDataSetChanged();
    }

    public String getDraft(String convId) {
        return mDraftMap.get(convId);
    }

    public boolean includeAtMsg(Conversation conv) {
        if (mAtConvMap.size() > 0) {
            Iterator<Map.Entry<Conversation, Integer>> iterator = mAtConvMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Conversation, Integer> entry = iterator.next();
                if (conv == entry.getKey()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean includeAtAllMsg(Conversation conv) {
        if (mAtAllConv.size() > 0) {
            Iterator<Map.Entry<Conversation, Integer>> iterator = mAtAllConv.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Conversation, Integer> entry = iterator.next();
                if (conv == entry.getKey()) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getAtMsgId(Conversation conv) {
        return mAtConvMap.get(conv);
    }

    public int getatAllMsgId(Conversation conv) {
        return mAtAllConv.get(conv);
    }

    public void putAtConv(Conversation conv, int msgId) {
        mAtConvMap.put(conv, msgId);
    }

    public void putAtAllConv(Conversation conv, int msgId) {
        mAtAllConv.put(conv, msgId);
    }

    private static class UIHandler extends Handler {

        private final WeakReference<ConversationListAdapter> mAdapter;

        public UIHandler(ConversationListAdapter adapter) {
            mAdapter = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (mAdapter != null && mAdapter.get() != null) {
                switch (msg.what) {
                    case REFRESH_CONVERSATION_LIST:
                        mAdapter.get().notifyDataSetChanged();
                        break;
                }
            }
        }
    }

}
