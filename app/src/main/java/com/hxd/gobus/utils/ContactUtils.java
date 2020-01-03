package com.hxd.gobus.utils;

import android.os.AsyncTask;

import com.hxd.gobus.BusApp;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.bean.list.ContactList;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.core.http.params.HttpParamsManager;
import com.hxd.gobus.core.http.ObserverImpl;
import com.hxd.gobus.core.http.rx.RxRetrofitClient;
import com.hxd.gobus.greendao.ContactDao;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * https://www.cnblogs.com/tonycheng93/p/6295724.html
 * https://www.jianshu.com/p/8b4fa39cf3b0
 * Created by Administrator on 2019/7/3.
 */

public class ContactUtils {

    private static final String TAG = "ContactInfoModel";
    private PinyinUtil mPinyinUtil;
    private CharacterParser mParser;
    private OnContactsListListener mListener;
    private List<Contact> mList;
    private boolean mIsInRequesting = false;

    private ContactUtils(){
        mPinyinUtil = new PinyinUtil();
        mParser = CharacterParser.getInstance();
        if (mList == null){
            mList = new ArrayList<>();
        }
    }

    public static ContactUtils getInstance(){
        return ContactUtils.Holder.instance;
    }

    private static final class Holder{
        private static ContactUtils instance = new ContactUtils();
    }

    public void loadContactList(){
        if(mList != null && mList.size()>0) {
            if(mListener != null){
                mListener.onContactComplete(mList);
            }
        }else{
            mList = queryContactListFromDatabase();
            if(mList != null && mList.size()>0){
                if(mListener != null){
                    mListener.onContactComplete(mList);
                }
            }else{
                queryContactListFromServer();
            }
        }
        long tempTime = System.currentTimeMillis();
        if(canRefresh(tempTime) || mList.size() == 0){
            queryContactListFromServer();
        }
        queryContactListFromServer();
    }

    public Contact queryContactFromDatabase(String workNo){
        try {
            Contact contact = BusApp.getDaoSession().getContactDao()
                    .queryBuilder()
                    .where(ContactDao.Properties.WorkNo.eq(workNo)).build().unique();
            return contact;
        }catch (Exception e){
            LogUtils.d(TAG,"contact query failed!");
        }
        return null;
    }

    /**
     * 从数据库中查询通讯录列表人员
     * @return
     */
    private List<Contact> queryContactListFromDatabase(){
        try {
            List<Contact> contactInfoList = BusApp.getDaoSession().getContactDao().queryBuilder().list();
            return contactInfoList;
        }catch (Exception e){
            LogUtils.d(TAG,"contact list query failed!");
        }
        return null;
    }

    /**
     * 从网络中查找通讯录列表
     * @return
     */
    private void queryContactListFromServer(){
        if(mIsInRequesting){
            return;
        }
        mIsInRequesting = true;
        RxRetrofitClient
                .create()
                .url(Constant.CONTACTS_URL)
                .raw(HttpParamsManager.getBaseParams(new JSONObject()).toString())
                .build()
                .postRaw()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverImpl() {
                    @Override
                    protected void handleResponse(String response) {
                        ContactList jsonToObject = GsonUtils.jsonToObject(response, ContactList.class);
                        List<Contact> contactList = jsonToObject.getJsonArray();
                        mList = getSortContactList(contactList);
                        if(mList != null && mListener != null){
                            mListener.onContactComplete(mList);
                        }
                    }

                    @Override
                    protected void handleError(String errorMsg) {
                        if(mListener != null){
                            mListener.onContactFailure(errorMsg);
                        }
                        mIsInRequesting = false;
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        if(mList != null && mList.size()>0){
                            updateRefreshTime(System.currentTimeMillis());
                            asyncInsertData(mList);
                        }
                        mIsInRequesting = false;
                    }
                });
    }

    private List<Contact> getSortContactList(List<Contact> list) {
        for (int i = 0; i < list.size(); i++) {
            String namePinyin = mParser.getSelling(list.get(i).getName());
            list.get(i).setNamePinyin(namePinyin);
            if(list.get(i).getUnitName() != null){
                String deptPinyin = mParser.getSelling(list.get(i).getUnitName());
                list.get(i).setDeptPinyin(deptPinyin);
            }
            String sortNameString = namePinyin.substring(0, 1).toUpperCase();
            if (sortNameString.matches("[A-Z]")) {
                list.get(i).setFirstLetter(sortNameString.toUpperCase());
            } else {
                list.get(i).setFirstLetter("#");
            }
        }
        Collections.sort(list, mPinyinUtil);
        return list;
    }

    /**
     * 添加进数据库
     * @param list
     */
    private static void insertContactList(List<Contact> list){
        try {
            if (list != null && list.size() > 0){
                for (Contact contact : list){
                    BusApp.getDaoSession().getContactDao().insert(contact);
                }
            }
        }catch (Exception e){
            LogUtils.d(TAG,"contact insert failed!");
        }
    }

    private static class InsertAsyncTask extends AsyncTask<List<Contact>, Integer, Void> {
        @Override
        protected Void doInBackground(List<Contact>[] lists) {
            if(lists[0] != null){
                deleteContactsDatabase();
                insertContactList(lists[0]);
            }
            return null;
        }
    }

    private void asyncInsertData(List<Contact> list){
        List<Contact>[] lists = new List[1];
        lists[0] = list;
        new InsertAsyncTask().execute(lists[0]);
    }

    private static void deleteContactsDatabase(){
        if(BusApp.getDaoSession().getContactDao() != null){
            BusApp.getDaoSession().getContactDao().deleteAll();
        }
    }

    private void updateRefreshTime(long time){
        SharePreferenceManager.setLastRefreshTime(time);
    }

    private long queryRefreshTime(){
        return SharePreferenceManager.getLastRefreshTime();
    }

    private boolean canRefresh(long currentTime){
        long time = queryRefreshTime();
        if((currentTime - time) > BaseConfig.CONTACTS_UPDATE_TIME) {
            return true;
        }else{
            return false;
        }
    }

    public void setOnContactsListListener(OnContactsListListener listener){
        mListener = listener;
    }

    public interface OnContactsListListener{
        void onContactComplete(List<Contact> list);
        void onContactFailure(String error);
    }

}
