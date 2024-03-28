package com.karas.tracklaptime.subactivities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.karas.tracklaptime.MainActivity;
import com.karas.tracklaptime.R;
import com.karas.tracklaptime.utils.DatabaseHelper;

public class DatabaseActivity extends AppCompatActivity {

    private static final String DELETED_ROW_MESSAGE = "Pozycja usunięta";
    private static final String NOT_DELETED_ROW_MESSAGE = "Pozycja nie została usunięta";
    private DatabaseHelper databaseHelper;
    private Button deleteDatabaseButton;

    EditText deleteIdButtonEditText;
    TextView textView;

    boolean someFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button backDatabaseButton;
        TableLayout tableLayout;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(this);

        backDatabaseButton = findViewById(R.id.database_back_button);

        deleteDatabaseButton = findViewById(R.id.dataase_delete_button);

        deleteIdButtonEditText = findViewById(R.id.Idtext);


        textView = findViewById(R.id.textView);

        tableLayout = findViewById(R.id.tablelayout);

        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        String[] headerText = {"ID", "DATA", "ILOŚĆ OKR.", "CZAS", "CZASY"};
        for (String c : headerText) {
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

        Cursor dbCursor = databaseHelper.getAllData();

        if (areThereSomeRows(dbCursor)) {
            while (dbCursor.moveToNext()) {
                // Read columns data
                int id = dbCursor.getInt(dbCursor.getColumnIndex("ID"));
                String date = dbCursor.getString(dbCursor.getColumnIndex("DATE"));
                String count = dbCursor.getString(dbCursor.getColumnIndex("COUNT"));
                String fullTime = dbCursor.getString(dbCursor.getColumnIndex("FULL_TIME"));
                String times = dbCursor.getString(dbCursor.getColumnIndex("TIMES"));

                // data rows
                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                String[] colText = {id + date, count, fullTime, times};
                for (String text : colText) {
                    TextView tv = new TextView(this);
                    if (someFlag) {
                        row.setBackgroundColor(Color.WHITE);
                    } else {
                        row.setBackgroundColor(Color.LTGRAY);
                    }
                    someFlag = !someFlag;
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

        } else {
            deleteIdButtonEditText.setVisibility(View.INVISIBLE);
            deleteDatabaseButton.setVisibility(View.INVISIBLE);
        }

        backDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        deleteDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer deletedRows = databaseHelper.deleteData(deleteIdButtonEditText.getText().toString());
                if (deletedRows > 0) {
                    Toast.makeText(DatabaseActivity.this, DELETED_ROW_MESSAGE, Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(getIntent());
                } else {
                    Toast.makeText(DatabaseActivity.this, NOT_DELETED_ROW_MESSAGE, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private static boolean areThereSomeRows(Cursor dbCursor) {
        return dbCursor.getCount() > 0;
    }

    public void deleteData() {
        deleteDatabaseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows = databaseHelper.deleteData(deleteIdButtonEditText.getText().toString());
                        if (deletedRows > 0) {
                            Toast.makeText(DatabaseActivity.this, DELETED_ROW_MESSAGE, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), DatabaseActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(DatabaseActivity.this, NOT_DELETED_ROW_MESSAGE, Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}
