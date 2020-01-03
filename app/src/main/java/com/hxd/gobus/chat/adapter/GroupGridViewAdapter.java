package com.hxd.gobus.chat.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.chat.activity.GroupGridViewActivity;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.utils.SqliteUtils;
import com.hxd.gobus.views.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by ${chenyn} on 2017/5/8.
 */

public class GroupGridViewAdapter extends BaseAdapter {
    private Activity mContext;
    private List<UserInfo> mMemberList;
    private boolean mIsCreator;
    private LayoutInflater mInflater;
    private int mAvatarSize;
    private int mCurrentNum;
    private String groupOwner;

    public GroupGridViewAdapter(GroupGridViewActivity context, List<UserInfo> memberInfoList, boolean isCreator, int size, String groupOwner) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mMemberList = memberInfoList;
        this.mIsCreator = isCreator;
        this.mAvatarSize = size;
        this.groupOwner = groupOwner;
        mCurrentNum = mMemberList.size();

    }

    @Override
    public int getCount() {
        return mIsCreator ? mMemberList.size() + 2 : mMemberList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
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

        //群成员
        if (position < mMemberList.size()) {
            UserInfo userInfo = mMemberList.get(position);

            //设置群主标志
            if(userInfo.getUserName().equals(groupOwner)){
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
            String nameByWorkNo = SqliteUtils.getNameByWorkNo(mContext, displayName);
            viewTag.name.setText(nameByWorkNo);
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
