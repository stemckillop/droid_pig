package com.drkdagron.pigdicegame;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements IGameRules {

    boolean gameStarted = false;

    TextView p1ScoreText;
    TextView p2ScoreText;
    TextView currentPlayer;
    TextView currentScore;

    String p1Name;
    String p2Name;

    int p1Score;
    int p2Score;

    Random rnd;

    int turnScore;

    boolean p1Turn; //if true then p1's turn else p2's turn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        p1ScoreText = (TextView)findViewById(R.id.txt_player1);
        p2ScoreText = (TextView)findViewById(R.id.txt_player2);
        currentPlayer = (TextView)findViewById(R.id.txt_currPlayer);
        currentScore = (TextView)findViewById(R.id.txt_turnscore);

        DialogFragment newGame = new NewGameDialog();
        newGame.show(getFragmentManager(), "NewGame");

        Button btn = (Button)findViewById(R.id.btn_roll_dice);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RollDice();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_new_game:
            {
                NewGame();
                return true;
            }
            case R.id.nav_restart_game:
            {
                ResetGame();
                return true;
            }
            default:
            {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void NewGame()
    {
        DialogFragment dialog = new NewGameDialog();
        dialog.show(getFragmentManager(), "NewGame");
    }

    @Override
    public void ResetGame() {
        p1Score = 0;
        p2Score = 0;

        UpdateScores();
    }

    @Override
    public void StartGame(String p1, String p2) {
        p1Name = p1;
        p2Name = p2;

        p1Score = 0;
        p2Score = 0;

        rnd = new Random();
        p1Turn = rnd.nextBoolean();

        UpdateTurn();
        UpdateScores();

        gameStarted = true;
    }

    private void UpdateTurn()
    {
        if (p1Turn)
            currentPlayer.setText(p1Name + "'s Turn");
        else
            currentPlayer.setText(p2Name + "'s Turn");
    }

    public void RollDice()
    {
        if (gameStarted == false)
        {
            DialogFragment newGame = new NewGameDialog();
            Bundle bun = new Bundle();
            bun.putString("title", "Must start new game!");
            newGame.setArguments(bun);
            newGame.show(getFragmentManager(), "NewGame");
        }
        else {
            int turnRoll = rnd.nextInt(6);
            turnRoll++;
            if (turnRoll == 1) {

            } else {

            }
        }
    }

    public void HoldRoll(View v)
    {

    }

    @Override
    public void EndGame() {
        if (p1Score >= 100)
        {

        }
        else if (p2Score >= 100)
        {

        }
    }

    private void UpdateScores()
    {
        p1ScoreText.setText(p1Name + " Score: " + String.valueOf(p1Score));
        p2ScoreText.setText(p2Name + " Score: " + String.valueOf(p2Score));
    }
}
