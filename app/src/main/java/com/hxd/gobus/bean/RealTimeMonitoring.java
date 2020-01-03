package com.hxd.gobus.bean;

import com.google.gson.Gson;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: wangqingbin
 * @date: 2019/9/23 16:56
 */

public class RealTimeMonitoring implements Serializable {


    /**
     * infoManageId : int //主键ID
     * vehicleName : string //车辆名称
     * vehicleMark : string //车牌号
     * aFewCar : string //几座车
     * natureVehicle : string //车辆性质
     * vehicleState : string //车辆状态
     * useCompany : string //使用公司
     * useUnit : string //使用部门
     * managementId : int //用车ID
     * useCarStatus : string //用车状态
     * useReason : string //用车事由（行程任务）
     * receiveCarPerson : int //领车人（使用人）
     * receiveCarPersonName : string
     * mobilePhone : string //联系方式
     * realTimeLocation : string //实时位置
     * battery : double //电量
     * vin : string //车架号
     * lng : double //经度
     * lat : double //纬度
     */

    private Integer infoManageId;
    private String vehicleName;
    private String vehicleMark;
    private String aFewCar;
    private String natureVehicle;
    private String vehicleState;
    private String useCompany;
    private String useUnit;
    private Integer managementId;
    private String useCarStatus;
    private String useReason;
    private Integer receiveCarPerson;
    private String receiveCarPersonName;
    private String mobilePhone;
    private String realTimeLocation;
    private BigDecimal battery;
    private String vin;
    private BigDecimal lng;
    private BigDecimal lat;

    public static RealTimeMonitoring objectFromData(String str) {

        return new Gson().fromJson(str, RealTimeMonitoring.class);
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

    public String getAFewCar() {
        return aFewCar;
    }

    public void setAFewCar(String aFewCar) {
        this.aFewCar = aFewCar;
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

    public Integer getManagementId() {
        return managementId;
    }

    public void setManagementId(Integer managementId) {
        this.managementId = managementId;
    }

    public String getUseCarStatus() {
        return useCarStatus;
    }

    public void setUseCarStatus(String useCarStatus) {
        this.useCarStatus = useCarStatus;
    }

    public String getUseReason() {
        return useReason;
    }

    public void setUseReason(String useReason) {
        this.useReason = useReason;
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

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getRealTimeLocation() {
        return realTimeLocation;
    }

    public void setRealTimeLocation(String realTimeLocation) {
        this.realTimeLocation = realTimeLocation;
    }

    public BigDecimal getBattery() {
        return battery;
    }

    public void setBattery(BigDecimal battery) {
        this.battery = battery;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
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
}
