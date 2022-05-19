package com.example.remindme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class dbManager extends SQLiteOpenHelper {
    private static String dbname = "reminder";                                                      //Table  name to store reminders in sqllite

    public dbManager(@Nullable Context context) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {                                           //sql query to insert data in sqllite
        String query = "create table tbl_reminder(id integer primary key autoincrement,title text,date text,time text,completed text default \"no\")";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String query = "DROP TABLE IF EXISTS tbl_reminder";                                         //sql query to check table with the same name or not
        sqLiteDatabase.execSQL(query);                                                              //executes the sql command
        onCreate(sqLiteDatabase);

    }
    public boolean deleteone(Model model){
        SQLiteDatabase db=this.getWritableDatabase();
        String del="Delete from tbl_reminder where title= \""+model.getTitle()+"\"" ;
        Cursor cursor=db.rawQuery(del,null);
        if(cursor.moveToFirst()){
            return true;
        }
        else {
            return false;
        }

    }
    public boolean update_complete(Model model){
        SQLiteDatabase db=this.getWritableDatabase();
        String update="update tbl_reminder set completed= \"yes\" where title= \""+model.getTitle()+"\"" ;
        Cursor cursor=db.rawQuery(update,null);
        if(cursor.moveToFirst()){
            return true;
        }
        else {
            return false;
        }

    }


    public String addreminder(String title, String date, String time) {
        SQLiteDatabase database = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);                                                          //Inserts  data into sqllite database
        contentValues.put("date", date);
        contentValues.put("time", time);

        float result = database.insert("tbl_reminder", null, contentValues);    //returns -1 if data successfully inserts into database

        if (result == -1) {
            return "Failed";
        } else {
            return "Successfully inserted";
        }

    }

    public Cursor readallreminders() {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "select * from tbl_reminder order by id desc";                               //Sql query to  retrieve  data from the database
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }
    public Cursor read_all_incompleted_reminders() {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "select * from tbl_reminder where completed=\"no\" order by id desc";                               //Sql query to  retrieve  data from the database
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }
//    public ArrayList<Model> getEveryone(){
//        ArrayList<Model> customerModels=new ArrayList<>();
//        String select="Select * from tbl_reminder";
//        SQLiteDatabase database=this.getReadableDatabase();
//        Cursor cursor=database.rawQuery(select,null);
//
//        if(cursor.moveToFirst()){
//            do{
//                String title=cursor.getString(1);
//                String date=cursor.getString(2);
//                String time=cursor.getString(3);
//
//                Model customerModel=new Model(title ,date,time);
//                customerModels.add(customerModel);
//
//            }while(cursor.moveToNext());
//        }
//        else{
//            cursor.moveToFirst();
//        }
//        cursor.close();
//        database.close();
//
//        return customerModels;
//    }
}