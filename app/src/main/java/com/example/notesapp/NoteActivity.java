package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

    private Note currentNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        setDateTime();
        initNoteButton();
        initListButton();
        initSettingsButton();
        initToggleButton();
        initTextChangedEvents();
        initSaveButton();
        setForEditing(false);
        currentNote = new Note();

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
        } else {
            ScrollView s = findViewById(R.id.scrollView);
            s.fullScroll(ScrollView.FOCUS_UP);
        }
    }


    private void setDateTime() {
        TextView date  = (TextView) findViewById(R.id.textViewDate);
        String dateTime = new SimpleDateFormat("MM.dd.yyyy",
        Locale.getDefault()).format(new Date());
        date.setText(dateTime);
    }




    private void initTextChangedEvents() {
        final EditText etSubject = findViewById(R.id.editSubject);
        etSubject.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                currentNote.setSubject(etSubject.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        final EditText etNote = findViewById(R.id.editNote);
        etNote.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentNote.setNoteContent(etNote.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });
    }



    private void initSaveButton() {
        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                boolean wasSuccessful;
                hideKeyboard();
                DataSource ds = new DataSource(NoteActivity.this);
                try {
                    ds.open();

                    if (currentNote.getNoteID() == -1) {
                        wasSuccessful = ds.insertNote(currentNote);
                        if (wasSuccessful) {
                            int newId = ds.getLastNoteId();
                            currentNote.setNoteID(newId);
                        }

                    }
                    else {
                        wasSuccessful = ds.updateNote(currentNote);
                    }
                    ds.close();
                }
                catch (Exception e) {
                    wasSuccessful = false;
                }

                if (wasSuccessful) {
                    ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
                    editToggle.toggle();
                    setForEditing(false);
                }
            }
        });
    }


    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editSubject = findViewById(R.id.editSubject);
        imm.hideSoftInputFromWindow(editSubject.getWindowToken(), 0);
        EditText editNote = findViewById(R.id.editNote);
        imm.hideSoftInputFromWindow(editNote.getWindowToken(), 0);
    }





}