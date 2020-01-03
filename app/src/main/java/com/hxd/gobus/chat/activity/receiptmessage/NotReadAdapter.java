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
 * Created by ${chenyn} on 2017/9/5.
 */

public class NotReadAdapter extends BaseAdapter {

    List<UserInfo> unRead = BaseConfig.unRead;
    private Context mContext;
    private LayoutInflater mInflater;


    public NotReadAdapter(MessageNotReadFragment context) {
        this.mContext = context.getContext();
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return unRead.size();
    }

    @Override
    public Object getItem(int position) {
        return unRead.get(position);
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
            convertView = mInflater.inflate(R.layout.receipt_no_read, parent, false);
            holder.iv_noRead = (CircleImageView) convertView.findViewById(R.id.iv_noRead);
            holder.tv_noRead = (TextView) convertView.findViewById(R.id.tv_noRead);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        UserInfo info = unRead.get(position);
        String displayName = info.getDisplayName();
        if(!"admin".equals(displayName)){
            Contact contacts = SqliteUtils.getDaoByWorkNo(mContext, displayName);
            if(contacts != null) {
                if (contacts.getName() != null) {
                    displayName = contacts.getName();
                }
                String imageName = contacts.getPhotoUrl();
                if(imageName != null){
                    String imageUrl = Constant.HEAD_PICTURE_DOWNLOAD_URL + imageName;
                    ImageLoader.getInstance().displayImage(imageUrl, holder.iv_noRead, getOptions());
                }else{
                    holder.iv_noRead.setImageResource(R.mipmap.jmui_conv_head_icon);
                }
            }
        }
        holder.tv_noRead.setText(displayName);
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
        CircleImageView iv_noRead;
        TextView tv_noRead;
    }
}
