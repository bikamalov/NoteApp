package com.example.noteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "notes.db";
    public static final String TABLE_NAME = "notes_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "Title";
    public static final String COL3 = "Text";
    public static final String COL4 = "Data";
    public static final String COL5 = "Color";


    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "Title TEXT," +  "Text TEXT," + "Data TEXT," + "Color TEXT )";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean addData(  String text,String name,String data, String color){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,name);
        contentValues.put(COL3,text);
        contentValues.put(COL4,data);
        contentValues.put(COL5,color);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL1 + " DESC " ,null);
        return data;
    }


    public Cursor getItemId( String name,String text, String data){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME + " WHERE " + COL2 + " = '"+ name +"'" + " AND " +
                COL3 + " = '" + text +"'" + " AND " + COL4 + " = '" + data + "' ";
        Cursor id = db.rawQuery(query,null);
        return id;
    }

    public void updateName(int id,String name, String text,String data, String color ){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = " UPDATE " + TABLE_NAME + " SET " + COL2 + " = '" + name + "'" + " ," +
                COL3 + " = '" + text + "'" + " , " + COL4 + " = '" + data + "'" + " , " + COL5 + " = '" + color + "'"   + " WHERE " +
                COL1  + " = '" + id + "'";
        db.execSQL(query);
    }

    public void deleteName(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " +
                COL1 + " = '" + id + "'";
        db.execSQL(query);
    }
}
