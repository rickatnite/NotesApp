package com.example.notesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initNoteButton();
        initListButton();
        initSettingsButton();
        initSettings();
        initSortByClick();
        initSortOrderClick();

    }


    private void initNoteButton() {
        ImageButton ibNote = findViewById(R.id.imageButtonNote);
        ibNote.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, NoteActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, ListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void initSettingsButton() {
        ImageButton ibList = findViewById(R.id.imageButtonSettings);
        ibList.setEnabled(false);
    }


    private void initSettings() {
        String sortBy = getSharedPreferences("NotesAppPreferences",
                Context.MODE_PRIVATE).getString("sortfield","date");
        String sortOrder = getSharedPreferences("NotesAppPreferences",
                Context.MODE_PRIVATE).getString("sortorder","ASC");

        RadioButton rbDate = findViewById(R.id.radioDate);
        RadioButton rbPriority = findViewById(R.id.radioPriority);
        RadioButton rbSubject = findViewById(R.id.radioSubject);
        if (sortBy.equalsIgnoreCase("date")) {
            rbDate.setChecked(true);
        }
        else if (sortBy.equalsIgnoreCase("priority")) {
            rbPriority.setChecked(true);
        }
        else {
            rbSubject.setChecked(true);
        }

        RadioButton rbAscending = findViewById(R.id.radioAscending);
        RadioButton rbDescending = findViewById(R.id.radioDescending);
        if (sortOrder.equalsIgnoreCase("ASC")) {
            rbAscending.setChecked(true);
        }
        else {
            rbDescending.setChecked(true);
        }
    }


    private void initSortByClick() {
        RadioGroup rgSortBy = findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton rbDate = findViewById(R.id.radioDate);
                RadioButton rbPriority = findViewById(R.id.radioPriority);
                if (rbDate.isChecked()) {
                    getSharedPreferences("NotesAppPreferences",
                            Context.MODE_PRIVATE).edit().putString("sortfield", "date").apply();
                }
                else if (rbPriority.isChecked()) {
                    getSharedPreferences("NotesAppPreferences",
                            Context.MODE_PRIVATE).edit().putString("sortfield", "priority").apply();
                }
                else {
                    getSharedPreferences("NotesAppPreferences",
                            Context.MODE_PRIVATE).edit().putString("sortfield", "subject").apply();
                }
            }
        });
    }


    private void initSortOrderClick() {
        RadioGroup rgSortOrder = findViewById(R.id.radioGroupSortOrder);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton rbAscending = findViewById(R.id.radioAscending);
                if (rbAscending.isChecked()) {
                    getSharedPreferences("NotesAppPreferences",
                            Context.MODE_PRIVATE).edit().putString("sortorder", "ASC").apply();
                }
                else {
                    getSharedPreferences("NotesAppPreferences", Context.MODE_PRIVATE).edit().putString("sortorder", "DESC").apply();
                }
            }
        });
    }

}









