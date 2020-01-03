package com.hxd.gobus.bean;

import com.google.gson.Gson;

/**
 * @author: wangqingbin
 * @date: 2019/9/19 11:35
 */

public class Driver {


    /**
     * companyId : 124
     * createBy : 1
     * createDate : 2019-09-19 10:14:42
     * driverId : 26
     * no : 0
     * personId : 31491
     * personName : 彭德秀
     * status : 0
     * unitId : 2314
     * workNo : 002100
     */

    private Integer companyId;
    private String createBy;
    private String createDate;
    private Integer driverId;
    private Integer no;
    private Integer personId;
    private String personName;
    private String status;
    private Integer unitId;
    private String workNo;

    public static Driver objectFromData(String str) {

        return new Gson().fromJson(str, Driver.class);
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }
}
