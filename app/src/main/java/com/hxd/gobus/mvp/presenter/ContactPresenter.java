package com.hxd.gobus.mvp.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.core.http.callback.IFailure;
import com.hxd.gobus.di.scopes.FragmentScope;
import com.hxd.gobus.mvp.BasePresenter;
import com.hxd.gobus.mvp.contract.IContactContract;
import com.hxd.gobus.mvp.model.ContactModel;
import com.hxd.gobus.utils.ContactUtils;
import com.hxd.gobus.utils.NetUtil;
import com.hxd.gobus.utils.PinyinUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * @author: wangqingbin
 * @date: 2019/7/25 10:26
 */
@FragmentScope
public class ContactPresenter extends BasePresenter<IContactContract.View,ContactModel> {

    private List<Contact> mList;
    private PinyinUtil mPinyinUtil;

    @Inject
    public ContactPresenter(){
        if(mList == null){
            mList = new ArrayList<>();
        }
    }

    public void loadContactList(){
        getView().showLoading();
        ContactUtils.getInstance().setOnContactsListListener(new ContactUtils.OnContactsListListener() {
            @Override
            public void onContactComplete(List<Contact> list) {
                if (getView() != null){
                    mList = list;
                    getView().showContacts(list);
                    getView().dismissLoading();
                }
            }

            @Override
            public void onContactFailure(String error) {
                if (getView() != null){
                    getView().showToast(error);
                    getView().dismissLoading();
                }
            }
        });
        ContactUtils.getInstance().loadContactList();
    }


    public void filterData(String filterStr) {
        if(mList != null){
            List<Contact> filterDateList = new ArrayList<>();
            if (TextUtils.isEmpty(filterStr)) {
                filterDateList = mList;
                if (getView() != null){
                    getView().showContacts(filterDateList);
                }
            } else {
                filterDateList.clear();
                for (Contact sortModel : mList) {
                    String name = sortModel.getName();
                    String dept = sortModel.getUnitName();
                    String namePinyin = sortModel.getNamePinyin();
                    String deptPinyin = sortModel.getDeptPinyin();
                    if(name != null && dept != null){
                        if(namePinyin != null && deptPinyin != null){
                            if (name.indexOf(filterStr.toString()) != -1 || namePinyin.startsWith(filterStr.toString())
                                    || dept.indexOf(filterStr.toString()) != -1 || deptPinyin.startsWith(filterStr.toString())) {
                                filterDateList.add(sortModel);
                            }
                        }else{
                            if (name.indexOf(filterStr.toString()) != -1 || dept.indexOf(filterStr.toString()) != -1) {
                                filterDateList.add(sortModel);
                            }
                        }
                    }
                }
                if (mPinyinUtil == null){
                    mPinyinUtil = new PinyinUtil();
                }
                if (filterDateList.size() > 0){
                    Collections.sort(filterDateList, mPinyinUtil);
                    if (getView() != null){
                        getView().showContacts(filterDateList);
                    }
                }
            }
        }
    }
}
