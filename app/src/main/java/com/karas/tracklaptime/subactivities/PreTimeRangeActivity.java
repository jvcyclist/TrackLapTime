package com.karas.tracklaptime.subactivities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.karas.tracklaptime.R;

import java.util.ArrayList;
import java.util.List;

public class PreTimeRangeActivity extends AppCompatActivity {

    private Button applyBatchButton;
    private Button goToActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_time_range);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        int numOfLaps = getIntent().getIntExtra("LAP", 0);
        applyBatchButton = findViewById(R.id.batch_apply_button);
        goToActivity = findViewById(R.id.start_activity);
        setVisibilityOfLapSetters(numOfLaps);
        applyBatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               TextView textView = findViewById(R.id.batch_time_text);
               double timeToSet = Double.valueOf(textView.getText().toString());

                LinearLayout linearLayout = findViewById(R.id.time_range_table);

                for (int i=2; i<=6; i++) {
                    TableRow tableRow = (TableRow)linearLayout.getChildAt(i);
                    for (int x=0; x<=7; x++) {
                        LinearLayout linearLayout1 = (LinearLayout) tableRow.getChildAt(x);
                        if (linearLayout1.getVisibility() == View.VISIBLE) {
                            TextView textViewLap = (TextView) linearLayout1.getChildAt(2);
                            textViewLap.setText(String.format("%.1f", timeToSet).replace(",", "."));
                        }
                    }
                }
            }
        });

        goToActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), RunActivity.class);


                getAllTimes(intent);
                intent.putExtra("TIME", 1);
                intent.putExtra("TOLERANCY", Double.valueOf(((TextView)findViewById(R.id.tolerance_text)).getText().toString()));
                startActivity(intent);
            }
        });

        //good one !








    }

    private void setVisibilityOfLapSetters(int numOfLaps) {

        int initialRowIndex = getInitialRowIndex(numOfLaps);


        ViewGroup viewGroup = findViewById(R.id.time_range_table);

        System.out.println("test");

        for (int i = initialRowIndex; i <= 6 ; i++) {
            TableRow tableRow = (TableRow)viewGroup.getChildAt(i);

            for (int x = 0; x <=7; x++) {
                LinearLayout linearLayout = (LinearLayout) tableRow.getChildAt(x);
                AppCompatTextView appCompatTextView = (AppCompatTextView)linearLayout.getChildAt(0);
                int lapNumber = Integer.valueOf(appCompatTextView.getText().toString());
                if (lapNumber > numOfLaps) {
                    linearLayout.setVisibility(View.INVISIBLE);
                }
            }

        }

    }

    private int getInitialRowIndex(int numOfLaps) {
        if (numOfLaps >= 4 && numOfLaps <= 10) return 2;
        if (numOfLaps >= 11 && numOfLaps <= 18) return 3;
        if (numOfLaps >= 19 && numOfLaps <= 26) return 4;
        if (numOfLaps >= 27 && numOfLaps <= 34) return 5;
        else return 6;
    }

    private void setRowsVisibility(int numOfLaps) {
        LinearLayout linearLayout = findViewById(R.id.time_range_table);


        int visibleRows = getCountOfVisibleRows(numOfLaps);

        for (int i=visibleRows+2; i<=6; i++) {
            TableRow tableRowWithTimes = (TableRow) linearLayout.getChildAt(i);
            tableRowWithTimes.setVisibility(View.INVISIBLE);
        }
    }

    private int getCountOfVisibleRows(int numOfLaps) {
        if (numOfLaps <=4) {
            return 1;
        }
        if (numOfLaps <=10) {
            return 1;
        }
        if (numOfLaps <=18) {
            return 2;
        }
        if (numOfLaps <=26) {
            return 3;
        }
        if (numOfLaps <=34) {
            return 4;
        }
        return 5;
    }

    public void increaseTolerance(View view) {
        TextView toleranceTextView = findViewById(R.id.tolerance_text);
        double currentTolerance = Double.valueOf(toleranceTextView.getText().toString());
        toleranceTextView.setText(String.format("%.1f", currentTolerance + 0.1).replace(",", "."));
    }

    public void decreaseTolerance(View view) {
        TextView toleranceTextView = findViewById(R.id.tolerance_text);
        double currentTolerance = Double.valueOf(toleranceTextView.getText().toString());
        if (currentTolerance > 0) {
            toleranceTextView.setText(String.format("%.1f", currentTolerance - 0.1).replace(",", "."));
        }

    }

    public void clickPlusTest(View view) {
        ViewParent parent = view.getParent().getParent();
        ViewGroup viewGroup = (ViewGroup) parent ;
        TextView textView = (TextView) viewGroup.getChildAt(2);

        double currentTime = Double.valueOf(textView.getText().toString());
        textView.setText(String.format("%.1f", currentTime + 0.1).replace(",", "."));

    }

    public void clickMinusTest(View view) {
        ViewParent parent = view.getParent().getParent();
        ViewGroup viewGroup = (ViewGroup) parent ;
        TextView textView = (TextView) viewGroup.getChildAt(2);

        double currentTime = Double.valueOf(textView.getText().toString());
        if (currentTime > 0) {
            textView.setText(String.format("%.1f", currentTime - 0.1).replace(",", "."));
        }
    }


    public void getAllTimes(Intent intent) {
        List<Double> times = new ArrayList<>();

        LinearLayout linearLayout = findViewById(R.id.time_range_table);

        // get two first times
        TableRow tableRowWithTimes = (TableRow) linearLayout.getChildAt(1);
        LinearLayout layout = (LinearLayout) tableRowWithTimes.getChildAt(1);

        for (int i=0; i<=1; i++) {
            LinearLayout linearLayout1 = (LinearLayout) layout.getChildAt(i);
            if (linearLayout1.getVisibility() == View.VISIBLE) {
                TextView textView = (TextView) linearLayout1.getChildAt(2);
                times.add(Double.valueOf(textView.getText().toString()));
            }
        }

        for (int i=2; i<=6; i++) {
            LinearLayout layout3 = (LinearLayout) linearLayout.getChildAt(i);
            for (int x=0; x<=7; x++) {
                LinearLayout linearLayout1 = (LinearLayout) layout3.getChildAt(x);
                if (linearLayout1.getVisibility() == View.VISIBLE) {
                    TextView textView = (TextView) linearLayout1.getChildAt(2);
                    times.add(Double.valueOf(textView.getText().toString()));
                }
            }
        }


        if (!times.isEmpty()) {
            double[] target = new double[times.size()];
            for (int j = 0; j < target.length; j++) {
                target[j] = times.get(j);
            }
            intent.putExtra("LAPSTIME", target);
            intent.putExtra("LAP", times.size());
        }

       Log.d("ALL TIMES", times.toString());
    }

    public void setUpLapNumbers() {
        List<Double> times = new ArrayList<>();

        LinearLayout linearLayout = findViewById(R.id.time_range_table);

        // get two first times
        TableRow tableRowWithTimes =  (TableRow)  linearLayout.getChildAt(1);
        LinearLayout layout = (LinearLayout) (ViewGroup)tableRowWithTimes.getChildAt(1);

        for (int i=0; i<=1; i++) {
            LinearLayout linearLayout1 = (LinearLayout) layout.getChildAt(i);
            TextView textView = (TextView) linearLayout1.getChildAt(0);
            textView.setText(i+1);
        }

        for (int i=2; i<=6; i++) {
            LinearLayout layout3 = (LinearLayout) linearLayout.getChildAt(i);
            for (int x=0; x<=7; x++) {
                LinearLayout linearLayout1 = (LinearLayout) layout3.getChildAt(i);
                TextView textView = (TextView) linearLayout1.getChildAt(0);
                textView.setText(x+1);
            }
        }

        Log.d("ALL TIMES", times.toString());

    }
}
