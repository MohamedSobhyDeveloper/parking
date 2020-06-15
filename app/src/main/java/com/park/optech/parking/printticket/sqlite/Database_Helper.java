package com.park.optech.parking.printticket.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.park.optech.parking.model.Ticket_Model;
import com.park.optech.parking.printticket.models.MembersModel;
import com.park.optech.parking.printticket.models.TicketsModel;
import com.park.optech.parking.printticket.models.UsersModels;

import java.util.ArrayList;
import java.util.List;

public class Database_Helper extends SQLiteOpenHelper
{

    private static final int DATABASE_VERSION = 1;
    private static Database_Helper databasehelper;

    // Database Name
    private static final String DATABASE_NAME = "tickets_db";


    private Database_Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public static Database_Helper getInstance(Context context) {
        //instantiate a new CustomerLab if we didn't instantiate one yet
        if (databasehelper == null) {
            databasehelper = new Database_Helper(context);
        }
        return databasehelper;
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

    public long insertMember(MembersModel model) {

        /*
        * String member_name, String start_date, String end_date
            , String membership_no, String ssn, String company, String img_path
        * */

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them

        values.put(Members_Table.MEMBER_PK,model.getPk());
        values.put(Members_Table.MEMBER_NAME,model.getName());
        values.put(Members_Table.START_DATE,model.getStart_date());
        values.put(Members_Table.END_DATE,model.getEnd_date());
        values.put(Members_Table.MEMBERSHIP_NO,model.getMembership_no());
        values.put(Members_Table.SSN,model.getSsn());
//        values.put(Members_Table.COMPANY,model.get);
        values.put(Members_Table.IMAGE,model.getImg_path());


        long id = -1;

        try {
            Cursor c = db.rawQuery("SELECT  * FROM " + Members_Table.TABLE_NAME + " where " +
                    Members_Table.MEMBER_PK + "=" + model.getPk(),null);
            // insert row

            if (!(c.getCount() > 0))
            {
                id = db.insert(Members_Table.TABLE_NAME, null, values);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }


    public long insertTicket(TicketsModel ticketsModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Tickets_Table.CAMERA_NO,ticketsModel.getCameraNo());
//        values.put(Tickets_Table.TIMESTAMP,ticketsModel.getTimestamp());
//        values.put(Tickets_Table.PAY_TIME,ticketsModel.getPayTime());
        values.put(Tickets_Table.PAY_AMOUNT,ticketsModel.getPayAmount());
        values.put(Tickets_Table.PAY_USER,ticketsModel.getPayUser());
        values.put(Tickets_Table.COMPANY,ticketsModel.getCompany());
        values.put(Tickets_Table.PAID,ticketsModel.getPaid());
        values.put(Tickets_Table.TRX_NO,ticketsModel.getTrx_no());
        values.put(Tickets_Table.MEMBERS,ticketsModel.getMembers());
        values.put(Tickets_Table.SYNC,ticketsModel.getSync());

        // insert row
        long id = db.insert(Tickets_Table.TABLE_NAME, null, values);

        Log.e("TICKET Insertion ", "  " + id);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }



    public long insertUser(UsersModels models) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Users_Table.USER_PK,models.getPk());
        values.put(Users_Table.USER_NAME,models.getName());
        values.put(Users_Table.EMAIL,models.getEmail());
        values.put(Users_Table.PASSWORD,models.getPassword());

        long id = -1;

        try {
            Cursor c = db.rawQuery("SELECT  * FROM " + Users_Table.TABLE_NAME + " where " +
                    Users_Table.USER_PK + "=" + models.getPk(),null);
            // insert row

            if (!(c.getCount() > 0))
            {
                id = db.insert(Users_Table.TABLE_NAME, null, values);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public List<TicketsModel> getTickets()
    {
        List<TicketsModel> data = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Tickets_Table.TABLE_NAME + " ORDER BY " +
                Tickets_Table.TICKET_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TicketsModel ticketsModel = new TicketsModel();
//                data_client.setId(cursor.getInt(cursor.getColumnIndex(Data_Client.ID)));
                ticketsModel.setCameraNo(cursor.getString(cursor.getColumnIndex(Tickets_Table.CAMERA_NO)));
                ticketsModel.setTimestamp(cursor.getString(cursor.getColumnIndex(Tickets_Table.TIMESTAMP)));
                ticketsModel.setPayTime(cursor.getString(cursor.getColumnIndex(Tickets_Table.PAY_TIME)));
                ticketsModel.setPayAmount(cursor.getString(cursor.getColumnIndex(Tickets_Table.PAY_AMOUNT)));
                ticketsModel.setPayUser(cursor.getString(cursor.getColumnIndex(Tickets_Table.PAY_USER)));
                ticketsModel.setCompany(cursor.getString(cursor.getColumnIndex(Tickets_Table.COMPANY)));
                ticketsModel.setPaid(cursor.getString(cursor.getColumnIndex(Tickets_Table.PAID)));
                ticketsModel.setTrx_no(cursor.getString(cursor.getColumnIndex(Tickets_Table.TRX_NO)));
                ticketsModel.setMembers(cursor.getString(cursor.getColumnIndex(Tickets_Table.MEMBERS)));
                ticketsModel.setSync(cursor.getString(cursor.getColumnIndex(Tickets_Table.SYNC)));

                Log.e("MMMM ","NOT NULL");
                Log.e("time STAMP","  "+ticketsModel.getTimestamp());
                String ticket = ticketsModel.getCameraNo() + " : " + ticketsModel.getPaid();
                Log.e("TICKET",ticket + "  ");

                data.add(ticketsModel);
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

    public boolean login(String email , String password)
    {
        String [] columns = {"email"};
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = "email=? and password = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(Users_Table.TABLE_NAME,
                columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();

        cursor.close();
        close();

        return count > 0;
    }

    public TicketsModel check_ticket(String trx_no)
    {
        TicketsModel model = new TicketsModel();
        String [] columns = {"trx_no"};
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = "trx_no = ?";
        String[] selectionArgs = {trx_no};

        Cursor c = db.rawQuery("select * from " + Tickets_Table.TABLE_NAME+" where TRX_NO =?", new String[]{trx_no});


//        Cursor cursor = db.query(Tickets_Table.TABLE_NAME,
//                columns, selection, selectionArgs, null, null, null);
        int count = c.getCount();

        if (c.moveToLast())
        {
            model.setPk(c.getString(c.getColumnIndex(Tickets_Table.TICKET_ID)));
            model.setCameraNo(c.getString(c.getColumnIndex(Tickets_Table.CAMERA_NO)));
            model.setCompany(c.getString(c.getColumnIndex(Tickets_Table.COMPANY)));
            model.setMembers(c.getString(c.getColumnIndex(Tickets_Table.MEMBERS)));
            model.setTimestamp(c.getString(c.getColumnIndex(Tickets_Table.TIMESTAMP)));
            model.setPaid(c.getString(c.getColumnIndex(Tickets_Table.PAID)));
            model.setPayAmount(c.getString(c.getColumnIndex(Tickets_Table.PAY_AMOUNT)));
            model.setPayTime(c.getString(c.getColumnIndex(Tickets_Table.PAY_TIME)));
            model.setPayUser(c.getString(c.getColumnIndex(Tickets_Table.PAY_USER)));
            model.setTrx_no(c.getString(c.getColumnIndex(Tickets_Table.TRX_NO)));
            model.setSync(c.getString(c.getColumnIndex(Tickets_Table.SYNC)));
        }
        else {
            Log.e("MMMMTTT ","NULL");

        }
        c.close();
        return model;
    }

    //
    public boolean check_member(String membership_no , String ssn)
    {
        String [] columns = {"membership_no"};
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = "membership_no=? and ssn = ?";
        String[] selectionArgs = {membership_no,ssn};

        Cursor cursor = db.query(Users_Table.TABLE_NAME,
                columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();

        cursor.close();
        close();

        return count > 0;
    }


    public MembersModel getmember(String id) {
        MembersModel membersModel=new MembersModel();
        SQLiteDatabase db = this.getWritableDatabase(); //get the database that was created in this instance
        Cursor c = db.rawQuery("select * from " + Members_Table.TABLE_NAME+" where MEMBERSHIP_NO =?", new String[]{id});
        if (c.moveToLast()) {
            membersModel.setMembership_no(c.getString(c.getColumnIndex(Members_Table.MEMBERSHIP_NO)));
            membersModel.setPk(c.getString(c.getColumnIndex(Members_Table.MEMBER_PK)));
            membersModel.setName(c.getString(c.getColumnIndex(Members_Table.MEMBER_NAME)));
            membersModel.setStart_date(c.getString(c.getColumnIndex(Members_Table.START_DATE)));
            membersModel.setEnd_date(c.getString(c.getColumnIndex(Members_Table.END_DATE)));
            membersModel.setSsn(c.getString(c.getColumnIndex(Members_Table.SSN)));
            membersModel.setImg_path(c.getString(c.getColumnIndex(Members_Table.IMAGE)));

        }else {
            Log.e("error not found", "members can't be found or database empty");
        }
        return membersModel;
    }


    public UsersModels getusers(String email , String password) {
        UsersModels usersModels=new UsersModels();
        String Query = "SELECT * FROM " + Users_Table.TABLE_NAME + " WHERE "
                + "email" + " = " + email
                + " AND " + "password" + " = " + password;

        SQLiteDatabase db = this.getWritableDatabase(); //get the database that was created in this instance
//        Cursor c = db.rawQuery(Query,null);

        Cursor c = db.rawQuery("SELECT  * FROM " + Users_Table.TABLE_NAME + " where " +
                Users_Table.EMAIL + "= ? And " + Users_Table.PASSWORD + " = ? " ,new String[]{email,password});



        if (c.moveToLast()) {
            usersModels.setPk(c.getString(c.getColumnIndex(Users_Table.USER_PK)));
            usersModels.setName(c.getString(c.getColumnIndex(Users_Table.USER_NAME)));
            usersModels.setEmail(c.getString(c.getColumnIndex(Users_Table.EMAIL)));
            usersModels.setPassword(c.getString(c.getColumnIndex(Users_Table.PASSWORD)));

            return usersModels;

        }else {
            Log.e("error not found", "members can't be found or database empty");
            return usersModels;
        }

    }


    public TicketsModel getTicket()
    {
        TicketsModel model = new TicketsModel();

        String QUERY = "SELECT * FROM "+ Tickets_Table.TABLE_NAME + " ORDER BY " +
                Tickets_Table.TICKET_ID + " DESC LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase(); //get the database that was created in this instance
        Cursor c = db.rawQuery(QUERY,null);


        if (c.moveToLast()) {
            model.setPk(c.getString(c.getColumnIndex(Tickets_Table.TICKET_ID)));
            model.setCameraNo(c.getString(c.getColumnIndex(Tickets_Table.CAMERA_NO)));
            model.setCompany(c.getString(c.getColumnIndex(Tickets_Table.COMPANY)));
            model.setMembers(c.getString(c.getColumnIndex(Tickets_Table.MEMBERS)));
            model.setTimestamp(c.getString(c.getColumnIndex(Tickets_Table.TIMESTAMP)));
            model.setPaid(c.getString(c.getColumnIndex(Tickets_Table.PAID)));
            model.setPayAmount(c.getString(c.getColumnIndex(Tickets_Table.PAY_AMOUNT)));
            model.setPayTime(c.getString(c.getColumnIndex(Tickets_Table.PAY_TIME)));
            model.setPayUser(c.getString(c.getColumnIndex(Tickets_Table.PAY_USER)));
            model.setTrx_no(c.getString(c.getColumnIndex(Tickets_Table.TRX_NO)));
            model.setSync(c.getString(c.getColumnIndex(Tickets_Table.SYNC)));



            return model;

        }else {
            Log.e("error not found", "members can't be found or database empty");
            return model;
        }

    }



    public boolean updateTicket(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Tickets_Table.SYNC,"1");

        return db.update(Tickets_Table.TABLE_NAME,values
                , Tickets_Table.TICKET_ID + "=" + id ,null) > 0;

    }

}
