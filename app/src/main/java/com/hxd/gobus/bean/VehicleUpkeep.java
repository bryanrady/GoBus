package com.hxd.gobus.bean;

import com.google.gson.Gson;

import java.math.BigDecimal;

/**
 * @author: wangqingbin
 * @date: 2019/9/23 11:21
 */

public class VehicleUpkeep {


    /**
     * upkeepId : int //主键ID
     * infoManageId : int //车辆信息ID
     * vehicleName : string //车辆名称
     * vehicleMark : string //车牌号
     * upkeepTransactStatus : string //保养办理状态
     * upkeepDetails : string //保养内容
     * planUpkeepTime : date //预计保养时间
     * planUpkeepCost : double //预计保养费用/元
     * actualUpkeepTime : date //实际保养时间
     * actualUpkeepCost : double //实际保养费用/元
     * upkeepSite : string //保养地点
     * upkeepShopName : string //保养门店名称
     * upkeepShopPhone : string //保养门店电话
     * upkeepTransactor : string //保养办理人
     * upkeepTransactorPhone : string //保养办理人电话
     * serialNumber : string //记录编号
     * applyPerson : string //申报人
     * applyUnit : string //申报部门
     * applyDate : date //申报时间
     * remarks : string //备注
     * submitStatus : string //提交状态：0.未提交；1.已提交；
     * infoAlterStatus : string //信息变更状态:0.未变更;1.已变更;2:变更作废
     * infoOldId : int //旧信息ID(变更前主键ID)
     * datumType : string //流程类型
     * flowUnit : string //流程部门
     * approvalStatus : string //审核状态：0.未申请；1.审核中；2.审核完成；
     * status : string //数据状态：0.有效；1.无效；
     * createBy : string //创建人
     * createDate : date //创建时间
     * updateBy : string //修改人
     * updateDate : date //修改时间
     * alternateField1 : string //备用字段1
     * alternateField2 : string //备用字段2
     * alternateField3 : string //备用字段3
     * alternateField4 : string //备用字段4
     * alternateField5 : string //备用字段5
     * alternateField6 : string //备用字段6
     * alternateField7 : string //备用字段7
     * alternateField8 : string //备用字段8
     * alternateField9 : string //备用字段9
     * applyUnits : string //申报部门
     * applyUnitName : string //申报部门名称
     * sns : string //中文序号
     * No : int //序号
     * uuid : string //附件UUID
     * accessoryName : string //附件名称
     */

    private Integer upkeepId;
    private Integer infoManageId;
    private String vehicleName;
    private String vehicleMark;
    private String upkeepTransactStatus;
    private String upkeepDetails;
    private String planUpkeepTime;
    private BigDecimal planUpkeepCost;
    private String actualUpkeepTime;
    private BigDecimal actualUpkeepCost;
    private String upkeepSite;
    private String upkeepShopName;
    private String upkeepShopPhone;
    private String upkeepTransactor;
    private String upkeepTransactorPhone;
    private String serialNumber;
    private String applyPerson;
    private String applyUnit;
    private String applyDate;
    private String remarks;
    private String submitStatus;
    private String infoAlterStatus;
    private String infoOldId;
    private String datumType;
    private String flowUnit;
    private String approvalStatus;
    private String status;
    private String createBy;
    private String createDate;
    private String updateBy;
    private String updateDate;
    private String alternateField1;
    private String alternateField2;
    private String alternateField3;
    private String alternateField4;
    private String alternateField5;
    private String alternateField6;
    private String alternateField7;
    private String alternateField8;
    private String alternateField9;
    private String applyUnits;
    private String applyUnitName;
    private String sns;
    private String No;
    private String uuid;
    private String accessoryName;

    public static VehicleUpkeep objectFromData(String str) {

        return new Gson().fromJson(str, VehicleUpkeep.class);
    }

    public Integer getUpkeepId() {
        return upkeepId;
    }

    public void setUpkeepId(Integer upkeepId) {
        this.upkeepId = upkeepId;
    }

    public Integer getInfoManageId() {
        return infoManageId;
    }

