package com.hxd.gobus.mvp.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SectionIndexer;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hxd.gobus.R;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.config.Constant;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2019/7/29.
 */

public class ContactRecyclerAdapter extends BaseQuickAdapter<Contact,BaseViewHolder> implements SectionIndexer {

    private List<Contact> mList;
    private HashMap<Integer,Boolean> mIsSelected;

    public ContactRecyclerAdapter(int layoutResId, @Nullable List<Contact> data) {
        super(layoutResId, data);
        this.mList = data;
    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return mIsSelected;
    }

    public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        this.mIsSelected = isSelected;
    }

    @Override
    protected void convert(BaseViewHolder helper, Contact item) {
        int position = helper.getLayoutPosition();
        int section = getSectionForPosition(position);
        if (position == getPositionForSection(section)){
            helper.setText(R.id.tv_contacts_item_head,item.getFirstLetter());
            helper.setGone(R.id.tv_contacts_item_head,true);
        }else {
            helper.setGone(R.id.tv_contacts_item_head,false);
        }
        helper.setText(R.id.tv_contacts_item_name,item.getName());
        helper.setText(R.id.tv_contacts_item_unit,item.getUnitName());

        ImageView headView = helper.getView(R.id.iv_my_head);
        String imageName = item.getPhotoUrl();
//        if (imageName != null){
//            String imageUrl = Constant.HEAD_PICTURE_DOWNLOAD_URL + imageName;
//            String tag = (String) headView.getTag();
//            if (imageUrl.equals(tag)) {
//                headView.setTag(null);
//                Glide.with(headView.getContext())
//                        .load(imageUrl)
//                        .into(headView);
//                headView.setTag(imageUrl);
//            }
//        }
        if (!TextUtils.isEmpty(imageName)){
            String imageUrl = Constant.HEAD_PICTURE_DOWNLOAD_URL + imageName;
            headView.setTag(imageUrl);
            String tag = (String) headView.getTag();
            if (imageUrl.equals(tag)) {
                RequestOptions options = new RequestOptions()
                        .placeholder(R.mipmap.ic_contact_default_head)//图片加载出来前，显示的图片
                        .fallback(R.mipmap.ic_contact_default_head) //url为空的时候,显示的图片
                        .error(R.mipmap.ic_contact_default_head);//图片加载失败后，显示的图片
                Glide.with(mContext)
                        .load(imageUrl)
                        .apply(options)
                        .into(headView);
            }else{
                headView.setImageResource(R.mipmap.ic_contact_default_head);
            }
        }else{
            headView.setImageResource(R.mipmap.ic_contact_default_head);
        }
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

}
