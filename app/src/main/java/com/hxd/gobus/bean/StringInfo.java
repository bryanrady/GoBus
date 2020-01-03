package com.hxd.gobus.bean;

import java.math.BigDecimal;

/**
 * Created by wangqingbin on 2018/3/19.
 */

public class StringInfo {

    private Integer id;
    private String codeType;
    private Integer position;
    private String title;
    private String remark;
    private String extend;

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    private BigDecimal planUpkeepCost;

    public void setPlanUpkeepCost(BigDecimal planUpkeepCost) {
        this.planUpkeepCost = planUpkeepCost;
    }

    public BigDecimal getPlanUpkeepCost() {
        return planUpkeepCost;
    }
}
