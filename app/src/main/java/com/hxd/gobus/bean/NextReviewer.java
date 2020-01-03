package com.hxd.gobus.bean;

import com.google.gson.Gson;

/**
 * @author: wangqingbin
 * @date: 2019/10/9 11:14
 */

public class NextReviewer {

    /**
     * personId : 30033
     * personName : 赵杰华
     * title : 用车申请审核
     * unitName : 综合管理部
     * workNo : 001083
     */

    private Integer personId;
    private String personName;
    private String title;
    private String unitName;
    private String workNo;

    public static NextReviewer objectFromData(String str) {

        return new Gson().fromJson(str, NextReviewer.class);
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }
}
