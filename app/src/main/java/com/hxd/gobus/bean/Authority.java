package com.hxd.gobus.bean;

import java.io.Serializable;

/**
 * @author: wangqingbin
 * @date: 2019/9/17 17:24
 */

public class Authority implements Serializable{

    private Integer approvalSerialNo;//审批顺序号(通用)
    private Integer datumDataId;//流转id(通用)
    private String datumType;//流转类型(通用)
    private String datumTypeName;//审核类型名字
    private Integer totalNodeNumber;//总审核节点数
    private Integer workMonth;//年(月报审核使用)
    private Integer workYear;//月(月报审核使用)
    private double paySalary;//应发工资(月报审核使用)
    private double actualSalary;//实发工资(月报审核使用)
    private boolean isRegionalOffices;
    private Integer flowId;//平行审核流程ID

    public void setRegionalOffices(boolean regionalOffices) {
        isRegionalOffices = regionalOffices;
    }

    public void setIsRegionalOffices(){
        this.isRegionalOffices =  isRegionalOffices;
    }

    public boolean getIsRegionalOffices(){
        return isRegionalOffices;
    }

    public String getDatumTypeName() {
        return datumTypeName;
    }

    public void setDatumTypeName(String datumTypeName) {
        this.datumTypeName = datumTypeName;
    }

    public Integer getApprovalSerialNo() {
        return approvalSerialNo;
    }

    public void setApprovalSerialNo(Integer approvalSerialNo) {
        this.approvalSerialNo = approvalSerialNo;
    }

    public Integer getDatumDataId() {
        return datumDataId;
    }

    public void setDatumDataId(Integer datumDataId) {
        this.datumDataId = datumDataId;
    }

    public String getDatumType() {
        return datumType;
    }

    public void setDatumType(String datumType) {
        this.datumType = datumType;
    }

    public Integer getWorkMonth() {
        return workMonth;
    }

    public void setWorkMonth(Integer workMonth) {
        this.workMonth = workMonth;
    }

    public Integer getWorkYear() {
        return workYear;
    }

    public void setWorkYear(Integer workYear) {
        this.workYear = workYear;
    }

    public double getPaySalary() {
        return paySalary;
    }

    public void setPaySalary(double paySalary) {
        this.paySalary = paySalary;
    }

    public double getActualSalary() {
        return actualSalary;
    }

    public void setActualSalary(double actualSalary) {
        this.actualSalary = actualSalary;
    }

    public Integer getTotalNodeNumber() {
        return totalNodeNumber;
    }

    public void setTotalNodeNumber(Integer totalNodeNumber) {
        this.totalNodeNumber = totalNodeNumber;
    }

    public Integer getFlowId() {
        return flowId;
    }

    public void setFlowId(Integer flowId) {
        this.flowId = flowId;
    }

}
