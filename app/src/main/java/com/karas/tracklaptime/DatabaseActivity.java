package com.karas.tracklaptime;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DatabaseActivity extends AppCompatActivity {
    DatabaseHelper myDb;

    TableLayout tableLayout;

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_database);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDb = new DatabaseHelper(this);

        tableLayout = findViewById(R.id.tablelayout);
        TableRow rowHeader = new TableRow(context);
        rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        String[] headerText={"ID","DATE","COUNT","FULL TIME","TIMES"};
        for(String c:headerText) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(18);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(c);
            rowHeader.addView(tv);
        }
        tableLayout.addView(rowHeader);

        Cursor res = myDb.getAllData();

        if(res.getCount() >0)
        {
            while (res.moveToNext()) {
                // Read columns data
                int ID= res.getInt(res.getColumnIndex("ID"));
                String DATE = res.getString(res.getColumnIndex("DATE"));
                String COUNT= res.getString(res.getColumnIndex("COUNT"));
                String FULL_TIME = res.getString(res.getColumnIndex("FULL_TIME"));
                String TIMES = res.getString(res.getColumnIndex("TIMES"));




                // dara rows
                TableRow row = new TableRow(context);
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                String[] colText={ID+"",DATE,COUNT,FULL_TIME,TIMES};
                for(String text:colText) {
                    TextView tv = new TextView(context);
                    tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextSize(16);
                    tv.setPadding(5, 5, 5, 5);
                    tv.setText(text);
                    row.addView(tv);
                }
                tableLayout.addView(row);

            }

        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

     /*   viewAll();
        showAll.performClick();*/
    }



    /*public void viewAll() {
        showAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       *//* if(res.getCount() == 0) {
                            // show message
                            showMessage("Error","Nothing found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("Id: "+ res.getString(0)+"\n");
                            buffer.append("Data: "+ res.getString(1)+"\n");
                            buffer.append("Ilość okr.: "+ res.getString(2)+"\n");
                            buffer.append("Czas: "+ res.getString(3)+"\n\n");
                            buffer.append("Czasy: "+ res.getString(4)+"\n\n");
                        }*//*





                       *//* // Show all data
                        showMessage("Data",buffer.toString());*//*
                    }
                }
        );
    }*/


    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }




}
