package com.hxd.gobus.bean;

import com.google.gson.Gson;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author: wangqingbin
 * @date: 2019/9/20 10:16
 */

public class Repair implements Serializable{


    /**
     * repairId : int //主键ID
     * recordNumber : string //记录编号
     * applyPerson : int //申请人
     * applyPersonName : string
     * applyDate : date //申请日期
     * estimatedCosts : double //本次预计费用
     * repairStatus : string //维修状态
     * repairDetail : string //车辆情况描述
     * handler : int //处理人
     * actualCost : double //本次实际费用
     * repairDate : date //维修时间
     * costPayer : string //费用承担方
     * applyUnit : string //申请部门
     * approvalStatus : string //审核状态 0未申请 1审核中 2审核完成
     * approvalPerson : string //审核人
     * status : string //数据状态：0.有效；1.无效；
     * datumType : string //流程类型
     * flowUnit : string //流程部门
     * createBy : string //创建人
     * createDate : date //创建时间
     * updateBy : string //修改人
     * updateDate : date //修改时间
     * infoManageId : int //车辆信息ID
     * alternateField2 : string //备用字段2
     * alternateField3 : string //备用字段3
     * alternateField4 : string //备用字段4
     * alternateField5 : string //备用字段5
     * alternateField6 : string //备用字段6
     * alternateField7 : string //备用字段7
     * alternateField8 : string //备用字段8
     * alternateField9 : string //备用字段9
     * remarks : string //备注
     * sns : string //中文序号
     * No : int //序号 @return
     * vehicleName : string
     * vehicleMark : string
     */

    private Integer repairId;
    private String recordNumber;
    private Integer applyPerson;
    private String applyPersonName;
    private String applyDate;
    private BigDecimal estimatedCosts;
    private String repairStatus;
    private String repairStatusName;
    private String repairDetail;
    private String handler;
    private String handlerName;
    private BigDecimal actualCost;
    private String repairDate;
    private String costPayer;
    private String applyUnit;
    private String approvalStatus;
    private String approvalPerson;
    private String status;
    private String datumType;
    private String flowUnit;
    private String createBy;
    private String createDate;
    private String updateBy;
    private String updateDate;
    private Integer infoManageId;
    private String alternateField2;
    private String alternateField3;
    private String alternateField4;
    private String alternateField5;
    private String alternateField6;
    private String alternateField7;
    private String alternateField8;
    private String alternateField9;
    private String remarks;
    private String sns;
    private String No;
    private String vehicleName;
    private String vehicleMark;
    private String uuid;

    private List<DatumDetail> datumDetailInfoList;

    public static Repair objectFromData(String str) {

        return new Gson().fromJson(str, Repair.class);
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public List<DatumDetail> getDatumDetailInfoList() {
        return datumDetailInfoList;
    }

    public void setDatumDetailInfoList(List<DatumDetail> datumDetailInfoList) {
        this.datumDetailInfoList = datumDetailInfoList;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setRepairStatusName(String repairStatusName) {
        this.repairStatusName = repairStatusName;
    }

    public String getRepairStatusName() {
        return repairStatusName;
    }

    public Integer getRepairId() {
        return repairId;
    }

    public void setRepairId(Integer repairId) {
        this.repairId = repairId;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public Integer getApplyPerson() {
        return applyPerson;
    }

    public void setApplyPerson(Integer applyPerson) {
        this.applyPerson = applyPerson;
    }

    public String getApplyPersonName() {
        return applyPersonName;
    }

    public void setApplyPersonName(String applyPersonName) {
        this.applyPersonName = applyPersonName;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public BigDecimal getEstimatedCosts() {
        return estimatedCosts;
    }

    public void setEstimatedCosts(BigDecimal estimatedCosts) {
        this.estimatedCosts = estimatedCosts;
    }

    public String getRepairStatus() {
        return repairStatus;
    }

    public void setRepairStatus(String repairStatus) {
        this.repairStatus = repairStatus;
    }

    public String getRepairDetail() {
        return repairDetail;
    }

    public void setRepairDetail(String repairDetail) {
        this.repairDetail = repairDetail;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public BigDecimal getActualCost() {
        return actualCost;
    }

    public void setActualCost(BigDecimal actualCost) {
        this.actualCost = actualCost;
    }

    public String getRepairDate() {
        return repairDate;
    }

    public void setRepairDate(String repairDate) {
        this.repairDate = repairDate;
    }

    public String getCostPayer() {
        return costPayer;
    }

    public void setCostPayer(String costPayer) {
        this.costPayer = costPayer;
    }

    public String getApplyUnit() {
        return applyUnit;
    }

    public void setApplyUnit(String applyUnit) {
        this.applyUnit = applyUnit;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovalPerson() {
        return approvalPerson;
    }

    public void setApprovalPerson(String approvalPerson) {
        this.approvalPerson = approvalPerson;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Integer getInfoManageId() {
        return infoManageId;
    }

    public void setInfoManageId(Integer infoManageId) {
        this.infoManageId = infoManageId;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
}
