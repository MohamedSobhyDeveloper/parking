package com.park.optech.parking.printticket.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database_Helper extends SQLiteOpenHelper
{

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "tickets_db";


    public Database_Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Tickets_Table.CREATE_TABLE);
        db.execSQL(Members_Table.CREATE_TABLE);
        db.execSQL(Users_Table.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + Tickets_Table.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Members_Table.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Users_Table.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertMember(String member_name, String start_date, String end_date
            , String membership_no, String ssn, String company, String img_path) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Members_Table.MEMBER_NAME,member_name);
        values.put(Members_Table.START_DATE,start_date);
        values.put(Members_Table.END_DATE,end_date);
        values.put(Members_Table.MEMBERSHIP_NO,membership_no);
        values.put(Members_Table.SSN,ssn);
        values.put(Members_Table.COMPANY,company);
        values.put(Members_Table.IMAGE,img_path);


        // insert row
        long id = db.insert(Members_Table.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }


    public long insertTicket(String cameraNo, String timestamp, String payTime, String payAmount
            , String payUser, String company, String paid, String trx_no, String members, String sync) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Tickets_Table.CAMERA_NO,cameraNo);
        values.put(Tickets_Table.TIMESTAMP,timestamp);
        values.put(Tickets_Table.PAY_TIME,payTime);
        values.put(Tickets_Table.PAY_AMOUNT,payAmount);
        values.put(Tickets_Table.PAY_USER,payUser);
        values.put(Tickets_Table.COMPANY,company);
        values.put(Tickets_Table.PAID,paid);
        values.put(Tickets_Table.TRX_NO,trx_no);
        values.put(Tickets_Table.MEMBERS,members);
        values.put(Tickets_Table.SYNC,sync);

        // insert row
        long id = db.insert(Tickets_Table.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public long insertUser(String user_name, String email, String password) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Users_Table.USER_NAME,user_name);
        values.put(Users_Table.EMAIL,email);
        values.put(Users_Table.PASSWORD,password);


        // insert row
        long id = db.insert(Users_Table.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public List<Tickets_Table> getTickets()
    {
        List<Tickets_Table> data = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Tickets_Table.TABLE_NAME + " ORDER BY " +
                Tickets_Table.TICKET_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Tickets_Table tickets_table = new Tickets_Table();
//                data_client.setId(cursor.getInt(cursor.getColumnIndex(Data_Client.ID)));
                tickets_table.setCameraNo(cursor.getString(cursor.getColumnIndex(Tickets_Table.CAMERA_NO)));
                tickets_table.setTimestamp(cursor.getString(cursor.getColumnIndex(Tickets_Table.TIMESTAMP)));
                tickets_table.setPayTime(cursor.getString(cursor.getColumnIndex(Tickets_Table.PAY_TIME)));
                tickets_table.setPayAmount(cursor.getString(cursor.getColumnIndex(Tickets_Table.PAY_AMOUNT)));
                tickets_table.setPayUser(cursor.getString(cursor.getColumnIndex(Tickets_Table.PAY_USER)));
                tickets_table.setCompany(cursor.getString(cursor.getColumnIndex(Tickets_Table.COMPANY)));
                tickets_table.setPaid(cursor.getString(cursor.getColumnIndex(Tickets_Table.PAID)));
                tickets_table.setTrx_no(cursor.getString(cursor.getColumnIndex(Tickets_Table.TRX_NO)));
                tickets_table.setMembers(cursor.getString(cursor.getColumnIndex(Tickets_Table.MEMBERS)));
                tickets_table.setSync(cursor.getString(cursor.getColumnIndex(Tickets_Table.SYNC)));


                data.add(tickets_table);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        return data;
    }

    public List<Users_Table> getUsers()
    {
        List<Users_Table> users = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Tickets_Table.TABLE_NAME + " ORDER BY " +
                Tickets_Table.TICKET_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Users_Table users_table = new Users_Table();
//                data_client.setId(cursor.getInt(cursor.getColumnIndex(Data_Client.ID)));
                users_table.setUser_name(cursor.getString(cursor.getColumnIndex(Users_Table.USER_NAME)));
                users_table.setEmail(cursor.getString(cursor.getColumnIndex(Users_Table.EMAIL)));
                users_table.setPassword(cursor.getString(cursor.getColumnIndex(Users_Table.PASSWORD)));

                users.add(users_table);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        return users;
    }

}
