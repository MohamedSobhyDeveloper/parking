package com.park.optech.parking.printticket.models;

public class TicketsModel {
    private String pk;
    private String cameraNo;
    private String Timestamp;
    private String PayTime;
    private String PayAmount;
    private String PayUser;
    private String company;
    private String paid;
    private String trx_no;
    private String members;
    private String tagId;
    private String sync;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getCameraNo() {
        return cameraNo;
    }

    public void setCameraNo(String cameraNo) {
        this.cameraNo = cameraNo;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.Timestamp = timestamp;

    }

    public String getPayTime() {
        return PayTime;
    }

    public void setPayTime(String payTime) {
        PayTime = payTime;
    }

    public String getPayAmount() {
        return PayAmount;
    }

    public void setPayAmount(String payAmount) {
        PayAmount = payAmount;
    }

    public String getPayUser() {
        return PayUser;
    }

    public void setPayUser(String payUser) {
        PayUser = payUser;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getTrx_no() {
        return trx_no;
    }

    public void setTrx_no(String trx_no) {
        this.trx_no = trx_no;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
    }
}
