package com.hxd.gobus.bean;

import android.content.Intent;

import com.google.gson.Gson;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author: wangqingbin
 * @date: 2019/9/12 14:20
 */

public class Vehicle implements Serializable {

    /**
     * managementId : int //用车ID
     * applyPerson : int //申请人
     * applyPersonName : string
     * applyDate : date //申请时间
     * destination : string //目的地
     * useCarType : string //用车类型
     * useCarRange : string //用车范围
     * predictStartDate : date //预计用车时间
     * predictDuration : string //预计用车时长
     * predictDenDate : date //预计结束时间
     * carNumber : string //号牌
     * carName : string //车辆名称
     * useReason : string //用车事由
     * assignDriver : string //指定司机
     * travelPartner : string //随行人员
     * remareks : string //备注
     * approvalStatus : string //审核状态
     * useCarStatus : string //用车状态
     * recordNumber : string //记录编号
     * isUseCar : string //是否用车
     * receiveCarPerson : int //领车人
     * receiveCarPersonName : string
     * receiveCarDate : date //领车时间
     * backCarPerson : int //还车人
     * backCarPersonName : string
     * backCarDate : date //还车时间
     * status : string //数据状态：0:有效；1:无效
     * createBy : int //创建人
     * createDate : date //创建时间
     * updateBy : int //修改人
     * updateDate : date //修改时间
     * datumType : string //流转类型:0:项目策划审核;1:周报审核;2:月报审核;
     * applyUnitId : int //默认字段2
     * applyUnitName : string
     * applyUnitNames : string
     * defaultField3 : string //默认字段3
     * defaultField4 : string //默认字段4
     * defaultField5 : string //默认字段5
     * defaultField6 : string //默认字段6
     * defaultField7 : string //默认字段7
     * sns : string //中文序号
     * No : int //序号 @return
     * uuid : string
     * trafficMileage : string
     * infoManageId : int //车辆ID
     * vehicleName : string
     * querySql : string
     * datumDataId : int //流转数据ID
     * datumDetailInfoList : [{"datumDetailInfoId":"int //审核流转明细信息ID","datumApprovalInfoId":"int //审核流转信息ID","approvalUnit":"string //审核部门","approvalUser":"string //审核人","arrivedDate":"date //到达时间","approvalIdea":"string //审核意见","approvalDate":"date //审核时间","approvalStatus":"string //审核状态：0：通过；1：退回；2：废弃","createBy":"string //创建人","createDate":"date //创建时间","updateBy":"string //修改人","updateDate":"date //修改时间","approvalSerialNo":"double //审核顺序号","bakApprovalStatus":"string //备份审核状态：0：通过；1：退回；2：废弃","bakReturnStatus":"string //备份审核退回状态：0：是；1：否；","schemeBookId":"int","unitName":"string //部门名称","approvalWorkHours":"double //审核单位确认工时数","approvalStartDate":"date //审核单位确认开始日期","approvalEndDate":"date //审核单位确认结束日期","approvalTime":"string //审核时长","datumType":"string //流转类型:0:项目策划审核;1:周报审核;2:月报审核;","datumDataId":"int //流转数据ID","approvalDateStr":"string //审核时间","totalNodeNumber":"double //总审核节点数","personName":"string //名字","currentNodeNumber":"double //当前已审核节点数","flowId":"int //流程ID","paySalary":"double //应发工资","actualSalary":"double //实发工资","contractId":"int //承接合同ID","contractSn":"string","projectManager":"string //项目管理人","contractDate":"date //合同日期","signedDate":"date //签订日期","unitContractSum":"double //部门合同额","otherCost":"double //其他成本","isSendGeneralManager":"string //是否发送总经理：0：是；1：否；","subManager":"string //下一级审核人:分管领导","generalManager":"string //下一级审核人；总经理","articleManager":"string //下一级审核人:收文分管领导","branchedLeaders":"string //下一级审核人；(资产采购第二步审核选择分管领导)","workNo":"string //工号","title":"string //标题","step":"int //流程步长,add by Mr.Wang","isConsortiumBid":"string //IS_CONSORTIUM_BID;","appUnitIdOrPersonId":"string //待办事项提醒：0:审核人员提醒;1: 数据归属提醒;2:人员角色提醒;","undertakeDate":"date //承接时间","factSum":"double //实际承接额","mainUnit":"int //主体承担部门","assistUnit":"string //协助承担部门","url":"string //消息提醒跳转url","typeList":"string[] //流程类型集合","bakDatumType":"string //备用审核流程;","contractBusinessPersonId":"string //合同分解项目负责人\r\t  @return","workHourSettleNodeList":"string","needGeneralManagerAudit":"string //是否需要总经理审核 ：0：是；1：否；"}]
     * datumApprovalInfoId : int //审核流转信息ID
     * totalNodeNumber : double //总审核节点数
     * currentNodeNumber : double //审核流转信息-当前已审核节点数
     * flowId : int //流程ID
     * approvalSerialNo : double //审核顺序号
     */

