package com.hxd.gobus.config;

/**
 * Created by pengyu520 on 2016/6/14.
 */
public class Constant {

    //http://222.85.147.20:5678/login.jsp
    /** 测试环境 URL*/
    public static final String ROOT_URL="http://222.85.144.82:2211";
    /** 董丽伟 测试环境 URL*/
//    public static final String ROOT_URL="http://192.168.19.99:8080";
    /** 左康梅 测试环境 URL*/
//    public static final String ROOT_URL="http://192.168.19.73:8090";
    /** 王著 测试环境 URL*/
//    public static final String ROOT_URL = "http://192.168.19.46:8082";
    /** 胡工 测试环境 URL*/
//    public static final String ROOT_URL = "http://192.168.19.66:8080";
    /** 刘村 开发环境 URL*/
//    public static final String ROOT_URL="http://192.168.19.48:9999";
    /** 正式环境 URL*/
//    public static final String ROOT_URL = "http://222.85.144.82:8058";


    /**************************************** 用车接口 start *******************************************************/

    /** 查询用车申请列表接口*/
    public static final String QUERY_USE_CAR_LIST = ROOT_URL + "/ispApi/car/data/useCar/queryUseCarList";
    /** 查询用车详细信息接口*/
    public static final String QUERY_USE_CAR_DETAIL = ROOT_URL + "/ispApi/car/data/useCar/queryUseCarDetail";
    /** 添加用车申请接口*/
    public static final String ADD_USE_CAR = ROOT_URL + "/ispApi/car/data/useCar/insertUseCar";
    /** 删除用车申请接口*/
    public static final String DELATE_USE_CAR = ROOT_URL + "/ispApi/car/data/useCar/deleteUseCar";
    /** 修改用车申请接口*/
    public static final String UPDATE_USE_CAR = ROOT_URL + "/ispApi/car/data/useCar/updateUseCar";
    /** 查询用车申请审核信息接口*/
    public static final String QUERY_USE_CAR_APPROVAL = ROOT_URL + "/ispApi/car/data/useCar/queryUseCarApproval";
    /** 保存用车申请审核信息接口*/
    public static final String INSERT_USE_CAR_APPROVAL = ROOT_URL + "/ispApi/car/data/useCar/insertUseCarApproval";
    /** 查询用车使用所需数据字典*/
    public static final String QUERY_USE_CAR_SYSTEM_CODE_VALUE = ROOT_URL + "/ispApi/car/data/useCar/queryUseCarSystemCodeValue";
    /** 查询所有可用车辆信息*/
    public static final String QUERY_USE_CAR_IS_CAN = ROOT_URL + "/ispApi/car/data/useCar/queryUseCarInfoManage";

    /**************************************** 用车接口 end *******************************************************/


    /**************************************** 维修接口 start *******************************************************/

    /** 保存维修审核信息*/
    public static final String INSERT_REPAIR_CAR_APPROVAL = ROOT_URL + "/ispApi/car/data/repair/insertRepairCarApproval";
    /** 查询维修审核信息*/
    public static final String QUERY_REPAIR_CAR_APPROVAL = ROOT_URL + "/ispApi/car/data/repair/queryRepairCarApproval";
    /** 查询维修记录信息*/
    public static final String QUERY_REPAIR_CAR_LIST = ROOT_URL + "/ispApi/car/data/repair/queryRepairCarList";
    /** 查询维修详细信息*/
    public static final String QUERY_REPAIR_CAR_DETAIL = ROOT_URL + "/ispApi/car/data/repair/queryRepairCarDetail";
    /** 添加维修信息*/
    public static final String ADD_REPAIR_CAR = ROOT_URL + "/ispApi/car/data/repair/insertRepairCar";
    /** 修改维修信息*/
    public static final String UPDATE_REPAIR_CAR = ROOT_URL + "/ispApi/car/data/repair/updateRepairCar";
    /** 删除维修信息*/
    public static final String DELATE_REPAIR_CAR = ROOT_URL + "/ispApi/car/data/repair/deleteRepairCar";

    /**************************************** 维修接口 end *******************************************************/


    /**************************************** 保养接口 start *******************************************************/

    /** 保存保养审核信息*/
    public static final String INSERT_UPHOLD_CAR_APPROVAL = ROOT_URL + "/ispApi/car/data/uphold/insertUpholdCarApproval";
    /** 查询保养审核信息*/
    public static final String QUERY_UPHOLD_CAR_APPROVAL = ROOT_URL + "/ispApi/car/data/uphold/queryUpholdCarApproval";
    /** 查询保养记录信息*/
    public static final String QUERY_UPHOLD_CAR_LIST = ROOT_URL + "/ispApi/car/data/uphold/queryUpholdCarList";
    /** 查询保养详细信息*/
    public static final String QUERY_UPHOLD_CAR_DETAIL = ROOT_URL + "/ispApi/car/data/uphold/queryUpholdCarDetail";
    /** 添加维修信息*/
    public static final String ADD_UPHOLD_CAR_INFO = ROOT_URL + "/ispApi/car/data/uphold/insertUpholdCarInfo";
    /** 修改保养信息*/
    public static final String UPDATE_UPHOLD_CAR_INFO = ROOT_URL + "/ispApi/car/data/uphold/updateUpholdCarInfo";
    /** 删除维修信息*/
    public static final String DELETE_UPHOLD_CAR_INFO = ROOT_URL + "/ispApi/car/data/uphold/deleteUpholdCarInfos";
    /** 查询车辆保养计划信息*/
    public static final String QUERY_CAR_UPKEEP_INFOS = ROOT_URL + "/ispApi/car/data/useCar/queryUpkeepInfos";

