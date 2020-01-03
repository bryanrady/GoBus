package com.hxd.gobus.mvp.activity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.config.Constant;
import com.luck.picture.lib.photoview.PhotoView;
import butterknife.BindView;
import butterknife.OnClick;

public class ImagePreviewActivity extends BaseActivity{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.photoView)
    PhotoView mPhotoView;

    @OnClick({R.id.ll_toolbar_back})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
        }
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_image_preview;
    }

    @Override
    protected void initView() {
        initToolbar();
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.VISIBLE);
        mTvCenterTitle.setText("图片查看");
        mTvRightTitle.setVisibility(View.GONE);
    }

    @Override
    protected void doBusiness() {
        String imageName = getIntent().getStringExtra("imageName");
        String nativePath = getIntent().getStringExtra("nativePath");
        String imageUrl = getIntent().getStringExtra("imageUrl");
//        if (!TextUtils.isEmpty(imageName)){
//            mTvCenterTitle.setText(imageName);
//        }else{
//            mTvCenterTitle.setText("图片预览");
//        }
        if (!TextUtils.isEmpty(nativePath)){
            Glide.with(this).load(nativePath).into(mPhotoView);
        }else{
            if (!TextUtils.isEmpty(imageUrl)){
            //    Glide.with(this).load("http://192.168.19.34:9999/xuni/"+imageUrl).into(mPhotoView);
                Glide.with(this).load(Constant.ATTACH_PICTURE_DOWNLOAD_URL+imageUrl).into(mPhotoView);
            }
        }
    }

}
