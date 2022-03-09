package com.example.notesapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter {

    private ArrayList<Note> noteData;
    private View.OnClickListener mOnItemClickListener; //to hold OnClickListener obj passed from activity
    private Context parentContext; //gives context to open the db so note can be deleted and display msg if it fails
    private boolean isDeleting; //holds delete status of adapter and activity it is within

    //declare behavior of viewHolder class owned by this adapter
    public class NoteViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewSubject;
        public TextView textDate;
        public TextView textPriority;
        public Button deleteButton;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSubject = itemView.findViewById(R.id.textSubject);
            textDate = itemView.findViewById(R.id.textDate);
            textPriority = itemView.findViewById(R.id.textPriority);
            deleteButton = itemView.findViewById(R.id.buttonDeleteNote);
            itemView.setTag(this); //sets tag to identify which item is clicked
            itemView.setOnClickListener(mOnItemClickListener); //sets viewHolder's OnClickListener to listener passed from activity
        }

        //used by adapter to return the textViews to set and change the displayed text
        public TextView getTextViewSubject() {
            return textViewSubject;
        }
        public TextView getTextDate() {
            return textDate;
        }
        public TextView getTextPriority() {
            return textPriority;
        }
        public Button getDeleteButton() {
            return deleteButton;
        }
    }

    //declare constructor method for adapter to associate data to be displayed
    public Adapter(ArrayList<Note> arrayList, Context context) {
        noteData = arrayList;
        parentContext = context; //pass context to the adapter's constructor method from activity to held in new variable
    }


    //sets up adapter method to pass listener from activity to adapter
    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }


    @NonNull
    @Override //creates visual display for each item using layout
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_item_view, parent, false);
        return new NoteViewHolder(v);
    } // viewHolder is created for each item using the inflated xml and returned to RecyclerView to be displayed by activity



    //called for each item in the data set and passed to viewHolder created by onCreateViewHolder
    //then it is cast into the associated NoteViewHolder
    //getTextViewSubject is called to set text attribute to the name of the subject at current position
    @Override //position used so onClick can access its value - might need to be final
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        NoteViewHolder nvh = (NoteViewHolder) holder; //note obj methods used to get required values
        nvh.getTextViewSubject().setText(noteData.get(position).getSubject());
        nvh.getTextDate().setText(noteData.get(position).getDate());
        nvh.getTextPriority().setText(noteData.get(position).getPriority());

        // sets priority textview color based on selected priority
            switch(noteData.get(position).getPriority()){
                case "high": nvh.getTextPriority().setTextColor(Color.RED); break;
                case "medium": nvh.getTextPriority().setTextColor(Color.YELLOW); break;
                default: nvh.getTextPriority().setTextColor(Color.GREEN); break;
            }

        //if adapter is in delete mode, delete button for each note is set to visible
        if (isDeleting) {
            nvh.getDeleteButton().setVisibility(View.VISIBLE);
            nvh.getDeleteButton().setOnClickListener(view -> deleteItem(holder.getAdapterPosition()));
        } //calls adapter's delete method - passed to note position to delete correct note
        else {
            nvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }


    //returns number of items in the dataset to determine how many times to execute the other 2 methods
    @Override
    public int getItemCount() {
        return noteData.size();
    }

    //method to allow delete switch on activity to set the adapter to delete mode
    public void setDelete(boolean b) {
        isDeleting = b;
    }

    private void deleteItem(int position) {
        Note note = noteData.get(position); //get selected note
        DataSource ds = new DataSource(parentContext);
        try {
            ds.open(); //create and open new DS obj, delete note with deleteNote method
            boolean didDelete = ds.deleteNote(note.getNoteID());
            ds.close();
            if (didDelete) {
                noteData.remove(position); //contact is removed from adapter's data
                notifyDataSetChanged(); //adapter is told to refresh display
            }
            else {
                Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
        }
    }

}