    /**************************************** 保养接口 end *******************************************************/

    /** 查询车辆实时信息*/
    public static final String QUERY_USE_CAR_REALT_INFOS = ROOT_URL + "/ispApi/car/data/useCar/queryUseCarRealTimesInfos";
    /** 查询车辆实时监控*/
    public static final String QUERY_CAR_REAL_TIME_INFOS = ROOT_URL + "/ispApi/car/data/useCar/queryUseCarCurrentInfos";

    /**************************************** 车辆实时 start *******************************************************/



    /**************************************** 车辆实时 end *******************************************************/



    /*******************************************  普通公共接口 start  **********************************************/
    /** 通信录接口(备用)*/
    public static final String CONTACTS_URL = ROOT_URL + "/ispApi/hrm/selectContactsList";
    /** 查询码值对应类型列表接口*/
    public static final String QUERY_CODE_VALUE_LIST = ROOT_URL + "/ispApi/system/data/querySystemCodeValues";
    /** 查询申报人的所有申报项目信息*/
    public static final String APPLY_RECORD_URL = ROOT_URL + "/ispApi/system/data/approval/queryApprovalListInfo";
    /** 登录接口*/
    public static final String LOGIN_URL = ROOT_URL + "/ispApi/account/login";
    /** 代办事项数接口*/
    public static final String TODO_COUNT_URL = ROOT_URL + "/ispApi/system/queryUntreatedInfoCount";
    /** 代办事项接口*/
    public static final String TODO_URL = ROOT_URL + "/ispApi/system/queryUntreatedinfoList";
    /** 通用权限效验接口*/
    public static final String VERIFY_URL = ROOT_URL + "/ispApi/flow/verifyDatumdetailinfo";
    /** 撤销当前审核过的审核流程*/
    public static final String CANCEL_CURRENT_AUDIT = ROOT_URL + "/ispApi/flow/data/cancelCurrentApproval";
    /** 通用审核接口*/
    public static final String UNIVERSAL_AUDIT_URL = ROOT_URL + "/ispApi/flow/auditDatumdetailinfo";
    /** 更新待办事项状态接口*/
    public static final String UPDATE_TODO_URL = ROOT_URL + "/ispApi/system/updateUntreatedInfoStatusByPk";
    /** 添加附件开始传参数中dataId的值为接口返回数据 */
    public static final String QUERY_UUID = ROOT_URL + "/ispApi/system/data/Uuid";
    /** 上传附件*/
    public static final String UPLOAD_ATTACH = ROOT_URL + "/ispApi/system/data/addFile";
    /** 删除附件*/
    public static final String DELETE_ATTACH = ROOT_URL + "/ispApi/system/data/deleteAttach";
    /** 查询附件*/
    public static final String QUERY_ATTACH = ROOT_URL + "/ispApi/system/data/image/queryAttach";
    /** 附件图片下载地址*/
    public static final String ATTACH_PICTURE_DOWNLOAD_URL = ROOT_URL + "/xuni/";
    /** 新的头像下载地址*/
    public static final String HEAD_PICTURE_DOWNLOAD_URL = ROOT_URL + "/xuni/PressedImages/";
    /** 文件下载接口  正式*/
    public static final String PDFDOC_DOWNLOAD_URL = ROOT_URL + "/xuni/ImagesUploaded/";
    /** 查询联系人电话号码接口*/
    public static final String QUERY_ADMIN_CONTACTS = ROOT_URL + "/ispApi/system/data/queryRelationShip";
    /** 紧急救援接口*/
    public static final String EMERGENCY_RESCUE_LIST_URL = ROOT_URL + "/ispApi/system/data/mergency/queryMergencyPersons";
    /** 修改密码接口*/
    public static final String MODIFY_PASSWORD_URL = ROOT_URL + "/ispApi/account/passwordSettings";
    /** 退出登录接口*/
    public static final String LOGOUT_URL = ROOT_URL + "/ispApi/account/logout";
    /** APK更新接口*/
    public static final String APK_UPDATE_URL = ROOT_URL +"/ispApi/system/queryVersionCode";
    /** 使用帮助页面*/
    public static final String HTML_APP_HELPER = ROOT_URL + "/ispApi/jsp/Help.html";
    /** 查询下一步审核人信息接口*/
    public static final String QUERY_NEXT_APPROVAL_PERSONAL_INFO = ROOT_URL + "/ispApi/flow/queryNextApprovalUserInfo";
    /** 催办接口*/
    public static final String REMINDERS_URL = ROOT_URL + "/ispApi/flow/fastDeal";

    /*******************************************  普通公共接口 end  **********************************************/

    /** 通讯录更新时间间隔 4个小时 */
    public static final long CONTACTS_UPDATE_TIME = 1 * 60 * 60 * 1000;
}
