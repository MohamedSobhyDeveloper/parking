package com.park.optech.parking.printticket.sqlite;

public class Members_Table
{
    public static final String TABLE_NAME = "members";

    //pk , name ,start_date,end_date,membership_no,ssn,company,img_path

    public static final String MEMBER_ID = "member_id";
    public static final String MEMBER_PK = "pk";
    public static final String MEMBER_NAME = "member_name";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String MEMBERSHIP_NO = "membership_no";
    public static final String SSN = "ssn";
    public static final String COMPANY = "company";
    public static final String IMAGE = "img_path";


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + MEMBER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + MEMBER_PK + " TEXT,"
                    + MEMBER_NAME + " TEXT,"
                    + START_DATE + " TEXT,"
                    + END_DATE + " TEXT,"
                    + MEMBERSHIP_NO + " TEXT,"
                    + SSN + " TEXT,"
                    +COMPANY + " TEXT,"
                    +IMAGE + " TEXT"
                    + ")";

    private int user_id;
    private String member_pk;
    private String member_name;
    private String start_date;
    private String end_date;
    private String membership_no;
    private String ssn;
    private String company;
    private String img_path;

    public Members_Table() {
    }

    public Members_Table(int user_id, String member_name, String start_date, String end_date
            , String membership_no, String ssn, String company, String img_path) {
        this.user_id = user_id;
        this.member_name = member_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.membership_no = membership_no;
        this.ssn = ssn;
        this.company = company;
        this.img_path = img_path;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getMember_pk() {
        return member_pk;
    }

    public void setMember_pk(String member_pk) {
        this.member_pk = member_pk;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
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

    public String getMembership_no() {
        return membership_no;
    }

    public void setMembership_no(String membership_no) {
        this.membership_no = membership_no;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
}
