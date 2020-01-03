package com.hxd.gobus.utils;

import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

/**
 *  搜索操作进行优化
 * Created by wangqingbin on 2018/6/30.
 */

public class SearchOperation implements CommonHandler.MessageHandler {

    private String currentKeyWord;
    private ICanSearchListener mListener;

    private MyRunnable myRunnable = new MyRunnable();

    private CommonHandler mHandler;

    /**
     * 搜索间隔时间－毫秒  间隔时间之内不能搜索
     */
    private final int INTERVAL_TIME = 300;

    public SearchOperation() {
        mHandler = new CommonHandler(this);
    }

    public SearchOperation(Looper looper) {
        mHandler = new CommonHandler(looper, this);
    }

    /**
     * 实时搜索优化的关键，当EditText中的文字发生改变时，我们先会将handle中的Callback移除掉。
     * 然后使用Handle发一个延时的消息。最后通过回调getKeyword，让Activity开始搜索
     */
    public void optionSearch(String keyword) {
        this.currentKeyWord = keyword;
        if (myRunnable != null) {
            mHandler.removeCallbacks(myRunnable);
        }
        //发送可以搜索的延迟消息
        mHandler.postDelayed(myRunnable, INTERVAL_TIME);
    }


    @Override
    public void handleMessage(Message msg) {
        if(mListener!=null && !TextUtils.isEmpty(currentKeyWord)){
            mListener.canSearch(currentKeyWord);
        }
    }

    public void setCanSearchListener(ICanSearchListener listener) {
        this.mListener = listener;
    }

    public interface ICanSearchListener {
        void canSearch(String keyword);
    }

    private class MyRunnable implements Runnable {

        @Override
        public void run() {
            mHandler.sendEmptyMessage(1);
        }
    }


}
