package com.hxd.gobus.bean;

import com.google.gson.Gson;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: wangqingbin
 * @date: 2019/9/23 16:27
 */

public class RealTimeData implements Serializable {


    /**
     * infoManageId : int //主键ID
     * vehicleName : string //车辆名称
     * vehicleMark : string //车牌号
     * licensePlateType : string //车牌种类
     * engineNumber : string //发动机号
     * vin : string //车架号
     * aFewCar : string //几座车
     * vehiclePhoto : string //车辆照片
     * natureVehicle : string //车辆性质
     * vehicleState : string //车辆状态
     * limitNumberType : string //限号类型
     * useCompany : string //使用公司
     * useUnit : string //使用部门
     * rentWay : string //出租方式
     * leaseExpiryDate : date //租赁到期日
     * expiryDate : date //保险到期日
     * maintenanceDate : date //保养到期日
     * renter : string //租赁方联系人
     * renterPhone : string //租赁方联系电话
     * lesseeAdministrator : string //租赁方管理员
     * lesseeAdministratorPhone : string //租赁方管理员联系电话
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
     * fenceIds : string //围栏信息ID
     * fenceNames : string //围栏名称集合
     * soc : double //剩余电量
     * xhMileage : int //续航里程单位km（不区分大小）
     * mileage : double //总里程单位km
     * chargeStatus : int //充电状态(0:默认值 1:正在充电 2:充电结束 3:正在放电  其他值:预留,未定义)
     * lng : double //经度
     * lat : double //纬度
     * formattedAddress : string //大概位置
     */

    private Integer infoManageId;
    private String vehicleName;
    private String vehicleMark;
    private String licensePlateType;
    private String engineNumber;
    private String vin;
    private String aFewCar;
    private String vehiclePhoto;
    private String natureVehicle;
    private String vehicleState;
    private String limitNumberType;
    private String useCompany;
    private String useUnit;
    private String rentWay;
    private String leaseExpiryDate;
    private String expiryDate;
    private String maintenanceDate;
    private String renter;
    private String renterPhone;
    private String lesseeAdministrator;
    private String lesseeAdministratorPhone;
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
    private String fenceIds;
    private String fenceNames;
    private BigDecimal soc;
    private Integer xhMileage;
    private BigDecimal mileage;
    private Integer chargeStatus;
    private String chargeStatusName;
    private BigDecimal lng;
    private BigDecimal lat;
    private String formattedAddress;
    private String receiveCarPerson;
    private String receiveCarPersonName;

    public static RealTimeData objectFromData(String str) {

        return new Gson().fromJson(str, RealTimeData.class);
    }

    public void setChargeStatusName(String chargeStatusName) {
        this.chargeStatusName = chargeStatusName;
    }

    public String getChargeStatusName() {
        return chargeStatusName;
    }

    public void setaFewCar(String aFewCar) {
        this.aFewCar = aFewCar;
    }

    public String getaFewCar() {
        return aFewCar;
    }

    public void setReceiveCarPerson(String receiveCarPerson) {
        this.receiveCarPerson = receiveCarPerson;
    }

    public String getReceiveCarPerson() {
        return receiveCarPerson;
    }

    public void setReceiveCarPersonName(String receiveCarPersonName) {
        this.receiveCarPersonName = receiveCarPersonName;
    }

    public String getReceiveCarPersonName() {
        return receiveCarPersonName;
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

    public String getLicensePlateType() {
        return licensePlateType;
    }

    public void setLicensePlateType(String licensePlateType) {
        this.licensePlateType = licensePlateType;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getAFewCar() {
        return aFewCar;
    }

    public void setAFewCar(String aFewCar) {
        this.aFewCar = aFewCar;
    }

    public String getVehiclePhoto() {
        return vehiclePhoto;
    }

    public void setVehiclePhoto(String vehiclePhoto) {
        this.vehiclePhoto = vehiclePhoto;
    }

    public String getNatureVehicle() {
        return natureVehicle;
    }

    public void setNatureVehicle(String natureVehicle) {
        this.natureVehicle = natureVehicle;
    }

    public String getVehicleState() {
        return vehicleState;
    }

    public void setVehicleState(String vehicleState) {
        this.vehicleState = vehicleState;
    }

    public String getLimitNumberType() {
        return limitNumberType;
    }

    public void setLimitNumberType(String limitNumberType) {
        this.limitNumberType = limitNumberType;
    }

    public String getUseCompany() {
        return useCompany;
    }

    public void setUseCompany(String useCompany) {
        this.useCompany = useCompany;
    }

    public String getUseUnit() {
        return useUnit;
    }

    public void setUseUnit(String useUnit) {
        this.useUnit = useUnit;
    }

    public String getRentWay() {
        return rentWay;
    }

    public void setRentWay(String rentWay) {
        this.rentWay = rentWay;
    }

    public String getLeaseExpiryDate() {
        return leaseExpiryDate;
    }

    public void setLeaseExpiryDate(String leaseExpiryDate) {
        this.leaseExpiryDate = leaseExpiryDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(String maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public String getRenter() {
        return renter;
    }

    public void setRenter(String renter) {
        this.renter = renter;
    }

    public String getRenterPhone() {
        return renterPhone;
    }

    public void setRenterPhone(String renterPhone) {
        this.renterPhone = renterPhone;
    }

    public String getLesseeAdministrator() {
        return lesseeAdministrator;
    }

    public void setLesseeAdministrator(String lesseeAdministrator) {
        this.lesseeAdministrator = lesseeAdministrator;
    }

    public String getLesseeAdministratorPhone() {
        return lesseeAdministratorPhone;
    }

    public void setLesseeAdministratorPhone(String lesseeAdministratorPhone) {
        this.lesseeAdministratorPhone = lesseeAdministratorPhone;
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

    public String getFenceIds() {
        return fenceIds;
    }

    public void setFenceIds(String fenceIds) {
        this.fenceIds = fenceIds;
    }

    public String getFenceNames() {
        return fenceNames;
    }

    public void setFenceNames(String fenceNames) {
        this.fenceNames = fenceNames;
    }

    public BigDecimal getSoc() {
        return soc;
    }

    public void setSoc(BigDecimal soc) {
        this.soc = soc;
    }

    public Integer getXhMileage() {
        return xhMileage;
    }

    public void setXhMileage(Integer xhMileage) {
        this.xhMileage = xhMileage;
    }

    public BigDecimal getMileage() {
        return mileage;
    }

    public void setMileage(BigDecimal mileage) {
        this.mileage = mileage;
    }

    public Integer getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(Integer chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }
}
