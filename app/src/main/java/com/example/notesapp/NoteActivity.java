package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        setDateTime();
        initNoteButton();
        initListButton();
        initSettingsButton();
        initToggleButton();
        setForEditing(false);

    }

    private void initNoteButton() {
        ImageButton ibNote = findViewById(R.id.imageButtonNote);
        ibNote.setEnabled(false);
    }

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(view -> {
            Intent intent = new Intent(NoteActivity.this, ListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void initSettingsButton() {
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings);
        ibSettings.setOnClickListener(view -> {
            Intent intent = new Intent(NoteActivity.this, SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }


    private void initToggleButton() {
        final ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(view -> setForEditing(editToggle.isChecked()));
    }



    private void setForEditing(boolean enabled) {
        EditText editSubject = findViewById(R.id.editSubject);
        EditText editNote = findViewById(R.id.editNote);
        Button buttonSave = findViewById(R.id.buttonSave);



        editSubject.setEnabled(enabled);
        editNote.setEnabled(enabled);
        buttonSave.setEnabled(enabled);

        if (enabled) {
            editSubject.requestFocus();
        }
    }


    private void setDateTime() {
        TextView date  = (TextView) findViewById(R.id.textViewDate);
        String dateTime = new SimpleDateFormat("MM.dd.yyyy",
        Locale.getDefault()).format(new Date());
        date.setText(dateTime);
    }






}