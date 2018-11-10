//****************************************************
//* Name: Manasbi Parajuli
//* Project: Casino
//* Class: CMPS 366-01
//* Date: 11/20/2018
//****************************************************
package edu.ramapo.mparajul.casino.model.setup;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import edu.ramapo.mparajul.casino.model.players.Computer;
import edu.ramapo.mparajul.casino.model.players.Human;
import edu.ramapo.mparajul.casino.model.players.Player;
import edu.ramapo.mparajul.casino.model.utility.Score;

public class Round
{
    private int roundNumber;
    private int numberOfPlayers;
    private int humanIndex;
    private int computerIndex;

    private String lastCapturer;
    private String nextPlayer;
    private boolean isNewGame;

    private Vector<Card> tableCards;
    private Deck deck;

    private Player players[];


    // ****************************************************************
    // Function Name: Round
    // Purpose: serves as a default constructor for Round class
    // Parameters: -> nextPlayer, a string. Holds the name of the next player
    //             -> lastCapturer, a string. Holds the last capturer in the game
    //             -> roundNumber, an integer. Holds the current round value
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    Round (String nextPlayer, String lastCapturer,  int roundNumber)
    {
        this.nextPlayer = nextPlayer;
        this.lastCapturer = lastCapturer;
        this.roundNumber = roundNumber;
        numberOfPlayers = 2;
        isNewGame = false;

        // Set the index of the players based on who the next player is
        if (this.nextPlayer.equals("Human"))
        {
            humanIndex = 0;
            computerIndex = 1;
        }
        if (this.nextPlayer.equals("Computer"))
        {
            humanIndex = 1;
            computerIndex = 0;
        }

        if (this.lastCapturer.equals("Human"))
        {
            humanIndex = 0;
            computerIndex = 1;
        }
        if (this.lastCapturer.equals("Computer"))
        {
            humanIndex = 1;
            computerIndex = 0;
        }

        players[humanIndex] = new Human("Human");
        players[computerIndex] = new Computer("Computer");
    }

    // ****************************************************************
    // Function Name: createPlayers
    // Purpose: creates a pointer to two players each of human and computer and sets their index
    // Parameters: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void createPlayers()
    {
        // Set the index of the players based on who the next player is
        if (nextPlayer.equals("Human"))
        {
            humanIndex = 0;
            computerIndex = 1;
        }
        if (nextPlayer.equals("Computer"))
        {
            humanIndex = 1;
            computerIndex = 0;
        }
        players[humanIndex] = new Human("Human");
        players[computerIndex] = new Computer("Computer");
    }

    // ****************************************************************
    // Function Name: startGame
    // Purpose: loads the deck generated by the program
    // Parameters: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void startGame()
    {
        deck.createShuffledDeck();
        isNewGame = true;
    }

    // ****************************************************************
    // Function Name: dealCardsToPlayer
    // Purpose: deals cards to Human, Computer and places next four cards
    //          on the table
    // Parameters: newRound, the boolean that holds the flag as new round
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void dealCardsToPlayers(boolean newRound)
    {
        int totalCardsToDeal = 0;

        if (newRound) { totalCardsToDeal = 12; }
        else { totalCardsToDeal = 8; }

        // get cards based on whether it is a new or ongoing round and deal accordingly
        for (int i = 0; i < totalCardsToDeal; i++)
        {
            Card card = deck.dealCard();

            // Deal first four cards to the human player
            if (i < 4)
            {
                players[humanIndex].addCardsToHand(card);
            }
            // Deal next four cards to the computer
            else if (i < 8)
            {
                players[computerIndex].addCardsToHand(card);
            }
            // Deal cards on the table if it is a new round
            if (newRound)
            {
                if (i >= 8)
                {
                    tableCards.add(card);
                }
            }
        }
    }

