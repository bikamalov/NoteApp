package com.example.noteapp;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    LinkedList<Note> notes;
    CustomAdater adapter;
    ListView lv;
    AlertDialog dialog;
    DatabaseHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myDB = new DatabaseHelper(this);
        notes = new LinkedList<>();
        lv = findViewById(R.id.notes_list);
        adapter = new CustomAdater(this,notes);
        Cursor data = myDB.getListContents();

        while (data.moveToNext()){
            notes.add(new Note(data.getString(1),data.getString(2),data.getString(3),data.getString( 4)));
            adapter.notifyDataSetChanged();
            lv.setAdapter(adapter);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_note, null);
                final EditText title = mView.findViewById(R.id.dialog_title);
                final EditText text = mView.findViewById(R.id.dialog_text);
                Button save = mView.findViewById(R.id.save);
                Button cancel = mView.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(title.getText().toString().equals("") && text.getText().toString().equals("")){
                            dialog.cancel();
                        }else {
                            String timeStamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Calendar.getInstance().getTime());
                            notes.add(0,new Note(text.getText().toString(),title.getText().toString(),timeStamp,"#F7DA07"));
                            AddData(text.getText().toString(),title.getText().toString(),timeStamp,"#F7DA07");
                            adapter.notifyDataSetChanged();
                            lv.setAdapter(adapter);
                            dialog.cancel();}
                       // }
                    }
                });
                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.show();
            }
        });
    }
    public void AddData(String text,String title,String data, String color ){
        boolean insertData = myDB.addData(title,text,data,color);
        if(insertData){
            Toast.makeText(MainActivity.this,"Success", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this,"Some thing went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}