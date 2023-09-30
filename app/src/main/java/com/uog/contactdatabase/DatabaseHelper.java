package com.uog.contactdatabase;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.uog.contactdatabase.database.Contact;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME ="contactDatabase.db";
    private static final String TABLE_PERSON ="tblContact";

    public static final String PERSON_ID = "id";
    public static final String NAME = "name";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";
    public static final String AGE = "age";
    public static final String AVATAR_FILE_PATH = "avatarFilePath";

    private SQLiteDatabase database;

    private static final String CREATE_CONTACT_TABLE =String.format(
            "CREATE TABLE IF NOT EXISTS %s (" +
                    " %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT," +
                    " %s TEXT," +
                    " %s TEXT," +
                    " %s INTEGER," +
                    " %s TEXT)"
            , TABLE_PERSON, PERSON_ID, NAME, PHONE, ADDRESS, AGE, AVATAR_FILE_PATH);


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
        database =getWritableDatabase();
        if(database !=null) database.execSQL( "PRAGMA encoding ='UTF-8'" );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CONTACT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long save(String name, String address, String phone, int age, String avatarFilePath){
        long result =0;
        ContentValues rowValues =new ContentValues();
        rowValues.put(NAME, name);
        rowValues.put(ADDRESS, address);
        rowValues.put(PHONE, phone);
        rowValues.put(AGE, age);
        rowValues.put(AVATAR_FILE_PATH, avatarFilePath);
        result =database.insertOrThrow(TABLE_PERSON, null, rowValues);
        return result;
    }


    public List search( String keyword ) throws Exception{
        Cursor cursor = null;
        String query ="SELECT * FROM " + TABLE_PERSON
                +" WHERE " + NAME +" LIKE '%" + keyword +"%'";

        List<Contact> results =new ArrayList<>();
        cursor = database.rawQuery( query, null );
        cursor.moveToFirst( );
        while( !cursor.isAfterLast() ){
//            int id =cursor.getColumnIndex(PERSON_ID);
            int id =cursor.getInt(0);
            String name =cursor.getString(1);
            String address =cursor.getString(2);
            String phone =cursor.getString(3);
            int age =cursor.getInt(4);
            String avatarFilePath =cursor.getString(5);
            cursor.moveToNext( );

            Contact contact = new Contact(id,name,address,phone,age,avatarFilePath);
            results.add(contact);
        }
        cursor.close();
        return results;
    }

    public long delete(int id){
        long result = 0;
        String where = "id = ?";
        String valuse[] = {String.valueOf(id)};
        result  = database.delete(TABLE_PERSON,where,valuse);
        return  result;

    }


    public long update(int id, String name, String address, String phone, int age, String avatarFilePath){
        long result =0;
        ContentValues rowValues =new ContentValues();
        rowValues.put(NAME, name);
        rowValues.put(ADDRESS, address);
        rowValues.put(PHONE, phone);
        rowValues.put(AGE, age);
        rowValues.put(AVATAR_FILE_PATH, avatarFilePath);
        String where = "id = ?";
        String values[] = {id + ""};
        result =database.update(TABLE_PERSON,  rowValues, where, values);
        return result;
    }



    public void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
        Log.i("test","DB delete");
    }


}