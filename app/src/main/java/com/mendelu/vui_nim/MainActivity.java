package com.mendelu.vui_nim;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Jakub on 21.05.2016.
 *
 * Class represents back-end for introduction view into the application.
 */
public class MainActivity extends AppCompatActivity {
    private final int numberDefault = 12;
    private int number = numberDefault;
    private int begins = 1;
    String[] s = { "Player ", "Computer", "Random " };

    /**
     * Constructor connect this class with front-end layout.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    /**
     * Method is called after click on button New Game and it shows new activity game.
     */
    public void clickNewGame(View view) {
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("number",number);
        i.putExtra("begins",begins);
        startActivity(i);
    }

    /**
     * Method is called after click on button Settings and it shows new activity settings.
     */
    public void clickSettings(View view) {
        showMessageNumber();
    }


    /**
     * Method is called after click on button Statistics and it shows new activity statistics.
     */
    public void clickStatistics(View view) {
        Intent i = new Intent(this, StatisticsActivity.class);
        startActivity(i);
    }

    /**
     * Method is called after click on button Exit and it close application.
     */
    public void clickExit(View view) {
        System.exit(0);
    }


    /**
     * Function allows to set number of beans if the typed string contains only numbers.
     * If we didn't type any string, then it will be 12.
     */
    private void showMessageNumber(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Settings");
        builder.setMessage("Enter the number of beans(6-35)");
        final EditText typeNumberEditText = new EditText(this);
        builder.setView(typeNumberEditText, 20, 0, 20, 0);
        typeNumberEditText.setHint(String.valueOf(number));

        builder.setPositiveButton("Continue",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (isInteger(typeNumberEditText.getText().toString())){
                            if ((Integer.parseInt(typeNumberEditText.getText().toString()) > 3) && (Integer.parseInt(typeNumberEditText.getText().toString()) < 36)) {
                                number = Integer.parseInt(typeNumberEditText.getText().toString());
                            } else {
                                number = numberDefault;
                            }
                        }else{
                            number = numberDefault;
                        }
                        showMessageWho();
                        Toast.makeText(getApplicationContext(), "Set", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }
        );
        builder.show();
    }

    /**
     * Function allows to set who (player, computer, random) will start the game.
     * If we didn't choose any one, then will be chosen player.
     */
    private void showMessageWho(){
        final ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, s);

        final Spinner sp = new Spinner(this);
        sp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        sp.setAdapter(adp);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Settings");
        builder.setMessage("Enter who begins");
        builder.setView(sp);
        builder.setPositiveButton("Finish",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if(sp.getSelectedItem().toString().equals("Player")){
                            begins = 1;
                        }
                        if(sp.getSelectedItem().toString().equals("Computer")){
                            begins = 0;
                        }
                        if(sp.getSelectedItem().toString().equals("Random")){
                            Random r = new Random();
                            int i1 = r.nextInt(2) + 1;
                            if(i1==1){
                                begins = 0;
                            }else{
                                begins = 1;
                            }
                        }
                        Toast.makeText(getApplicationContext(), "Set", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }
        );
        builder.show();

    }

    /**
    * Method represents checks step by step if is typed string really integer.
    */
    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

}
