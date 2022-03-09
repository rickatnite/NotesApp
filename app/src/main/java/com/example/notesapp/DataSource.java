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

    //method to get contact name data from db
    public ArrayList<String> getNoteSubject() {
        ArrayList<String> noteSubs = new ArrayList<>();
        try {
            String query = "Select subject from note";
            Cursor cursor = database.rawQuery(query, null);
            //cursor holds query results

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) { //while loop tests if the end of cursor's record set is reached
                noteSubs.add(cursor.getString(0)); //subjects added to arraylist each iteration
                cursor.moveToNext(); //cursor moves to next record - causes infinite loop without this
            }
            cursor.close();
        }
        catch (Exception e) {
            noteSubs = new ArrayList<>(); //in case of crash, calling activity tests for empty list
        }
        return noteSubs;
    }


    //method similar to getNotes but to return single note rather than arrayList
    public Note getSpecificNote(int noteId) { //param holds id of note
        Note note = new Note(); //returns note obj instead of arrayList
        String query = "SELECT  * FROM note WHERE _id =" + noteId; //where clause specifies id value to return
        Cursor cursor = database.rawQuery(query, null);

        //no while loop needed bc only one note is returned
        //cursor moves to first record - if note found, note obj is populated
        if (cursor.moveToFirst()) {
            note.setNoteID(cursor.getInt(0));
            note.setSubject(cursor.getString(1));
            note.setDate(cursor.getString(2));
            note.setNoteContent(cursor.getString(3));
            note.setPriority(cursor.getString(4));

            // if no note retrieved, moveToFirst is false and note will not populate
            cursor.close();
        }
        return note;
    }


    //method to retrieve note data for all notes from db
    public ArrayList<Note> getNotes(String sortField, String sortOrder) {
        ArrayList<Note> notes = new ArrayList<Note>();
        try {
            String query = "SELECT  * FROM note ORDER BY " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);

            Note newNote;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newNote = new Note(); //new obj instance for each cursor record
                newNote.setNoteID(cursor.getInt(0));
                newNote.setSubject(cursor.getString(1));
                newNote.setDate(cursor.getString(2));
                newNote.setNoteContent(cursor.getString(3));
                newNote.setPriority(cursor.getString(4));
                notes.add(newNote); //values get added to attribute in new obj
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            notes = new ArrayList<>();
        }
        return notes;
    }



    public boolean deleteNote(int noteId) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("note", "_id=" + noteId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -return value already set to false
        }
        return didDelete;
    }


}
