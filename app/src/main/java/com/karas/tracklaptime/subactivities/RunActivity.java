package com.karas.tracklaptime.subactivities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;

import android.widget.Toast;

import com.karas.tracklaptime.MainActivity;
import com.karas.tracklaptime.R;
import com.karas.tracklaptime.utils.DatabaseHelper;
import com.karas.tracklaptime.utils.FileSaver;
import com.karas.tracklaptime.utils.ResultFileMapper;
import com.karas.tracklaptime.utils.TrackTimeService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;


public class RunActivity extends AppCompatActivity{
    DatabaseHelper myDb;
    TextView fullTime,lapTime,numOfLap,test;
    Button start, pause, reset ,nextlap,noButton,yesButton;
    Handler handler,handler2;
    TrackTimeService trackTimeService;
    FileSaver fileSaver;

    int amountOfRounds;
    List<String> listTimeR = new LinkedList<String>();
    DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
    String date;
    TextView quest_textView;
    List<Double> lapsTime = new ArrayList<>();
    int isRangeLapTimeFlag;
    ResultFileMapper resultFileMapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        trackTimeService = new TrackTimeService();
        resultFileMapper = new ResultFileMapper();
        fileSaver = new FileSaver();

        amountOfRounds = getIntent().getIntExtra("LAP",0);
        isRangeLapTimeFlag = getIntent().getIntExtra("TIME",0);

        if (isRangeLapTimeFlag == 1) {
            double[] timesLapDoubleArray = getIntent().getDoubleArrayExtra("LAPSTIME");
            for (double tl : timesLapDoubleArray){
                lapsTime.add(Double.valueOf(tl));
            }
        }

        myDb = new DatabaseHelper(this);
        fullTime = (TextView)findViewById(R.id.fullTime);
        lapTime = (TextView)findViewById(R.id.lapTime);
        numOfLap = (TextView)findViewById(R.id.numOfLap);
        test = (TextView) findViewById(R.id.test);
        test.setVisibility(View.INVISIBLE);
        date= dateFormat.format(Calendar.getInstance().getTime());
        start = (Button)findViewById(R.id.startbtn);
        pause = (Button)findViewById(R.id.stopbtn);
        reset = (Button)findViewById(R.id.resetbtn);
        nextlap = (Button)findViewById(R.id.nextLap);


        if (amountOfRounds > 0) {
            trackTimeService.setCurrentLap(amountOfRounds);
            numOfLap.setText("Lap: " + trackTimeService.getCurrentLap());
        }

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

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listTimeR.clear();
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
                numOfLap.setText("Lap: "+Integer.toString(trackTimeService.getCurrentLap()));
                test.setVisibility(View.VISIBLE);
                yesButton.setVisibility(View.VISIBLE);
                noButton.setVisibility(View.VISIBLE);
                quest_textView.setVisibility(View.VISIBLE);

            }
        });

        nextlap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amountOfRounds > 0) {
                    trackTimeService.decrementLaps();
                } else {
                    trackTimeService.incrementLaps();
                }

                numOfLap.setText("Lap: "+Integer.toString(trackTimeService.getCurrentLap()));
                trackTimeService.incrementNumofClicks();
                if(trackTimeService.getNumofClick() == 1) {
                    start.performClick();
                }
                trackTimeService.setUpdateTime2(
                        SystemClock.uptimeMillis() - trackTimeService.getStartTime2());
                trackTimeService.setSeconds2((int) trackTimeService.getUpdateTime2() / 1000);
                trackTimeService.setMinutes2(trackTimeService.getSeconds2() / 60);
                trackTimeService.setSeconds2Mod60();
                trackTimeService.setMilliSeconds2(
                (int) (trackTimeService.getUpdateTime2() % 1000));

                if(trackTimeService.getIteration()>1) {
                    if (isRangeLapTimeFlag == 1) {
                        if (!isTimeInRage(
                                trackTimeService.getSeconds2(),
                                lapsTime.get(trackTimeService.getIteration() - 2))) {
                            lapTime.setTextColor(Color.rgb(92, 254, 0));
                        } else {
                            lapTime.setTextColor(Color.rgb(255, 0, 4));
                        }
                    }
                }
                if(amountOfRounds > 0){
                    if(trackTimeService.getSeconds2() <= 2) {
                        trackTimeService.incrementNumofClicks();

                    } else{trackTimeService.setNumofClick(1);
                    }
                    if((trackTimeService.getNumofClick() == 3)||(trackTimeService.getCurrentLap() == -1)){
                        if(amountOfRounds >0){

                            lapTime.setText(""
                                    + String.format("%01d", trackTimeService.getSeconds2()) + "."
                                    + String.format("%01d", trackTimeService.getMilliSeconds2()/100));
                            if(trackTimeService.getIteration()>1){ resultFileMapper.addGivenResult(Double.valueOf(lapTime.getText().toString()));}
                            trackTimeService.setTimeR("("+Integer.toString(amountOfRounds - 1 - trackTimeService.getCurrentLap())
                                    + ")-{"+String.format("%01d", trackTimeService.getSeconds2())
                                    + "." + String.format("%01d", trackTimeService.getMilliSeconds2()/100)
                                    +"}");
                            if(trackTimeService.getCurrentLap() > -2){
                                listTimeR.remove(0);
                                listTimeR.add(trackTimeService.getTimeR());
                                test.setText(listTimeR.toString());
                            }

                        }
                        pause.performClick()
                        ;reset.performClick();
                        lapTime.setText("0.0");
                    } else{
                        lapTime.setText(""
                                + String.format("%01d", trackTimeService.getSeconds2()) + "."
                                + String.format("%01d", trackTimeService.getMilliSeconds2()/100));

                        if(trackTimeService.getIteration()>1){ resultFileMapper.addGivenResult(Double.valueOf(lapTime.getText().toString()));}
                        trackTimeService.setTimeR("("+Integer.toString(amountOfRounds - 1 - trackTimeService.getCurrentLap())
                                + ")-{"+String.format("%01d", trackTimeService.getSeconds2())
                                + "." + String.format("%01d", trackTimeService.getMilliSeconds2()/100)
                                +"}");
                        if(trackTimeService.getCurrentLap() > -2){
                            listTimeR.add(trackTimeService.getTimeR());
                            test.setText(listTimeR.toString());
                        }
                    }
                    trackTimeService.setStartTime2(SystemClock.uptimeMillis());

                }
                else{
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
                    trackTimeService.setTimeR("("+Integer.toString(trackTimeService.getCurrentLap()-1)+")-{"+String.format("%01d", trackTimeService.getSeconds2()) + "." + String.format("%01d", trackTimeService.getMilliSeconds2()/100)+"}");
                    if(trackTimeService.getCurrentLap() > 1) {
                        listTimeR.add(trackTimeService.getTimeR());
                        test.setText(listTimeR.toString());
                    }
                }
                trackTimeService.setStartTime2(SystemClock.uptimeMillis());
            }
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
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

                        resultFileMapper.setFullTime(trackTimeService.getLastFullTime());
                        if(lapsTime.size()>0){
                        resultFileMapper.setExpectedResults(lapsTime);
                        }
                        //fileSaver.writeFileOnInternalStorage(getApplicationContext(), , resultFileMapper.getResultsAsCsvContent());
                        fileSaver.writeToFile(resultFileMapper.getFileName(),resultFileMapper.getResultsAsCsvContent(), getApplicationContext());
                        resultFileMapper.getResultsAsCsvContent();



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
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_HEADSETHOOK) {
            start.setText(String.valueOf(trackTimeService.getNumofClick()));

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