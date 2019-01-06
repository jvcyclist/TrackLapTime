package com.karas.tracklaptime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Cycling.db";
    public static final String TABLE_NAME = "results_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "DATE";
    public static final String COL_3 = "COUNT";
    public static final String COL_4 = "FULL_TIME";
    public static final String COL_5 = "TIMES";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,DATE TEXT,COUNT INTEGER,FULL_TIME TEXT,TIMES TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }


    public boolean insertData(String date,String count,String fulltime,String times) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,date);
        contentValues.put(COL_3,count);
        contentValues.put(COL_4,fulltime);
        contentValues.put(COL_5,times);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }






}




