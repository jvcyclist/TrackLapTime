package com.karas.tracklaptime.subactivities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.util.Locale;


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
    private Button doNotSaveResultsButton;
    private Button saveResultsButton;
    private Handler handler;
    private TrackTimeService trackTimeService;
    private FileSaver fileSaver;
    private int amountOfRounds;

    private final List<String> listTimeR = new LinkedList<>();
    private final DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
    private String date;
    private TextView questTextView;
    private final List<Double> lapsTime = new ArrayList<>();
    private int isRangeLapTimeFlag;
    private ResultFileMapper resultFileMapper;
    private double tolerancy;

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
            tolerancy = getIntent().getDoubleExtra("TOLERANCY", 0);
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
            numOfLap.setText(String.format("Lap: %s", trackTimeService.getCurrentLap()));
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
                saveResultsButton.setVisibility(View.INVISIBLE);
                doNotSaveResultsButton.setVisibility(View.INVISIBLE);
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
                saveResultsButton.setVisibility(View.VISIBLE);
                doNotSaveResultsButton.setVisibility(View.VISIBLE);
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
                    Log.d("TIMELAP_RECORDED",     Double.valueOf(String.format("%01d", trackTimeService.getSeconds2()) + "." + String.format("%01d", trackTimeService.getMilliSeconds2() / 100)).toString());
                    Log.d("TIMELAP_EXPECTED",    lapsTime.get(trackTimeService.getIteration() - 2) + " seconds" );
                    Log.d("TIMELAP_TOLERANCY",   tolerancy +"");
                    if (isTimeInRage(
                            Double.valueOf(String.format("%01d", trackTimeService.getSeconds2()) + "."
                                    + String.format("%01d", trackTimeService.getMilliSeconds2() / 100)),
                            lapsTime.get(trackTimeService.getIteration() - 2))
                    ) {
                        lapTime.setTextColor(Color.rgb(255, 0, 4));
                    } else {
                        lapTime.setTextColor(Color.rgb(92, 254, 0));
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

        doNotSaveResultsButton.setOnClickListener(new View.OnClickListener() {
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
        fullTime = findViewById(R.id.full_time_text);
        lapTime = findViewById(R.id.lat_time_text);
        numOfLap = findViewById(R.id.number_of_lap_text);
        test = findViewById(R.id.save_result_background_view);
        test.setVisibility(View.INVISIBLE);

        start = findViewById(R.id.start_ride_button);
        pause = findViewById(R.id.stop_ride_button);
        reset = findViewById(R.id.reset_ride_button);
        nextLap = findViewById(R.id.next_lap_button);
        doNotSaveResultsButton = findViewById(R.id.do_not_save_results_button);
        saveResultsButton = findViewById(R.id.save_results_button);
        questTextView = findViewById(R.id.save_result_text_view);

        nextLap.setVisibility(View.INVISIBLE);
        start.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.INVISIBLE);
        reset.setVisibility(View.INVISIBLE);
        saveResultsButton.setVisibility(View.INVISIBLE);
        doNotSaveResultsButton.setVisibility(View.INVISIBLE);
        questTextView.setVisibility(View.INVISIBLE);
        handler = new Handler();
    }

    private void addData() {
        saveResultsButton.setOnClickListener(

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

    private boolean isTimeInRage(double time, double requiredTime) {
        return (requiredTime + tolerancy) <= time;
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