    private Integer managementId;
    private Integer applyPerson;
    private String applyPersonName;
    private String applyDate;
    private String destination;
    private String useCarType;
    private String useCarRange;
    private String predictStartDate;
    private String predictDuration;
    private String predictDenDate;
    private String carNumber;
    private String carName;
    private String useReason;
    private Integer assignDriver;
    private String assignDriverName;
    private String travelPartner;
    private String remareks;
    private String approvalStatus;
    private String useCarStatus;
    private String recordNumber;
    private String isUseCar;
    private String isUseCarName;
    private Integer receiveCarPerson;
    private String receiveCarPersonName;
    private String receiveCarDate;
    private Integer backCarPerson;
    private String backCarPersonName;
    private String backCarDate;
    private String status;
    private String createBy;
    private String createDate;
    private String updateBy;
    private String updateDate;
    private String datumType;
    private Integer applyUnitId;
    private String applyUnitName;
    private String applyUnitNames;
    private String defaultField3;
    private String defaultField4;
    private String defaultField5;
    private String defaultField6;
    private String defaultField7;
    private String sns;
    private String No;
    private String uuid;
    private String trafficMileage;
    private Integer infoManageId;
    private String vehicleName;
    private String querySql;
    private Integer datumDataId;
    private String datumApprovalInfoId;
    private String totalNodeNumber;
    private String currentNodeNumber;
    private String flowId;
    private String approvalSerialNo;
    private List<DatumDetail> datumDetailInfoList;
    private BigDecimal lat;
    private BigDecimal lng;
    private String useCarTypeName;
    private String useCarStatusName;
    private String useCarRangeName;
    private String predictDurationName;


    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setPredictDurationName(String predictDurationName) {
        this.predictDurationName = predictDurationName;
    }

    public String getPredictDurationName() {
        return predictDurationName;
    }

    public void setUseCarRangeName(String useCarRangeName) {
        this.useCarRangeName = useCarRangeName;
    }

    public void setIsUseCarName(String isUseCarName) {
        this.isUseCarName = isUseCarName;
    }

    public String getIsUseCarName() {
        return isUseCarName;
    }

    public String getUseCarRangeName() {
        return useCarRangeName;
    }

    public void setUseCarTypeName(String useCarTypeName) {
        this.useCarTypeName = useCarTypeName;
    }

    public String getUseCarTypeName() {
        return useCarTypeName;
    }

    public static Vehicle objectFromData(String str) {

        return new Gson().fromJson(str, Vehicle.class);
    }

    public Integer getManagementId() {
        return managementId;
    }

