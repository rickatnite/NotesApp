package com.example.notesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter {

    private ArrayList<Note> noteData;
    private View.OnClickListener mOnItemClickListener;

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewSubject;
        public TextView textDate;
        public Button deleteButton;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSubject = itemView.findViewById(R.id.textSubject);
            textDate = itemView.findViewById(R.id.textViewDate);
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
        public TextView getDeleteButton() {
            return deleteButton;
        }
    }

    public Adapter(ArrayList<Note> arrayList, Context context) {
        noteData = arrayList;
        //parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_item_view, parent, false); // or list_item???
        return new NoteViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NoteViewHolder nvh = (NoteViewHolder) holder;
        nvh.getTextViewSubject().setText(noteData.get(position).getSubject());
        nvh.getTextDate().setText(noteData.get(position).getDate());
//        if (isDeleting) {
//            nvh.getDeleteButton().setVisibility(View.VISIBLE);
//            nvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    deleteItem(position);
//                }
//            });
//        }
//        else {
//            nvh.getDeleteButton().setVisibility(View.INVISIBLE);
//        }
    }


    @Override
    public int getItemCount() {
        return noteData.size();
    }





}
