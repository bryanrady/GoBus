package com.hxd.gobus.chat.controller;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.hxd.gobus.chat.activity.MainActivity;
import com.hxd.gobus.chat.activity.fragment.ContactsFragment;
import com.hxd.gobus.chat.activity.fragment.ConversationListFragment;
import com.hxd.gobus.chat.adapter.ViewPagerAdapter;
import com.hxd.gobus.chat.view.MainView;
import com.hxd.gobus.mvp.fragment.HomeFragment;
import com.hxd.gobus.mvp.fragment.MyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chenyn} on 2017/2/20.
 */

public class MainController implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private MainView mMainView;
    private MainActivity mContext;
    private ConversationListFragment mConvListFragment;
    private HomeFragment mApplicationFragment;
    private ContactsFragment mContactsFragment;
    private MyFragment mMeFragment;


    public MainController(MainView mMainView, MainActivity context) {
        this.mMainView = mMainView;
        this.mContext = context;
        setViewPager();
    }

    private void setViewPager() {
        final List<Fragment> fragments = new ArrayList<>();
        // init Fragment
        mConvListFragment = new ConversationListFragment();
        mApplicationFragment = new HomeFragment();
        mContactsFragment = new ContactsFragment();
        mMeFragment = new MyFragment();

        fragments.add(mConvListFragment);
        fragments.add(mApplicationFragment);
        fragments.add(mContactsFragment);
        fragments.add(mMeFragment);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mContext.getSupportFragmentManger(),
                fragments);
        mMainView.setViewPagerAdapter(viewPagerAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.actionbar_msg_btn:
//                mMainView.setCurrentItem(0, false);
//                break;
//            case R.id.actionbar_app_btn:
//                mMainView.setCurrentItem(1, false);
//                break;
//            case R.id.actionbar_wmh_btn:
//                mMainView.setCurrentItem(2, false);
//                break;
//            case R.id.actionbar_contact_btn:
//                mMainView.setCurrentItem(3, false);
//                break;
//            case R.id.actionbar_me_btn:
//                mMainView.setCurrentItem(4, false);
//                break;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mMainView.setButtonColor(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void sortConvList() {
        mConvListFragment.sortConvList();
    }


}