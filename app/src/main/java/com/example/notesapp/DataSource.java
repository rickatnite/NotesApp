package com.example.notesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

public class DataSource {

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public DataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }



    public boolean insertNote(Note n) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("subject", n.getSubject());
            initialValues.put("date", n.getDate());
            initialValues.put("note", n.getNoteContent());
            initialValues.put("priority", n.getPriority());

            didSucceed = database.insert("note", null, initialValues) > 0;
        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }



    public boolean updateNote(Note n) {
        boolean didSucceed = false;
        try {
            Long rowId = (long) n.getNoteID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("subject", n.getSubject());
            updateValues.put("date", n.getDate());
            updateValues.put("note", n.getNoteContent());
            updateValues.put("priority", n.getPriority());

            didSucceed = database.update("note", updateValues, "_id=" + rowId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }


    public int getLastNoteId() {
        int lastId;
        try {
            String query = "Select MAX(_id) from note";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e) {
            lastId = -1;
        }
        return lastId;
    }


    public ArrayList<String> getNoteSubject() {
        ArrayList<String> noteSubs = new ArrayList<>();
        try {
            String query = "Select subject from note";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                noteSubs.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            noteSubs = new ArrayList<String>();
        }
        return noteSubs;
    }



    public Note getSpecificNote(int noteId) {
        Note note = new Note();
        String query = "SELECT  * FROM contact WHERE _id =" + noteId;
        Cursor cursor = database.rawQuery(query, null);

        //no while loop needed bc only one contact is returned
        //cursor moves to first record - if contact found, contact obj is populated
        if (cursor.moveToFirst()) {
            note.setNoteID(cursor.getInt(0));
            note.setSubject(cursor.getString(1));
            note.setDate(cursor.getString(2));
            note.setNoteContent(cursor.getString(3));
            note.setPriority(cursor.getString(3));

//            Calendar calendar = Calendar.getInstance();
//            calendar.setTimeInMillis(Long.valueOf(cursor.getString(9)));

            cursor.close();
        }
        return note;
    }

}
