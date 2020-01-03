package com.hxd.gobus.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * @author: wangqingbin
 * @date: 2019/9/12 14:23
 */

public class DatumDetail implements Serializable{


    /**
     * datumDetailInfoId : int //审核流转明细信息ID
     * datumApprovalInfoId : int //审核流转信息ID
     * approvalUnit : string //审核部门
     * approvalUser : string //审核人
     * arrivedDate : date //到达时间
     * approvalIdea : string //审核意见
     * approvalDate : date //审核时间
     * approvalStatus : string //审核状态：0：通过；1：退回；2：废弃
     * createBy : string //创建人
     * createDate : date //创建时间
     * updateBy : string //修改人
     * updateDate : date //修改时间
     * approvalSerialNo : double //审核顺序号
     * bakApprovalStatus : string //备份审核状态：0：通过；1：退回；2：废弃
     * bakReturnStatus : string //备份审核退回状态：0：是；1：否；
     * schemeBookId : int
     * unitName : string //部门名称
     * approvalWorkHours : double //审核单位确认工时数
     * approvalStartDate : date //审核单位确认开始日期
     * approvalEndDate : date //审核单位确认结束日期
     * approvalTime : string //审核时长
     * datumType : string //流转类型:0:项目策划审核;1:周报审核;2:月报审核;
     * datumDataId : int //流转数据ID
     * approvalDateStr : string //审核时间
     * totalNodeNumber : double //总审核节点数
     * personName : string //名字
     * currentNodeNumber : double //当前已审核节点数
     * flowId : int //流程ID
     * paySalary : double //应发工资
     * actualSalary : double //实发工资
     * contractId : int //承接合同ID
     * contractSn : string
     * projectManager : string //项目管理人
     * contractDate : date //合同日期
     * signedDate : date //签订日期
     * unitContractSum : double //部门合同额
     * otherCost : double //其他成本
     * isSendGeneralManager : string //是否发送总经理：0：是；1：否；
     * subManager : string //下一级审核人:分管领导
     * generalManager : string //下一级审核人；总经理
     * articleManager : string //下一级审核人:收文分管领导
     * branchedLeaders : string //下一级审核人；(资产采购第二步审核选择分管领导)
     * workNo : string //工号
     * title : string //标题
     * step : int //流程步长,add by Mr.Wang
     * isConsortiumBid : string //IS_CONSORTIUM_BID;
     * appUnitIdOrPersonId : string //待办事项提醒：0:审核人员提醒;1: 数据归属提醒;2:人员角色提醒;
     * undertakeDate : date //承接时间
     * factSum : double //实际承接额
     * mainUnit : int //主体承担部门
     * assistUnit : string //协助承担部门
     * url : string //消息提醒跳转url
     * typeList : string[] //流程类型集合
     * bakDatumType : string //备用审核流程;
     * contractBusinessPersonId : string //合同分解项目负责人 @return
     * workHourSettleNodeList : string
     * needGeneralManagerAudit : string //是否需要总经理审核 ：0：是；1：否；
     */

    private String datumDetailInfoId;
    private String datumApprovalInfoId;
    private String approvalUnit;
    private String approvalUser;
    private String arrivedDate;
    private String approvalIdea;
    private String approvalDate;
    private String approvalStatus;
    private String createBy;
    private String createDate;
    private String updateBy;
    private String updateDate;
    private String approvalSerialNo;
    private String bakApprovalStatus;
    private String bakReturnStatus;
    private String schemeBookId;
    private String unitName;
    private String approvalWorkHours;
    private String approvalStartDate;
    private String approvalEndDate;
    private String approvalTime;
    private String datumType;
    private String datumDataId;
    private String approvalDateStr;
    private String totalNodeNumber;
    private String personName;
    private String currentNodeNumber;
    private String flowId;
    private String paySalary;
    private String actualSalary;
    private String contractId;
    private String contractSn;
    private String projectManager;
    private String contractDate;
    private String signedDate;
    private String unitContractSum;
    private String otherCost;
    private String isSendGeneralManager;
    private String subManager;
    private String generalManager;
    private String articleManager;
    private String branchedLeaders;
    private String workNo;
    private String title;
    private String step;
    private String isConsortiumBid;
    private String appUnitIdOrPersonId;
    private String undertakeDate;
    private String factSum;
    private String mainUnit;
    private String assistUnit;
    private String url;
    private String typeList;
    private String bakDatumType;
    private String contractBusinessPersonId;
    private String workHourSettleNodeList;
    private String needGeneralManagerAudit;

