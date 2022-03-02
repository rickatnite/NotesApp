package com.example.notesapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

//    private static final String DATABASE_NAME = "notefiles.db";
//    private static final int DATABASE_VERSION = 1;
//
//
//    private static final String CREATE_TABLE_NOTE =
//            "create table note (_id integer primary key autoincrement, "
//                    + "subject text not null, datetime text, note text, priority text);";
//
//    public DBHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(CREATE_TABLE_NOTE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVers, int newVers) {
//        Log.w(DBHelper.class.getName(),
//                    "Upgrading database from version " + oldVers + " to "
//                            + newVers + ", which will destroy all old data");
//            db.execSQL("DROP TABLE IF EXISTS note");
//            onCreate(db);
////        try {
////            db.execSQL("ALTER TABLE note ADD COLUMN newcolumn text");
////        }
////        catch (Exception e) {
////            //do nothing
////        }
//    }
//}
