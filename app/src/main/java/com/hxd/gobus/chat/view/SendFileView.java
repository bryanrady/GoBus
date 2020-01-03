package com.hxd.gobus.chat.view;

import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxd.gobus.R;


public class SendFileView extends RelativeLayout {

    private ScrollControlViewPager mViewContainer;
    private Button[] mBtnArray;
    private int[] mBtnIdArray;
    private ImageView[] mIVArray;
    private int[] mIVIdArray;
    private Button mSendBtn;
    private TextView mTotalSize;
    private Context mContext;

    private Toolbar mToolbar;
    private LinearLayout llBack;
    private TextView tvToolbarTitle;

    public SendFileView(Context context) {
        super(context);
        this.mContext = context;
    }

    public SendFileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public void initModule(AppCompatActivity activity) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        llBack = (LinearLayout) findViewById(R.id.ll_toolbar_back);
        tvToolbarTitle = (TextView) findViewById(R.id.tv_toolbar_center_title);
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("文件");
        mToolbar.setTitle("");// 标题的文字需在setSupportActionBar之前，不然会无效
        activity.setSupportActionBar(mToolbar);

        mViewContainer = (ScrollControlViewPager) findViewById(R.id.viewpager);

        mBtnIdArray = new int[] { R.id.actionbar_file_btn, R.id.actionbar_video_btn,
                R.id.actionbar_album_btn, R.id.actionbar_audio_btn, R.id.actionbar_other_btn};
        mIVIdArray = new int[] { R.id.slipping_1, R.id.slipping_2, R.id.slipping_3,
                R.id.slipping_4, R.id.slipping_5 };
        mBtnArray = new Button[mBtnIdArray.length];
        mIVArray = new ImageView[mBtnIdArray.length];
        for (int i=0; i < mBtnIdArray.length; i++) {
            mBtnArray[i] = (Button) findViewById(mBtnIdArray[i]);
            mIVArray[i] = (ImageView) findViewById(mIVIdArray[i]);
        }
        mIVArray[0].setVisibility(VISIBLE);
        mBtnArray[0].setTextColor(getResources().getColor(R.color.colorPrimary));
        mSendBtn = (Button) findViewById(R.id.send_file_btn);
        mTotalSize = (TextView) findViewById(R.id.size_desc);
    }

    public void setOnClickListener(OnClickListener listener) {
        llBack.setOnClickListener(listener);
        mSendBtn.setOnClickListener(listener);
        for (int i=0; i<mBtnIdArray.length; i++) {
            mBtnArray[i].setOnClickListener(listener);
        }
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        mViewContainer.addOnPageChangeListener(onPageChangeListener);
    }

    public void setViewPagerAdapter(FragmentPagerAdapter adapter) {
        mViewContainer.setAdapter(adapter);
    }

    public void setCurrentItem(int index) {
        mViewContainer.setCurrentItem(index);
        for (int i=0; i<mBtnIdArray.length; i++) {
            if (i == index) {
                mIVArray[i].setVisibility(VISIBLE);
                mBtnArray[i].setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                mIVArray[i].setVisibility(INVISIBLE);
                mBtnArray[i].setTextColor(getResources().getColor(R.color.send_file_action_bar));
            }
        }
    }

    public void updateSelectedState(int selectedNum, String sizeStr) {
        String sendStr;
        if (selectedNum != 0) {
            sendStr = mContext.getString(R.string.jmui_send) + "(" + selectedNum + ")";
        } else {
            sendStr = mContext.getString(R.string.jmui_send);
        }
        mSendBtn.setText(sendStr);
        mTotalSize.setText(sizeStr);
    }

    public void setScroll(boolean isScroll) {
        mViewContainer.setScroll(isScroll);
    }
}