    private boolean isReminder;

    public static DatumDetail objectFromData(String str) {

        return new Gson().fromJson(str, DatumDetail.class);
    }

    public void setReminder(boolean reminder) {
        isReminder = reminder;
    }

    public boolean isReminder() {
        return isReminder;
    }

    public String getDatumDetailInfoId() {
        return datumDetailInfoId;
    }

    public void setDatumDetailInfoId(String datumDetailInfoId) {
        this.datumDetailInfoId = datumDetailInfoId;
    }

    public String getDatumApprovalInfoId() {
        return datumApprovalInfoId;
    }

    public void setDatumApprovalInfoId(String datumApprovalInfoId) {
        this.datumApprovalInfoId = datumApprovalInfoId;
    }

    public String getApprovalUnit() {
        return approvalUnit;
    }

    public void setApprovalUnit(String approvalUnit) {
        this.approvalUnit = approvalUnit;
    }

    public String getApprovalUser() {
        return approvalUser;
    }

    public void setApprovalUser(String approvalUser) {
        this.approvalUser = approvalUser;
    }

    public String getArrivedDate() {
        return arrivedDate;
    }

    public void setArrivedDate(String arrivedDate) {
        this.arrivedDate = arrivedDate;
    }

    public String getApprovalIdea() {
        return approvalIdea;
    }

    public void setApprovalIdea(String approvalIdea) {
        this.approvalIdea = approvalIdea;
    }

    public String getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
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

    public String getApprovalSerialNo() {
        return approvalSerialNo;
    }

    public void setApprovalSerialNo(String approvalSerialNo) {
        this.approvalSerialNo = approvalSerialNo;
    }

    public String getBakApprovalStatus() {
        return bakApprovalStatus;
    }

    public void setBakApprovalStatus(String bakApprovalStatus) {
        this.bakApprovalStatus = bakApprovalStatus;
    }

    public String getBakReturnStatus() {
        return bakReturnStatus;
    }

    public void setBakReturnStatus(String bakReturnStatus) {
        this.bakReturnStatus = bakReturnStatus;
    }

    public String getSchemeBookId() {
        return schemeBookId;
    }

