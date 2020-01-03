package com.hxd.gobus.mvp.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.TabTitle;
import com.hxd.gobus.mvp.fragment.ApprovalTabFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * @author: wangqingbin
 * @date: 2019/8/30 10:13
 */

public class MobileApprovalActivity extends BaseActivity implements HasSupportFragmentInjector {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.tl_tab)
    TabLayout mTabTl;

    @BindView(R.id.vp_content)
    ViewPager mContentVp;

    @OnClick({R.id.ll_toolbar_back})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
        }
    }

    @Inject
    DispatchingAndroidInjector<Fragment> mFragmentInjector;

    private List<TabTitle> tabIndicators;
    private List<Fragment> tabFragments;
    private ContentPagerAdapter contentAdapter;

    @Override
    protected int bindLayout() {
        return R.layout.activity_mobile_approval;
    }

    @Override
    protected void initView() {
        initToolbar();
        initContent();
        initTab();
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.VISIBLE);
        mTvCenterTitle.setText("移动审批");
        mTvCenterTitle.setTextColor(getResources().getColor(R.color.grey_6));
        mTvRightTitle.setVisibility(View.GONE);
    }

    private void initTab(){
        mTabTl.setTabMode(TabLayout.MODE_FIXED);
        mTabTl.setTabTextColors(ContextCompat.getColor(this, R.color.grey_6), ContextCompat.getColor(this, R.color.colorAccent));
        mTabTl.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent));
        ViewCompat.setElevation(mTabTl, 10);
        mTabTl.setupWithViewPager(mContentVp);
    }

    private void initContent(){
        tabIndicators = new ArrayList<>();
        TabTitle tabTitle1 = new TabTitle();
        tabTitle1.setPosition("0");
        tabTitle1.setTitle("代办事项");
        tabIndicators.add(tabTitle1);

        TabTitle tabTitle2 = new TabTitle();
        tabTitle2.setPosition("1");
        tabTitle2.setTitle("已办事项");
        tabIndicators.add(tabTitle2);

        tabFragments = new ArrayList<>();
        for (TabTitle tab : tabIndicators) {
            tabFragments.add(ApprovalTabFragment.newInstance(tab.getPosition()));
        }
        contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        mContentVp.setAdapter(contentAdapter);
    }

    @Override
    protected void doBusiness() {

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return mFragmentInjector;
    }

    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return tabIndicators.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabIndicators.get(position).getTitle();
        }
    }
}