    public void setInfoManageId(Integer infoManageId) {
        this.infoManageId = infoManageId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleMark() {
        return vehicleMark;
    }

    public void setVehicleMark(String vehicleMark) {
        this.vehicleMark = vehicleMark;
    }

    public String getUpkeepTransactStatus() {
        return upkeepTransactStatus;
    }

    public void setUpkeepTransactStatus(String upkeepTransactStatus) {
        this.upkeepTransactStatus = upkeepTransactStatus;
    }

    public String getUpkeepDetails() {
        return upkeepDetails;
    }

    public void setUpkeepDetails(String upkeepDetails) {
        this.upkeepDetails = upkeepDetails;
    }

    public String getPlanUpkeepTime() {
        return planUpkeepTime;
    }

    public void setPlanUpkeepTime(String planUpkeepTime) {
        this.planUpkeepTime = planUpkeepTime;
    }

    public BigDecimal getPlanUpkeepCost() {
        return planUpkeepCost;
    }

    public void setPlanUpkeepCost(BigDecimal planUpkeepCost) {
        this.planUpkeepCost = planUpkeepCost;
    }

    public String getActualUpkeepTime() {
        return actualUpkeepTime;
    }

    public void setActualUpkeepTime(String actualUpkeepTime) {
        this.actualUpkeepTime = actualUpkeepTime;
    }

    public BigDecimal getActualUpkeepCost() {
        return actualUpkeepCost;
    }

    public void setActualUpkeepCost(BigDecimal actualUpkeepCost) {
        this.actualUpkeepCost = actualUpkeepCost;
    }

    public String getUpkeepSite() {
        return upkeepSite;
    }

    public void setUpkeepSite(String upkeepSite) {
        this.upkeepSite = upkeepSite;
    }

    public String getUpkeepShopName() {
        return upkeepShopName;
    }

    public void setUpkeepShopName(String upkeepShopName) {
        this.upkeepShopName = upkeepShopName;
    }

    public String getUpkeepShopPhone() {
        return upkeepShopPhone;
    }

    public void setUpkeepShopPhone(String upkeepShopPhone) {
        this.upkeepShopPhone = upkeepShopPhone;
    }

    public String getUpkeepTransactor() {
        return upkeepTransactor;
    }

    public void setUpkeepTransactor(String upkeepTransactor) {
        this.upkeepTransactor = upkeepTransactor;
    }

    public String getUpkeepTransactorPhone() {
        return upkeepTransactorPhone;
    }

    public void setUpkeepTransactorPhone(String upkeepTransactorPhone) {
        this.upkeepTransactorPhone = upkeepTransactorPhone;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getApplyPerson() {
        return applyPerson;
    }

    public void setApplyPerson(String applyPerson) {
        this.applyPerson = applyPerson;
    }

    public String getApplyUnit() {
        return applyUnit;
    }

    public void setApplyUnit(String applyUnit) {
        this.applyUnit = applyUnit;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(String submitStatus) {
        this.submitStatus = submitStatus;
    }

    public String getInfoAlterStatus() {
        return infoAlterStatus;
    }

    public void setInfoAlterStatus(String infoAlterStatus) {
        this.infoAlterStatus = infoAlterStatus;
    }

    public String getInfoOldId() {
        return infoOldId;
    }

    public void setInfoOldId(String infoOldId) {
        this.infoOldId = infoOldId;
    }

    public String getDatumType() {
        return datumType;
    }

    public void setDatumType(String datumType) {
        this.datumType = datumType;
    }

    public String getFlowUnit() {
        return flowUnit;
    }

    public void setFlowUnit(String flowUnit) {
        this.flowUnit = flowUnit;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getAlternateField1() {
        return alternateField1;
    }

    public void setAlternateField1(String alternateField1) {
        this.alternateField1 = alternateField1;
    }

    public String getAlternateField2() {
        return alternateField2;
    }

    public void setAlternateField2(String alternateField2) {
        this.alternateField2 = alternateField2;
    }

    public String getAlternateField3() {
        return alternateField3;
    }

    public void setAlternateField3(String alternateField3) {
        this.alternateField3 = alternateField3;
    }

    public String getAlternateField4() {
        return alternateField4;
    }

    public void setAlternateField4(String alternateField4) {
        this.alternateField4 = alternateField4;
    }

    public String getAlternateField5() {
        return alternateField5;
    }

    public void setAlternateField5(String alternateField5) {
        this.alternateField5 = alternateField5;
    }

    public String getAlternateField6() {
        return alternateField6;
    }

    public void setAlternateField6(String alternateField6) {
        this.alternateField6 = alternateField6;
    }

    public String getAlternateField7() {
        return alternateField7;
    }

    public void setAlternateField7(String alternateField7) {
        this.alternateField7 = alternateField7;
    }

    public String getAlternateField8() {
        return alternateField8;
    }

    public void setAlternateField8(String alternateField8) {
        this.alternateField8 = alternateField8;
    }

    public String getAlternateField9() {
        return alternateField9;
    }

    public void setAlternateField9(String alternateField9) {
        this.alternateField9 = alternateField9;
    }

    public String getApplyUnits() {
        return applyUnits;
    }

    public void setApplyUnits(String applyUnits) {
        this.applyUnits = applyUnits;
    }

    public String getApplyUnitName() {
        return applyUnitName;
    }

    public void setApplyUnitName(String applyUnitName) {
        this.applyUnitName = applyUnitName;
    }

    public String getSns() {
        return sns;
    }

    public void setSns(String sns) {
        this.sns = sns;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String No) {
        this.No = No;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAccessoryName() {
        return accessoryName;
    }

    public void setAccessoryName(String accessoryName) {
        this.accessoryName = accessoryName;
    }
}
