package com.karas.tracklaptime.subactivities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
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


public class RunActivity extends AppCompatActivity {
    private static final String DATE_TIME_FORMAT = "EEE, d MMM yyyy, HH:mm";

    private DatabaseHelper myDb;
    private TextView fullTime;
    private TextView lapTime;
    private TextView numOfLap;
    private TextView test;
    private Button start;
    private Button pause;
    private Button reset;
    private Button nextLap;
    private Button noButton;
    private Button yesButton;
    private Handler handler;
    private TrackTimeService trackTimeService;
    private FileSaver fileSaver;
    private int amountOfRounds;

    private final List<String> listTimeR = new LinkedList<>();
    private final DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
    private String date;
    private TextView questTextView;
    private final List<Double> lapsTime = new ArrayList<>();
    private int isRangeLapTimeFlag;
    private ResultFileMapper resultFileMapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewAndOrientation();

        trackTimeService = new TrackTimeService();
        resultFileMapper = new ResultFileMapper();
        fileSaver = new FileSaver();

        amountOfRounds = getIntent().getIntExtra("LAP", 0);
        isRangeLapTimeFlag = getIntent().getIntExtra("TIME", 0);

        if (isRangeLapTimeFlag == 1) {
            double[] timesLapDoubleArray = getIntent().getDoubleArrayExtra("LAPSTIME");
            for (double tl : timesLapDoubleArray) {
                lapsTime.add(tl);
            }
        }


        myDb = new DatabaseHelper(this);
        date = dateFormat.format(Calendar.getInstance().getTime());

        initializeViews();

        if (amountOfRounds > 0) {
            trackTimeService.setCurrentLap(amountOfRounds);
            numOfLap.setText("Lap: " + trackTimeService.getCurrentLap());
        }

