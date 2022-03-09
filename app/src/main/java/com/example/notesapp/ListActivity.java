package com.example.notesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ListActivity extends AppCompatActivity {

    RecyclerView notesList;
    Adapter noteAdapter;
    ArrayList<Note> notes;

    //new instance of listener to pass to adapter
    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) { //ref to viewHolder that produced the click with getTag
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition(); //use viewHolder to get position in the list to get corresponding Note obj
            int noteId = notes.get(position).getNoteID(); //position value is index of item clicked in list
            Intent intent = new Intent(ListActivity.this, NoteActivity.class);
            intent.putExtra("noteId", noteId); //puts noteId in bundle passed to NoteActivity
            startActivity(intent); //starts NoteActivity by clicking any note in list
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initNoteButton();
        initListButton();
        initSettingsButton();
        initAddNoteButton();
        initDeleteSwitch();
    }


    @Override
    public void onResume() {
        super.onResume();

        //in onResume instead of onCreate bc it is executed every time the user navigates to the activity
        //retrieve store user preferences
        String sortBy = getSharedPreferences("NotesAppPreferences", Context.MODE_PRIVATE).getString("sortfield", "subject");
        String sortOrder = getSharedPreferences("NotesAppPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");

        //create new datasource object, open db, retrieve note names with getNotes, close db
        DataSource ds = new DataSource(this);
        try {
            ds.open();
            notes = ds.getNotes(sortBy, sortOrder);
            ds.close();
            if (notes.size() > 0) { //setup recyclerView to display data
                notesList = findViewById(R.id.rvNotes); //ref the widget and create instance of LayoutManager to display indv items
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this); //LLM displays vertical scrolling list
                notesList.setLayoutManager(layoutManager); //pass arraylist of note names
                noteAdapter = new Adapter(notes, this);
                noteAdapter.setOnItemClickListener(onItemClickListener);
                notesList.setAdapter(noteAdapter); //associate adapter with recyclerView
            }
            else { //opens NoteActivity if there are no notes
                Intent intent = new Intent(ListActivity.this, NoteActivity.class);
                startActivity(intent);
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "Error retrieving notes", Toast.LENGTH_LONG).show();
        }

    }



    private void initNoteButton() {
        ImageButton ibNote = findViewById(R.id.imageButtonNote);
        ibNote.setOnClickListener(view -> {
            Intent intent = new Intent(ListActivity.this, NoteActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setEnabled(false);
    }

    private void initSettingsButton() {
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings);
        ibSettings.setOnClickListener(view -> {
            Intent intent = new Intent(ListActivity.this, SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }


    private void initAddNoteButton() {
        Button newNote = findViewById(R.id.buttonNewNote);
        newNote.setOnClickListener(v -> {
            Intent intent = new Intent(ListActivity.this, NoteActivity.class);
            startActivity(intent);
        });
    }

    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.switchDelete); //assign switch ref and create listener
        // if switch is on, delete button will be displayed
        s.setOnCheckedChangeListener((compoundButton, b) -> {
            boolean status = compoundButton.isChecked(); //check switch status - on=true - off=false
            noteAdapter.setDelete(status); //status passed to adapter
            noteAdapter.notifyDataSetChanged(); //tells adapter to redraw the list
        });
    }


}
