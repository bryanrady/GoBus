package com.hxd.gobus.mvp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.views.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;

/**
 * Created by pengyu520 on 2016/7/8.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> implements SectionIndexer {

    private List<Contact> mList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private int position;
    private HashMap<Integer,Boolean> isSelected;

    public ContactsAdapter(List<Contact> list, Context context){
        this.mList = list;
        this.mContext = context;
    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        this.isSelected = isSelected;
    }

    public void setItem(int position){
        this.position = position;
    }

    public Contact getItem(int position){
        return mList.get(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fm_contacts_item,parent,false);
        ContactsViewHolder holder = new ContactsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ContactsViewHolder holder, final int position) {
        setItem(position);
        int section=getSectionForPosition(position);
        if (position==getPositionForSection(section)){
            holder.tvHead.setVisibility(View.VISIBLE);
            holder.tvHead.setText(mList.get(position).getFirstLetter());
        }else {
            holder.tvHead.setVisibility(View.GONE);
        }
        holder.tvName.setText(mList.get(position).getName());
        holder.tvUnit.setText(mList.get(position).getUnitName());

        String imageName = mList.get(position).getPhotoUrl();
        String imageUrl = Constant.HEAD_PICTURE_DOWNLOAD_URL + imageName;
        holder.iv_my_head.setTag(imageUrl);
        final String tag = (String) holder.iv_my_head.getTag();
        if (imageUrl.equals(tag)) {
            ImageLoader.getInstance().displayImage(imageUrl, holder.iv_my_head, getOptions());
        }

        if (mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView,position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView,position);
                    return true;
                }
            });
        }

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

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mList.get(i).getFirstLetter();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return mList.get(position).getFirstLetter().charAt(0);
    }

    public void updateData(List<Contact> list){
        this.mList=list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener=listener;
    }
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder{
        TextView tvHead;
        CircleImageView iv_my_head;
        TextView tvName;
        TextView tvUnit;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            tvHead= (TextView) itemView.findViewById(R.id.tv_contacts_item_head);
            iv_my_head = (CircleImageView) itemView.findViewById(R.id.iv_my_head);
            tvName= (TextView) itemView.findViewById(R.id.tv_contacts_item_name);
            tvUnit= (TextView) itemView.findViewById(R.id.tv_contacts_item_unit);
        }
    }
}


