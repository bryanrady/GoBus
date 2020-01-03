package com.hxd.gobus.bean;

import java.io.Serializable;

/**
 * @author: wangqingbin
 * @date: 2019/8/23 9:41
 */

public class Todo implements Serializable {

    private Integer dataId;//数据ID
    private Integer untreatedInfoId;//待办事项ID
    private Integer unitId;//部门Id
    private String title;//标题
    private String targetUrl;//目标URL
    private String datumType;//待办类型:0:项目策划审核;1:周报审核;2:月报审核
    private String createDate;//创建时间
    private String status;//待办事项状态 1：已处理，0：未处理
    private Integer parentContractId;

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public Integer getUntreatedInfoId() {
        return untreatedInfoId;
    }

    public void setUntreatedInfoId(Integer untreatedInfoId) {
        this.untreatedInfoId = untreatedInfoId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getDatumType() {
        return datumType;
    }

    public void setDatumType(String datumType) {
        this.datumType = datumType;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getParentContractId() {
        return parentContractId;
    }

    public void setParentContractId(Integer parentContractId) {
        this.parentContractId = parentContractId;
    }
}
