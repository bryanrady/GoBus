package com.hxd.gobus.core.http.params;

import android.text.TextUtils;

import com.hxd.gobus.bean.Authority;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.utils.GsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author: wangqingbin
 * @date: 2019/9/16 9:48
 */

public class VehicleParamsManager {

    /**
     * 分页查询用车申请列表
     * @param vehicleMark
     * @param startDate
     * @param endDate
     * @param useReason
     * @param page
     * @param rows
     * @return
     */
    public static JSONObject queryVehicleListParams(String vehicleMark, String startDate, String endDate, String useReason,int page,int rows) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            if(!TextUtils.isEmpty(vehicleMark)){
                jsonObject.put("carNumber",vehicleMark);
            }
            if(!TextUtils.isEmpty(startDate)){
                jsonObject.put("start",startDate);
            }
            if(!TextUtils.isEmpty(endDate)){
                jsonObject.put("end",endDate);
            }
            if(!TextUtils.isEmpty(useReason)){
                jsonObject.put("useReason",useReason);
            }
            jsonObject.put("page",page);
            jsonObject.put("rows", rows);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 分页查询用车登记列表
     * @param page
     * @param rows
     * @return
     */
    public static JSONObject queryVehicleRegistrationListParams(int page,int rows) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("approvalStatus","2");
            jsonObject.put("page",page);
            jsonObject.put("rows", rows);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 分页查询用车登记列表
     * @param page
     * @param rows
     * @return
     */
    public static JSONObject queryReturnRegistrationListParams(int page,int rows) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("useCarStatus","1");
            jsonObject.put("page",page);
            jsonObject.put("rows", rows);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    /**
     * 添加用车申请信息
     * @param vehicle
     * @return
     */
    public static JSONObject addVehicleApplyParams(Vehicle vehicle) {
        try {
            return new JSONObject(GsonUtils.objectToJson(vehicle));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改用车申请信息
     * @param vehicle
     * @return
     */
    public static JSONObject updateVehicleApplyParams(Vehicle vehicle) {
        try {
            return new JSONObject(GsonUtils.objectToJson(vehicle));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除用车申请信息
     * @param managementId
     * @return
     */
    public static JSONObject deleteVehicleApplyParams(Integer managementId) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(managementId);
            jsonObject.put("ids",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject queryVehicleApplyDetailParams(Integer managementId) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("managementId",managementId);
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

    public static JSONObject queryVehicleApprovalParams(Integer datumDataId,String datumType){
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

    public static JSONObject permissionAuthorityParams(Integer datumDataId,String datumType){
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

    public static JSONObject saveVehicleApprovalParams(Vehicle vehicle,String approvalStatus, String approvalAdvice, Authority authority){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            if (vehicle.getInfoManageId() != null){
                jsonObject.put("infoManageId",vehicle.getInfoManageId());
            }
            if (vehicle.getAssignDriver() != null){
                jsonObject.put("assignDriver",vehicle.getAssignDriver());
            }
            if (!TextUtils.isEmpty(vehicle.getPredictStartDate())){
                jsonObject.put("predictStartDate",vehicle.getPredictStartDate());
            }
            if (!TextUtils.isEmpty(vehicle.getPredictDuration())){
                jsonObject.put("predictDuration",vehicle.getPredictDuration());
            }
            if (!TextUtils.isEmpty(vehicle.getRemareks())){
                jsonObject.put("remareks",vehicle.getRemareks());
            }
            jsonObject.put("datumDataId", authority.getDatumDataId());
            jsonObject.put("datumType", authority.getDatumType());
            jsonObject.put("approvalSerialNo", authority.getApprovalSerialNo());
            jsonObject.put("approvalStatus", approvalStatus);
            jsonObject.put("approvalIdea", approvalAdvice);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public static JSONObject queryRealTimeMonitoringList(int page,int rows) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("page",page);
            jsonObject.put("rows", rows);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject queryRealTimeDataList(int page,int rows) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("page",page);
            jsonObject.put("rows", rows);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
