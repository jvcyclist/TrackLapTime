package com.karas.tracklaptime;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class DatabaseActivity extends AppCompatActivity {
    DatabaseHelper myDb;

    TableLayout tableLayout;
    Button backDatabaseButton;
    Button deleteDatabaseButton;

    Button deleteRow;
    EditText IdText;
    TextView textView;

    int i=1;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_database);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDb = new DatabaseHelper(this);

        backDatabaseButton = findViewById(R.id.database_back_button);

        deleteDatabaseButton= findViewById(R.id.dataase_delete_button);

        IdText = findViewById(R.id.Idtext);

        textView = findViewById(R.id.textView);

        tableLayout = findViewById(R.id.tablelayout);

        TableRow rowHeader = new TableRow(context);
        rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        String[] headerText={"ID","DATA","ILOŚĆ OKR.","CZAS","CZASY"};
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

                // data rows
                TableRow row = new TableRow(context);
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                String[] colText={ID+"",DATE,COUNT,FULL_TIME,TIMES};
                for(String text:colText) {
                    TextView tv = new TextView(context);
                    if(i==1){row.setBackgroundColor(Color.WHITE);i=-i;}
                    else{row.setBackgroundColor(Color.LTGRAY);i=-i;}
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

        backDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });

        deleteDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer deletedRows = myDb.deleteData(IdText.getText().toString());
                if(deletedRows > 0)
                {Toast.makeText(DatabaseActivity.this,"Data Deleted",Toast.LENGTH_LONG).show();finish();
                    startActivity(getIntent());}
                else
                {Toast.makeText(DatabaseActivity.this,"Data not Deleted",Toast.LENGTH_LONG).show();}
            }
        });

    }

    public void DeleteData() {
        deleteDatabaseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows= myDb.deleteData(IdText.getText().toString());
                        if(deletedRows > 0){
                            Toast.makeText(DatabaseActivity.this,"Data Deleted",Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getApplicationContext(),DatabaseActivity.class);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(DatabaseActivity.this,"Data not Deleted",Toast.LENGTH_LONG).show();}
                    }
                }
        );
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

}
