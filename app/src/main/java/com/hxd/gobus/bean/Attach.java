package com.hxd.gobus.bean;

import com.google.gson.Gson;

/**
 * @author: wangqingbin
 * @date: 2019/9/27 10:59
 */

public class Attach {

    /**
     * attachId : int //附件ID
     * dataId : string //数据ID
     * serialNo : int //顺序号
     * attachName : string //附件名称
     * attachType : string //附件类型：0：学历；1：职称；2：执业资格；3：荣誉；4：劳动合同
     * attachAddress : string //附件地址
     * appendixThumbnailAddress : string //附件缩略图地址
     * status : string //状态：0:有效；1:无效
     * remarks : string //备注
     * createBy : string //创建人
     * createDate : date //创建时间
     * updateBy : string //修改人
     * updateDate : date //修改时间
     */

    private Integer attachId;
    private String dataId;
    private String serialNo;
    private String attachName;
    private String attachType;
    private String attachAddress;
    private String appendixThumbnailAddress;
    private String status;
    private String remarks;
    private String createBy;
    private String createDate;
    private String updateBy;
    private String updateDate;

    private String nativePath;

    public static Attach objectFromData(String str) {

        return new Gson().fromJson(str, Attach.class);
    }

    public void setNativePath(String nativePath) {
        this.nativePath = nativePath;
    }

    public String getNativePath() {
        return nativePath;
    }

    public Integer getAttachId() {
        return attachId;
    }

    public void setAttachId(Integer attachId) {
        this.attachId = attachId;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public String getAttachType() {
        return attachType;
    }

    public void setAttachType(String attachType) {
        this.attachType = attachType;
    }

    public String getAttachAddress() {
        return attachAddress;
    }

    public void setAttachAddress(String attachAddress) {
        this.attachAddress = attachAddress;
    }

    public String getAppendixThumbnailAddress() {
        return appendixThumbnailAddress;
    }

    public void setAppendixThumbnailAddress(String appendixThumbnailAddress) {
        this.appendixThumbnailAddress = appendixThumbnailAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    @Override
    public String toString() {
        return "Attach{" +
                "attachId=" + attachId +
                ", dataId='" + dataId + '\'' +
                ", serialNo='" + serialNo + '\'' +
                ", attachName='" + attachName + '\'' +
                ", attachType='" + attachType + '\'' +
                ", attachAddress='" + attachAddress + '\'' +
                ", appendixThumbnailAddress='" + appendixThumbnailAddress + '\'' +
                ", status='" + status + '\'' +
                ", remarks='" + remarks + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createDate='" + createDate + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", updateDate='" + updateDate + '\'' +
                '}';
    }
}
