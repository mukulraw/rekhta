package com.example.mukul.rekhta.POJO;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Poet {

    @SerializedName("CS")
    @Expose
    private String cS;
    @SerializedName("DS")
    @Expose
    private Object dS;
    @SerializedName("PN")
    @Expose
    private String pN;
    @SerializedName("IU")
    @Expose
    private String iU;
    @SerializedName("LI")
    @Expose
    private Boolean lI;

    public String getCS() {
        return cS;
    }

    public void setCS(String cS) {
        this.cS = cS;
    }

    public Object getDS() {
        return dS;
    }

    public void setDS(Object dS) {
        this.dS = dS;
    }

    public String getPN() {
        return pN;
    }

    public void setPN(String pN) {
        this.pN = pN;
    }

    public String getIU() {
        return iU;
    }

    public void setIU(String iU) {
        this.iU = iU;
    }

    public Boolean getLI() {
        return lI;
    }

    public void setLI(Boolean lI) {
        this.lI = lI;
    }

}
