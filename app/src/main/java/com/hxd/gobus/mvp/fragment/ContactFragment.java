package com.hxd.gobus.mvp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseLazyFragment;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.mvp.activity.PersonalDataActivity;
import com.hxd.gobus.mvp.adapter.ContactRecyclerAdapter;
import com.hxd.gobus.mvp.contract.IContactContract;
import com.hxd.gobus.mvp.presenter.ContactPresenter;
import com.hxd.gobus.utils.SearchOperation;
import com.hxd.gobus.views.RecycleViewDivider;
import com.hxd.gobus.views.SearchBox;
import com.hxd.gobus.views.SideBar;

import java.util.List;

import butterknife.BindView;

/**
 * @author: wangqingbin
 * @date: 2019/7/29 14:29
 */

public class ContactFragment extends BaseLazyFragment<ContactPresenter> implements IContactContract.View
        ,SideBar.OnSideBarListener,BaseQuickAdapter.OnItemClickListener,TextWatcher {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.searchBox_contacts)
    SearchBox mSearchBox;

    @BindView(R.id.rv_contacts)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_contacts_letter_tip)
    TextView tvLetterTip;

    @BindView(R.id.sidebar_contacts)
    SideBar mSideBar;

    private ContactRecyclerAdapter mAdapter;
    private SearchOperation mSearchOperation;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_main_contact;
    }

    @Override
    protected void initView(View view) {
        initToolbar();

        mSideBar.setTextView(tvLetterTip);
        mSideBar.setOnSideBarListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, R.drawable.recycleview_divider));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mSearchBox.addTextChangedListener(this);
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.GONE);
        mTvCenterTitle.setText("通讯录");
        mTvCenterTitle.setTextColor(getResources().getColor(R.color.grey_6));
    }

    @Override
    protected void initData() {
        mPresenter.loadContactList();
        mSearchOperation = new SearchOperation(Looper.myLooper());
        mSearchOperation.setCanSearchListener(new SearchOperation.ICanSearchListener() {
            @Override
            public void canSearch(String keyword) {
                startSearch(keyword);
            }
        });
    }

    private void startSearch(String keyword) {
        mPresenter.filterData(keyword);
        mSearchBox.clearFocus();
    }

    @Override
    protected void lazyLoad(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Contact item = (Contact) adapter.getItem(position);
        Intent intent = new Intent(mContext, PersonalDataActivity.class);
        intent.putExtra("TAG", "ContactsFragment");
        intent.putExtra("contacts", item);
        mContext.startActivity(intent);
    }

    @Override
    public void onLetterChanged(String letter) {
        if (mAdapter != null){
            int position = mAdapter.getPositionForSection(letter.charAt(0));
            if (position != -1) {
                mRecyclerView.scrollToPosition(position);
            }
        }
    }

    @Override
    public void showContacts(List<Contact> list) {
        mAdapter = new ContactRecyclerAdapter(R.layout.fm_contacts_item,list);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String filterStr = s.toString().trim();
        if(!TextUtils.isEmpty(filterStr)){
            //我们可以把每次输入框改变的字符串传给一个工具类，并让它来判断是否进行搜索
            mSearchOperation.optionSearch(filterStr);
        }else{
            mPresenter.filterData(filterStr);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
