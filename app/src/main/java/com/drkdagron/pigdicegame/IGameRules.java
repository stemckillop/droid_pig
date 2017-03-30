package com.drkdagron.pigdicegame;

/**
 * Created by drkdagron on 2017-03-29.
 */

public interface IGameRules {
    void NewGame();
    void ResetGame();
    void StartGame(String p1, String p2);
    void EndGame();
}
