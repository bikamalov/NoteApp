package com.example.noteapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Random;

public class CustomAdater extends ArrayAdapter {

    private LinkedList<Note> notes;
    private DatabaseHelper myDB;
    private Dialog dialog;
    int pos=0;
    int bt=0;


    public CustomAdater(@NonNull Context context, LinkedList<Note> notes) {
        super(context, R.layout.note);
        this.notes = notes;
        myDB = new DatabaseHelper(getContext());
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.note,parent,false);
        final TextView tvTitle = customView.findViewById(R.id.note_title);
        final TextView tvDate = customView.findViewById(R.id.date_note);
        final TextView tvText = customView.findViewById(R.id.note_text);

        final String color = notes.get(position).getColor();
        ImageButton delete = customView.findViewById(R.id.delete);
        final LinearLayout layout1 = customView.findViewById(R.id.linearlayout);
        final LinearLayout layout2 = customView.findViewById(R.id.colorchange);

        layout2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Cursor id = myDB.getItemId(tvText.getText().toString(),tvTitle.getText().toString(),tvDate.getText().toString());
                int itemID = -1;
                while (id.moveToNext()){
                    itemID = id.getInt(0);
                }
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                final View mView = inflater.inflate(R.layout.dialog_note, null);
                    final int myId = itemID;
                    final EditText title = mView.findViewById(R.id.dialog_title);
                    final EditText text = mView.findViewById(R.id.dialog_text);
                    title.setText(tvTitle.getText().toString());
                    text.setText(tvText.getText().toString());
                    Button save = mView.findViewById(R.id.save);
                    Button cancel = mView.findViewById(R.id.cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                final Cursor data = myDB.getListContents();

                save.setOnClickListener(new View.OnClickListener() {
                    Cursor id = myDB.getItemId(tvText.getText().toString(),tvTitle.getText().toString(),tvDate.getText().toString());
                    @Override
                        public void onClick(View v) {
                                String timeStamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Calendar.getInstance().getTime());
                                Toast toast = Toast.makeText(getContext(),"id: " + myId , Toast.LENGTH_LONG);
                                toast.show();

                                int itemID=position;
                                while (id.moveToNext()){
                                itemID = id.getInt(0);
                            }
                        notes.remove(position);
                        myDB.deleteName(itemID);
                                notes.add(0,new Note(text.getText().toString().replace('\'','`'),title.getText().toString().replace('\'','`'),timeStamp,color));
                                AddData(text.getText().toString(),title.getText().toString(),timeStamp,color);
                                notifyDataSetChanged();
                                dialog.cancel();
                        }
                    });
                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.show();
                notifyDataSetChanged();
                return true;
            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Cursor id = myDB.getItemId(tvText.getText().toString(),tvTitle.getText().toString(),tvDate.getText().toString());
              int itemID = -1;
              String oldcolor = "";
              while (id.moveToNext()){
                  itemID = id.getInt(0);
                  oldcolor = id.getString(4);
              }
              String newcolor = "";
              if(oldcolor.equals("#F7DA07")){
                  newcolor = "#95F442";
              }
              else if (oldcolor.equals("#95F442")){
                  newcolor = "#8EEEFF";
              }
              else if(oldcolor.equals("#8EEEFF")){
                  newcolor = "#F96D6D";
              }else if(oldcolor.equals("#F96D6D")){
                  newcolor = "#F7DA07";
              }
              int nc = Integer.parseInt(newcolor.replaceFirst("^#",""), 16);
              layout1.setBackgroundColor(nc);
              notes.remove(position);
              notes.add(position,new Note(tvText.getText().toString(),tvTitle.getText().toString(),tvDate.getText().toString(),newcolor));
              myDB.updateName(itemID,tvText.getText().toString(),tvTitle.getText().toString(),tvDate.getText().toString(),newcolor);
              notifyDataSetChanged();
          }
        });
        delete.setTag(position);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder a_builder = new AlertDialog.Builder(getContext());
                a_builder.setMessage("Вы точно хотите удалить?").setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Cursor id = myDB.getItemId(tvText.getText().toString(),tvTitle.getText().toString(),tvDate.getText().toString());
                        int itemID=position;
                        while (id.moveToNext()){
                            itemID = id.getInt(0);
                        }
                        myDB.deleteName(itemID);
                        notes.remove(position);
                        notifyDataSetChanged();
                    }
                }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                a_builder.show();
            }
        });
        layout1.setBackgroundColor(Color.parseColor(notes.get(position).getColor()));
        tvDate.setText(notes.get(position).getDate());
        tvTitle.setText(notes.get(position).getTitle());
        tvText.setText(notes.get(position).getText());
        return customView;
    }
    @Override
    public int getCount() {
        return notes.size();
    }
    public void AddData(String text,String title,String data, String color ){
        boolean insertData = myDB.addData(title,text,data,color);
        if(insertData){
            Toast.makeText(getContext(),"Success", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(),"Some thing went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
