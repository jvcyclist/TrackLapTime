package com.karas.tracklaptime;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.DoubleStream;


public class RunActivity extends AppCompatActivity{
    DatabaseHelper myDb;
    TextView fullTime,lapTime,numOfLap,test;
    Button start, pause, reset ,nextlap,noButton,yesButton;
    Handler handler,handler2;
    TrackTimeService trackTimeService;

    List<String> list = new LinkedList<String>();
    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
    String date;
    TextView quest_textView;
    List<Double> lapsTime = new ArrayList<>();
    int flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        flag = getIntent().getIntExtra("TIME",0);
        if (flag == 1) {
            double[] timesLapsdoubleArray = getIntent().getDoubleArrayExtra("LAPSTIME");
            for (double tl : timesLapsdoubleArray){
                lapsTime.add(Double.valueOf(tl));
            }
        }



        trackTimeService = new TrackTimeService();
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
                trackTimeService.setStartTime(SystemClock.uptimeMillis());
                trackTimeService.setStartTime2(SystemClock.uptimeMillis());
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
                trackTimeService.addTimeToPauseTimeBuff();
                handler.removeCallbacks(runnable);
                reset.setEnabled(true);
                nextlap.setEnabled(false);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trackTimeService.reset();
                nextlap.setEnabled(false);
               trackTimeService.setLastFullTime(fullTime.getText().toString());
                fullTime.setText("00:00:00");
                lapTime.setText("0.0");
                numOfLap.setText("Lap: "+Integer.toString(trackTimeService.getLap()));
                test.setVisibility(View.VISIBLE);
                yesButton.setVisibility(View.VISIBLE);
                noButton.setVisibility(View.VISIBLE);
                quest_textView.setVisibility(View.VISIBLE);

            }
        });

        nextlap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trackTimeService.incrementLaps();
                numOfLap.setText("Lap: "+Integer.toString(trackTimeService.getLap()));
                trackTimeService.incrementNumofClicks();
                if(trackTimeService.getNumofClick() == 1) {
                    start.performClick();
                }
                trackTimeService.setUpdateTime2(
                        SystemClock.uptimeMillis()-trackTimeService.getStartTime2());
                trackTimeService.setSeconds2((int) trackTimeService.getUpdateTime2() / 1000);
                trackTimeService.setMinutes2(trackTimeService.getSeconds2() / 60);
                trackTimeService.setSeconds2Mod60();
                trackTimeService.setMilliSeconds2(
                        (int) (trackTimeService.getUpdateTime2() % 1000));

                if(!isTimeInRage(trackTimeService.getSeconds2(),lapsTime.get(trackTimeService.getLap()-1))) {
                    lapTime.setTextColor(Color.rgb( 92, 254, 0));
                }else  {
                    lapTime.setTextColor(Color.rgb( 255, 0, 4));
                }
                if(trackTimeService.getSeconds2() <= 2) {
                    trackTimeService.incrementNumofClicks();
                } else{trackTimeService.setNumofClick(1);
                }
                if(trackTimeService.getNumofClick() == 3){
                    pause.performClick();reset.performClick();
                    lapTime.setText("0.0");
                } else{
                    lapTime.setText(""
                            + String.format("%01d", trackTimeService.getSeconds2()) + "."
                            + String.format("%01d", trackTimeService.getMilliSeconds2()/100));
                    trackTimeService.setTimeR("("+Integer.toString(trackTimeService.getLap()-1)+")-{"+String.format("%01d", trackTimeService.getSeconds2()) + "." + String.format("%01d", trackTimeService.getMilliSeconds2()/100)+"}");
                    if(trackTimeService.getLap() > 1) {
                        list.add(trackTimeService.getTimeR());
                        test.setText(list.toString());
                    }
                }
                trackTimeService.setStartTime2(SystemClock.uptimeMillis());
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
                                Integer.toString(trackTimeService.getLastLap()-2), trackTimeService.getLastFullTime(),
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

    public boolean isTimeInRage(double time, double requiredTime){
        double gap = 1.0;
        if (requiredTime+gap <= time) {
            return true;
        }
        else {
            return false;
        }
    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {

     //   if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            start.setText(String.valueOf(trackTimeService.numofClick));

            nextlap.performClick();
            return true;
        }

        else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            trackTimeService.initialize();

            fullTime.setText("" + trackTimeService.getMinutes() + "."
                    + String.format("%02d", trackTimeService.getSeconds()) + "."
                    + String.format("%03d", trackTimeService.getMilliSeconds()));

            handler.postDelayed(this, 0);
        }

    };





}