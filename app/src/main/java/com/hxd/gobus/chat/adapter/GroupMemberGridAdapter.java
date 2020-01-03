package com.hxd.gobus.chat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.utils.SqliteUtils;
import com.hxd.gobus.views.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;

public class GroupMemberGridAdapter extends BaseAdapter {

    private static final String TAG = "GroupMemberGridAdapter";

    private LayoutInflater mInflater;
    //群成员列表
    private List<UserInfo> mMemberList = new ArrayList<UserInfo>();
    private boolean mIsCreator = false;
    //群成员个数
    private int mCurrentNum;
    //用群成员项数余4得到，作为下标查找mRestArray，得到空白项
    private int mRestNum;
    private static final int MAX_GRID_ITEM = 40;
    private boolean mIsGroup;
    private String mTargetId;
    private Context mContext;
    private String groupOwnerId;
    private int mAvatarSize;
    private String mTargetAppKey;
    private boolean mListType = true;//是否显示全部群成员
    //记录空白项的数组
    private int[] mRestArray = new int[] {3, 2, 1, 0, 4};

    //群聊
    public GroupMemberGridAdapter(Context context, List<UserInfo> memberList, boolean isCreator,
                                  int size, String groupOwnerId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mIsGroup = true;
        this.mMemberList = memberList;
        this.groupOwnerId = groupOwnerId;
        mCurrentNum = mMemberList.size();
        this.mIsCreator = isCreator;
        this.mAvatarSize = size;
        initBlankItem(mCurrentNum);
    }

