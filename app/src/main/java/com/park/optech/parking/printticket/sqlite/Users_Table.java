package com.park.optech.parking.printticket.sqlite;

public class Users_Table
{
    public static final String TABLE_NAME = "users";

    public static final String USER_ID = "user_id";
    public static final String USER_PK = "pk";
    public static final String USER_NAME = "user_name";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    private int user_id;
    private String pk;
    private String user_name;
    private String email;
    private String password;

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public Users_Table(int user_id, String user_name, String email, String password) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.email = email;
        this.password = password;
    }

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + USER_PK + " TEXT,"
                    + USER_NAME + " TEXT,"
                    + EMAIL + " TEXT,"
                    + PASSWORD + " TEXT"
                    + ")";

    public Users_Table() {
    }



}
