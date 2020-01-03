package com.hxd.gobus.mvp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.ApkInfo;
import com.hxd.gobus.mvp.contract.IMainContract;
import com.hxd.gobus.mvp.fragment.HomeFragment;
import com.hxd.gobus.mvp.fragment.ContactFragment;
import com.hxd.gobus.mvp.fragment.MessageFragment;
import com.hxd.gobus.mvp.fragment.MyFragment;
import com.hxd.gobus.mvp.presenter.MainPresenter;
import com.hxd.gobus.service.DownloadApkService;
import com.hxd.gobus.utils.AppManager;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * @author: wangqingbin
 * @date: 2019/7/16 15:11
 */
public class MainActivity extends BaseActivity<MainPresenter> implements IMainContract.View, HasSupportFragmentInjector {

    private long exitTime = 0;

    private MainFragmentAdapter adapter;

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @Inject
    DispatchingAndroidInjector<Fragment> mFragmentInjector;

    private int TAB_IMGS[] = {
            R.drawable.tab_main_home_selector,
            R.drawable.tab_main_message_selector,
            R.drawable.tab_main_contacts_selector,
            R.drawable.tab_main_my_selector
    };

    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        // 初始化页卡
        initPager();
        setTabs(tabLayout, getLayoutInflater(), TAB_IMGS);
    }

    /**
     * 设置页卡显示效果
     * @param tabLayout
     * @param inflater
     * @param tabImgs
     */
    private void setTabs(TabLayout tabLayout, LayoutInflater inflater,int[] tabImgs) {
        for (int i = 0; i < tabImgs.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View view = inflater.inflate(R.layout.item_main_menu, null);
            // 使用自定义视图，目的是为了便于修改，也可使用自带的视图
            tab.setCustomView(view);

            ImageView imgTab = view.findViewById(R.id.img_tab);
            imgTab.setImageResource(tabImgs[i]);
            tabLayout.addTab(tab);
        }
    }

    private void initPager() {
        adapter = new MainFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // 关联切换
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 取消平滑切换
                viewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void showUpdateDialog(final ApkInfo apkInfo) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.version_upgrade));
        builder.setMessage(apkInfo.getVersionContent());
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.update_now), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent(mContext,DownloadApkService.class);
                intent.putExtra("apkUrl",apkInfo.getDownloadUrl());
                intent.putExtra("fileName","gobus");
                startService(intent);
            }
        });
        builder.setNegativeButton(getString(R.string.not_update_now), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    public class MainFragmentAdapter extends FragmentPagerAdapter {
        public MainFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            switch (i) {
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new MessageFragment();
                    break;
                case 2:
                    fragment = new ContactFragment();
                    break;
                case 3:
                    fragment = new MyFragment();
                    break;
                default:
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

    }

    @Override
    protected void doBusiness() {
        mPresenter.initUser();
        mPresenter.checkApkUpdate();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return mFragmentInjector;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showShortToast(getString(R.string.press_exit_program));
                exitTime = System.currentTimeMillis();
            } else {
                AppManager.getInstance().finishAllActivity();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
