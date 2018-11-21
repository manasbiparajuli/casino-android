//****************************************************
//* Name: Manasbi Parajuli
//* Project: Casino
//* Class: CMPS 366-01
//* Date: 11/20/2018
//****************************************************
package edu.ramapo.mparajul.casino.model.setup;

import android.content.Intent;

import java.util.HashMap;

public class Tournament
{
    private int roundNumber;
    private int humanTournamentScore;
    private int computerTournamentScore;
    private int lastRoundHumanScore;
    private int lastRoundComputerScore;
    private String nextPlayer;
    private String lastCapturer;
    private Round round = new Round();

    private HashMap<String, String> endRoundScores = new HashMap<>();

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
        humanTournamentScore = 0;
        computerTournamentScore = 0;
        lastRoundHumanScore = 0;
        lastRoundComputerScore = 0;
        round = new Round();
        endRoundScores = new HashMap<>();
    }

    public void newGame()
    {
        round = new Round(nextPlayer, " ", roundNumber);
        round.startGame();
        round.setNewGame(true);
    }

    public void loadGame(Intent intent)
    {
        round = new Round (nextPlayer, " ", roundNumber);
        round.setSavedPreferences(intent);
        round.setNewGame(false);
    }

    public int getRoundNumber() { return roundNumber; }

    public void setRoundNumber(int roundNumber)
    {
        this.roundNumber = roundNumber;
    }

    public void setHumanTournamentScore(int humanTournamentScore)
    {
        this.humanTournamentScore = humanTournamentScore;
    }

    public void setComputerTournamentScore(int computerTournamentScore)
    {
        this.computerTournamentScore = computerTournamentScore;
    }
    public int getHumanTournamentScore()
    {
        return humanTournamentScore;
    }

    public int getComputerTournamentScore()
    {
        return computerTournamentScore;
    }


    public int getLastRoundHumanScore()
    {
        return lastRoundHumanScore;
    }

    public void setLastRoundHumanScore(int lastRoundHumanScore)
    {
        this.lastRoundHumanScore = lastRoundHumanScore;
    }

    public int getLastRoundComputerScore()
    {
        return lastRoundComputerScore;
    }

    public void setLastRoundComputerScore(int lastRoundComputerScore)
    {
        this.lastRoundComputerScore = lastRoundComputerScore;
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

    public String getLastCapturer()
    {
        return lastCapturer;
    }

    public Round getRound()
    {
        return round;
    }

    public HashMap<String, String> getEndRoundScores()
    {
        return endRoundScores;
    }

    public void setEndRoundScores(HashMap<String, String> endRoundScores)
    {
        this.endRoundScores = endRoundScores;
    }

    public void startNewRound()
    {
        lastCapturer = round.getLastCapturer();
        round = new Round("", lastCapturer, roundNumber);
        round.startGame();
        round.dealCardsToPlayers(true);
    }
}