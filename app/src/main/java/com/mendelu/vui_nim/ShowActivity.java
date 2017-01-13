package com.mendelu.vui_nim;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

/**
 * Created by Jakub on 06.06.2016.
 *
 * Class demonstrate list of player's game from Archive (database).
 * Every row contains name, result, number of beans and date.
 */
public class ShowActivity extends AppCompatActivity {

    TextView name,result,number,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        name = (TextView)findViewById(R.id.textViewName);
        result = (TextView)findViewById(R.id.textViewResult);
        number = (TextView)findViewById(R.id.textViewNumber);
        date = (TextView)findViewById(R.id.textViewDate);

        Intent intent = getIntent();
        if (null != intent) {
            String nameString = intent.getStringExtra("NAME");
            String resultString = intent.getStringExtra("RESULT");
            String numberString = intent.getStringExtra("NUMBER");
            String dateString = intent.getStringExtra("DATE");
            name.setText("Name: " + nameString);
            result.setText("Result: " + resultString);
            number.setText("Number of beans: " + numberString);
            date.setText("Date: " + dateString);

            name.setTextColor(Color.WHITE);
            result.setTextColor(Color.WHITE);
            number.setTextColor(Color.WHITE);
            date.setTextColor(Color.WHITE);
        }
    }

    /**
     * Created menu including a popup window for extension.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
