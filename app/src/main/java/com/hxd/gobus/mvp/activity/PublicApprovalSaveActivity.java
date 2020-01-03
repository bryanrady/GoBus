package com.hxd.gobus.mvp.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.Authority;
import com.hxd.gobus.config.VehicleConfig;
import com.hxd.gobus.mvp.contract.IPublicApprovalSaveContract;
import com.hxd.gobus.mvp.presenter.PublicApprovalSavePresenter;
import com.hxd.gobus.utils.DateUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: wangqingbin
 * @date: 2019/9/20 14:51
 */

public class PublicApprovalSaveActivity extends BaseActivity<PublicApprovalSavePresenter>
        implements IPublicApprovalSaveContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.tv_public_approval_type)
    TextView tv_public_approval_type;

    @BindView(R.id.tv_public_approval_time)
    TextView tv_public_approval_time;

    @BindView(R.id.tv_public_approval_status)
    TextView tv_public_approval_status;

    @BindView(R.id.et_public_approval_advice)
    EditText et_public_approval_advice;

    @BindView(R.id.btn_public_approval_save)
    Button btn_public_approval_save;

    @OnClick({R.id.ll_toolbar_back,R.id.btn_public_approval_save})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.btn_public_approval_save:
                String approvalAdvice = et_public_approval_advice.getText().toString().trim();
                if (TextUtils.isEmpty(approvalAdvice)){
                    showShortToast("审核意见不能为空!");
                    return;
                }
                mPresenter.saveApprovalInfo(mStatus,approvalAdvice);
                break;
        }
    }

    private String mStatus;

    @Override
    protected int bindLayout() {
        return R.layout.activity_public_approval_save;
    }

    @Override
    protected void initView() {
        initToolbar();
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.VISIBLE);
        mTvCenterTitle.setText("流程审核");
        mTvRightTitle.setVisibility(View.GONE);
    }

    @Override
    protected void doBusiness() {
        mPresenter.getPublicApprovalData(getIntent());
    }

    @Override
    public void initPublicData(Authority authority,String approvalStatus) {
        mStatus = approvalStatus;

        tv_public_approval_type.setText(authority.getDatumTypeName());
        tv_public_approval_time.setText(DateUtils.getCurTimeFormat(DateUtils.FORMAT_1));

        if (VehicleConfig.APPROVAL_DISAGREE.equals(mStatus)){
            tv_public_approval_status.setText("退回");
            tv_public_approval_status.setTextColor(getResources().getColor(R.color.approval_not_through));
            et_public_approval_advice.setText("不同意!");
        }else if(VehicleConfig.APPROVAL_AGREE.equals(mStatus)){
            tv_public_approval_status.setText("通过");
            tv_public_approval_status.setTextColor(getResources().getColor(R.color.approval_complete));
            et_public_approval_advice.setText("同意!");
        }
    }

    @Override
    public void saveApprovalSuccess() {
        Intent intent = new Intent();
        intent.putExtra("approval_success", VehicleConfig.APPROVAL_SUCCESS);
        setResult(RESULT_OK,intent);
        defaultFinish();
    }

}