package com.hxd.gobus.mvp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.bean.Maintain;
import com.hxd.gobus.bean.Repair;
import com.hxd.gobus.utils.ScreenUtils;
import com.hxd.gobus.views.SideSlipMenuItemView;

import java.util.List;

/**
 * @author: wangqingbin
 * @date: 2019/9/12 14:17
 */

public class MaintainRecordRecyclerAdapter extends RecyclerView.Adapter<MaintainRecordRecyclerAdapter.ViewHolder>
        implements SideSlipMenuItemView.OnSlidingLayoutListener{

    private Context mContext;
    private List<Maintain> mList;

    private SideSlipMenuItemView mItemView;
    private OnSlidingViewClickListener mListener;

    public MaintainRecordRecyclerAdapter(Context context, List<Maintain> list){
        this.mContext = context;
        this.mList = list;
    }

    /**
     * 删除
     * @param position
     */
    public void removeData(int position) {
        if(position < 0 || position > getItemCount()) {
            return;
        }
        mList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mItemView.closeSlideLayout();
        mItemView = null;

    }

    /**
     * 判断是否有菜单打开
     * @return
     */
    public boolean menuIsOpen() {
        if(mItemView != null && mItemView.slideLayoutIsOpen()){
            return true;
        }
        return false;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.recycler_maintain_record_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        viewHolder.mTvLicense.setText(mList.get(position).getVehicleMark());
        if (mList.get(position).getApplyDate() != null){
            String[] split = mList.get(position).getApplyDate().split(" ");
            viewHolder.mTvApplyDate.setText(split[0]);
        }
        viewHolder.mTvGetcarStatus.setText(mList.get(position).getUpkeepTransactStatusStr());
        String approvalStatus = mList.get(position).getApprovalStatus();
        if (approvalStatus != null){
            if("0".equals(approvalStatus)){
                viewHolder.mIvApprovalStatus.setBackgroundResource(R.mipmap.ic_data_status_no_commit);
            }else if("1".equals(approvalStatus)){
                viewHolder.mIvApprovalStatus.setBackgroundResource(R.mipmap.ic_data_status_in_progress);
            }else if("2".equals(approvalStatus)){
                if ("0".equals(mList.get(position).getUpkeepTransactStatus())){
                    viewHolder.mIvApprovalStatus.setBackgroundResource(R.mipmap.ic_data_status_not_registration);
                }else{
                    viewHolder.mIvApprovalStatus.setBackgroundResource(R.mipmap.ic_data_status_complete);
                }
            }else{
                viewHolder.mIvApprovalStatus.setBackgroundResource(R.mipmap.ic_data_status_not_through);
            }
        }
        viewHolder.mLeftLayout.getLayoutParams().width = ScreenUtils.getScreenWidth(mContext);
        viewHolder.mLeftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuIsOpen()) {
                    closeMenu();
                } else {
                    if(mListener != null){
                        int pos = viewHolder.getLayoutPosition();
                        mListener.onItemClick(v, pos, mList.get(position));
                    }
                }
            }
        });
        viewHolder.mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    int pos = viewHolder.getLayoutPosition();
                    mListener.onItemDeleteClick(v, pos, mList.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    //设置在滑动侦听器上
    public void setOnSlidindViewClickListener(OnSlidingViewClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onMenuIsOpen(SideSlipMenuItemView recyclerItemView) {
        mItemView = recyclerItemView;
    }

    @Override
    public void onDownOrMove(SideSlipMenuItemView recyclerItemView) {
        if(menuIsOpen()){
            if(mItemView != recyclerItemView){
                closeMenu();
            }
        }
    }

    // 在滑动视图上单击侦听器
    public interface OnSlidingViewClickListener {
        void onItemClick(View view, int position, Maintain maintain);
        void onItemDeleteClick(View view, int position, Maintain maintain);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout mLeftLayout;
        private TextView mTvLicense;
        private TextView mTvApplyDate;
        private TextView mTvGetcarStatus;
        private ImageView mIvApprovalStatus;

        private TextView mTvSetting;
        private TextView mTvDelete;
        private TextView mTvRead;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            SideSlipMenuItemView slideRecyclerItemView = (SideSlipMenuItemView) itemView;
            mLeftLayout = slideRecyclerItemView.findViewById(R.id.layout_left);
            mTvLicense = slideRecyclerItemView.findViewById(R.id.tv_maintain_record_item_license);
            mTvApplyDate = slideRecyclerItemView.findViewById(R.id.tv_maintain_record_item_apply_date);
            mTvGetcarStatus = slideRecyclerItemView.findViewById(R.id.tv_maintain_record_item_get_car_status);
            mIvApprovalStatus = slideRecyclerItemView.findViewById(R.id.iv_maintain_record_item_approval_status);


            mTvSetting = slideRecyclerItemView.findViewById(R.id.tv_slide_item_setting);
            mTvDelete = slideRecyclerItemView.findViewById(R.id.tv_slide_item_delete);
            mTvRead = slideRecyclerItemView.findViewById(R.id.tv_slide_item_read);

            ((SideSlipMenuItemView)itemView).setOnSlidingLayoutListener(MaintainRecordRecyclerAdapter.this);
        }
    }
}
