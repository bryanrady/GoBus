package com.hxd.gobus.mvp.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hxd.gobus.R;
import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.DatumDetail;

import java.util.List;

/**
 * 审核Item适配器
 * @author: wangqingbin
 * @date: 2019/9/17 16:03
 */

public class ApprovalItemRecyclerAdapter extends BaseQuickAdapter<DatumDetail,BaseViewHolder> {

    private List<DatumDetail> mList;
    private OnItemClickListener mListener;

    public ApprovalItemRecyclerAdapter(int layoutResId, @Nullable List<DatumDetail> data) {
        super(layoutResId, data);
        this.mList = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, DatumDetail item) {
        helper.setImageResource(R.id.iv_audit_step_head, R.mipmap.ic_my_default_head);
        if (item.isReminder()){
            helper.setImageResource(R.id.iv_audit_status_item, R.mipmap.ic_approval_item_in_progress);
            helper.setText(R.id.tv_audit_step_name,item.getApprovalUser());
            helper.setText(R.id.tv_audit_step_status, "审核中");

            helper.setGone(R.id.tv_audit_step_reminder, true);
            helper.setGone(R.id.tv_audit_step_time, false);

            helper.setOnClickListener(R.id.tv_audit_step_reminder, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemReminderClick(item);
                }
            });
        }else{
            String approvalStatus = item.getApprovalStatus();
            if ("0".equals(approvalStatus)){
                helper.setImageResource(R.id.iv_audit_status_item, R.mipmap.ic_approval_item_through);
            }else if("1".equals(approvalStatus)){
                helper.setImageResource(R.id.iv_audit_status_item, R.mipmap.ic_approval_item_not_through);
            }else{
                helper.setImageResource(R.id.iv_audit_status_item, R.mipmap.ic_approval_item_in_progress);
            }
            helper.setText(R.id.tv_audit_step_name,item.getApprovalUser());
            helper.setText(R.id.tv_audit_step_status, item.getApprovalIdea());

            helper.setText(R.id.tv_audit_step_time,item.getApprovalDate());
            helper.setGone(R.id.tv_audit_step_reminder, false);
            helper.setGone(R.id.tv_audit_step_time, true);
        }
        if (mList != null && mList.size() > 0){
            if (helper.getLayoutPosition() == mList.size()-1){
                helper.setGone(R.id.audit_step_vertical_line,false);
            }else{
                helper.setGone(R.id.audit_step_vertical_line,true);
            }
        }
    }

    public interface OnItemClickListener{
        void onItemReminderClick(DatumDetail item);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

}
