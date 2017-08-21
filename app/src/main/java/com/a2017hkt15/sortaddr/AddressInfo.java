package com.a2017hkt15.sortaddr;

import java.io.Serializable;

/**
 * Created by 쿠키AND망고 on 2017-08-21.
 */
@SuppressWarnings("serial")
public class AddressInfo implements Serializable{
    private float lon;
    private float lat;
    private String addr;

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}