package com.hxd.gobus.config;

/**
 * @author: wangqingbin
 * @date: 2019/9/16 11:17
 */

public class VehicleConfig {

    /**
     * 用车范围
     */
    public static final String KEY_TRANSPORT_USE_CAR_RANGE = "KEY_TRANSPORT_USE_CAR_RANGE";

    /**
     * 用车类型
     */
    public static final String KEY_TRANSPORT_USE_CAR_TYPE = "KEY_TRANSPORT_USE_CAR_TYPE";

    /**
     * 用车时长
     */
    public static final String KEY_TRANSPORT_PREDICT_DURATION = "KEY_TRANSPORT_PREDICT_DURATION";

    /**
     * 是否用车
     */
    public static final String KEY_TRANSPORT_IS_USE_CAR = "KEY_TRANSPORT_IS_USE_CAR";

    /**
     * 维修状态
     */
    public static final String KEY_REPAIR_STATUS = "KEY_REPAIR_STATUS";

    /**
     * 保养状态
     */
    public static final String KEY_UPKEEP_TRANSACT_STATUS = "KEY_UPKEEP_TRANSACT_STATUS";

    /**
     * 新增状态
     */
    public static final String APPLY_ADD = "0";

    /**
     * 修改状态
     */
    public static final String APPLY_UPDATE = "1";

    /**
     * 审核同意
     */
    public static final String APPROVAL_AGREE = "0";

    /**
     * 审核退回
     */
    public static final String APPROVAL_DISAGREE = "1";

    /**
     * 审核成功标识
     */
    public static final String APPROVAL_SUCCESS = "approval_success";

    /**
     * 进入用车审核保存页面请求
     */
    public static final int REQUEST_CODE_INTO_VEHICLE_APPROVAL = 10000;

    /**
     * 进入公共审核保存页面请求
     */
    public static final int REQUEST_CODE_INTO_PUBLIC_APPROVAL = 10001;

    /**
     * 进入选择联系人页面请求
     */
    public static final int REQUEST_CODE_INTO_CHOOSE_CONTACT = 10002;

    /**
     * 用车未登记
     */
    public static final String VEHICLE_NOT_REGISTRATION = "0";

    /**
     * 用车已登记
     */
    public static final String VEHICLE_ALREADY_REGISTRATION = "1";

    /**
     * 用车已归还
     */
    public static final String VEHICLE_ALREADY_RETURN = "2";




    /**
     * 用车附件类型
     */
    public static final String ATTACH_TYPE_VEHICLE = "77";
    /**
     * 维修附件类型
     */
    public static final String ATTACH_TYPE_REPAIR = "80";
    /**
     * 保养附件类型
     */
    public static final String ATTACH_TYPE_MAINTAIN = "81";



}
