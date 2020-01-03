package com.hxd.gobus.core.http.download;

import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.hxd.gobus.BusApp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * 保存文件任务
 * Created by wangqingbin on 2018/6/6.
 */
public class SaveFileTask extends AsyncTask<Object,Long,File> {

    private final DownloadListener listener;

    public SaveFileTask(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected File doInBackground(Object... params) {
        String downloadDir = (String)params[0];
        String extension = (String)params[1];
        ResponseBody body = (ResponseBody)params[2];
        String name = (String)params[3];
        InputStream is = body.byteStream();

        if(TextUtils.isEmpty(downloadDir)){
            if(TextUtils.isEmpty(Environment.getExternalStorageDirectory().getAbsolutePath())){
                downloadDir = BusApp.getContext().getCacheDir().getAbsolutePath() + "/download";
            } else{
                downloadDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download";
            }
        }
        //下面用不到 扩展名 所以随便弄下
        if(TextUtils.isEmpty(extension)){
            extension = "";
        }
        if(TextUtils.isEmpty(name)){
            throw new IllegalArgumentException("request params filename can not empty!");
        }
        return writeToDisk(downloadDir,name, is, body.contentLength());
    }

    //如果文件已经下完了
    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
    }

    private  File writeToDisk(String downloadDir,String filename, InputStream is, long totalLength) {
        File dir = new File(downloadDir);
        if (!dir.getParentFile().exists()){
            dir.getParentFile().mkdir();
        }
        if (!dir.exists()){
            dir.mkdir();
        }
        File file = new File(dir,filename);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //开始下载
        if (listener != null){
            listener.onStart();
        }
        OutputStream os = null;
        long currentLength = 0;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            byte data[] = new byte[8192];
            int len;
            while ((len = is.read(data, 0, 8192)) != -1) {
                os.write(data, 0, len);
                currentLength += len;
                //计算当前下载进度
                int progress = (int) (100 * currentLength / totalLength);
                if (listener != null){
                    listener.onProgress(progress ,currentLength,totalLength);
                }
            }
            //下载完成，并返回当前保存的文件路径
            if (listener != null){
                listener.onFinish(file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

}