    //单聊
    public GroupMemberGridAdapter(Context context, String targetId, String appKey) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mTargetId = targetId;
        this.mTargetAppKey = appKey;
    }

    public void initBlankItem(int size) {
        if (mMemberList.size() > MAX_GRID_ITEM) {
            mCurrentNum = MAX_GRID_ITEM - 1;
        } else {
            mCurrentNum = mMemberList.size();
        }
        mRestNum = mRestArray[mCurrentNum % 5];
    }

    public void refreshMemberList() {
        if (mMemberList.size() > MAX_GRID_ITEM) {
            mCurrentNum = MAX_GRID_ITEM - 1;
        } else {
            mCurrentNum = mMemberList.size();
        }
        mRestNum = mRestArray[mCurrentNum % 5];
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //如果是普通成员，并且群组成员余4等于3，特殊处理，隐藏下面一栏空白
        if (mCurrentNum % 5 == 4 && !mIsCreator) {
            return mCurrentNum > 14 ? 15 : mCurrentNum + 1;
        } else {
            return mCurrentNum > 13 ? 15 : mCurrentNum + mRestNum + 2;
        }
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mMemberList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ItemViewTag viewTag;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_group, null);
            viewTag = new ItemViewTag(
                    (ImageView) convertView.findViewById(R.id.grid_group_manager),
                    (CircleImageView) convertView.findViewById(R.id.grid_avatar),
                    (TextView) convertView.findViewById(R.id.grid_name),
                    (ImageView) convertView.findViewById(R.id.grid_delete_icon));
            convertView.setTag(viewTag);
        } else {
            viewTag = (ItemViewTag) convertView.getTag();
        }
        //群聊
        if (mIsGroup) {
            //群成员
            if (position < mMemberList.size()) {
                UserInfo userInfo = mMemberList.get(position);
                viewTag.icon.setVisibility(View.VISIBLE);
                viewTag.name.setVisibility(View.VISIBLE);
                if(userInfo.getUserName().equals(groupOwnerId)){
                    viewTag.groupIcon.setImageResource(R.mipmap.ic_group_manager);
                }else{
                    viewTag.groupIcon.setImageResource(R.color.white);
                }
                Contact contacts = SqliteUtils.getDaoByWorkNo(mContext, userInfo.getDisplayName());
                String imageName = contacts.getPhotoUrl();
                if(imageName != null){
                    String imageUrl = Constant.HEAD_PICTURE_DOWNLOAD_URL + imageName;
                    ImageLoader.getInstance().displayImage(imageUrl, viewTag.icon, getOptions());
                }else{
                    viewTag.icon.setImageResource(R.mipmap.jmui_conv_head_icon);
                }

                String displayName = userInfo.getDisplayName();
                String name = SqliteUtils.getNameByWorkNo(mContext,displayName);
                if(TextUtils.isEmpty(name)){
                    viewTag.name.setText(displayName);
                }else{
                    viewTag.name.setText(name);
                }
            }
            viewTag.deleteIcon.setVisibility(View.INVISIBLE);
            if (position < mCurrentNum) {
                viewTag.icon.setVisibility(View.VISIBLE);
                viewTag.name.setVisibility(View.VISIBLE);
            } else if (position == mCurrentNum) {
                viewTag.icon.setImageResource(R.mipmap.chat_detail_add);
                viewTag.icon.setVisibility(View.VISIBLE);
                viewTag.name.setVisibility(View.INVISIBLE);

                //设置删除群成员按钮
            } else if (position == mCurrentNum + 1) {
                if (mIsCreator && mCurrentNum > 1) {
                    viewTag.icon.setImageResource(R.mipmap.chat_detail_del);
                    viewTag.icon.setVisibility(View.VISIBLE);
                    viewTag.name.setVisibility(View.INVISIBLE);
                } else {
                    viewTag.icon.setVisibility(View.GONE);
                    viewTag.name.setVisibility(View.GONE);
                }
                //空白项
            } else {
                viewTag.icon.setVisibility(View.INVISIBLE);
                viewTag.name.setVisibility(View.INVISIBLE);
            }
        } else {
            if (position == 0) {
                Conversation conv = JMessageClient.getSingleConversation(mTargetId, mTargetAppKey);
                UserInfo userInfo = (UserInfo) conv.getTargetInfo();
//                if (!TextUtils.isEmpty(userInfo.getAvatar())) {
//                    userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
//                        @Override
//                        public void gotResult(int status, String desc, Bitmap bitmap) {
//                            if (status == 0) {
//                                Log.d(TAG, "Get small avatar success");
//                                viewTag.icon.setImageBitmap(bitmap);
//                            }
//                        }
//                    });
//                }

                Contact contacts = SqliteUtils.getDaoByWorkNo(mContext, userInfo.getDisplayName());
                String imageName = contacts.getPhotoUrl();
                if(imageName != null){
                    String imageUrl = Constant.HEAD_PICTURE_DOWNLOAD_URL + imageName;
                    ImageLoader.getInstance().displayImage(imageUrl, viewTag.icon, getOptions());
                }else{
                    viewTag.icon.setImageResource(R.mipmap.jmui_conv_head_icon);
                }

                String displayName = userInfo.getNotename();
                if (TextUtils.isEmpty(displayName)) {
                    displayName = userInfo.getNickname();
                    if (TextUtils.isEmpty(displayName)) {
                        displayName = userInfo.getUserName();
                    }
                }
                String name = SqliteUtils.getNameByWorkNo(mContext,displayName);
                if(TextUtils.isEmpty(name)){
                    viewTag.name.setText(displayName);
                }else{
                    viewTag.name.setText(name);
                }
                viewTag.icon.setVisibility(View.VISIBLE);
                viewTag.name.setVisibility(View.VISIBLE);
            } else {
                viewTag.icon.setImageResource(R.mipmap.chat_detail_add);
                viewTag.icon.setVisibility(View.VISIBLE);
                viewTag.name.setVisibility(View.INVISIBLE);
            }

        }

        return convertView;
    }

    /**
     * 显示图片的配置
     * @return
     */
    private DisplayImageOptions getOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.jmui_conv_head_icon)//下载中显示的图片
                .showImageOnFail(R.mipmap.jmui_conv_head_icon)//下载失败时显示的图片
                .cacheInMemory(true)//内存缓存
                .cacheOnDisk(true)//硬盘缓存
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public void setCreator(boolean isCreator) {
        mIsCreator = isCreator;
        notifyDataSetChanged();
    }

    private static class ItemViewTag {

        private ImageView groupIcon;
        private CircleImageView icon;
        private ImageView deleteIcon;
        private TextView name;

        public ItemViewTag(ImageView groupIcon, CircleImageView icon, TextView name, ImageView deleteIcon) {
            this.groupIcon = groupIcon;
            this.icon = icon;
            this.deleteIcon = deleteIcon;
            this.name = name;
        }
    }


}
