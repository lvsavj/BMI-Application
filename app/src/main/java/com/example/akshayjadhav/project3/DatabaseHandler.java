package com.example.akshayjadhav.project3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by akshayjadhav on 10/03/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    Context context;
    SQLiteDatabase db;

    DatabaseHandler (Context context) {
        super(context,"history",null,1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table history(id integer primary key, localDate text, bmi text, weight text)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db=this.getWritableDatabase();
        db.execSQL("drop table if exist history");
    }


    public void addRecord(String localDate, String bmi, String weight){
        db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("localDate",localDate);
        values.put("bmi",bmi);
        values.put("weight",weight);
        long rid=db.insert("history",null,values);
        if (rid<0){
            Toast.makeText(context,"Insert issue", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(context,"Insert success",Toast.LENGTH_LONG).show();
        }
    }

    public String getRecord() {
        db=this.getWritableDatabase();


        Cursor cursor=db.query("history",null,null,null,null,null,null);
        StringBuffer sb=new StringBuffer();

        cursor.moveToFirst();

        if (cursor.getCount()>0){
            do{
                String id=cursor.getString(0);
                String localDate = cursor.getString(1);
                String bmi = cursor.getString(2);
                String weight = cursor.getString(3);
                sb.append("Record No: "+id+"\n"+"Date: "+localDate+"\n"+"BMI: "+bmi+"\n"+"Weight: "+weight+"\n--------------------------------------------------------\n");
            }while (cursor.moveToNext());

        }
        else {
            sb.append("No Records to show");
        }
        return sb.toString();
    }
}