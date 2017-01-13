package com.mendelu.vui_nim;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import com.mendelu.vui_nim.db.Archive;
import com.mendelu.vui_nim.db.ArchiveOpenHelper;
/**
 * Created by Jakub on 06.06.2016.
 *
 * Activity is core of whole application.
 * Represents all back-end functions in the game.
 */
public class GameActivity extends AppCompatActivity {

    ArchiveOpenHelper aoh;
    public TextView amountTextView, logTextView;
    public ScrollView windowScrollView;
    public Button buttonOneBean, buttonTwoBeans;

    private int number, numberDefault,begin;
    private String log, Result;
    private boolean computer, gameOver;
    boolean show = false;

    /**
     * Constructor of new game connect activity with layout
     * and take parameters from settings, if they haven't been set,
     * then method set defaults.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        aoh = new ArchiveOpenHelper(this);
        amountTextView = (TextView)findViewById(R.id.amount_beans);
        logTextView = (TextView)findViewById(R.id.log_state);
        buttonOneBean =  (Button) findViewById(R.id.button_new_game);
        buttonTwoBeans =  (Button) findViewById(R.id.button_settings);

        log = logTextView.getText().toString();
        Result = "Draw";
        windowScrollView = ((ScrollView) findViewById(R.id.print));
        computer = false;
        gameOver = false;

        buttonOneBean.setTextColor(Color.WHITE);
        buttonTwoBeans.setTextColor(Color.WHITE);
        amountTextView.setTextColor(Color.WHITE);
        logTextView.setTextColor(Color.WHITE);


        amountTextView.setText(getString(R.string.amount_beans) + "\t " + number);
        Intent intent = getIntent();
        if (null != intent) {
            number = intent.getIntExtra("number",12);
            begin = intent.getIntExtra("begin",1);
            amountTextView.setText(getString(R.string.amount_beans) + "\t " + number);
        }
        if (begin==1){
            computer = false;
        }else{
            computer = true;
            goMachineTurn();
        }
        numberDefault = number;
        Toast.makeText(getApplicationContext(), "Good luck", Toast.LENGTH_SHORT).show();
    }

    /**
     * Created menu including a popup window for save and restart.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Method is situated situated in popup window and it save the state of the game into the database.
     * After define name function takes actual date time and send them by method insert from open helper
     * into the db with result of the game and number of beans.
     */
    public void save(MenuItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Saving");
        builder.setMessage("Enter your name");
        final EditText zadejJmenoEditText = new EditText(this);
        builder.setView(zadejJmenoEditText, 20, 0, 20, 0);
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
        final String formattedDate = df.format(c.getTime());

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        aoh.insert(new Archive(zadejJmenoEditText.getText().toString(), Result,String.valueOf(numberDefault), formattedDate));
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
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
     * Method is situated situated in popup window and it restart the game
     * by setting all parameters to default or changed by settings.
     */
    public void restart(MenuItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Really restart?");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        number = numberDefault;
                        amountTextView.setText(getString(R.string.amount_beans) + "\t " + number);
                        logTextView.setText("");
                        log = logTextView.getText().toString();
                        computer = false;
                        gameOver = false;
                        buttonOneBean.setEnabled(true);
                        buttonTwoBeans.setEnabled(true);
                        Toast.makeText(getApplicationContext(), "Restarted", Toast.LENGTH_SHORT).show();
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
     * Function is used for took one bean. It is used for player or computer.
     * Decrease number of beans and then it switch computer with player or vice versa.
     * Returns new state or final result of the game.
     */
    public void clickTookOne(View view){
        if(computer==false) {
            number -= 1;
            amountTextView.setText(getString(R.string.amount_beans) + "\t " + number);
            if (number == 0) {
                logTextView.setText(log + "\n Player took 1 bean \n" +
                        " ------------------------------------ \n GAME OVER \n" +
                        " The winner is Computer");
                buttonOneBean.setEnabled(false);
                buttonTwoBeans.setEnabled(false);
            } else {
                logTextView.setText(log + "\n Player took 1 bean");
                computer = true;
                goMachineTurn();
            }
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Wait please");
            builder.setMessage("Computer turn");
            builder.setNeutralButton("OK", null);
            builder.show();
        }
        log = logTextView.getText().toString();
        scrollDown();

    }

    /**
     * Function is used for took two beans. It is used for player or computer.
     * Check number of beans and then decrease them, after that it switch
     * computer with player or vice versa. Returns new state or final result of the game.
     */
    public void clickTookTwo(View view) {
        if(computer==false) {
            if (number != 1) {
                number -= 2;
                amountTextView.setText(getString(R.string.amount_beans) + "\t " + number);
                if (number == 0) {
                    logTextView.setText(log + "\n Player took 2 beans \n ------------------------------------ \n GAME OVER \n" +
                            " The winner is Computer");
                    buttonOneBean.setEnabled(false);
                    buttonTwoBeans.setEnabled(false);
                } else {
                    logTextView.setText(log + "\n Player took 2 beans");
                    computer = true;
                    goMachineTurn();
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle("You can not do it.");
                builder.setMessage("There is only one left");
                builder.setNeutralButton("OK", null);
                builder.show();
            }
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Wait please");
            builder.setMessage("Computer turn");
            builder.setNeutralButton("OK", null);
            builder.show();
        }
        log = logTextView.getText().toString();
        scrollDown();
    }

    /**
     * Procedure scroll down the log of the game.
     */
    private void scrollDown(){
        windowScrollView.post(new Runnable() {
            @Override
            public void run() {
                windowScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    /**
     * The recursive function is core of full and-or graph.
     * In the case of human is function easy and represents just a logs.
     * In the case of computer it is the reduction of moves to conjunctions
     * and disjunctions of submoves.
     */
    private boolean tryMachineTurn(int number, boolean computer) {
        if(number==-1){
            return (tryMachineTurn(number+1, computer));
        }else {
            if(show){
                String whoPlays;
                if(computer){
                    whoPlays = "Computer";
                }else{
                    whoPlays = "Player";
                }

                Log.e(whoPlays, " turn");
                Log.e("Continuous computation", String.valueOf(number));
            }

            if ((computer) && (number == 0)) {
                if (show) {
                    Log.e("IN THIS CASE WON", "COMPUTER");
                }
                return true;
            }
            if ((!computer) && (number == 0)) {
                if (show) {
                    Log.e("IN THIS CASE WON", "PLAYER");
                }
                return false;
            }
            if (computer) {
                return ((tryMachineTurn(number - 1, false)) || (tryMachineTurn(number - 2, false)));
            } else {
                return ((tryMachineTurn(number - 1, true)) && (tryMachineTurn(number - 2, true)));
            }
        }
    }

    /**
     * Method represents turn of computer and it is based of left beans in the game.
     * If is in the game more than two beans, then method call function
     * tryMachineTurn() and choose the best way for computer.
     */
    private void goMachineTurn(){
        if(computer==true) {
            if(number>0){
                if(((number-2)==0)||((number-1)==0)){
                    number-=1;
                    amountTextView.setText(getString(R.string.amount_beans) + "\t " + number);
                    log = logTextView.getText().toString();
                    logTextView.setText(log + "\n Computer took 1 bean");
                    computer=false;
                }else{
                    if((tryMachineTurn(number,computer))&&(!gameOver)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setCancelable(true);
                        builder.setTitle("Warning");
                        builder.setMessage("The computer is no longer possible to beat in this game");
                        Result = "Defeat";

                        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "We recommend restarting the game", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.show();
                        gameOver = true;
                    }
                    show=true;
                    boolean Victoryje = tryMachineTurn(number,computer);
                    boolean left = tryMachineTurn(number-1,!computer);
                    boolean right = tryMachineTurn(number-2,!computer);
                    Log.e("Results", "-------------------------------");
                    Log.e("Victory?", String.valueOf(Victoryje));
                    Log.e("The left side -1", String.valueOf(left));
                    Log.e("The right side -2", String.valueOf(right));
                    show = false;

                    if(tryMachineTurn(number-1,!computer)){
                        number-=1;
                        amountTextView.setText(getString(R.string.amount_beans) + "\t " + number);
                        log = logTextView.getText().toString();
                        logTextView.setText(log + "\n Computer took 1 bean");
                    }else if(tryMachineTurn(number-2,!computer)){
                        number-=2;
                        amountTextView.setText(getString(R.string.amount_beans) + "\t " + number);
                        log = logTextView.getText().toString();
                        logTextView.setText(log + "\n Computer took 2 beans");
                    }else{
                        Random r = new Random();
                        int i1 = r.nextInt(2) + 1;
                        if(i1==1){
                            number-=1;
                            amountTextView.setText(getString(R.string.amount_beans) + "\t " + number);
                            log = logTextView.getText().toString();
                            logTextView.setText(log + "\n Computer took "+ i1 +"  bean");
                        }else{
                            number-=2;
                            amountTextView.setText(getString(R.string.amount_beans) + "\t " + number);
                            log = logTextView.getText().toString();
                            logTextView.setText(log + "\n Computer took "+ i1 +" beans");
                        }
                        Log.e("computer decided", "to play random - specifically"+i1);
                    }
                }
                computer=false;

                if(number==0){
                    log = logTextView.getText().toString();
                    logTextView.setText(log + "\n ------------------------------------ \n GAME OVER \n" +
                            " The winner is Player");
                    Result = "Victory";
                    buttonOneBean.setEnabled(false);
                    buttonTwoBeans.setEnabled(false);
                }
            }
        }
    }
}
