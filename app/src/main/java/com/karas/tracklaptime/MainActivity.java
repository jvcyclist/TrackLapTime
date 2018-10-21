package com.karas.tracklaptime;

import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.KeyEvent.Callback;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;

import android.widget.ArrayAdapter;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity{



    static int counter=0;
    TextView fullTime,lapTime,numOfLap ;

    Button start, pause, reset ,nextlap;

    long MillisecondTime, StartTime, TimeBuff, UpdateTime,MillisecondTime2,StartTime2,TimeBuff2,UpdateTime2 = 0L ;

    Handler handler,handler2;

    int Seconds, Minutes, MilliSeconds,Lap=1 ;
    int Seconds2,Minutes2,MilliSeconds2;

    ArrayAdapter<String> adapter ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fullTime = (TextView)findViewById(R.id.fullTime);
        lapTime = (TextView)findViewById(R.id.lapTime);
        numOfLap = (TextView)findViewById(R.id.numOfLap);
        start = (Button)findViewById(R.id.startbtn);
        pause = (Button)findViewById(R.id.stopbtn);
        reset = (Button)findViewById(R.id.resetbtn);
        nextlap = (Button)findViewById(R.id.nextLap);
        nextlap.setVisibility(View.INVISIBLE);

        handler = new Handler() ;
        handler2 = new Handler();


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StartTime = SystemClock.uptimeMillis();
                StartTime2 = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);

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
                Lap=1;

                MillisecondTime2 = 0L ;
                StartTime2 = 0L ;
                TimeBuff2 = 0L ;
                UpdateTime2 = 0L ;
                Seconds2 = 0 ;
                Minutes2 = 0 ;



                nextlap.setEnabled(false);


                fullTime.setText("00:00:00");
                lapTime.setText("0.00");
                numOfLap.setText("Lap: "+Integer.toString(Lap));


        }
        });


        nextlap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Lap = Lap + 1;
            numOfLap.setText("Lap: "+Integer.toString(Lap));





                UpdateTime2=SystemClock.uptimeMillis()-StartTime2;

                Seconds2 = (int) (UpdateTime2 / 1000);

                Minutes2 = Seconds2 / 60;

                Seconds2 = Seconds2 % 60;

                MilliSeconds2 = (int) (UpdateTime2 % 1000);

                if(Seconds2<15){lapTime.setTextColor(Color.rgb( 92, 254, 0));}
                if(Seconds2>15){lapTime.setTextColor(Color.rgb( 255, 0, 4));}

                lapTime.setText(""
                        + String.format("%01d", Seconds2) + "."
                        + String.format("%01d", MilliSeconds2/100));
                StartTime2=SystemClock.uptimeMillis();
            }
        });


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            start.setText("Counter : " + String.valueOf(++counter));
            Toast.makeText(this, "Volume Down Pressed", Toast.LENGTH_SHORT)
                    .show();
            return true;
        }*/
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            start.setText("Counter : " + String.valueOf(--counter));
            Toast.makeText(this, "Volume Up Pressed", Toast.LENGTH_SHORT)
                    .show();
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