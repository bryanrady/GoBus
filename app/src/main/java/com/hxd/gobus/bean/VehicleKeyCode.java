package com.hxd.gobus.bean;

import java.util.List;

/**
 * @author: wangqingbin
 * @date: 2019/9/19 14:12
 */

public class VehicleKeyCode {

    private List<KeyCode> KEY_TRANSPORT_PREDICT_DURATION;
    private List<KeyCode> KEY_TRANSPORT_USE_CAR_TYPE;
    private List<KeyCode> KEY_TRANSPORT_USE_CAR_RANGE;
    private List<VehicleLicense> infoManages;
    private List<Driver> drives;

    public List<KeyCode> getKEY_TRANSPORT_PREDICT_DURATION() {
        return KEY_TRANSPORT_PREDICT_DURATION;
    }

    public void setKEY_TRANSPORT_PREDICT_DURATION(List<KeyCode> KEY_TRANSPORT_PREDICT_DURATION) {
        this.KEY_TRANSPORT_PREDICT_DURATION = KEY_TRANSPORT_PREDICT_DURATION;
    }

    public List<KeyCode> getKEY_TRANSPORT_USE_CAR_TYPE() {
        return KEY_TRANSPORT_USE_CAR_TYPE;
    }

    public void setKEY_TRANSPORT_USE_CAR_TYPE(List<KeyCode> KEY_TRANSPORT_USE_CAR_TYPE) {
        this.KEY_TRANSPORT_USE_CAR_TYPE = KEY_TRANSPORT_USE_CAR_TYPE;
    }

    public List<KeyCode> getKEY_TRANSPORT_USE_CAR_RANGE() {
        return KEY_TRANSPORT_USE_CAR_RANGE;
    }

    public void setKEY_TRANSPORT_USE_CAR_RANGE(List<KeyCode> KEY_TRANSPORT_USE_CAR_RANGE) {
        this.KEY_TRANSPORT_USE_CAR_RANGE = KEY_TRANSPORT_USE_CAR_RANGE;
    }

    public List<VehicleLicense> getInfoManages() {
        return infoManages;
    }

    public void setInfoManages(List<VehicleLicense> infoManages) {
        this.infoManages = infoManages;
    }

    public List<Driver> getDrives() {
        return drives;
    }

    public void setDrives(List<Driver> drives) {
        this.drives = drives;
    }
}
