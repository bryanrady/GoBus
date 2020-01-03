package com.hxd.gobus.bean;

/**
 * Created by Administrator on 2019/6/19.
 */

public class ApkInfo {

    /**
     * downloadUrl : http://222.85.144.82:2211/ispApi/appUpdate/OA.apk
     * versionCode : 1
     * versionContent : 更新内容：1.新加会议管理功能.2.合同签署、信息评审审核修改.
     * versionName : V1.1
     */

    private String downloadUrl;
    private String versionCode;
    private String versionContent;
    private String versionName;

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionContent() {
        return versionContent;
    }

    public void setVersionContent(String versionContent) {
        this.versionContent = versionContent;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

}
