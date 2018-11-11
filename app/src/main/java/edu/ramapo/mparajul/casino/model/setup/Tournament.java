//****************************************************
//* Name: Manasbi Parajuli
//* Project: Casino
//* Class: CMPS 366-01
//* Date: 11/20/2018
//****************************************************
package edu.ramapo.mparajul.casino.model.setup;

import android.content.Intent;

public class Tournament
{
    private int roundNumber;
    private int playerOneTournamentScore;
    private int playerTwoTournamentScore;
    private String nextPlayer;
    private String lastCapturer;
    private Round round = new Round();

    // ****************************************************************
    // Function Name: Tournament
    // Purpose: serves as a default constructor for Tournament class
    // Parameters: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public Tournament()
    {
        roundNumber = 1;
        playerOneTournamentScore = 0;
        playerTwoTournamentScore = 0;
        round = new Round();
    }

    public void newGame()
    {
        round = new Round(nextPlayer, " ", roundNumber);
        round.startGame();
        round.dealCardsToPlayers(true);

    }

    public void loadGame(Intent intent)
    {
        round = new Round (nextPlayer, " ", roundNumber);
        round.setSavedPreferences(intent);
    }

    public void setRoundNumber(int roundNumber)
    {
        this.roundNumber = roundNumber;
    }

    public void setPlayerOneTournamentScore(int playerOneTournamentScore)
    {
        this.playerOneTournamentScore = playerOneTournamentScore;
    }

    public void setPlayerTwoTournamentScore(int playerTwoTournamentScore)
    {
        this.playerTwoTournamentScore = playerTwoTournamentScore;
    }

    // ****************************************************************
    // Function Name: setNextPlayer
    // Purpose: sets the first player to start the game
    // Parameters: nextPlayer, a string. Options include "Human" and "Computer".
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setNextPlayer(String nextPlayer)
    {
        this.nextPlayer = nextPlayer;
    }

    public void setLastCapturer(String lastCapturer)
    {
        this.lastCapturer = lastCapturer;
    }

    public Round getRound()
    {
        return round;
    }

}
