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

    // ****************************************************************
    // Function Name: newGame
    // Purpose: creates a new game
    // Parameter: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void newGame()
    {
        round = new Round(nextPlayer, " ", roundNumber);
        round.startGame();
        round.setNewGame(true);
    }

    // ****************************************************************
    // Function Name: loadGame
    // Purpose: loads a previously saved game
    // Parameter: intent, an intent. Holds the values of the saved game to be loaded by the round
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void loadGame(Intent intent)
    {
        round = new Round (nextPlayer, " ", roundNumber);
        round.setSavedPreferences(intent);
        round.setNewGame(false);
    }

    // ****************************************************************
    // Function Name: getRoundNumber
    // Purpose: gets the tournament's round number
    // Parameters: none
    // Return value: current round number
    // Assistance Received: none
    // ****************************************************************
    public int getRoundNumber() { return roundNumber; }

    // ****************************************************************
    // Function Name: setRoundNumber
    // Purpose: sets the tournament's round number
    // Parameters: roundNumber, an integer. Holds current round number
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setRoundNumber(int roundNumber)
    {
        this.roundNumber = roundNumber;
    }

    // ****************************************************************
    // Function Name: setHumanTournamentScore
    // Purpose: sets the human player's score of the tournament
    // Parameters: humanTournamentScore, an integer. Holds the human's tourney score
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setHumanTournamentScore(int humanTournamentScore)
    {
        this.humanTournamentScore = humanTournamentScore;
    }

    // ****************************************************************
    // Function Name: setComputerTournamentScore
    // Purpose: sets the computer player's score of the tournament
    // Parameters: computerTournamentScore, an integer. Holds the computer's tourney score
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setComputerTournamentScore(int computerTournamentScore)
    {
        this.computerTournamentScore = computerTournamentScore;
    }

    // ****************************************************************
    // Function Name: getHumanTournamentScore
    // Purpose: gets the human player's score of the tournament
    // Parameters: none
    // Return value: humanTournamentScore, an integer. Holds the human's tourney score
    // Assistance Received: none
    // ****************************************************************
    public int getHumanTournamentScore()
    {
        return humanTournamentScore;
    }

    // ****************************************************************
    // Function Name: getComputerTournamentScore
    // Purpose: gets the computer's score of the tournament
    // Parameters: none
    // Return value: computerTournamentScore, an integer. Holds the computer's tourney score
    // Assistance Received: none
    // ****************************************************************
    public int getComputerTournamentScore()
    {
        return computerTournamentScore;
    }

    // ****************************************************************
    // Function Name: getLastRoundHumanScore
    // Purpose: gets the human player's last round score
    // Parameters: none
    // Return value: lastRoundHumanScore, an integer. Holds the human's last round score
    // Assistance Received: none
    // ****************************************************************
    public int getLastRoundHumanScore()
    {
        return lastRoundHumanScore;
    }

    // ****************************************************************
    // Function Name: setLastRoundHumanScore
    // Purpose: sets the human player's last round score
    // Parameters: lastRoundHumanScore, an integer. Holds the human's last round score
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setLastRoundHumanScore(int lastRoundHumanScore)
    {
        this.lastRoundHumanScore = lastRoundHumanScore;
    }

    // ****************************************************************
    // Function Name: getLastRoundComputerScore
    // Purpose: gets the computer player's last round score
    // Parameters: none
    // Return value: lastRoundComputerScore, an integer. Holds the computer's last round score
    // Assistance Received: none
    // ****************************************************************
    public int getLastRoundComputerScore()
    {
        return lastRoundComputerScore;
    }

    // ****************************************************************
    // Function Name: setLastRoundComputerScore
    // Purpose: sets the computer's last round score
    // Parameters: lastRoundComputerScore, an integer. Holds the computer's last round score
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
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

    // ****************************************************************
    // Function Name: setLastCapturer
    // Purpose: sets the last capturer after the round has ended
    // Parameters: lastCapturer, a string. Options include "Human" and "Computer".
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setLastCapturer(String lastCapturer)
    {
        this.lastCapturer = lastCapturer;
    }

    // ****************************************************************
    // Function Name: getLastCapturer
    // Purpose: gets the last capturer after the round has ended
    // Parameters: none
    // Return value: lastCapturer, a string. Options include "Human" and "Computer".
    // Assistance Received: none
    // ****************************************************************
    public String getLastCapturer()
    {
        return lastCapturer;
    }

    // ****************************************************************
    // Function Name: getRound
    // Purpose: gets the Round object instantiated in the tournament
    // Parameters: none
    // Return value: a Round class object. The current round.
    // Assistance Received: none
    // ****************************************************************
    public Round getRound()
    {
        return round;
    }

    // ****************************************************************
    // Function Name: getEndRoundScores
    // Purpose: gets the last rounds scores, total cards, total spades of the players
    // Parameters: none
    // Return value: a hash map of player's names corresponding to their card (Spade, Ace) scores
    // Assistance Received: none
    // ****************************************************************
    public HashMap<String, String> getEndRoundScores()
    {
        return endRoundScores;
    }

    // ****************************************************************
    // Function Name: setEndRoundScores
    // Purpose: sets the last rounds scores, total cards, total spades of the players
    // Parameters: endRoundSores, a hash map of player's names corresponding to their card (Spade, Ace) scores
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setEndRoundScores(HashMap<String, String> endRoundScores)
    {
        this.endRoundScores = endRoundScores;
    }
}