package com.hxd.gobus.core.http.params;

import android.text.TextUtils;

import com.hxd.gobus.bean.Repair;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.utils.GsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author: wangqingbin
 * @date: 2019/9/20 10:30
 */

public class RepairParamsManager {

    /**
     * 分页查询维修申请列表
     * @param vehicleMark
     * @param startDate
     * @param endDate
     * @param remarks
     * @param page
     * @param rows
     * @return
     */
    public static JSONObject queryRepairListParams(String vehicleMark, String startDate, String endDate, String remarks,int page,int rows) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            if(!TextUtils.isEmpty(vehicleMark)){
                jsonObject.put("vehicleMark",vehicleMark);
            }
            if(!TextUtils.isEmpty(startDate)){
                jsonObject.put("start",startDate);
            }
            if(!TextUtils.isEmpty(endDate)){
                jsonObject.put("end",endDate);
            }
            if(!TextUtils.isEmpty(remarks)){
                jsonObject.put("remarks",remarks);
            }
            jsonObject.put("page",page);
            jsonObject.put("rows", rows);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    /**
     * 删除用车申请信息
     * @param repairId
     * @return
     */
    public static JSONObject deleteRepairApplyParams(Integer repairId) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(repairId);
            jsonObject.put("ids",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject queryAvailableVehicleParams() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("page",1);
            jsonObject.put("rows", 100);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject addRepairApplyParams(Repair repair) {
        try {
            return new JSONObject(GsonUtils.objectToJson(repair));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject updateRepairApplyParams(Repair repair) {
        try {
            return new JSONObject(GsonUtils.objectToJson(repair));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject queryRepairApplyDetailParams(Integer repairId) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("repairId",repairId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject queryRepairApprovalParams(Integer datumDataId,String datumType){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("datumDataId",datumDataId);
            jsonObject.put("datumType", datumType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
