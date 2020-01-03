package com.hxd.gobus.chat.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.chat.utils.SharePreferenceManager;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.utils.FileUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.callback.ProgressUpdateCallback;
import cn.jpush.im.android.api.content.FileContent;
import cn.jpush.im.android.api.model.Message;

/**
 * Created by ${chenyn} on 2017/5/19.
 */

public class DownLoadActivity extends Activity {

    private Context mContext;
    private Message mMessage;
    private TextView mFileName;
    private TextView mProcess;
    private ProgressBar mProcessBar;
    private Button mBtnDown;
    private FileContent mFileContent;
    private ImageView mImg_file;
    private TextView mTv_titleName;
    private ImageView mIv_back;
    private int mProcessNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load);
        mContext = this;
        mImg_file = (ImageView) findViewById(R.id.file_img);
        mFileName = (TextView) findViewById(R.id.file_name);
        mProcess = (TextView) findViewById(R.id.process_num);
        mProcessBar = (ProgressBar) findViewById(R.id.processbar);
        mBtnDown = (Button) findViewById(R.id.btn_down);
        mTv_titleName = (TextView) findViewById(R.id.tv_titleName);
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        EventBus.getDefault().register(this);

        mFileName.setText(mFileContent.getFileName());
        mTv_titleName.setText(mFileContent.getFileName());
        long fileSize = mFileContent.getFileSize();
        mBtnDown.setText("下载(" + byteToMB(fileSize) + ")");
        mBtnDown.setTextColor(Color.WHITE);

        String fileType = mFileContent.getStringExtra("fileType");
        if(fileType == null){
            FileContent fileContent1 = mFileContent;
            String fileName = fileContent1.getFileName();
            if(fileName != null){
                fileType = fileName.substring(fileName.lastIndexOf('.')+1,fileName.length());
            }
        }
        Drawable drawable;
        if (fileType != null && (fileType.equals("mp4") || fileType.equals("mov") || fileType.equals("rm") ||
                fileType.equals("rmvb") || fileType.equals("wmv") || fileType.equals("avi") ||
                fileType.equals("3gp") || fileType.equals("mkv"))) {
            drawable = mContext.getResources().getDrawable(R.mipmap.jmui_video);
        } else if (fileType != null && (fileType.equals("wav") || fileType.equals("mp3") || fileType.equals("wma") || fileType.equals("midi"))) {
            drawable = mContext.getResources().getDrawable(R.mipmap.jmui_audio);
        } else if (fileType != null && (fileType.equals("ppt") || fileType.equals("pptx") || fileType.equals("doc") ||
                fileType.equals("docx") || fileType.equals("pdf") || fileType.equals("xls") ||
                fileType.equals("xlsx") || fileType.equals("txt") || fileType.equals("wps"))) {
            drawable = mContext.getResources().getDrawable(R.mipmap.jmui_document);
            //.jpeg .jpg .png .bmp .gif
        } else if (fileType != null && (fileType.equals("jpeg") || fileType.equals("jpg") || fileType.equals("png") ||
                fileType.equals("bmp") || fileType.equals("gif"))) {
            drawable = mContext.getResources().getDrawable(R.mipmap.image_file);
        } else {
            drawable = mContext.getResources().getDrawable(R.mipmap.jmui_other);
        }
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        mImg_file.setImageBitmap(bitmapDrawable.getBitmap());

        mBtnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBtnDown.setVisibility(View.GONE);
                mProcess.setVisibility(View.VISIBLE);
                mProcessBar.setVisibility(View.VISIBLE);
                mMessage.setOnContentDownloadProgressCallback(new ProgressUpdateCallback() {

                    @Override
                    public void onProgressUpdate(double percent) {
                        mProcessNum = (int) (percent * 100);
                        mProcess.setText(mProcessNum + "%");
                        mHandler.post(progressBar);
                    }
                });

                mFileContent.downloadFile(mMessage, new DownloadCompletionCallback() {
                    @Override
                    public void onComplete(int responseCode, String responseMessage, File file) {
                        if (responseCode == 0) {
                            SharePreferenceManager.setIsOpen(true);
                            FileUtil.writeToDirectory(mFileContent.getLocalPath(), BaseConfig.FILE_DIR+mFileContent.getFileName());
                            finish();
                        }else {
                            finish();
                        }
                    }
                });
            }
        });

        mIv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            mProcessBar.setProgress(msg.arg1);
            mHandler.post(progressBar);
        }
    };

    Runnable progressBar = new Runnable() {
        @Override
        public void run() {
            android.os.Message msg = mHandler.obtainMessage();
            msg.arg1 = mProcessNum;

            mHandler.sendMessage(msg);
            if (mProcessNum == 100) {
                mHandler.removeCallbacks(progressBar);
            }
        }
    };

    private String byteToMB(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size > kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void receiveUser(Message message) {
        mMessage = message;
        mFileContent = (FileContent) message.getContent();

    }
}
