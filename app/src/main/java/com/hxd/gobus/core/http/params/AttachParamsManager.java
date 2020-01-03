package com.hxd.gobus.core.http.params;

import android.util.Log;

import com.hxd.gobus.bean.Repair;
import com.hxd.gobus.utils.FileUtil;
import com.hxd.gobus.utils.GsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author: wangqingbin
 * @date: 2019/9/20 10:30
 */

public class AttachParamsManager {

    /**
     * 查询附件列表
     * @param dataId
     * @param attachType
     * @return
     */
    public static JSONObject queryAttachListParams(String dataId, String attachType){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("dataId",dataId);
            jsonObject.put("attachType", attachType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public static JSONObject deleteAttachParams(int attachId){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(attachId);
            jsonObject.put("ids",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject uploadAttachParams(String dataId, String attachType, String filePath, String fileSuffix){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("dataId", dataId);
            jsonObject.put("attachType",attachType);
            Log.d("wangqingbin","filePath=="+filePath);
            Log.d("wangqingbin","FileUtil.fileToBase64(filePath)=="+FileUtil.fileToBase64(filePath));
            jsonObject.put("fileName", FileUtil.fileToBase64(filePath));
            jsonObject.put("fileType", fileSuffix);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
