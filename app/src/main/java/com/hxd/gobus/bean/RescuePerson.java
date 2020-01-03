package com.hxd.gobus.bean;

import com.google.gson.Gson;

/**
 * @author: wangqingbin
 * @date: 2019/12/4 14:45
 */
public class RescuePerson {

    /**
     * birthday : 1990-09-20 00:00:00
     * bonusCardNo : 6228481198276618973
     * cardNo : 520202198909207233
     * companyWorkAge : 5
     * dataStatus : 0
     * degreeId : 5
     * email : 627600815@qq.com
     * entryDate : 2014-07-09 00:00:00
     * fastChannleMenu : 50913,51118,10100
     * gradesId : 28
     * graduateSchool : 贵州大学
     * homeAddress : 贵阳市白云区铝兴社区
     * isPrincipal : 1
     * loanUnitId : 1803
     * marryStatus : 0
     * name : 徐飞
     * nameShort : xf
     * nation : 汉
     * officeAddress : 白云区沙文科技园
     * originPlace : 贵州
     * personId : 31841
     * personStatus : 0
     * personType : 0
     * photoUrl : 1477627340974.jpg
     * politicalStatus : 0
     * postId : 261
     * qqNo : 627600815
     * salaryCardNo : 6228481198276618973
     * sex : 0
     * technicalPostDate : 2012-07-01 00:00:00
     * unitId : 1803
     * updateBy : 34355
     * updateDate : 2019-02-22 16:10:32
     * valueCoefficient : 0
     * workNo : 001990
     */

    private String birthday;
    private String bonusCardNo;
    private String cardNo;
    private int companyWorkAge;
    private String dataStatus;
    private int degreeId;
    private String email;
    private String entryDate;
    private String fastChannleMenu;
    private String mobilePhone;
    private int gradesId;
    private String graduateSchool;
    private String homeAddress;
    private String isPrincipal;
    private int loanUnitId;
    private String marryStatus;
    private String name;
    private String nameShort;
    private String nation;
    private String officeAddress;
    private String originPlace;
    private int personId;
    private String personStatus;
    private String personType;
    private String photoUrl;
    private String politicalStatus;
    private int postId;
    private String qqNo;
    private String salaryCardNo;
    private String sex;
    private String technicalPostDate;
    private int unitId;
    private String updateBy;
    private String updateDate;
    private int valueCoefficient;
    private String workNo;

    public static RescuePerson objectFromData(String str) {

        return new Gson().fromJson(str, RescuePerson.class);
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBonusCardNo() {
        return bonusCardNo;
    }

    public void setBonusCardNo(String bonusCardNo) {
        this.bonusCardNo = bonusCardNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getCompanyWorkAge() {
        return companyWorkAge;
    }

    public void setCompanyWorkAge(int companyWorkAge) {
        this.companyWorkAge = companyWorkAge;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public int getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(int degreeId) {
        this.degreeId = degreeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getFastChannleMenu() {
        return fastChannleMenu;
    }

    public void setFastChannleMenu(String fastChannleMenu) {
        this.fastChannleMenu = fastChannleMenu;
    }

    public int getGradesId() {
        return gradesId;
    }

    public void setGradesId(int gradesId) {
        this.gradesId = gradesId;
    }

    public String getGraduateSchool() {
        return graduateSchool;
    }

    public void setGraduateSchool(String graduateSchool) {
        this.graduateSchool = graduateSchool;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getIsPrincipal() {
        return isPrincipal;
    }

    public void setIsPrincipal(String isPrincipal) {
        this.isPrincipal = isPrincipal;
    }

    public int getLoanUnitId() {
        return loanUnitId;
    }

    public void setLoanUnitId(int loanUnitId) {
        this.loanUnitId = loanUnitId;
    }

    public String getMarryStatus() {
        return marryStatus;
    }

    public void setMarryStatus(String marryStatus) {
        this.marryStatus = marryStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getOriginPlace() {
        return originPlace;
    }

    public void setOriginPlace(String originPlace) {
        this.originPlace = originPlace;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPoliticalStatus() {
        return politicalStatus;
    }

    public void setPoliticalStatus(String politicalStatus) {
        this.politicalStatus = politicalStatus;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getQqNo() {
        return qqNo;
    }

    public void setQqNo(String qqNo) {
        this.qqNo = qqNo;
    }

    public String getSalaryCardNo() {
        return salaryCardNo;
    }

    public void setSalaryCardNo(String salaryCardNo) {
        this.salaryCardNo = salaryCardNo;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTechnicalPostDate() {
        return technicalPostDate;
    }

    public void setTechnicalPostDate(String technicalPostDate) {
        this.technicalPostDate = technicalPostDate;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
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

    public int getValueCoefficient() {
        return valueCoefficient;
    }

    public void setValueCoefficient(int valueCoefficient) {
        this.valueCoefficient = valueCoefficient;
    }

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }
}
