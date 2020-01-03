package com.hxd.gobus.mvp.activity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.mvp.contract.IFilePreviewContract;
import com.hxd.gobus.mvp.presenter.FilePreviewPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: wangqingbin
 * @date: 2019/9/30 15:03
 */

public class FilePreviewActivity extends BaseActivity<FilePreviewPresenter> implements IFilePreviewContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.iv_file_preview_icon)
    ImageView iv_file_preview_icon;

    @BindView(R.id.tv_file_preview_name)
    TextView tv_file_preview_name;

    @BindView(R.id.ll_file_preview_progress)
    LinearLayout ll_file_preview_progress;

    @BindView(R.id.tv_file_preview_progress)
    TextView tv_file_preview_progress;

    @BindView(R.id.progress)
    ProgressBar mProgress;

    @BindView(R.id.btn_file_preview)
    Button btn_file_preview;

    @OnClick({R.id.ll_toolbar_back,R.id.btn_file_preview})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.btn_file_preview:
                if ("0".equals(mPresenter.getDataStatus())){
                    mPresenter.openFile(mFilePath);
                }else{
                    mPresenter.downFile();
                }
                break;
        }
    }

    private String mFilePath;

    @Override
    protected int bindLayout() {
        return R.layout.activity_file_preview;
    }

    @Override
    protected void initView() {
        initToolbar();
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.VISIBLE);
        mTvCenterTitle.setText("文件查看");
        mTvRightTitle.setVisibility(View.GONE);
    }

    @Override
    protected void doBusiness() {
        mPresenter.getIntentData(getIntent());
    }

    @Override
    public void initOpenFileData(String fileName, String fileSuffix, String filePath) {
        mFilePath = filePath;
        if (!TextUtils.isEmpty(fileSuffix)){
            if ("doc".equals(fileSuffix) || "docx".equals(fileSuffix)){         //word
                iv_file_preview_icon.setBackgroundResource(R.mipmap.ic_attach_word);
            }else if("xls".equals(fileSuffix) || "xlsx".equals(fileSuffix)){    //excel
                iv_file_preview_icon.setBackgroundResource(R.mipmap.ic_attach_excel);
            }else if("pdf".equals(fileSuffix)){    //pdf
                iv_file_preview_icon.setBackgroundResource(R.mipmap.ic_attach_pdf);
            }else{
                iv_file_preview_icon.setBackgroundResource(R.mipmap.ic_attach_other);
            }
        }else{
            iv_file_preview_icon.setBackgroundResource(R.mipmap.ic_attach_other);
        }
        tv_file_preview_name.setText(fileName);

        ll_file_preview_progress.setVisibility(View.GONE);
        btn_file_preview.setText("打 开");

    }

    @Override
    public void initDownloadFileData(String fileName, String fileSuffix) {
        if (!TextUtils.isEmpty(fileSuffix)){
            if ("doc".equals(fileSuffix) || "docx".equals(fileSuffix)){         //word
                iv_file_preview_icon.setBackgroundResource(R.mipmap.ic_attach_word);
            }else if("xls".equals(fileSuffix) || "xlsx".equals(fileSuffix)){    //excel
                iv_file_preview_icon.setBackgroundResource(R.mipmap.ic_attach_excel);
            }else if("pdf".equals(fileSuffix)){    //pdf
                iv_file_preview_icon.setBackgroundResource(R.mipmap.ic_attach_pdf);
            }else{
                iv_file_preview_icon.setBackgroundResource(R.mipmap.ic_attach_other);
            }
        }else{
            iv_file_preview_icon.setBackgroundResource(R.mipmap.ic_attach_other);
        }
        tv_file_preview_name.setText(fileName);

        ll_file_preview_progress.setVisibility(View.GONE);
        btn_file_preview.setText("下 载");
    }

    @Override
    public void startDownload() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ll_file_preview_progress.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void showDownloadProgress(int progress,double current, double total) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_file_preview_progress.setText("下载中("+current+"MB/"+total+"MB)");
                mProgress.setProgress(progress);
            }
        });
    }

    @Override
    public void downloadFinished(String path) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ll_file_preview_progress.setVisibility(View.GONE);
                btn_file_preview.setText("打 开");
                mFilePath = path;
                mPresenter.openFile(mFilePath);
            }
        });
    }

    @Override
    public void downloadFailure() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_file_preview_progress.setText("");
                mProgress.setProgress(0);
                ll_file_preview_progress.setVisibility(View.GONE);
                btn_file_preview.setText("重新下载");
            }
        });
    }
}
