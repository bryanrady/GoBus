package com.hxd.gobus.core.http.params;

import com.hxd.gobus.bean.Authority;
import com.hxd.gobus.bean.User;
import com.hxd.gobus.utils.AppUtil;
import com.hxd.gobus.utils.DateUtil;
import com.hxd.gobus.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 网络请求参数管理类
 * Created by wangqingbin on 2018/11/9.
 */

public class HttpParamsManager {

    /**
     * 基本参数
     * @param jsonObject
     * @return
     */
    public static JSONObject getBaseParams(JSONObject jsonObject) {
        JSONObject obj;
        try {
            obj = new JSONObject();
            obj.put("msgId", DateUtil.getCurTimeFormat(DateUtil.FORMAT_0));
            obj.put("protocolVersion", AppUtil.getAppVersionCode());
            obj.put("osType", "1");
            obj.put("token", AppUtil.getDeviceId());
            obj.put("inData", jsonObject);
            LogUtils.d("getBaseParams", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return obj;
    }

    /**
     * 用户登录
     * @param loginName
     * @param loginPassword
     * @return
     */
    public static JSONObject loginParams(String loginName, String loginPassword) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("loginName", loginName);
            jsonObject.put("loginPassword", loginPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 用户退出
     * @return
     */
    public static JSONObject logoutParams() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("loginName", User.getInstance().getLoginName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 查询代办个数
     * @return
     */
    public static JSONObject queryTodoCountParams() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("status", "0");
            jsonObject.put("userId",User.getInstance().getPersonId() );
            jsonObject.put("unitId", User.getInstance().getUnitId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 查询代办事项列表
     * @return
     */
    public static JSONObject queryTodoListParams() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("status", "0");
            jsonObject.put("userId",User.getInstance().getPersonId() );
            jsonObject.put("unitId", User.getInstance().getUnitId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 查询已办事项列表
     * @return
     */
    public static JSONObject queryAlreadyDoListParams() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("status", "1");
            jsonObject.put("userId",User.getInstance().getPersonId() );
            jsonObject.put("unitId", User.getInstance().getUnitId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 查询代办已办列表
     * @param positionArguments
     * @return
     */
    public static JSONObject queryTodoAlreadyDoListParams(String positionArguments) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("status", positionArguments);
            jsonObject.put("userId",User.getInstance().getPersonId() );
            jsonObject.put("unitId", User.getInstance().getUnitId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject updateTodoParams(Integer ids){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("ids",ids);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 查询下一步审核人
     * @param datumDataId
     * @param datumType
     * @return
     */
    public static JSONObject queryNextReviewer(int datumDataId, String datumType){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("datumDataId",datumDataId);
            jsonObject.put("datumType",datumType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 催办
     * @param title
     * @param workNo
     * @return
     */
    public static JSONObject reminder(String title, String workNo){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("title",title);
            jsonObject.put("workNo",workNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 修改密码
     * @param oldPwd
     * @param newPwd
     * @return
     */
    public static JSONObject modifyPasswordParams(String oldPwd,String newPwd) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("loginName", User.getInstance().getLoginName());
            jsonObject.put("loginPassword", oldPwd);
            jsonObject.put("newLoginPassword", newPwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    /**
     * 查询数据字典
     * @param key
     * @return
     */
    public static JSONObject queryKeyCode(String key) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("key", key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject savePublicApprovalParams(String approvalStatus, String approvalAdvice, Authority authority){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
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

}
