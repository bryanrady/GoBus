package com.hxd.gobus.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author: wangqingbin
 * @date: 2019/7/29 11:38
 */
@Entity
public class Contact implements Serializable{

    private static final long serialVersionUID = -2573092534689485602L;

    @Id(autoincrement = true)
    private Long id;
    private Integer personId;
    private String name;
    private String unitName;
    private String technicalPost;
    private String workNo;
    private String mobilePhone;
    private String email;
    private String firstLetter;
    private String photoUrl;
    private String namePinyin;
    private String deptPinyin;
    private String cardNo;
    private Integer unitId;
    @Generated(hash = 1441963471)
    public Contact(Long id, Integer personId, String name, String unitName,
            String technicalPost, String workNo, String mobilePhone, String email,
            String firstLetter, String photoUrl, String namePinyin,
            String deptPinyin, String cardNo, Integer unitId) {
        this.id = id;
        this.personId = personId;
        this.name = name;
        this.unitName = unitName;
        this.technicalPost = technicalPost;
        this.workNo = workNo;
        this.mobilePhone = mobilePhone;
        this.email = email;
        this.firstLetter = firstLetter;
        this.photoUrl = photoUrl;
        this.namePinyin = namePinyin;
        this.deptPinyin = deptPinyin;
        this.cardNo = cardNo;
        this.unitId = unitId;
    }
    @Generated(hash = 672515148)
    public Contact() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Integer getPersonId() {
        return this.personId;
    }
    public void setPersonId(Integer personId) {
        this.personId = personId;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUnitName() {
        return this.unitName;
    }
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
    public String getTechnicalPost() {
        return this.technicalPost;
    }
    public void setTechnicalPost(String technicalPost) {
        this.technicalPost = technicalPost;
    }
    public String getWorkNo() {
        return this.workNo;
    }
    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }
    public String getMobilePhone() {
        return this.mobilePhone;
    }
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getFirstLetter() {
        return this.firstLetter;
    }
    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }
    public String getPhotoUrl() {
        return this.photoUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    public String getNamePinyin() {
        return this.namePinyin;
    }
    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }
    public String getDeptPinyin() {
        return this.deptPinyin;
    }
    public void setDeptPinyin(String deptPinyin) {
        this.deptPinyin = deptPinyin;
    }
    public String getCardNo() {
        return this.cardNo;
    }
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
    public Integer getUnitId() {
        return this.unitId;
    }
    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", personId=" + personId +
                ", name='" + name + '\'' +
                ", unitName='" + unitName + '\'' +
                ", technicalPost='" + technicalPost + '\'' +
                ", workNo='" + workNo + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", email='" + email + '\'' +
                ", firstLetter='" + firstLetter + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", namePinyin='" + namePinyin + '\'' +
                ", deptPinyin='" + deptPinyin + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", unitId=" + unitId +
                '}';
    }
}