    public void setSchemeBookId(String schemeBookId) {
        this.schemeBookId = schemeBookId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getApprovalWorkHours() {
        return approvalWorkHours;
    }

    public void setApprovalWorkHours(String approvalWorkHours) {
        this.approvalWorkHours = approvalWorkHours;
    }

    public String getApprovalStartDate() {
        return approvalStartDate;
    }

    public void setApprovalStartDate(String approvalStartDate) {
        this.approvalStartDate = approvalStartDate;
    }

    public String getApprovalEndDate() {
        return approvalEndDate;
    }

    public void setApprovalEndDate(String approvalEndDate) {
        this.approvalEndDate = approvalEndDate;
    }

    public String getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(String approvalTime) {
        this.approvalTime = approvalTime;
    }

    public String getDatumType() {
        return datumType;
    }

    public void setDatumType(String datumType) {
        this.datumType = datumType;
    }

    public String getDatumDataId() {
        return datumDataId;
    }

    public void setDatumDataId(String datumDataId) {
        this.datumDataId = datumDataId;
    }

    public String getApprovalDateStr() {
        return approvalDateStr;
    }

    public void setApprovalDateStr(String approvalDateStr) {
        this.approvalDateStr = approvalDateStr;
    }

    public String getTotalNodeNumber() {
        return totalNodeNumber;
    }

    public void setTotalNodeNumber(String totalNodeNumber) {
        this.totalNodeNumber = totalNodeNumber;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
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

    public String getPaySalary() {
        return paySalary;
    }

    public void setPaySalary(String paySalary) {
        this.paySalary = paySalary;
    }

    public String getActualSalary() {
        return actualSalary;
    }

    public void setActualSalary(String actualSalary) {
        this.actualSalary = actualSalary;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getContractSn() {
        return contractSn;
    }

    public void setContractSn(String contractSn) {
        this.contractSn = contractSn;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(String contractDate) {
        this.contractDate = contractDate;
    }

    public String getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(String signedDate) {
        this.signedDate = signedDate;
    }

    public String getUnitContractSum() {
        return unitContractSum;
    }

    public void setUnitContractSum(String unitContractSum) {
        this.unitContractSum = unitContractSum;
    }

    public String getOtherCost() {
        return otherCost;
    }

    public void setOtherCost(String otherCost) {
        this.otherCost = otherCost;
    }

    public String getIsSendGeneralManager() {
        return isSendGeneralManager;
    }

    public void setIsSendGeneralManager(String isSendGeneralManager) {
        this.isSendGeneralManager = isSendGeneralManager;
    }

    public String getSubManager() {
        return subManager;
    }

    public void setSubManager(String subManager) {
        this.subManager = subManager;
    }

    public String getGeneralManager() {
        return generalManager;
    }

    public void setGeneralManager(String generalManager) {
        this.generalManager = generalManager;
    }

    public String getArticleManager() {
        return articleManager;
    }

    public void setArticleManager(String articleManager) {
        this.articleManager = articleManager;
    }

    public String getBranchedLeaders() {
        return branchedLeaders;
    }

    public void setBranchedLeaders(String branchedLeaders) {
        this.branchedLeaders = branchedLeaders;
    }

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getIsConsortiumBid() {
        return isConsortiumBid;
    }

    public void setIsConsortiumBid(String isConsortiumBid) {
        this.isConsortiumBid = isConsortiumBid;
    }

    public String getAppUnitIdOrPersonId() {
        return appUnitIdOrPersonId;
    }

    public void setAppUnitIdOrPersonId(String appUnitIdOrPersonId) {
        this.appUnitIdOrPersonId = appUnitIdOrPersonId;
    }

    public String getUndertakeDate() {
        return undertakeDate;
    }

    public void setUndertakeDate(String undertakeDate) {
        this.undertakeDate = undertakeDate;
    }

    public String getFactSum() {
        return factSum;
    }

    public void setFactSum(String factSum) {
        this.factSum = factSum;
    }

    public String getMainUnit() {
        return mainUnit;
    }

    public void setMainUnit(String mainUnit) {
        this.mainUnit = mainUnit;
    }

    public String getAssistUnit() {
        return assistUnit;
    }

    public void setAssistUnit(String assistUnit) {
        this.assistUnit = assistUnit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTypeList() {
        return typeList;
    }

    public void setTypeList(String typeList) {
        this.typeList = typeList;
    }

    public String getBakDatumType() {
        return bakDatumType;
    }

    public void setBakDatumType(String bakDatumType) {
        this.bakDatumType = bakDatumType;
    }

    public String getContractBusinessPersonId() {
        return contractBusinessPersonId;
    }

    public void setContractBusinessPersonId(String contractBusinessPersonId) {
        this.contractBusinessPersonId = contractBusinessPersonId;
    }

    public String getWorkHourSettleNodeList() {
        return workHourSettleNodeList;
    }

    public void setWorkHourSettleNodeList(String workHourSettleNodeList) {
        this.workHourSettleNodeList = workHourSettleNodeList;
    }

    public String getNeedGeneralManagerAudit() {
        return needGeneralManagerAudit;
    }

    public void setNeedGeneralManagerAudit(String needGeneralManagerAudit) {
        this.needGeneralManagerAudit = needGeneralManagerAudit;
    }
}
