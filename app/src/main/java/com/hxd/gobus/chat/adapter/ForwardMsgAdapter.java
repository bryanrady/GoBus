package com.hxd.gobus.chat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.utils.NumberValidationUtils;
import com.hxd.gobus.utils.SqliteUtils;
import com.hxd.gobus.views.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by ${chenyn} on 2017/7/16.
 */

public class ForwardMsgAdapter extends BaseAdapter {
    private Context mContext;
    private List<Conversation> mConvList;

    public ForwardMsgAdapter(Context context, List<Conversation> conversationList) {
        this.mContext = context;
        this.mConvList = conversationList;
    }

    @Override
    public int getCount() {
        return mConvList.size();
    }

    @Override
    public Object getItem(int position) {
        return mConvList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contact, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.avatar = (CircleImageView) convertView.findViewById(R.id.head_icon_iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Conversation conversation = mConvList.get(position);
        if (conversation.getType() == ConversationType.group) {
            holder.name.setText(conversation.getTitle());
            holder.avatar.setImageResource(R.mipmap.jmui_msg_group_head_icon);
        } else {
            UserInfo userInfo = (UserInfo) conversation.getTargetInfo();
            if(NumberValidationUtils.isNumeric(userInfo.getDisplayName())){
                Contact contacts = SqliteUtils.getDaoByWorkNo(mContext, userInfo.getDisplayName());
                holder.name.setText(contacts.getName());
                String imageName = contacts.getPhotoUrl();
                if(imageName == null){
                    holder.avatar.setImageResource(R.mipmap.jmui_msg_head_icon);
                }else{
                    String imageUrl = Constant.HEAD_PICTURE_DOWNLOAD_URL + imageName;
                    ImageLoader.getInstance().displayImage(imageUrl, holder.avatar, getOptions());
                }
            }else{
                holder.name.setText(userInfo.getDisplayName());
                holder.avatar.setImageResource(R.mipmap.jmui_msg_head_icon);
            }

//            if (userInfo.getAvatarFile() != null) {
//                holder.avatar.setImageBitmap(BitmapFactory.decodeFile(userInfo.getAvatarFile().getAbsolutePath()));
//            }else {
//                holder.avatar.setImageResource(R.drawable.jmui_head_icon);
//            }
        }
        return convertView;
    }

    /**
     * 显示图片的配置
     * @return
     */
    private DisplayImageOptions getOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.jmui_msg_head_icon)//下载中显示的图片
                .showImageOnFail(R.mipmap.jmui_msg_head_icon)//下载失败时显示的图片
                .cacheInMemory(true)//内存缓存
                .cacheOnDisk(true)//硬盘缓存
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    private static class ViewHolder {
        TextView name;
        CircleImageView avatar;
    }
}
