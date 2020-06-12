package com.park.optech.parking.printticket.models;

public class MembersModel
{

    /**
     * pk : 20
     * name : أحمد صوار
     * membership_no : 201845454
     * start_date : 2019-11-19 00:00:00
     * end_date : 2020-11-21 00:00:00
     * img_path :
     */

    private String pk;
    private String name;
    private String membership_no;
    private String start_date;
    private String end_date;
    private String img_path;
    private String ssn;

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMembership_no() {
        return membership_no;
    }

    public void setMembership_no(String membership_no) {
        this.membership_no = membership_no;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
}
