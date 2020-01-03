package com.hxd.gobus.mvp.presenter;

import android.widget.Toast;

import com.hxd.gobus.bean.StringInfo;
import com.hxd.gobus.bean.list.VehicleLicenseList;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.di.scopes.FragmentScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IMyContract;
import com.hxd.gobus.mvp.model.MyModel;
import com.hxd.gobus.utils.GsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * @author: wangqingbin
 * @date: 2019/7/25 10:26
 */
@FragmentScope
public class MyPresenter extends BasePresenter<IMyContract.View,MyModel> {

    @Inject
    public MyPresenter(){

    }

    public void getAdminList(){
        getView().showLoading();
        getModel().queryAdmin().subscribe(new ObserverImpl() {

            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onComplete() {
                getView().dismissLoading();
            }

            @Override
            protected void handleResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray phoneList = jsonObject.getJSONArray("phoneList");
                    List<StringInfo> numberList = new ArrayList<>();
                    if(phoneList.length() > 1){
                        for(int i=0;i<phoneList.length();i++){
                            JSONObject jsonObject1 = phoneList.getJSONObject(i);
                            String phoneNumber = jsonObject1.optString("phone["+i+"]");
                            StringInfo stringInfo = new StringInfo();
                            stringInfo.setTitle(phoneNumber);
                            numberList.add(stringInfo);
                        }
                        if(numberList != null && numberList.size()>0){
                            getView().showAdminListDialog(numberList);
                        }else{
                            getView().showToast("管理员的电话号码配置有问题，请通过其他途径联系管理员！");
                        }
                    }else if(phoneList.length() == 1){
                        for(int i=0;i<phoneList.length();i++){
                            JSONObject jsonObject1 = phoneList.getJSONObject(i);
                            String phoneNumber = jsonObject1.optString("phone");
                            StringInfo stringInfo = new StringInfo();
                            stringInfo.setTitle(phoneNumber);
                            numberList.add(stringInfo);
                        }
                        if(numberList != null && numberList.size()>0){
                            getView().showAdminListDialog(numberList);
                        }else{
                            getView().showToast("管理员的电话号码配置有问题，请通过其他途径联系管理员！");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void handleError(String errorMsg) {
                getView().showToast(errorMsg);
            }
        });
    }

}
