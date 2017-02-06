package com.example.justin.myfirstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import java.util.Random;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    public static int player_score = 0;
    public static int computer_score = 0;
    public static int player_round = 0;
    public static int computer_round = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.status);
        textView.setText("Your score: " + player_score + " | Computer score: " + computer_score);
        ImageView imageView = (ImageView) findViewById(R.id.dice);
        imageView.setImageResource(R.drawable.dice1);
    }

    public void actionRoll(View view) {
        int roll = rollDice();
        TextView textView = (TextView) findViewById(R.id.status);
        if(roll != 1) {
            player_round += roll;
            textView.setText("Your score: " + player_score + " | Computer score: " + computer_score + " | Your turn score: " + player_round);
        }
        else {
            player_round = 0;
            textView.setText("Your score: " + player_score + " | Computer score: " + computer_score);
            computerTurn();
        }
    }

    public void actionHold(View view) {
        player_score += player_round;
        player_round = 0;
        TextView textView = (TextView) findViewById(R.id.status);
        textView.setText("Your score: " + player_score + " | Computer score: " + computer_score);

        computerTurn();
    }

    public void actionReset(View view) {
        player_score = 0;
        computer_score = 0;
        player_round = 0;
        computer_round = 0;
        TextView textView = (TextView) findViewById(R.id.status);
        textView.setText("Your score: " + player_score + " | Computer score: " + computer_score);
    }

    public void computerTurn() {
        Button button_roll = (Button) findViewById(R.id.roll);
        button_roll.setEnabled(false);
        Button button_hold = (Button) findViewById(R.id.hold);
        button_hold.setEnabled(false);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                TextView textView = (TextView) findViewById(R.id.status);

                int roll = rollDice();
                if(roll != 1) {
                    computer_round += roll;
                }
                else {
                    computer_round = 0;
                }

                if(computer_round != 0) {
                    if(computer_round < 20) {
                        textView.setText("Your score: " + player_score + " | Computer score: " + computer_score + " | Computer's turn score: " + computer_round);
                        computerTurn();
                    }
                    else {
                        computer_score += computer_round;
                        computer_round = 0;
                    }
                }
                textView.setText("Your score: " + player_score + " | Computer score: " + computer_score);
            }
        }, 500);
        button_roll.setEnabled(true);
        button_hold.setEnabled(true);
    }

    private int rollDice() {
        Random r = new Random();
        int rand_num = r.nextInt(6) + 1;
        ImageView imageView = (ImageView) findViewById(R.id.dice);
        switch(rand_num) {
            case 1:
                imageView.setImageResource(R.drawable.dice1);
                break;
            case 2:
                imageView.setImageResource(R.drawable.dice2);
                break;
            case 3:
                imageView.setImageResource(R.drawable.dice3);
                break;
            case 4:
                imageView.setImageResource(R.drawable.dice4);
                break;
            case 5:
                imageView.setImageResource(R.drawable.dice5);
                break;
            case 6:
                imageView.setImageResource(R.drawable.dice6);
                break;
            default:
        }
        return rand_num;
    }
}
