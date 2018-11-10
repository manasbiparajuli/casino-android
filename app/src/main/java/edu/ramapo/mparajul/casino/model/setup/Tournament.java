//****************************************************
//* Name: Manasbi Parajuli
//* Project: Casino
//* Class: CMPS 366-01
//* Date: 11/20/2018
//****************************************************
package edu.ramapo.mparajul.casino.model.setup;

public class Tournament
{
    private int roundNumber;
    private int playerOneTournamentScore;
    private int playerTwoTournamentScore;
    private String firstPlayer;
    private String lastCapturer;

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
    // Function Name: setFirstPlayer
    // Purpose: sets the first player to start the game
    // Parameters: firstPlayer, a string. Options include "Human" and "Computer".
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setFirstPlayer(String firstPlayer)
    {
        this.firstPlayer = firstPlayer;
    }

    public void setLastCapturer(String lastCapturer)
    {
        this.lastCapturer = lastCapturer;
    }
}
