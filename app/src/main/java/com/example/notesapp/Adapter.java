package com.example.notesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter {

    private ArrayList<Note> noteData;
    private View.OnClickListener mOnItemClickListener;
    private Context parentContext;
    private boolean isDeleting;

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
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }

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

    public Adapter(ArrayList<Note> arrayList, Context context) {
        noteData = arrayList;
        parentContext = context;
    }


    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_item_view, parent, false);
        return new NoteViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NoteViewHolder nvh = (NoteViewHolder) holder;
        nvh.getTextViewSubject().setText(noteData.get(position).getSubject());
        nvh.getTextDate().setText(noteData.get(position).getDate());
        nvh.getTextPriority().setText(noteData.get(position).getPriority());
        if (isDeleting) {
            nvh.getDeleteButton().setVisibility(View.VISIBLE);
            nvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem(holder.getAdapterPosition());
                }
            });
        }
        else {
            nvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return noteData.size();
    }

    public void setDelete(boolean b) {
        isDeleting = b;
    }

    private void deleteItem(int position) {
        Note note = noteData.get(position);
        DataSource ds = new DataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteNote(note.getNoteID());
            ds.close();
            if (didDelete) {
                noteData.remove(position);
                notifyDataSetChanged();
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

//    private void deleteOption(int noteToDelete, Context context) {
//        DataSource db = new DataSource(context);
//        try {
//            db.open();
//            db.deleteNote(noteToDelete);
//            db.close();
//        }
//        catch (Exception e) {
//            Toast.makeText(parentContext, "Delete Note Failed", Toast.LENGTH_LONG).show();
//        }
//        this.notifyDataSetChanged();
//    }
//


//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View v = convertView;
//        try {
//            Note note = noteData.get(position);
//
//            if (v == null) {
//                LayoutInflater vi = (LayoutInflater) parentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                v = vi.inflate(R.layout.simple_item_view, null); // or list_item?
//            }
//            TextView noteSubject = (TextView) v.findViewById(R.id.textSubject);
//            noteSubject.setText(note.getSubject());
//
//            TextView notePriority = (TextView) v.findViewById(R.id.textPriority);
//            switch(note.getPriority()){
//                case 3: notePriority.setText("High"); break;
//                case 2: notePriority.setText("Medium"); break;
//                default: notePriority.setText("Low"); break;
//            }
//            TextView noteDate = (TextView) v.findViewById(R.id.textDate);
//            noteDate.setText(note.getDate().toString());
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            e.getCause();
//        }
//
//        final Button bDelete = (Button) v.findViewById(R.id.buttonDeleteNote);
//        final Note note = noteData.get(position);
//
//        bDelete.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                noteData.remove(note);
//                deleteOption(note.getNoteID(), parentContext);
//            }
//        });
//        return v;
//    }

