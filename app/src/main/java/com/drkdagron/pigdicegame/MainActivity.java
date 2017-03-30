package com.drkdagron.pigdicegame;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements IGameRules {

    boolean gameStarted = false;

    TextView p1NameText;
    TextView p2NameText;
    TextView p1ScoreText;
    TextView p2ScoreText;
    TextView currentPlayer;
    TextView currentScore;

    CardView p1Back;
    CardView p2Back;

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

        p1Back = (CardView)findViewById(R.id.card_p1);
        p2Back = (CardView)findViewById(R.id.card_p2);

        p1NameText = (TextView)findViewById(R.id.txt_player1);
        p2NameText = (TextView)findViewById(R.id.txt_player2);
        p1ScoreText = (TextView)findViewById(R.id.txt_player1_score);
        p2ScoreText = (TextView)findViewById(R.id.txt_player2_score);
        currentPlayer = (TextView)findViewById(R.id.txt_currPlayer);
        currentScore = (TextView)findViewById(R.id.txt_turnscore);

        if (!getSharedPreferences("pigPrefs", MODE_PRIVATE).getBoolean("gameSaved", false)) {
            DialogFragment newGame = new NewGameDialog();
            newGame.show(getFragmentManager(), "NewGame");
        }

        Button btn = (Button)findViewById(R.id.btn_roll_dice);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RollDice();
            }
        });

        Button hold = (Button)findViewById(R.id.btn_hold_player);
        hold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HoldRoll();
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
    protected void onPause()
    {
        super.onPause();

        SharedPreferences.Editor edit = getSharedPreferences("pigPrefs", MODE_PRIVATE).edit();
        edit.putBoolean("gameSaved", true);
        edit.putString("p1Name", p1Name);
        edit.putString("p2Name", p2Name);
        edit.putInt("p1Score", p1Score);
        edit.putInt("p2Score", p2Score);
        edit.putBoolean("currTurn", p1Turn);
        edit.putInt("currScore", turnScore);
        edit.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences shared = getSharedPreferences("pigPrefs", MODE_PRIVATE);
        if (shared.getBoolean("gameSaved", false)) {
            p1Name = shared.getString("p1Name", "");
            p2Name = shared.getString("p2Name", "");
            p1Score = shared.getInt("p1Score", 0);
            p2Score = shared.getInt("p2Score", 0);
            p1Turn = shared.getBoolean("currTurn", false);
            turnScore = shared.getInt("currScore", 0);
            gameStarted = true;
            UpdateScores();
            UpdateTurnScore();
            UpdateTurn();
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

        p1NameText.setText(p1Name + " Score: " + String.valueOf(p1Score));
        p2NameText.setText(p2Name + " Score: " + String.valueOf(p2Score));

        gameStarted = true;
    }

    private void UpdateTurn()
    {
        if (p1Turn) {
            currentPlayer.setText(p1Name + "'s Turn");
            p1Back.setBackgroundColor(Color.rgb(141, 255, 137));
            p2Back.setBackgroundColor(Color.WHITE);
        }
        else {
            currentPlayer.setText(p2Name + "'s Turn");
            p1Back.setBackgroundColor(Color.WHITE);
            p2Back.setBackgroundColor(Color.rgb(141, 255, 137));
        }
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
            if (rnd == null)
                rnd = new Random();

            int turnRoll = rnd.nextInt(6);
            turnRoll++;
            if (turnRoll == 1) {
                p1Turn = !p1Turn;
                turnScore = 0;
                AlertDialog.Builder build = new AlertDialog.Builder(this);
                build.setTitle("1 was rolled").setMessage("Your turn is over").setNegativeButton("Ok", null).show();
                UpdateTurn();
                UpdateTurnScore();
            } else {
                turnScore += turnRoll;
                AlertDialog.Builder build = new AlertDialog.Builder(this);
                build.setTitle(String.valueOf(turnRoll) + " was rolled").setMessage("Current turn score is: " + turnScore).setNegativeButton("Ok", null).show();
                UpdateTurnScore();
            }
        }
    }

    public void HoldRoll()
    {
        Log.e("TURN OVER", "HoldRoll Entered");

        if (p1Turn)
            p1Score += turnScore;
        else
            p2Score += turnScore;

        turnScore = 0;
        p1Turn = !p1Turn;

        UpdateTurn();
        UpdateTurnScore();
        UpdateScores();

        EndGame();
    }

    @Override
    public void EndGame() {

        boolean winner = false;
        String winName = "";

        if (p1Score >= 100)
        {
            winName = p1Name;
            winner = true;
        }
        else if (p2Score >= 100)
        {
            winName = p2Name;
            winner = true;
        }

        if (winner)
        {
            AlertDialog.Builder build = new AlertDialog.Builder(this);
            build.setTitle("WINNER").setMessage(winName + " IS THE WINNER!!!").setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    StartGame(p1Name, p2Name);
                }
            }).setNegativeButton("New Players", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    NewGame();
                }
            }).show();
        }
    }

    private void UpdateTurnScore()
    {
        currentScore.setText("Turn Total: " + turnScore);
    }

    private void UpdateScores()
    {
        p1ScoreText.setText(String.valueOf(p1Score));
        p2ScoreText.setText(String.valueOf(p2Score));
    }
}
