package com.hxd.gobus.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hxd.gobus.bean.Contact;
import com.hxd.gobus.greendao.DaoMaster;

/**
 * Created by Administrator on 2018/5/29.
 */

public class SqliteUtils {

    private static DaoMaster.DevOpenHelper helper;
    private static SQLiteDatabase db;

    /**
     * 通过工号查询名字
     * @param context
     * @param workNo
     * @return
     */
    public static synchronized String getNameByWorkNo(Context context,String workNo){
        String name = null;
//        helper = new DaoMaster.DevOpenHelper(context,"contacts.db");
//        helper = DaoMaster.DevOpenHelper.getInstance(context,"contacts.db");
//        db = helper.getReadableDatabase();
//        Cursor cursor = db.query("CONTACTS", new String[]{"NAME"},
//                "WORK_NO=?", new String[]{workNo},null,null ,null);
//        while (cursor.moveToNext()){
//            name = cursor.getString(cursor.getColumnIndex("NAME"));
//        }
//        cursor.close();
        return name;
    }

    /**
     * 通过工号查询某条数据
     * @param context
     * @param workNo
     * @return
     */
    public static synchronized Contact getDaoByWorkNo(Context context, String workNo){
        int person_id = 0;
        String name = null;
        String unit_name = null;
        String post = null;
        String phone = null;
        String email = null;
        String photo_url = null;
        Contact contacts = new Contact();
    //    helper = new DaoMaster.DevOpenHelper(context,"contacts.db");
//        helper = DaoMaster.DevOpenHelper.getInstance(context,"contacts.db");
//        if(helper != null){
//            db = helper.getReadableDatabase();
//            if(db != null){
//                Cursor cursor = db.query("CONTACTS", new String[]{"PERSON_ID,NAME,UNIT_NAME,TECHNICAL_POST,MOBILE_PHONE,EMAIL,PHOTO_URL"},
//                        "WORK_NO=?", new String[]{workNo},null,null ,null);
//                while (cursor.moveToNext()){
//                    person_id = cursor.getInt(cursor.getColumnIndex("PERSON_ID"));
//                    name = cursor.getString(cursor.getColumnIndex("NAME"));
//                    unit_name = cursor.getString(cursor.getColumnIndex("UNIT_NAME"));
//                    post = cursor.getString(cursor.getColumnIndex("TECHNICAL_POST"));
//                    phone = cursor.getString(cursor.getColumnIndex("MOBILE_PHONE"));
//                    email = cursor.getString(cursor.getColumnIndex("EMAIL"));
//                    photo_url = cursor.getString(cursor.getColumnIndex("PHOTO_URL"));
//                }
//                cursor.close();
//                contacts.setPersonId(person_id);
//                contacts.setName(name);
//                contacts.setUnitName(unit_name);
//                contacts.setWorkNo(workNo);
//                contacts.setTechnicalPost(post);
//                contacts.setMobilePhone(phone);
//                contacts.setEmail(email);
//                contacts.setPhotoUrl(photo_url);
//            }
//        }
        return contacts;
    }
}
