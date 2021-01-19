package com.karas.tracklaptime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity {

    int numOfLaps = 0;
    EditText editText;
    Button incrementLapsButton;
    Button decrementLapsButton;
    Switch switchLapRange;
    Switch switchTimeRange;
    LinearLayout linearLayoutLapsTime;
    EditText editText2;
    List<Double> timeLaps = new ArrayList<>();
    TextView textViewForLaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button backButton = findViewById(R.id.start_back_button);
        Button startButton = findViewById(R.id.start_start_button);

        switchLapRange = findViewById(R.id.switch_lap_range);
        switchTimeRange = findViewById(R.id.switch_time_range);

        incrementLapsButton = findViewById(R.id.button3);
        incrementLapsButton.setVisibility(View.GONE);
       // button3.setEnabled(true);
        decrementLapsButton = findViewById(R.id.button4);
        decrementLapsButton.setVisibility(View.GONE);
      //  button4.setEnabled(true);
        editText = findViewById(R.id.editTextNumber4);
        editText.setText(String.valueOf(this.numOfLaps));
        editText.setVisibility(View.GONE);
        switchTimeRange.setVisibility(View.GONE);

        linearLayoutLapsTime = findViewById(R.id.ll_laps_time);

        editText2 = new EditText(this);


        switchTimeRange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addEditTexts();
                    linearLayoutLapsTime.setVisibility(View.VISIBLE);
                } else {
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
                    editText.setVisibility(View.VISIBLE);
                    switchTimeRange.setVisibility(View.VISIBLE);
                } else {
                    incrementLapsButton.setVisibility(View.GONE);
                    decrementLapsButton.setVisibility(View.GONE);
                    editText.setVisibility(View.GONE);
                    switchTimeRange.setVisibility(View.GONE);
                }

            }
        });



        incrementLapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increment();
            }
        });

        decrementLapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrement();
            }
        });





        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);

            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),RunActivity.class);

                if(switchLapRange.isChecked()){
                    i.putExtra("LAP", numOfLaps);
                }

                if (switchTimeRange.isChecked()){
                    i.putExtra("TIME", 1);
                }
                if (timeLaps.size() > 0) {
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

    public void increment() {
        this.numOfLaps++;
       this.editText.setText(String.valueOf(numOfLaps));
    }

    public void decrement() {
        this.numOfLaps--;
        this.editText.setText(String.valueOf(numOfLaps));
        Log.println(1,"xd","siema");

    }

    public void addEditTexts(){
        for (int i = 0;i < numOfLaps; i++)
        {   initLapsTime();

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);


            TextView textView = new TextView(this);
            textView.setId(50+i);
            textView.setText(String.valueOf(i+1));


            final Button bplus = new Button(this);
            bplus.setId(100+i);
            bplus.setText("+");
            bplus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = bplus.getId();
                    id = id % 100;

                    timeLaps.set(id,timeLaps.get(id)+0.1);
                    textViewForLaps = findViewById(id+300);

                    textViewForLaps.setText(
                            String.format("%-10.1f%n",timeLaps.get(id)));

                }
            });

            final Button bminus = new Button(this);
            bminus.setId(200+i);
            bminus.setText("-");
            bminus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = bminus.getId();
                    id = id % 200;

                    timeLaps.set(id,timeLaps.get(id)-0.1);
                    textViewForLaps = findViewById(id+300);

                    textViewForLaps.setText(
                            String.format("%-10.1f%n",timeLaps.get(id)));
                }
            });

            TextView textView1 = new TextView(this);
            textView1.setId(300+i);
            textView1.setText(String.valueOf(timeLaps.get(i)));
            textView1.setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT)
                           );
            linearLayout.addView(textView);
            linearLayout.addView(bplus);
            linearLayout.addView(textView1);
            linearLayout.addView(bminus);

           // linearLayout.setLayoutParams(new LinearLayout.LayoutParams
            // (LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT));

            linearLayoutLapsTime.addView(linearLayout);
        }
    }

    public void initLapsTime () {
        if (numOfLaps == 4){
            timeLaps.add(5.0);
            timeLaps.add(4.0);
            timeLaps.add(4.5);
            timeLaps.add(5.5);
        }

        if (numOfLaps > 4){
            timeLaps.add(21.0);
            for (int i = 1;i < numOfLaps; i++){
             timeLaps.add(17.5);
            }
            timeLaps.add(16.5);
        }
    }

}
