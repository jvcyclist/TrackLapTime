package com.karas.tracklaptime;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.KeyEvent.Callback;
import android.view.View;

import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class RunActivity extends AppCompatActivity{

    DatabaseHelper myDb;
    static int counter=0;
    TextView fullTime,lapTime,numOfLap,test;
    Button start, pause, reset ,nextlap,noButton,yesButton;
    String timeR,res;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime,MillisecondTime2,StartTime2,TimeBuff2,UpdateTime2 = 0L ;
    String lastFullTime;
    Handler handler,handler2;

    int Seconds, Minutes, MilliSeconds,Lap=0,lastLap ;
    int Seconds2,Minutes2,MilliSeconds2;
    int numofClick=0;
    ArrayAdapter<String> adapter ;

    List<String> list = new LinkedList<String>();
    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
    String date;
    TextView quest_textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        myDb = new DatabaseHelper(this);
        fullTime = (TextView)findViewById(R.id.fullTime);
        lapTime = (TextView)findViewById(R.id.lapTime);
        numOfLap = (TextView)findViewById(R.id.numOfLap);
        test = (TextView) findViewById(R.id.test);
        test.setVisibility(View.INVISIBLE);
        date= df.format(Calendar.getInstance().getTime());
        start = (Button)findViewById(R.id.startbtn);
        pause = (Button)findViewById(R.id.stopbtn);
        reset = (Button)findViewById(R.id.resetbtn);
        nextlap = (Button)findViewById(R.id.nextLap);

        noButton = (Button)findViewById(R.id.run_no_button);
        yesButton = (Button)findViewById(R.id.run_yes_button);
        quest_textView=findViewById(R.id.quest_textView);
        nextlap.setVisibility(View.INVISIBLE);
        start.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.INVISIBLE);
        reset.setVisibility(View.INVISIBLE);
        yesButton.setVisibility(View.INVISIBLE);
        noButton.setVisibility(View.INVISIBLE);
        quest_textView.setVisibility(View.INVISIBLE);
        handler = new Handler() ;
        handler2 = new Handler();
        AddData();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                StartTime = SystemClock.uptimeMillis();
                StartTime2 = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                test.setVisibility(View.INVISIBLE);
                yesButton.setVisibility(View.INVISIBLE);
                noButton.setVisibility(View.INVISIBLE);
                reset.setEnabled(false);
                nextlap.setEnabled(true);
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimeBuff += MillisecondTime;

                handler.removeCallbacks(runnable);

                reset.setEnabled(true);
                nextlap.setEnabled(false);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MillisecondTime = 0L ;
                StartTime = 0L ;
                TimeBuff = 0L ;
                UpdateTime = 0L ;
                Seconds = 0 ;
                Minutes = 0 ;
                MilliSeconds = 0 ;
                lastLap=Lap;
                Lap=0;

                MillisecondTime2 = 0L ;
                StartTime2 = 0L ;
                TimeBuff2 = 0L ;
                UpdateTime2 = 0L ;
                Seconds2 = 0 ;
                Minutes2 = 0 ;
                numofClick=0;

                nextlap.setEnabled(false);
                lastFullTime=fullTime.getText().toString();
                fullTime.setText("00:00:00");
                lapTime.setText("0.0");
                numOfLap.setText("Lap: "+Integer.toString(Lap));
                test.setVisibility(View.VISIBLE);
                yesButton.setVisibility(View.VISIBLE);
                noButton.setVisibility(View.VISIBLE);
                quest_textView.setVisibility(View.VISIBLE);

            }
        });

        nextlap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Lap = Lap + 1;
                numOfLap.setText("Lap: "+Integer.toString(Lap));

                numofClick++;

                if(numofClick==1){start.performClick();}

                UpdateTime2=SystemClock.uptimeMillis()-StartTime2;

                Seconds2 = (int) (UpdateTime2 / 1000);

                Minutes2 = Seconds2 / 60;

                Seconds2 = Seconds2 % 60;

                MilliSeconds2 = (int) (UpdateTime2 % 1000);

                if(Seconds2<15){lapTime.setTextColor(Color.rgb( 92, 254, 0));}
                if(Seconds2>15){lapTime.setTextColor(Color.rgb( 255, 0, 4));}
                if(Seconds2<=2){numofClick++;} else{numofClick=1;}
                if(numofClick==3){pause.performClick();reset.performClick();lapTime.setText("0.0");}
                else{

                    lapTime.setText(""
                            + String.format("%01d", Seconds2) + "."
                            + String.format("%01d", MilliSeconds2/100));

                    timeR= "("+Integer.toString(Lap-1)+")-{"+String.format("%01d", Seconds2) + "." + String.format("%01d", MilliSeconds2/100)+"}";
                    if(Lap>1) {
                        list.add(timeR);

                        test.setText(list.toString());
                    }

                }
                StartTime2=SystemClock.uptimeMillis();
            }
        });




        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);

            }
        });

    }

    public  void AddData() {
        yesButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertData(date,
                                Integer.toString(lastLap-2), lastFullTime,
                                test.getText().toString() );
                        if(isInserted == true)
                            Toast.makeText(RunActivity.this,"Data Inserted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(RunActivity.this,"Data not Inserted",Toast.LENGTH_LONG).show();

                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                    }
                }
        );

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            start.setText(String.valueOf(numofClick));

            nextlap.performClick();
            return true;
        }

        else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            fullTime.setText("" + Minutes + "."
                    + String.format("%02d", Seconds) + "."
                    + String.format("%03d", MilliSeconds));

            handler.postDelayed(this, 0);
        }

    };

}