    // ****************************************************************
    // Function Name: saveGame
    // Purpose: saves the current game values to a text file
    // Parameter: fileName, a string. the name of the file to be saved to.
    // Return value: a boolean flag that returns whether game was successfully saved or not
    // Assistance Received: none
    // ****************************************************************
    public boolean saveGameToFile(String fileName)
    {
        // the contents of the saved file
        String serializedFile = "";

        StringBuilder stringBuilder = new StringBuilder();

        // Add contents to the file
        serializedFile += "Round: " + getRoundNumber() + "\n\n";

        // Save Computer's game state
        serializedFile += "Computer:\n";
        serializedFile += "Score: " + players[computerIndex].getScore() + "\n";
        serializedFile += "Hand: ";
        for (Card card : players[computerIndex].getCardsOnHand())
        {
            stringBuilder.append(card.cardToString()).append(" ");
        }
        serializedFile += stringBuilder.toString() + "\n";

        serializedFile += "Pile: ";
        stringBuilder = new StringBuilder();
        for (Card card : players[computerIndex].getCardsOnPile())
        {
            stringBuilder.append(card.cardToString()).append(" ");
        }
        serializedFile += stringBuilder.toString() + "\n\n";

        // Save Human's game state
        serializedFile += "Human:\n";
        serializedFile += "Score: " + players[humanIndex].getScore() + "\n";
        serializedFile += "Hand: ";
        stringBuilder = new StringBuilder();
        for (Card card : players[humanIndex].getCardsOnHand())
        {
            stringBuilder.append(card.cardToString()).append(" ");
        }
        serializedFile += stringBuilder.toString() + "\n";

        serializedFile += "Pile: ";
        stringBuilder = new StringBuilder();
        for (Card card : players[humanIndex].getCardsOnPile())
        {
            stringBuilder.append(card.cardToString()).append(" ");
        }
        serializedFile += stringBuilder.toString()  + "\n\n";

        // Save table
        serializedFile += "Table: " + saveTableCardsToFile();

        // Save build owner
        serializedFile += "Build Owner: " + saveBuildOwnerToFile();

        // Save Last Capturer
        serializedFile += "Last Capturer: " + getLastCapturer() + "\n";

        // Save deck
        serializedFile += "Deck: ";
        stringBuilder = new StringBuilder();
        for(Card card: deck.getDeck())
        {
            stringBuilder.append(card.cardToString()).append(" ");
        }
        serializedFile += stringBuilder.toString() + "\n\n";

        // save next player
        serializedFile += "Next Player: " + getNextPlayer();

        return generateCasinoOnSD (fileName, serializedFile);
    }

