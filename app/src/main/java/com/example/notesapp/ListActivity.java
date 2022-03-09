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

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int noteId = notes.get(position).getNoteID();
            Intent intent = new Intent(ListActivity.this, NoteActivity.class);
            intent.putExtra("noteId", noteId);
            startActivity(intent);
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

        String sortBy = getSharedPreferences("NotesAppPreferences", Context.MODE_PRIVATE).getString("sortfield", "subject");
        String sortOrder = getSharedPreferences("NotesAppPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");

        DataSource ds = new DataSource(this);
        try {
            ds.open();
            notes = ds.getNotes(sortBy, sortOrder);
            ds.close();
            if (notes.size() > 0) {
                notesList = findViewById(R.id.rvNotes);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                notesList.setLayoutManager(layoutManager);
                noteAdapter = new Adapter(notes, this);
                noteAdapter.setOnItemClickListener(onItemClickListener);
                notesList.setAdapter(noteAdapter);
            }
            else {
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
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean status = compoundButton.isChecked(); //check switch status - on=true - off=false
                noteAdapter.setDelete(status); //status passed to adapter
                noteAdapter.notifyDataSetChanged(); //tells adapter to redraw the list
            }// if switch is on, delete button will be displayed
        });
    }


//    private void initDeleteSwitch() {
//        Switch s = findViewById(R.id.switchDelete);
//        s.setOnCheckedChangeListener((compoundButton, b) -> {
//            boolean status = compoundButton.isChecked();
//            noteAdapter.setDelete(status);
//            noteAdapter.notifyDataSetChanged();
//        });
//    }

}
