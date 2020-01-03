package com.hxd.gobus.bean;

import com.google.gson.Gson;

/**
 * @author: wangqingbin
 * @date: 2019/9/16 11:21
 */

public class KeyCode {


    /**
     * codeVlaueId : int //代码值ID
     * codeId : int //代码ID
     * codeName : string //代码名称
     * codeValue : string //代码值
     * status : string //状态：0：有效；1：无效；
     * orderNum : double //顺序
     * createDate : date //创建时间
     * noneOption : string[] //不显示下拉框值
     * keyName : string //代码key名称
     */

    private Integer codeVlaueId;
    private Integer codeId;
    private String codeName;
    private String codeValue;
    private String status;
    private String orderNum;
    private String createDate;
    private String noneOption;
    private String keyName;

    public static KeyCode objectFromData(String str) {

        return new Gson().fromJson(str, KeyCode.class);
    }

    public Integer getCodeVlaueId() {
        return codeVlaueId;
    }

    public void setCodeVlaueId(Integer codeVlaueId) {
        this.codeVlaueId = codeVlaueId;
    }

    public Integer getCodeId() {
        return codeId;
    }

    public void setCodeId(Integer codeId) {
        this.codeId = codeId;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getNoneOption() {
        return noneOption;
    }

    public void setNoneOption(String noneOption) {
        this.noneOption = noneOption;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }
}