    public void setManagementId(Integer managementId) {
        this.managementId = managementId;
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

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getUseCarType() {
        return useCarType;
    }

    public void setUseCarType(String useCarType) {
        this.useCarType = useCarType;
    }

    public String getUseCarRange() {
        return useCarRange;
    }

    public void setUseCarRange(String useCarRange) {
        this.useCarRange = useCarRange;
    }

    public String getPredictStartDate() {
        return predictStartDate;
    }

    public void setPredictStartDate(String predictStartDate) {
        this.predictStartDate = predictStartDate;
    }

    public String getPredictDuration() {
        return predictDuration;
    }

    public void setPredictDuration(String predictDuration) {
        this.predictDuration = predictDuration;
    }

    public String getUseCarStatusName() {
        return useCarStatusName;
    }

    public void setUseCarStatusName(String useCarStatusName) {
        this.useCarStatusName = useCarStatusName;
    }

    public String getPredictDenDate() {
        return predictDenDate;
    }

    public void setPredictDenDate(String predictDenDate) {
        this.predictDenDate = predictDenDate;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getUseReason() {
        return useReason;
    }

    public void setUseReason(String useReason) {
        this.useReason = useReason;
    }

    public Integer getAssignDriver() {
        return assignDriver;
    }

    public void setAssignDriver(Integer assignDriver) {
        this.assignDriver = assignDriver;
    }

    public void setAssignDriverName(String assignDriverName) {
        this.assignDriverName = assignDriverName;
    }

    public String getAssignDriverName() {
        return assignDriverName;
    }

    public String getTravelPartner() {
        return travelPartner;
    }

    public void setTravelPartner(String travelPartner) {
        this.travelPartner = travelPartner;
    }

    public String getRemareks() {
        return remareks;
    }

    public void setRemareks(String remareks) {
        this.remareks = remareks;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getUseCarStatus() {
        return useCarStatus;
    }

    public void setUseCarStatus(String useCarStatus) {
        this.useCarStatus = useCarStatus;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public String getIsUseCar() {
        return isUseCar;
    }

    public void setIsUseCar(String isUseCar) {
        this.isUseCar = isUseCar;
    }

    public Integer getReceiveCarPerson() {
        return receiveCarPerson;
    }

    public void setReceiveCarPerson(Integer receiveCarPerson) {
        this.receiveCarPerson = receiveCarPerson;
    }

    public String getReceiveCarPersonName() {
        return receiveCarPersonName;
    }

    public void setReceiveCarPersonName(String receiveCarPersonName) {
        this.receiveCarPersonName = receiveCarPersonName;
    }

    public String getReceiveCarDate() {
        return receiveCarDate;
    }

    public void setReceiveCarDate(String receiveCarDate) {
        this.receiveCarDate = receiveCarDate;
    }

    public Integer getBackCarPerson() {
        return backCarPerson;
    }

    public void setBackCarPerson(Integer backCarPerson) {
        this.backCarPerson = backCarPerson;
    }

    public String getBackCarPersonName() {
        return backCarPersonName;
    }

    public void setBackCarPersonName(String backCarPersonName) {
        this.backCarPersonName = backCarPersonName;
    }

    public String getBackCarDate() {
        return backCarDate;
    }

    public void setBackCarDate(String backCarDate) {
        this.backCarDate = backCarDate;
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

    public String getDatumType() {
        return datumType;
    }

    public void setDatumType(String datumType) {
        this.datumType = datumType;
    }

    public Integer getApplyUnitId() {
        return applyUnitId;
    }

    public void setApplyUnitId(Integer applyUnitId) {
        this.applyUnitId = applyUnitId;
    }

    public String getApplyUnitName() {
        return applyUnitName;
    }

    public void setApplyUnitName(String applyUnitName) {
        this.applyUnitName = applyUnitName;
    }

    public String getApplyUnitNames() {
        return applyUnitNames;
    }

    public void setApplyUnitNames(String applyUnitNames) {
        this.applyUnitNames = applyUnitNames;
    }

    public String getDefaultField3() {
        return defaultField3;
    }

    public void setDefaultField3(String defaultField3) {
        this.defaultField3 = defaultField3;
    }

    public String getDefaultField4() {
        return defaultField4;
    }

    public void setDefaultField4(String defaultField4) {
        this.defaultField4 = defaultField4;
    }

    public String getDefaultField5() {
        return defaultField5;
    }

    public void setDefaultField5(String defaultField5) {
        this.defaultField5 = defaultField5;
    }

    public String getDefaultField6() {
        return defaultField6;
    }

    public void setDefaultField6(String defaultField6) {
        this.defaultField6 = defaultField6;
    }

    public String getDefaultField7() {
        return defaultField7;
    }

    public void setDefaultField7(String defaultField7) {
        this.defaultField7 = defaultField7;
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

    public String getTrafficMileage() {
        return trafficMileage;
    }

    public void setTrafficMileage(String trafficMileage) {
        this.trafficMileage = trafficMileage;
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

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public Integer getDatumDataId() {
        return datumDataId;
    }

    public void setDatumDataId(Integer datumDataId) {
        this.datumDataId = datumDataId;
    }

    public String getDatumApprovalInfoId() {
        return datumApprovalInfoId;
    }

    public void setDatumApprovalInfoId(String datumApprovalInfoId) {
        this.datumApprovalInfoId = datumApprovalInfoId;
    }

    public String getTotalNodeNumber() {
        return totalNodeNumber;
    }

    public void setTotalNodeNumber(String totalNodeNumber) {
        this.totalNodeNumber = totalNodeNumber;
    }

    public String getCurrentNodeNumber() {
        return currentNodeNumber;
    }

    public void setCurrentNodeNumber(String currentNodeNumber) {
        this.currentNodeNumber = currentNodeNumber;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getApprovalSerialNo() {
        return approvalSerialNo;
    }

    public void setApprovalSerialNo(String approvalSerialNo) {
        this.approvalSerialNo = approvalSerialNo;
    }

    public List<DatumDetail> getDatumDetailInfoList() {
        return datumDetailInfoList;
    }

    public void setDatumDetailInfoList(List<DatumDetail> datumDetailInfoList) {
        this.datumDetailInfoList = datumDetailInfoList;
    }

}
