package com.example.devyug.userinterface;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 28/9/17.
 */

public class userdatabase extends SQLiteOpenHelper {


    userdatabase(Context context) {
        super(context, "userwords1.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table userword(username TEXT,word TEXT);");
        Log.w("Database", "HELLo userword created");
        db.execSQL("create table flag(id TEXT,fg INTEGER);");
        Log.w("Database", "HELLo flag created");
        Cursor c = db.rawQuery("select * from flag", null);
        if (c.getCount() == 0) {
            ContentValues m = new ContentValues();
            m.put("id", "f");
            m.put("fg", 0);
            db.insert("flag", null, m);

            ContentValues m1 = new ContentValues();
            m1.put("id", "s");
            m1.put("fg", 0);
            db.insert("flag", null, m1);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXITS userword");
        db.execSQL("DROP TABLE IF EXITS flag");
        onCreate(db);
    }

    public void addword(String user, String word) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues m = new ContentValues();
        m.put("username", user);
        m.put("word", word);
        db.insert("userword", null, m);
        db.close();
    }

    public void setfilter() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues m = new ContentValues();
        m.put("id", "f");
        m.put("fg", 1);
        Cursor c = db.rawQuery("select * from flag", null);
        db.update("flag", m, "id='f'", null);
    }

    public void setspecific() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues m = new ContentValues();
        m.put("id", "s");
        m.put("fg", 1);
        db.update("flag", m, "id='s'", null);
    }

    public void clearspecific() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues m = new ContentValues();
        m.put("id", "s");
        m.put("fg", 0);
        db.update("flag", m, "id='s'", null);
    }

    public void clearfilter() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues m = new ContentValues();
        m.put("id", "f");
        m.put("fg", 0);

        db.update("flag", m, "id='f'", null);
    }

    public void clearword() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from userword");
    }

    public boolean getfilter()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c=db.rawQuery("select fg from flag where id='f'",null);
        if(c.moveToFirst()) {
            if (c.getInt(0) == 1)
                return true;
        }
        return false;
    }
    public boolean getspecific()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c=db.rawQuery("select fg from flag where id='s'",null);
        if(c.moveToFirst()) {
            if (c.getInt(0) == 1)
                return true;
        }
        return false;
    }

    public List<String> getwords()
    {
        List<String> l=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from userword",null);
        if(c.moveToFirst())
        {
            do{
            l.add(c.getString(1));
            }while(c.moveToNext());
        }
        return l;
    }

}
