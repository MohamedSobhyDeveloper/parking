package com.park.optech.parking.printticket.sqlite;

public class Tickets_Table
{
    public static final String TABLE_NAME = "tickets";

    //pk , cameraNo,Timestamp,PayTime,PayAmount,PayUser,company,paid,trx_no,members,sync

    public static final String TICKET_ID = "pk";
    public static final String CAMERA_NO = "cameraNo";
    public static final String TIMESTAMP = "Timestamp";
    public static final String PAY_TIME = "PayTime";
    public static final String PAY_AMOUNT = "PayAmount";
    public static final String PAY_USER = "PayUser";
    public static final String COMPANY = "company";
    public static final String PAID = "paid";
    public static final String TRX_NO = "trx_no";
    public static final String MEMBERS = "members";
    public static final String Tag_id = "tag_id";
    public static final String SYNC = "sync";
    public static final String exit = "exit";



    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + TICKET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + CAMERA_NO + " TEXT,"
                    + TIMESTAMP + " DATETIME DEFAULT (datetime('now','localtime')),"
                    + PAY_TIME + " DATETIME DEFAULT (datetime('now','localtime')),"
                    + PAY_AMOUNT + " TEXT,"
                    + PAY_USER + " TEXT,"
                    + COMPANY + " TEXT,"
                    + PAID + " TEXT,"
                    + TRX_NO + " TEXT,"
                    + MEMBERS + " TEXT,"
                    + Tag_id + " TEXT,"
                    + SYNC + " TEXT,"
                    + exit + " TEXT"
                    + ")";

    private int ticket_id;
    private String cameraNo;
    private String Timestamp;
    private String PayTime;
    private String PayAmount;
    private String PayUser;
    private String company;
    private String paid;
    private String trx_no;
    private String members;
    private String sync;
    private String exitv;


    public Tickets_Table() {
    }


    public Tickets_Table(int ticket_id, String cameraNo, String timestamp, String payTime, String payAmount
            , String payUser, String company, String paid, String trx_no, String members, String sync,String exitv) {
        this.ticket_id = ticket_id;
        this.cameraNo = cameraNo;
        Timestamp = timestamp;
        PayTime = payTime;
        PayAmount = payAmount;
        PayUser = payUser;
        this.company = company;
        this.paid = paid;
        this.trx_no = trx_no;
        this.members = members;
        this.sync = sync;
        this.exitv=exitv;
    }

    public int getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(int ticket_id) {
        this.ticket_id = ticket_id;
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
        Timestamp = timestamp;
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

    public String getExitv() {
        return exitv;
    }

    public void setExitv(String exitv) {
        this.exitv = exitv;
    }
}
