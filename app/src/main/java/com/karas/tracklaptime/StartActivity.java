package com.karas.tracklaptime;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import org.w3c.dom.Text;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button backButton = findViewById(R.id.start_back_button);
        Button startButton = findViewById(R.id.start_start_button);

        CheckBox isSetLat = findViewById(R.id.are_set_laps_ch);
        CheckBox isByk = findViewById(R.id.is_byk_ch);

        final CheckBox checkBox = new CheckBox(this);



        isByk.setOnClickListener(new View.OnClickListener(){


          @Override
          public void onClick(View v) {

              checkBox.setText("siema");
              setContentView(checkBox);
          }
      }

        );

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
                startActivity(i);

            }
        });

    }

}
