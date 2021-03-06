package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {

    private Note currentNote;
    RadioButton rbLow, rbMed, rbHigh;

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
        initPriorityClick();

        rbLow = findViewById(R.id.radioLow);
        rbMed = findViewById(R.id.radioMedium);
        rbHigh = findViewById(R.id.radioHigh);

        Bundle extras = getIntent().getExtras(); //get passed id and call initNote to retrieve and display note - corresponds to ListActivity onclick
        if(extras != null) {
            initNote(extras.getInt("noteId"));
        }         //checks intent for extras, if found gets noteId, retrieves note from db, displays data in layout
        else {
            currentNote = new Note();
        }         //if no extra, assigns new note obj to currentNote making id -1
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
        } else {
            ScrollView s = findViewById(R.id.scrollView);
            s.fullScroll(ScrollView.FOCUS_UP);
        }
    }


    //method to set current date/time from locale
    private void setDateTime() {
        TextView date = findViewById(R.id.textViewDate);
        String dateTime = new SimpleDateFormat("MM.dd.yyyy", Locale.getDefault()).format(new Date());
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

        final TextView tvDate = findViewById(R.id.textViewDate);
        etNote.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentNote.setDate(tvDate.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

    }



    private void initSaveButton() {
        Button saveButton = findViewById(R.id.buttonSave);

        saveButton.setOnClickListener(view -> {
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
        });
    }


    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editSubject = findViewById(R.id.editSubject);
        imm.hideSoftInputFromWindow(editSubject.getWindowToken(), 0);
        EditText editNote = findViewById(R.id.editNote);
        imm.hideSoftInputFromWindow(editNote.getWindowToken(), 0);
    }



    //method to retrieve note and populate layout with retrieved values
    private void initNote(int id) {
        //retrieve note and assign to activity's currentNote variable
        DataSource ds = new DataSource(NoteActivity.this);
        try {
            ds.open();
            currentNote = ds.getSpecificNote(id);
            ds.close();
        }
        catch (Exception e) {
            Toast.makeText(this, "Load Note Failed", Toast.LENGTH_LONG).show();
        }

        //refs to all widgets in layout so note data is displayed in them
        EditText editSubject = findViewById(R.id.editSubject);
        TextView textDate = findViewById(R.id.textViewDate);
        EditText editNote = findViewById(R.id.editNote);

        // widgets set to display the values in retrieved note
        editSubject.setText(currentNote.getSubject());
        textDate.setText(currentNote.getDate());
        editNote.setText(currentNote.getNoteContent());

        //retrieve priority level from radio buttons
        switch (currentNote.getPriority()) {
            case "high": rbHigh.toggle();
                break;
            case "medium": rbMed.toggle();
                break;
            default: rbLow.toggle();
        }
    }


    //method to set priority in obj
    private void initPriorityClick() {
        RadioGroup rgPriority = findViewById(R.id.radioGroupPriority);
        rgPriority.setOnCheckedChangeListener((rg, checkedId) -> {
            switch (checkedId) {
                case R.id.radioLow:
                    currentNote.setPriority("low");
                    break;
                case R.id.radioMedium:
                    currentNote.setPriority("medium");
                    break;
                case R.id.radioHigh:
                    currentNote.setPriority("high");
                    break;
            }
        });
    }




    //method to set textview to selected priority
    public void onPriorityClicked(View view) {
        View.OnClickListener optionOnClickListener = v -> {
            TextView tvPriority = findViewById(R.id.textPriority);
            String str;
            str = ((RadioButton)v).getText().toString(); // copy the string of clicked button
            tvPriority.setText(str);
        };
        rbLow.setOnClickListener(optionOnClickListener);
        rbMed.setOnClickListener(optionOnClickListener);
        rbHigh.setOnClickListener(optionOnClickListener);
    }


}