        addData();

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
                nextLap.setEnabled(true);
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trackTimeService.addTimeToPauseTimeBuff();
                handler.removeCallbacks(runnable);
                reset.setEnabled(true);
                nextLap.setEnabled(false);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trackTimeService.reset();
                nextLap.setEnabled(false);
                trackTimeService.setLastFullTime(fullTime.getText().toString());
                fullTime.setText("00:00:00");
                lapTime.setText("0.0");
                numOfLap.setText("Lap: " + trackTimeService.getCurrentLap());
                test.setVisibility(View.VISIBLE);
                yesButton.setVisibility(View.VISIBLE);
                noButton.setVisibility(View.VISIBLE);
                questTextView.setVisibility(View.VISIBLE);

            }
        });

        nextLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amountOfRounds > 0) {
                    trackTimeService.decrementLaps();
                } else {
                    trackTimeService.incrementLaps();
                }

                numOfLap.setText("Lap: " + trackTimeService.getCurrentLap());
                trackTimeService.incrementNumOfClicks();
                if (trackTimeService.getNumofClick() == 1) {
                    start.performClick();
                }
                trackTimeService.setUpdateTime2(
                        SystemClock.uptimeMillis() - trackTimeService.getStartTime2());
                trackTimeService.setSeconds2((int) trackTimeService.getUpdateTime2() / 1000);
                trackTimeService.setMinutes2(trackTimeService.getSeconds2() / 60);
                trackTimeService.setSeconds2Mod60();
                trackTimeService.setMilliSeconds2(
                        (int) (trackTimeService.getUpdateTime2() % 1000));

                if (trackTimeService.getIteration() > 1 && (isRangeLapTimeFlag == 1)) {
                        if (!isTimeInRage(
                                trackTimeService.getSeconds2(),
                                lapsTime.get(trackTimeService.getIteration() - 2))) {
                            lapTime.setTextColor(Color.rgb(92, 254, 0));
                        } else {
                            lapTime.setTextColor(Color.rgb(255, 0, 4));
                        }

                }
                if (amountOfRounds > 0) {
                    if (trackTimeService.getSeconds2() <= 2) {
                        trackTimeService.incrementNumOfClicks();

                    } else {
                        trackTimeService.setNumofClick(1);
                    }
                    if ((trackTimeService.getNumofClick() == 3) || (trackTimeService.getCurrentLap() == -1)) {
                        if (amountOfRounds > 0) {

                            lapTime.setText(String.format("%01d", trackTimeService.getSeconds2()) + "."
                                    + String.format("%01d", trackTimeService.getMilliSeconds2() / 100));
                            if (trackTimeService.getIteration() > 1) {
                                resultFileMapper.addGivenResult(Double.valueOf(lapTime.getText().toString()));
                            }
                            trackTimeService.setTimeR("(" + (amountOfRounds - 1 - trackTimeService.getCurrentLap())
                                    + ")-{" + String.format("%01d", trackTimeService.getSeconds2())
                                    + "." + String.format("%01d", trackTimeService.getMilliSeconds2() / 100)
                                    + "}");
                            if (trackTimeService.getCurrentLap() > -2) {
                                listTimeR.remove(0);
                                listTimeR.add(trackTimeService.getTimeR());
                                test.setText(listTimeR.toString());
                            }

                        }
                        pause.performClick()
                        ;
                        reset.performClick();
                        lapTime.setText("0.0");
                    } else {
                        lapTime.setText(
                                String.format("%01d", trackTimeService.getSeconds2()) + "."
                                + String.format("%01d", trackTimeService.getMilliSeconds2() / 100));

                        if (trackTimeService.getIteration() > 1) {
                            resultFileMapper.addGivenResult(Double.valueOf(lapTime.getText().toString()));
                        }
                        trackTimeService.setTimeR("(" + (amountOfRounds - 1 - trackTimeService.getCurrentLap())
                                + ")-{" + String.format("%01d", trackTimeService.getSeconds2())
                                + "." + String.format("%01d", trackTimeService.getMilliSeconds2() / 100)
                                + "}");
                        if (trackTimeService.getCurrentLap() > -2) {
                            listTimeR.add(trackTimeService.getTimeR());
                            test.setText(listTimeR.toString());
                        }
                    }
                    trackTimeService.setStartTime2(SystemClock.uptimeMillis());

                } else {
                    if (trackTimeService.getSeconds2() <= 2) {
                        trackTimeService.incrementNumOfClicks();
                    } else {
                        trackTimeService.setNumofClick(1);
                    }
                    if (trackTimeService.getNumofClick() == 3) {
                        pause.performClick();
                        reset.performClick();
                        lapTime.setText("0.0");
                    } else {
                        lapTime.setText(
                                String.format("%01d", trackTimeService.getSeconds2()) + "."
                                + String.format("%01d", trackTimeService.getMilliSeconds2() / 100)
                        );
                        trackTimeService.setTimeR("(" + (trackTimeService.getCurrentLap() - 1) + ")-{" + String.format("%01d", trackTimeService.getSeconds2()) + "." + String.format("%01d", trackTimeService.getMilliSeconds2() / 100) + "}");
                        if (trackTimeService.getCurrentLap() > 1) {
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

    private void setupViewAndOrientation() {
        setContentView(R.layout.activity_run);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void initializeViews() {
        fullTime = findViewById(R.id.fullTime);
        lapTime = findViewById(R.id.lapTime);
        numOfLap = findViewById(R.id.numOfLap);
        test = findViewById(R.id.test);
        test.setVisibility(View.INVISIBLE);

        start = findViewById(R.id.startbtn);
        pause = findViewById(R.id.stopbtn);
        reset = findViewById(R.id.resetbtn);
        nextLap = findViewById(R.id.nextLap);
        noButton = findViewById(R.id.run_no_button);
        yesButton = findViewById(R.id.run_yes_button);
        questTextView = findViewById(R.id.quest_textView);
        nextLap.setVisibility(View.INVISIBLE);
        start.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.INVISIBLE);
        reset.setVisibility(View.INVISIBLE);
        yesButton.setVisibility(View.INVISIBLE);
        noButton.setVisibility(View.INVISIBLE);
        questTextView.setVisibility(View.INVISIBLE);
        handler = new Handler();
    }

    public void addData() {
        yesButton.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertData(date,
                                Integer.toString(trackTimeService.getLastLap() - 2), trackTimeService.getLastFullTime(),
                                test.getText().toString());
                        if (isInserted)
                            Toast.makeText(RunActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(RunActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();

                        resultFileMapper.setFullTime(trackTimeService.getLastFullTime());
                        if (!lapsTime.isEmpty()) {
                            resultFileMapper.setExpectedResults(lapsTime);
                        }
                        fileSaver.writeToFile(resultFileMapper.getFileName(), resultFileMapper.getResultsAsCsvContent(), getApplicationContext());
                        resultFileMapper.getResultsAsCsvContent();

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }
                }
        );

    }

    public boolean isTimeInRage(double time, double requiredTime) {
        double gap = 1.0;
        return (requiredTime + gap) <= time;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_HEADSETHOOK) {
            start.setText(String.valueOf(trackTimeService.getNumofClick()));

            nextLap.performClick();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public Runnable runnable = new Runnable() {

        public void run() {
            trackTimeService.initialize();
            fullTime.setText(trackTimeService.getMinutes() + "."
                    + String.format("%02d", trackTimeService.getSeconds()) + "."
                    + String.format("%03d", trackTimeService.getMilliSeconds()));
            handler.postDelayed(this, 0);
        }

    };

}