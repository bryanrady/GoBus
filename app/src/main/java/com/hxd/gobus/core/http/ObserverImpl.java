package com.hxd.gobus.core.http;

import com.hxd.gobus.BusApp;
import com.hxd.gobus.R;
import com.hxd.gobus.bean.event.StringEvent;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import io.reactivex.Observer;

/**
 * Created by Administrator on 2019/6/28.
 */

public abstract class ObserverImpl implements Observer<String>{

    @Override
    public void onNext(String response) {
        LogUtils.d("response",response);
        if(response != null){
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.optBoolean("success");
                String error = jsonObject.optString("error");
                if (success){
                    JSONObject outData = jsonObject.optJSONObject("outData");
                    if (outData != null){
                        handleResponse(outData.toString());
                    }else{
                        handleResponse(error);
                    }
                }else{
                    String errorCode = jsonObject.optString("errorCode");
                    if(BaseConfig.CONVERSATION_TIMEOUT.equals(errorCode)){
                        //会话超时操作
                        EventBus.getDefault().post(new StringEvent(error));
                    }else{
                        handleError(error);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            handleError(BusApp.getContext().getString(R.string.network_service_error));
        }
        onComplete();
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
        String error = throwable.getMessage();
        if (throwable instanceof UnknownHostException
                || throwable instanceof ConnectException
                || throwable instanceof NoRouteToHostException) {   //网络连接错误
            error = BusApp.getContext().getString(R.string.network_connect_failed);
        } else if (throwable instanceof SocketTimeoutException) {   //网络请求超时
            error = BusApp.getContext().getString(R.string.network_request_timeout);
        } else if (throwable instanceof IllegalStateException) {      //其他错误
            error = throwable.toString();
        }
        handleError(error);
        onComplete();
    }

    protected abstract void handleResponse(String response);

    protected abstract void handleError(String errorMsg);

}
