package com.karas.tracklaptime.subactivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.karas.tracklaptime.MainActivity;
import com.karas.tracklaptime.R;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity {
    private int numOfLaps = 4;
    private EditText numOfLapsEditText;
    private Button incrementLapsButton;
    private Button decrementLapsButton;
    private Switch switchLapRange;
    private Switch switchTimeRange;
    private LinearLayout linearLayoutLapsTime;
    private List<Double> timeLaps = new ArrayList<>();
    private TextView textViewForLaps;
    private Toolbar mainToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mainToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);

        Button backButton = findViewById(R.id.start_back_button);
        Button startButton = findViewById(R.id.start_start_button);

        switchLapRange = findViewById(R.id.switch_lap_range);
        switchTimeRange = findViewById(R.id.switch_time_range);

        incrementLapsButton = findViewById(R.id.button3);
        incrementLapsButton.setVisibility(View.GONE);

        decrementLapsButton = findViewById(R.id.button4);
        decrementLapsButton.setVisibility(View.GONE);

        numOfLapsEditText = findViewById(R.id.editTextNumber4);
        numOfLapsEditText.setText(String.valueOf(this.numOfLaps));
        numOfLapsEditText.setVisibility(View.GONE);

        switchTimeRange.setVisibility(View.GONE);

        linearLayoutLapsTime = findViewById(R.id.ll_laps_time);

        switchTimeRange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addEditTexts();
                    linearLayoutLapsTime.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutLapsTime.removeAllViews();
                    linearLayoutLapsTime.setVisibility(View.GONE);
                }
            }
        });

        switchLapRange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    incrementLapsButton.setVisibility(View.VISIBLE);
                    decrementLapsButton.setVisibility(View.VISIBLE);
                    numOfLapsEditText.setVisibility(View.VISIBLE);
                    switchTimeRange.setVisibility(View.VISIBLE);
                } else {
                    incrementLapsButton.setVisibility(View.GONE);
                    decrementLapsButton.setVisibility(View.GONE);
                    numOfLapsEditText.setVisibility(View.GONE);
                    switchTimeRange.setVisibility(View.GONE);
                }

            }
        });

        incrementLapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementNumOfLapsAndUpdateEditText();
            }
        });

        decrementLapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementNumOfLapsAndUpdateEditText();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), RunActivity.class);

                if (switchLapRange.isChecked()) {
                    i.putExtra("LAP", numOfLaps);
                }

                if (switchTimeRange.isChecked()) {
                    i.putExtra("TIME", 1);
                }
                if (!timeLaps.isEmpty()) {
                    double[] target = new double[timeLaps.size()];
                    for (int j = 0; j < target.length; j++) {
                        target[j] = timeLaps.get(j);
                    }
                    i.putExtra("LAPSTIME", target);
                }
                startActivity(i);
            }
        });
    }

    public void incrementNumOfLapsAndUpdateEditText() {
        this.numOfLaps++;
        this.numOfLapsEditText.setText(String.valueOf(numOfLaps));
    }

    public void decrementNumOfLapsAndUpdateEditText() {
        if (numOfLaps > 1) {
            this.numOfLaps--;
            this.numOfLapsEditText.setText(String.valueOf(numOfLaps));
        }
    }

    public void addEditTexts() {
        initLapsTime();
        for (int i = 0; i < numOfLaps; i++) {


            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            TextView indexOfExpectedTimeOfLap = new TextView(this);
            indexOfExpectedTimeOfLap.setId(50 + i);
            indexOfExpectedTimeOfLap.setText(String.valueOf(i + 1));

            final Button bplus = new Button(this);
            bplus.setId(100 + i);
            bplus.setText("+");
            bplus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = bplus.getId();
                    id = id % 100;

                    timeLaps.set(id, timeLaps.get(id) + 0.1);
                    textViewForLaps = findViewById(id + 300);

                    textViewForLaps.setText(
                            String.format("%-10.1f%n", timeLaps.get(id)));

                }
            });

            final Button bminus = new Button(this);
            bminus.setId(200 + i);
            bminus.setText("-");
            bminus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = bminus.getId();
                    id = id % 200;

                    timeLaps.set(id, timeLaps.get(id) - 0.1);
                    textViewForLaps = findViewById(id + 300);

                    textViewForLaps.setText(
                            String.format("%-10.1f%n", timeLaps.get(id)));
                }
            });

            TextView expectedTimeOfLapTextView = new TextView(this);
            expectedTimeOfLapTextView.setId(300 + i);
            expectedTimeOfLapTextView.setText(String.valueOf(timeLaps.get(i)));
            expectedTimeOfLapTextView.setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
            );
            linearLayout.addView(indexOfExpectedTimeOfLap);
            linearLayout.addView(bplus);
            linearLayout.addView(expectedTimeOfLapTextView);
            linearLayout.addView(bminus);

            linearLayoutLapsTime.addView(linearLayout);
        }
    }

    public void initLapsTime() {
        if (numOfLaps == 4) {
            timeLaps.add(5.0);
            timeLaps.add(4.0);
            timeLaps.add(4.5);
            timeLaps.add(5.5);
        }

        if (numOfLaps > 4) {
            timeLaps.add(21.0);
            for (int i = 1; i < numOfLaps - 1; i++) {
                timeLaps.add(17.5);
            }
            timeLaps.add(16.5);
        }

        if (numOfLaps == 3) {
            timeLaps.add(21.0);
            timeLaps.add(18.5);
            timeLaps.add(19.5);
        }

        if (numOfLaps == 2) {
            timeLaps.add(1.0);
            timeLaps.add(18.5);
        }

        if (numOfLaps == 1) {
            timeLaps.add(20.5);
        }

    }

}
