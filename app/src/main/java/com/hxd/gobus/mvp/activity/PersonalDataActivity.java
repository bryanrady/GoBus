package com.hxd.gobus.mvp.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.bean.User;
import com.hxd.gobus.chat.activity.ChatActivity;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.mvp.contract.IPersonalDataContract;
import com.hxd.gobus.mvp.presenter.PersonalDataPresenter;
import com.hxd.gobus.views.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * @author: wangqingbin
 * @date: 2019/8/8 16:53
 */

public class PersonalDataActivity extends BaseActivity<PersonalDataPresenter> implements IPersonalDataContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.iv_personal_head_portrait)
    CircleImageView iv_personal_head_portrait;

    @BindView(R.id.tv_personal_data_name)
    TextView tv_personal_data_name;

    @BindView(R.id.tv_personal_data_work_no)
    TextView tv_personal_data_work_no;

    @BindView(R.id.tv_personal_data_unit)
    TextView tv_personal_data_unit;

    @BindView(R.id.tv_personal_data_position)
    TextView tv_personal_data_position;

    @BindView(R.id.tv_personal_data_telephone)
    TextView tv_personal_data_telephone;

    @BindView(R.id.tv_personal_data_email)
    TextView tv_personal_data_email;

    @BindView(R.id.ll_personal_data_send)
    LinearLayout ll_personal_data_send;

    @BindView(R.id.btn_personal_data_send)
    Button btn_personal_data_send;

    @OnClick({R.id.ll_toolbar_back,R.id.btn_personal_data_send})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.btn_personal_data_send:
                mPresenter.sendMessage();
                break;
        }
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_personal_data;
    }

    @Override
    protected void initView() {
        initToolbar();
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.VISIBLE);
        mTvCenterTitle.setTextColor(getResources().getColor(R.color.grey_6));
        mTvRightTitle.setVisibility(View.GONE);
    }

    @Override
    protected void doBusiness() {
        mPresenter.initData(getIntent());
    }

    @Override
    public void showToolbarTitle(String title) {
        mTvCenterTitle.setText(title);
    }

    @Override
    public void showHeadPortrait(String imageUrl) {
    //    Glide.with(this).load(imageUrl).into(iv_personal_head_portrait);
    }

    @Override
    public void setSendLayoutVisible(boolean flag) {
//        if (flag){
//            ll_personal_data_send.setVisibility(View.VISIBLE);
//        }else{
//            ll_personal_data_send.setVisibility(View.GONE);
//        }
        ll_personal_data_send.setVisibility(View.GONE);
    }

    @Override
    public void showUserInfo(User user) {
        tv_personal_data_name.setText(user.getName());
        tv_personal_data_work_no.setText(user.getWorkNo());
        tv_personal_data_unit.setText(user.getUnitName());
        tv_personal_data_position.setText(user.getTechnicalPost());
        tv_personal_data_telephone.setText(user.getMobilePhone());
        tv_personal_data_email.setText(user.getEmail());
    }

    @Override
    public void showContact(Contact contact) {
        tv_personal_data_name.setText(contact.getName());
        tv_personal_data_work_no.setText(contact.getWorkNo());
        tv_personal_data_unit.setText(contact.getUnitName());
        tv_personal_data_position.setText(contact.getTechnicalPost());
        tv_personal_data_telephone.setText(contact.getMobilePhone());
        tv_personal_data_email.setText(contact.getEmail());
    }

    @Override
    public void intoChatActivity(UserInfo info) {
        Intent intent = new Intent();
        intent.setClass(this, ChatActivity.class);
        intent.putExtra(BaseConfig.TARGET_ID, info.getUserName());
        intent.putExtra(BaseConfig.TARGET_APP_KEY, info.getAppKey());
        String notename = info.getNotename();
        if (TextUtils.isEmpty(notename)) {
            notename = info.getNickname();
            if (TextUtils.isEmpty(notename)) {
                notename = info.getUserName();
            }
        }
        intent.putExtra(BaseConfig.CONV_TITLE, notename);
        startActivity(intent);
        defaultFinish();
    }
}
