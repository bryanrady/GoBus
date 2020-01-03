package com.hxd.gobus.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.hxd.gobus.BusApp;
import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.config.Constant;
import com.hxd.gobus.greendao.ContactDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 通讯录数据库操作类
 * Created by wangqingbin on 2018/8/7.
 */

public class ContactsSingleton {

    private ContactsSingleton(){}

    public static ContactsSingleton getInstance(){
        return SingleTonHolder.instance;
    }

    public static class SingleTonHolder{
        public static final ContactsSingleton instance = new ContactsSingleton();
    }

    private Context mContext;
    private ContactDao mContactsDao;
    private CharacterParser mParser;
    private SQLiteDatabase mDatabase;
    private ImplTimeDatabaseHelper mDatabaseHelper;
    private boolean mIsInRequesting = false;
    private PinyinUtil mPinyinUtil;
    public OnContactsListListener mListener;
    private List<Contact> mList;

    public void startSyncQuery(Context context){
        init(context);
        if(mList != null && mList.size()>0) {
            if(mListener != null){
                mListener.onContactsList(mList);
                mListener = null;
            }
        }else if(mList.size() == 0){
            queryContactsList();
            if(mList != null && mList.size()>0){
                if(mListener != null){
                    mListener.onContactsList(mList);
                    mListener = null;
                }
            }
        }
        long tempTime = System.currentTimeMillis();
        if(canRefreshCotacts(tempTime) || mList.size() == 0){
            requestSyncContacts();
        }
    }

    private void init(Context context){
        mContext = context;
        mParser = CharacterParser.getInstance();
        mContactsDao = BusApp.getDaoSession().getContactDao();
    //    mDatabaseHelper = new ImplTimeDatabaseHelper(context, "refresh_time.db");
        mPinyinUtil = new PinyinUtil();
        if(mList == null) mList = new ArrayList<Contact>();
    }

    private void updateRefreshTime(long time){
        SharePreferenceManager.setLastRefreshTime(time);
//        if(mDatabaseHelper != null){
//            mDatabase = mDatabaseHelper.getWritableDatabase();
//            if(mDatabase != null){
//                ContentValues contentValues = new ContentValues();
//                contentValues.put("refreshtime",time);
//                mDatabase.insert("impltime", null, contentValues);
//            }
//        }
    }

    private long queryRefreshTime(){
        long refreshTime = SharePreferenceManager.getLastRefreshTime();
//        if(mDatabaseHelper != null){
//            mDatabase = mDatabaseHelper.getReadableDatabase();
//            if(mDatabase != null) {
//                Cursor cursor = mDatabase.query("impltime", new String[]{"refreshtime"}, null, null, null, null, null);
//                while (cursor.moveToNext()) {
//                    time = cursor.getLong((cursor.getColumnIndex("refreshtime")));
//                }
//            }
//        }
        return refreshTime;
    }

    private boolean canRefreshCotacts(long currentTime){
        long time = queryRefreshTime();
        if((currentTime - time) > Constant.CONTACTS_UPDATE_TIME) {
            return true;
        }else{
            return false;
        }
    }

    class InsertAsyncTask extends AsyncTask<List<Contact>, Integer, Void> {
        @Override
        protected Void doInBackground(List<Contact>[] lists) {
            if(lists[0] != null){
                deleteContactsDatabase();
                insertContactsList(lists[0]);
            }
            return null;
        }
    }

    public List<Contact> getSortContactList(List<Contact> list) {
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

    private void asyncInsertData(List<Contact> list){
        List<Contact>[] lists = new List[1];
        lists[0] = list;
        new InsertAsyncTask().execute(lists[0]);
    }

    private void insertContactsList(List<Contact> list){
        for (int i = 0; i < list.size(); i++) {
            Contact contacts = new Contact();
            contacts.setPersonId(list.get(i).getPersonId());
            contacts.setName(list.get(i).getName());
            contacts.setUnitName(list.get(i).getUnitName());
            contacts.setTechnicalPost(list.get(i).getTechnicalPost());
            contacts.setWorkNo(list.get(i).getWorkNo());
            contacts.setMobilePhone(list.get(i).getMobilePhone());
            contacts.setEmail(list.get(i).getEmail());
            contacts.setFirstLetter(list.get(i).getFirstLetter());
            contacts.setPhotoUrl(list.get(i).getPhotoUrl());
            contacts.setNamePinyin(list.get(i).getNamePinyin());
            contacts.setDeptPinyin(list.get(i).getDeptPinyin());
            contacts.setCardNo(list.get(i).getCardNo());
            contacts.setUnitId(list.get(i).getUnitId());
            mContactsDao.insert(contacts);
        }
    }

    private void deleteContactsDatabase(){
        if(mContactsDao != null){
            mContactsDao.deleteAll();
        }
    }

    private List<Contact> queryContactsList(){
        if(mContactsDao != null){
            mList = mContactsDao.queryBuilder().build().list();
        }
        return mList;
    }

    private void requestSyncContacts(){
        if(mIsInRequesting){
            return;
        }
        mIsInRequesting = true;
//        HttpManager.doPostContacts(mContext, Constant.CONTACTS_URL, new JSONObject(), new HttpManager.RequestListener() {
//            @Override
//            public void onSuccess(String url, String jsonString) {
//                mIsInRequesting = false;
//                try {
//                    JSONObject outData = new JSONObject(jsonString);
//                    JSONArray jsonArray = outData.getJSONArray("jsonArray");
//                    Gson gson = new Gson();
//                    List<Contacts> list = gson.fromJson(jsonArray.toString(), new TypeToken<List<Contacts>>() {}.getType());
//                    if (list != null && list.size() > 0) {
//                        updateRefreshTime(System.currentTimeMillis());
//                        mList = getSortContactList(list);
//                        if(mListener != null){
//                            mListener.onContactsList(mList);
//                            mListener = null;
//                        }
//                        asyncInsertData(mList);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFail(String url, String errorMsg) {
//                mIsInRequesting = false;
//                if(mListener != null){
//                    mListener.onListError(errorMsg);
//                    mListener = null;
//                }
//            }
//        });
    }

    public void setOnContactsListListener(OnContactsListListener listener){
        mListener = listener;
    }

    public interface OnContactsListListener{
        void onContactsList(List<Contact> list);
        void onListError(String error);
    }

}
