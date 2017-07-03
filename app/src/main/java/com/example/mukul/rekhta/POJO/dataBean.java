package com.example.mukul.rekhta.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class dataBean {

    @SerializedName("S")
    @Expose
    private Integer s;
    @SerializedName("Me")
    @Expose
    private Object me;
    @SerializedName("Mh")
    @Expose
    private Object mh;
    @SerializedName("Mu")
    @Expose
    private Object mu;
    @SerializedName("R")
    @Expose
    private R r;
    @SerializedName("T")
    @Expose
    private String t;

    public Integer getS() {
        return s;
    }

    public void setS(Integer s) {
        this.s = s;
    }

    public Object getMe() {
        return me;
    }

    public void setMe(Object me) {
        this.me = me;
    }

    public Object getMh() {
        return mh;
    }

    public void setMh(Object mh) {
        this.mh = mh;
    }

    public Object getMu() {
        return mu;
    }

    public void setMu(Object mu) {
        this.mu = mu;
    }

    public R getR() {
        return r;
    }

    public void setR(R r) {
        this.r = r;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

}