    private boolean generateCasinoOnSD (String saveFileName, String serializedContent)
    {
        try
        {
            File root = new File (Environment.getExternalStorageDirectory(), "Casino");

            // Check if Casino folder exists to save the file
            // If is doesn't, then create the directory
            if (!root.exists()) { root.mkdirs(); }

            File file = new File (root, saveFileName);
            FileWriter writer = new FileWriter(file);
            writer.write(serializedContent);
            writer.flush();
            writer.close();
            return true;
        }catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    // ****************************************************************
    // Function Name: calculateScore
    // Purpose: creates Score object and calls its calculate function to
    //          calculate and then store the scores of human and computer
    // Parameter: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    void calculateScore()
    {
        // Create a score object by passing the cards on the pile of the players
        Score score = new Score(players[humanIndex].getCardsOnPile(), players[computerIndex].getCardsOnPile());

        // calculate the score of the player using the rules of the game
        score.calculateTotalScore();

        // get the scores of both players and then set their scores by passing their scores.
        int humanScore = score.getPlayerOneScore() + players[humanIndex].getScore();
        int computerScore = score.getPlayerTwoScore() + players[computerIndex].getScore();

        players[humanIndex].setScore(humanScore);
        players[computerIndex].setScore(computerScore);
    }

    // ****************************************************************
    // Function Name: removeCardFromTable
    // Purpose: remove the group of cards from table
    // Parameters: cardsToRemove, a vector of cards that need to be
    //                   removed from the table
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void removeCardsFromTable(Vector<Card> cardsToRemove)
    {
        for (Card builtCards : cardsToRemove)
        {
            tableCards.remove(builtCards);
        }
    }

    // ****************************************************************
    // Function Name: saveTableCardsToFile
    // Purpose: saves the player's single and multiple builds and then the ongoing table cards to file
    // Parameter: none
    // Return value: a string value of builds and loose table value
    // Assistance Received: none
    // ****************************************************************
    private String saveTableCardsToFile()
    {
        StringBuilder stringBuilder = new StringBuilder();

        Vector<Vector<Card>> multipleBuild;
        Vector<Card> singleBuild;

        // Save multiple builds to file
        if (players[computerIndex].isMultipleBuildExist())
        {
            stringBuilder.append("[ ");
            multipleBuild = players[computerIndex].getMultipleBuildCard().get(players[computerIndex].getPlayerName());
            saveMultipleBuildToFile(stringBuilder, multipleBuild);
        }

        if (players[humanIndex].isMultipleBuildExist())
        {
            stringBuilder.append("[ ");
            multipleBuild = players[humanIndex].getMultipleBuildCard().get(players[humanIndex].getPlayerName());
            saveMultipleBuildToFile(stringBuilder, multipleBuild);
        }

        // Save single builds to file
        if (players[computerIndex].isSingleBuildExist())
        {
            stringBuilder.append("[ ");
            singleBuild = players[computerIndex].getSingleBuildCard().get(players[computerIndex].getPlayerName());
            saveSingleBuildToFile(stringBuilder, singleBuild);
        }

        if (players[humanIndex].isSingleBuildExist())
        {
            stringBuilder.append("[ ");
            singleBuild = players[humanIndex].getSingleBuildCard().get(players[humanIndex].getPlayerName());
            saveSingleBuildToFile(stringBuilder, singleBuild);
        }

        // Save loose table cards to file
        for (Card card : tableCards)
        {
            stringBuilder.append(card.cardToString()).append(" ");
        }
        stringBuilder.append("\n\n");

        return stringBuilder.toString();
    }

    // ****************************************************************
    // Function Name: saveBuildOwnerToFile
    // Purpose: saves the build owner to file
    // Parameter: none
    // Return value: a string object with builds followed by their owner's names
    // Assistance Received: none
    // ****************************************************************
    private String saveBuildOwnerToFile()
    {
        StringBuilder stringBuilder = new StringBuilder();

        Vector<Vector<Card>> multipleBuild;
        Vector<Card> singleBuild;

        if (players[computerIndex].isMultipleBuildExist())
        {
            stringBuilder.append("[ ");
            multipleBuild = players[computerIndex].getMultipleBuildCard().get(players[computerIndex].getPlayerName());
            saveMultipleBuildToFile(stringBuilder, multipleBuild);
            stringBuilder.append(players[computerIndex].getPlayerName()).append(" ");
        }

        if (players[humanIndex].isMultipleBuildExist())
        {
            stringBuilder.append("[ ");
            multipleBuild = players[humanIndex].getMultipleBuildCard().get(players[humanIndex].getPlayerName());
            saveMultipleBuildToFile(stringBuilder, multipleBuild);
            stringBuilder.append(players[humanIndex].getPlayerName()).append(" ");
        }

        // Save single builds to file
        if (players[computerIndex].isSingleBuildExist())
        {
            stringBuilder.append("[ ");
            singleBuild = players[computerIndex].getSingleBuildCard().get(players[computerIndex].getPlayerName());
            saveSingleBuildToFile(stringBuilder, singleBuild);
            stringBuilder.append(players[computerIndex].getPlayerName()).append(" ");
        }

        if (players[humanIndex].isSingleBuildExist())
        {
            stringBuilder.append("[ ");
            singleBuild = players[humanIndex].getSingleBuildCard().get(players[humanIndex].getPlayerName());
            saveSingleBuildToFile(stringBuilder, singleBuild);
            stringBuilder.append(players[humanIndex].getPlayerName()).append(" ");
        }
        stringBuilder.append("\n\n");
        return stringBuilder.toString();
    }

    // ****************************************************************
    // Function Name: saveSingleBuildToFile
    // Purpose: saves the player's single build to a text file
    // Parameter: saveToFile -> StringBuilder object. Holds the object to append string of builds
    //            singleBuild -> vector of Cards. Holds the player's single build
    //                           to be saved to a file
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    private void saveSingleBuildToFile(StringBuilder stringBuilder, Vector<Card> singleBuild)
    {
        stringBuilder.append("[");
        for (Card card : singleBuild)
        {
            stringBuilder.append(card.cardToString()).append(" ");
        }
        stringBuilder.append("] ");
    }

    // ****************************************************************
    // Function Name: saveMultipleBuildToFile
    // Purpose: saves the player's multiple build to a text file
    // Parameter: stringBuilder -> StringBuilder object. Holds the object to append string of builds
    //            multipleBuild -> vector of vector of Cards. Holds the player's multibuild to be
    // saved to a file
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    private void saveMultipleBuildToFile(StringBuilder stringBuilder, Vector<Vector<Card>> multipleBuild)
    {
        for (Vector<Card> build : multipleBuild)
        {
            stringBuilder.append("[");
            for (Card card : build)
            {
                stringBuilder.append(card.cardToString()).append(" ");
            }
            stringBuilder.append("] ");
        }
        stringBuilder.append("] ");
    }

    // ****************************************************************
    // Function Name: getRoundNumber
    // Purpose: gets the current round number
    // Parameter: none
    // Return value: the current round, an integer value
    // Assistance Received: none
    // ****************************************************************
    public int getRoundNumber()
    {
        return roundNumber;
    }

    // ****************************************************************
    // Function Name: setRoundNumber
    // Purpose: sets the current round number
    // Parameter: rnd, an integer value. Holds the new round number
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setRoundNumber(int roundNumber)
    {
        this.roundNumber = roundNumber;
    }

    // ****************************************************************
    // Function Name: getLastCapturer
    // Purpose: gets the last capturer player in the game
    // Parameters: none
    // Return value: identifier of the last capturer player, a string.
    // Assistance Received: none
    // ****************************************************************
    public String getLastCapturer()
    {
        return lastCapturer;
    }

    // ****************************************************************
    // Function Name: setLastCapturer
    // Purpose: sets the last capturer in the game
    // Parameters: capturer, a string. Holds the name of the last capturer in the game
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setLastCapturer(String lastCapturer)
    {
        this.lastCapturer = lastCapturer;
    }

    // ****************************************************************
    // Function Name: getNextPlayer
    // Purpose: gets the next player in the game
    // Parameters: none
    // Return value: identifier of the next player, a string.
    // Assistance Received: none
    // ****************************************************************
    public String getNextPlayer()
    {
        return nextPlayer;
    }

    // ****************************************************************
    // Function Name: setNextPlayer
    // Purpose: sets the next player in the game
    // Parameters: next, a string. Holds the name of the next player in the game
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setNextPlayer(String nextPlayer)
    {
        this.nextPlayer = nextPlayer;
    }

    // ****************************************************************
    // Function Name: setDeck
    // Purpose: sets the deck in the game
    // Parameters: tempDeck, a vector of cards. Hold the new deck for the game
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setDeck(Vector<Card> tempDeck)
    {
        deck.setDeck(tempDeck);
    }

    // ****************************************************************
    // Function Name: getTableCards
    // Purpose: gets the current cards in the table
    // Parameters: none
    // Return value: a vector of table cards.
    // Assistance Received: none
    // ****************************************************************
    public Vector<Card> getTableCards()
    {
        return tableCards;
    }

    // ****************************************************************
    // Function Name: setTableCards
    // Purpose: sets the current cards in the table
    // Parameters: cards, a vector of table cards
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setTableCards(Vector<Card> tableCards)
    {
        this.tableCards = tableCards;
    }

    // ****************************************************************
    // Function Name: isTableEmpty
    // Purpose: checks if the table is empty or not
    // Parameters: none
    // Return value: returns true or false based on whether the table is empty or not
    // Assistance Received: none
    // ****************************************************************
    public boolean isTableEmpty()
    {
        return tableCards.isEmpty();
    }
}