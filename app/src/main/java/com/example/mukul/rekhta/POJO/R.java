package com.example.mukul.rekhta.POJO;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class R {

    @SerializedName("NF")
    @Expose
    private Boolean nF;
    @SerializedName("AS")
    @Expose
    private Object aS;
    @SerializedName("ATS")
    @Expose
    private Object aTS;
    @SerializedName("EC")
    @Expose
    private Boolean eC;
    @SerializedName("PC")
    @Expose
    private Boolean pC;
    @SerializedName("I")
    @Expose
    private String i;
    @SerializedName("SI")
    @Expose
    private Integer sI;
    @SerializedName("CS")
    @Expose
    private String cS;
    @SerializedName("CT")
    @Expose
    private String cT;
    @SerializedName("ST")
    @Expose
    private String sT;
    @SerializedName("FT")
    @Expose
    private String fT;
    @SerializedName("HN")
    @Expose
    private Boolean hN;
    @SerializedName("HH")
    @Expose
    private Boolean hH;
    @SerializedName("HU")
    @Expose
    private Boolean hU;
    @SerializedName("HT")
    @Expose
    private Boolean hT;
    @SerializedName("RF")
    @Expose
    private Integer rF;
    @SerializedName("RA")
    @Expose
    private Integer rA;
    @SerializedName("TS")
    @Expose
    private String tS;
    @SerializedName("CR")
    @Expose
    private String cR;
    @SerializedName("RFP")
    @Expose
    private String rFP;
    @SerializedName("CTS")
    @Expose
    private Object cTS;
    @SerializedName("TD")
    @Expose
    private Object tD;
    @SerializedName("TN")
    @Expose
    private Object tN;
    @SerializedName("PT")
    @Expose
    private Object pT;
    @SerializedName("PS")
    @Expose
    private Object pS;
    @SerializedName("PTS")
    @Expose
    private Object pTS;
    @SerializedName("Videos")
    @Expose
    private List<Object> videos = null;
    @SerializedName("Audios")
    @Expose
    private List<Object> audios = null;
    @SerializedName("FPMappings")
    @Expose
    private List<Object> fPMappings = null;
    @SerializedName("FPParaMappings")
    @Expose
    private List<Object> fPParaMappings = null;
    @SerializedName("Tags")
    @Expose
    private List<Object> tags = null;
    @SerializedName("Poet")
    @Expose
    private Poet poet;
    @SerializedName("ParaInfo")
    @Expose
    private List<Object> paraInfo = null;

    public Boolean getNF() {
        return nF;
    }

    public void setNF(Boolean nF) {
        this.nF = nF;
    }

    public Object getAS() {
        return aS;
    }

    public void setAS(Object aS) {
        this.aS = aS;
    }

    public Object getATS() {
        return aTS;
    }

    public void setATS(Object aTS) {
        this.aTS = aTS;
    }

    public Boolean getEC() {
        return eC;
    }

    public void setEC(Boolean eC) {
        this.eC = eC;
    }

    public Boolean getPC() {
        return pC;
    }

    public void setPC(Boolean pC) {
        this.pC = pC;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public Integer getSI() {
        return sI;
    }

    public void setSI(Integer sI) {
        this.sI = sI;
    }

    public String getCS() {
        return cS;
    }

    public void setCS(String cS) {
        this.cS = cS;
    }

    public String getCT() {
        return cT;
    }

    public void setCT(String cT) {
        this.cT = cT;
    }

    public String getST() {
        return sT;
    }

    public void setST(String sT) {
        this.sT = sT;
    }

    public String getFT() {
        return fT;
    }

    public void setFT(String fT) {
        this.fT = fT;
    }

    public Boolean getHN() {
        return hN;
    }

    public void setHN(Boolean hN) {
        this.hN = hN;
    }

    public Boolean getHH() {
        return hH;
    }

    public void setHH(Boolean hH) {
        this.hH = hH;
    }

    public Boolean getHU() {
        return hU;
    }

    public void setHU(Boolean hU) {
        this.hU = hU;
    }

    public Boolean getHT() {
        return hT;
    }

    public void setHT(Boolean hT) {
        this.hT = hT;
    }

    public Integer getRF() {
        return rF;
    }

    public void setRF(Integer rF) {
        this.rF = rF;
    }

    public Integer getRA() {
        return rA;
    }

    public void setRA(Integer rA) {
        this.rA = rA;
    }

    public String getTS() {
        return tS;
    }

    public void setTS(String tS) {
        this.tS = tS;
    }

    public String getCR() {
        return cR;
    }

    public void setCR(String cR) {
        this.cR = cR;
    }

    public String getRFP() {
        return rFP;
    }

    public void setRFP(String rFP) {
        this.rFP = rFP;
    }

    public Object getCTS() {
        return cTS;
    }

    public void setCTS(Object cTS) {
        this.cTS = cTS;
    }

    public Object getTD() {
        return tD;
    }

    public void setTD(Object tD) {
        this.tD = tD;
    }

    public Object getTN() {
        return tN;
    }

    public void setTN(Object tN) {
        this.tN = tN;
    }

    public Object getPT() {
        return pT;
    }

    public void setPT(Object pT) {
        this.pT = pT;
    }

    public Object getPS() {
        return pS;
    }

    public void setPS(Object pS) {
        this.pS = pS;
    }

    public Object getPTS() {
        return pTS;
    }

    public void setPTS(Object pTS) {
        this.pTS = pTS;
    }

    public List<Object> getVideos() {
        return videos;
    }

    public void setVideos(List<Object> videos) {
        this.videos = videos;
    }

    public List<Object> getAudios() {
        return audios;
    }

    public void setAudios(List<Object> audios) {
        this.audios = audios;
    }

    public List<Object> getFPMappings() {
        return fPMappings;
    }

    public void setFPMappings(List<Object> fPMappings) {
        this.fPMappings = fPMappings;
    }

    public List<Object> getFPParaMappings() {
        return fPParaMappings;
    }

    public void setFPParaMappings(List<Object> fPParaMappings) {
        this.fPParaMappings = fPParaMappings;
    }

    public List<Object> getTags() {
        return tags;
    }

    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    public Poet getPoet() {
        return poet;
    }

    public void setPoet(Poet poet) {
        this.poet = poet;
    }

    public List<Object> getParaInfo() {
        return paraInfo;
    }

    public void setParaInfo(List<Object> paraInfo) {
        this.paraInfo = paraInfo;
    }

}
