package com.hxd.gobus.bean;

import java.io.Serializable;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * @author: wangqingbin
 * @date: 2019/7/16 10:07
 */

public class User implements Serializable {

    private String dataStatus;
    private String entryDate;
    private Integer gradesId;
    private String isPrincipal;
    private String lockedFlag;
    private String loginName;
    private String loginPassword;
    private String mobilePhone;//员工电话
    private String name;//员工名称
    private Integer personId;//员工Id
    private String personStatus;//员工状态
    private String personType;//员工状态
    private String sex;//员工性别
    private String signUrl;//签字盖章
    private Integer unitCode;//部门编码
    private Integer unitId;//部门Id
    private String unitName;//部门名称
    private String unitType;//部门类型
    private String usedFlag;
    private String photoUrl;//头像名字
    private Integer userId;//用户Id
    private String userName;//用户名称
    private String userStatus;//用户状态
    private String userType;//用户类型
    private String workNo;//员工工号
    private String technicalPost;//员工职位
    private String email;//员工邮箱
    private static User userInfo = null;

    /**
     * 获取单例
     * @return
     */
    public static User getInstance(){
        return userInfo;
    }
    /**
     * 保存单例
     */
    public static void setInstance(User info){
        userInfo = info;
    }

    /**
     * 销毁单例(退出登录)
     */
    public static void destroyInstance(){
        userInfo = null;
    }

    public User() {

    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public Integer getGradesId() {
        return gradesId;
    }

    public void setGradesId(Integer gradesId) {
        this.gradesId = gradesId;
    }

    public String getIsPrincipal() {
        return isPrincipal;
    }

    public void setIsPrincipal(String isPrincipal) {
        this.isPrincipal = isPrincipal;
    }

    public String getLockedFlag() {
        return lockedFlag;
    }

    public void setLockedFlag(String lockedFlag) {
        this.lockedFlag = lockedFlag;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getPersonStatus() {
        return personStatus;
    }

    public void setPersonStatus(String personStatus) {
        this.personStatus = personStatus;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSignUrl() {
        return signUrl;
    }

    public void setSignUrl(String signUrl) {
        this.signUrl = signUrl;
    }

    public Integer getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(Integer unitCode) {
        this.unitCode = unitCode;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getUsedFlag() {
        return usedFlag;
    }

    public void setUsedFlag(String usedFlag) {
        this.usedFlag = usedFlag;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    public String getTechnicalPost() {
        return technicalPost;
    }

    public void setTechnicalPost(String technicalPost) {
        this.technicalPost = technicalPost;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "dataStatus='" + dataStatus + '\'' +
                ", entryDate='" + entryDate + '\'' +
                ", gradesId=" + gradesId +
                ", isPrincipal='" + isPrincipal + '\'' +
                ", lockedFlag='" + lockedFlag + '\'' +
                ", loginName='" + loginName + '\'' +
                ", loginPassword='" + loginPassword + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", name='" + name + '\'' +
                ", personId=" + personId +
                ", personStatus='" + personStatus + '\'' +
                ", personType='" + personType + '\'' +
                ", sex='" + sex + '\'' +
                ", signUrl='" + signUrl + '\'' +
                ", unitCode=" + unitCode +
                ", unitId=" + unitId +
                ", unitName='" + unitName + '\'' +
                ", unitType='" + unitType + '\'' +
                ", usedFlag='" + usedFlag + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userStatus='" + userStatus + '\'' +
                ", userType='" + userType + '\'' +
                ", workNo='" + workNo + '\'' +
                ", technicalPost='" + technicalPost + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
