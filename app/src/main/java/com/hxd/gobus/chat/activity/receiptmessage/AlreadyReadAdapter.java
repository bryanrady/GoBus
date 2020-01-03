package com.hxd.gobus.chat.activity.receiptmessage;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.utils.SqliteUtils;
import com.hxd.gobus.views.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by ${chenyn} on 2017/9/6.
 */

public class AlreadyReadAdapter extends BaseAdapter {
    List<UserInfo> alreadyRead = BaseConfig.alreadyRead;
    private Context mContext;
    private LayoutInflater mInflater;

    public AlreadyReadAdapter(com.hxd.gobus.chat.activity.receiptmessage.MessageAlreadyReadFragment context) {
        this.mContext = context.getContext();
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return alreadyRead.size();
    }

    @Override
    public Object getItem(int position) {
        return alreadyRead.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.receipt_already_read, parent, false);
            holder.iv_alreadyRead = (CircleImageView) convertView.findViewById(R.id.iv_alreadyRead);
            holder.tv_alreadyRead = (TextView) convertView.findViewById(R.id.tv_alreadyRead);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        UserInfo info = alreadyRead.get(position);
        String displayName = info.getDisplayName();
        if(!"admin".equals(displayName)){
            Contact contacts = SqliteUtils.getDaoByWorkNo(mContext, displayName);
            if(contacts != null){
                if(contacts.getName() != null){
                    displayName = contacts.getName();
                }
                String imageName = contacts.getPhotoUrl();
                if(imageName != null){
                    String imageUrl = Constant.HEAD_PICTURE_DOWNLOAD_URL + imageName;
                    ImageLoader.getInstance().displayImage(imageUrl, holder.iv_alreadyRead, getOptions());
                }else{
                    holder.iv_alreadyRead.setImageResource(R.mipmap.jmui_conv_head_icon);
                }
            }
        }
        holder.tv_alreadyRead.setText(displayName);
        return convertView;
    }

    /**
     * 显示图片的配置
     * @return
     */
    private DisplayImageOptions getOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.my_personal_head)//下载中显示的图片
                .showImageOnFail(R.mipmap.my_personal_head)//下载失败时显示的图片
                .cacheInMemory(true)//内存缓存
                .cacheOnDisk(true)//硬盘缓存
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public static class ViewHolder {
        CircleImageView iv_alreadyRead;
        TextView tv_alreadyRead;
    }
}
