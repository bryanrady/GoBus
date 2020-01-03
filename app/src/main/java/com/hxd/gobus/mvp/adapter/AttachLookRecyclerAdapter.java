package com.hxd.gobus.mvp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxd.gobus.R;
import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.utils.FileUtil;

import java.util.List;

/**
 * @author: wangqingbin
 * @date: 2019/9/27 10:57
 */

public class AttachLookRecyclerAdapter extends RecyclerView.Adapter<AttachLookRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<Attach> mList;
    private OnItemClickListener mListener;

    public AttachLookRecyclerAdapter(Context context, List<Attach> data) {
        this.mContext = context;
        this.mList = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.recycler_attach_look_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.rl_attach_shadow.setVisibility(View.VISIBLE);
        String attachAddress = mList.get(position).getAttachAddress();
        String fileSuffix = FileUtil.getFileNameNoEx(attachAddress);
        if (!TextUtils.isEmpty(fileSuffix)){
            if ("doc".equals(fileSuffix) || "docx".equals(fileSuffix)){         //word
                holder.iv_attach.setBackgroundResource(R.mipmap.ic_attach_word);
            }else if("xls".equals(fileSuffix) || "xlsx".equals(fileSuffix)){    //excel
                holder.iv_attach.setBackgroundResource(R.mipmap.ic_attach_excel);
            }else if("pdf".equals(fileSuffix)){    //pdf
                holder.iv_attach.setBackgroundResource(R.mipmap.ic_attach_pdf);
            }else if("png".equals(fileSuffix) || "jpg".equals(fileSuffix) || "jpeg".equals(fileSuffix)){    //图片
                if (!TextUtils.isEmpty(mList.get(position).getNativePath())){
                    Glide.with(mContext)
                            .load(mList.get(position).getNativePath())
                            .placeholder(R.mipmap.ic_attach_default)
                            .error(R.mipmap.ic_attach_default)
                            //        .apply(RequestOptions.overrideOf(120, 120).centerCrop())
                            .override(120,120)
                            .centerCrop()
                            .into(holder.iv_attach);
                }else{
                    Glide.with(mContext)
                            .load(Constant.ATTACH_PICTURE_DOWNLOAD_URL+attachAddress)
                    //        .load("http://192.168.19.34:9999/xuni/"+attachAddress)
                            .placeholder(R.mipmap.ic_attach_default)
                            .error(R.mipmap.ic_attach_default)
                            .override(120,120)
                            .centerCrop()
                            .into(holder.iv_attach);
                }
            }else{
                holder.iv_attach.setBackgroundResource(R.mipmap.ic_attach_other);
            }
        }else{
            holder.iv_attach.setBackgroundResource(R.mipmap.ic_attach_other);
        }

        String attachName = mList.get(position).getAttachName();
        if (!TextUtils.isEmpty(attachName)){
            holder.tv_attach_name.setText(attachName);
        }
        holder.iv_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(fileSuffix)){
                    if("png".equals(fileSuffix) || "jpg".equals(fileSuffix) || "jpeg".equals(fileSuffix)){    //图片
                        if(mListener != null){
                            mListener.onItemImageClick(position,mList.get(position));
                        }
                    }else{
                        if(mListener != null){
                            mListener.onItemFileClick(position,mList.get(position));
                        }
                    }
                }else{
                    if(mListener != null){
                        mListener.onItemFileClick(position,mList.get(position));
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public interface OnItemClickListener{
        void onItemImageClick(int position, Attach item);
        void onItemFileClick(int position, Attach item);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout rl_attach_shadow;
        private ImageView iv_attach;
        private TextView tv_attach_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rl_attach_shadow = itemView.findViewById(R.id.rl_attach_shadow);
            iv_attach = itemView.findViewById(R.id.iv_attach);
            tv_attach_name = itemView.findViewById(R.id.tv_attach_name);
        }
    }

}