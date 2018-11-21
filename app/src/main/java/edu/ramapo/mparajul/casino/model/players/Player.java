//****************************************************
//* Name: Manasbi Parajuli
//* Project: Casino
//* Class: CMPS 366-01
//* Date: 11/20/2018
//****************************************************
package edu.ramapo.mparajul.casino.model.players;

import java.util.HashMap;
import java.util.Vector;

import edu.ramapo.mparajul.casino.model.setup.Card;

public class Player
{
    protected int score;
    protected int tourneyScore;
    protected int firstBuildScore;
    protected String playerName;
    protected boolean hasCapturedCardsInMove;
    protected boolean moveSuccessful;
    protected boolean helpRequested;
    protected boolean makeOpponentBuildScoreEmpty;
    protected Vector<Card> cardsOnPile = new Vector<>();
    protected Vector<Card> cardsOnHand = new Vector<>();
    protected Vector<Card> clickedTableCards = new Vector<>();
    protected Vector<Card> clickedBuildCards = new Vector<>();
    protected Card clickedHandCard = new Card();

    protected String moveActionIdentifier;
    protected String moveExplanation;
    protected String helpExplanation;

    protected HashMap<String, Vector<Card>> singleBuildCard = new HashMap<>();
    protected HashMap<String, Vector<Vector<Card>>> multipleBuildCard = new HashMap<>();

    public Player()
    {
        this.playerName = playerName;
        this.score = 0;
        this.firstBuildScore = 0;
        this.cardsOnHand = new Vector<>();
        this.cardsOnPile = new Vector<>();
        this.tourneyScore = 0;
        this.hasCapturedCardsInMove = false;
        this.moveSuccessful = false;
        this.helpRequested = false;
        this.makeOpponentBuildScoreEmpty = false;
        this.moveActionIdentifier = "";
        this.helpExplanation = "";
        this.clickedTableCards = new Vector<>();
        this.clickedBuildCards = new Vector<>();
        this.clickedHandCard = new Card();
        singleBuildCard = new HashMap<>();
        multipleBuildCard = new HashMap<>();
    }

    // ****************************************************************
    // Function Name: Player
    // Purpose: serves as a default constructor for Player Class
    // Parameters: name, the name of the player
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public Player (String playerName)
    {
        this.playerName = playerName;
        this.score = 0;
        this.firstBuildScore = 0;
        this.cardsOnHand = new Vector<>();
        this.cardsOnPile = new Vector<>();
        this.tourneyScore = 0;
        this.hasCapturedCardsInMove = false;
        this.makeOpponentBuildScoreEmpty = false;
        this.moveSuccessful = false;
        this.helpRequested = false;
        this.moveActionIdentifier = "";
        this.helpExplanation = "";
        this.clickedTableCards = new Vector<>();
        this.clickedBuildCards = new Vector<>();
        this.clickedHandCard = new Card();
        singleBuildCard = new HashMap<>();
        multipleBuildCard = new HashMap<>();
    }

    // ****************************************************************
    // Function Name: calcSingleCardScore
    // Purpose: calculate the numerical equivalent of a card
    // Parameters: card, a Card object
    // Return value: integer value, the numerical equivalent of a card
    // Assistance Received: none
    // ****************************************************************
    final public int calcSingleCardScore(Card card)
    {
        String face = card.getFace();

        if (face.equals("A")) { return 1;}
        else if (face.equals("2")) { return 2;}
        else if (face.equals("3")) { return 3;}
        else if (face.equals("4")) { return 4;}
        else if (face.equals("5")) { return 5;}
        else if (face.equals("6")) { return 6;}
        else if (face.equals("7")) { return 7;}
        else if (face.equals("8")) { return 8;}
        else if (face.equals("9")) { return 9;}
        else if (face.equals("X")) { return 10;}
        else if (face.equals("J")) { return 11;}
        else if (face.equals("Q")) { return 12;}
        else { return 13;}
    }

    // ****************************************************************
    // Function Name: calcLooseCardScore
    // Purpose: calculates the total score of a vector of cards
    // Parameters: looseCards, a vector of cards. Holds the cards whose
    //                   score needs to be determined
    // Return value: integer value, the score of the total cards
    // Assistance Received: none
    // ****************************************************************
    final public int calcLooseCardScore (Vector<Card> looseCards)
    {
        int score = 0;

        for (Card card : looseCards)
        {
            score += calcSingleCardScore(card);
        }
        return score;
    }

    // ****************************************************************
    // Function Name: addCardsToHand
    // Purpose: adds cards to the players' hand
    // Parameters: card, a Card object. It holds the current card to be added.
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void addCardsToHand(Card card)
    {
        cardsOnHand.add(card);
    }

    // ****************************************************************
    // Function Name: addCardsToPile
    // Purpose: adds cards to the players' pile
    // Parameters: card, a Card object. It holds the current card to be added.
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void addCardsToPile(Card card)
    {
        cardsOnPile.add(card);
    }

    // ****************************************************************
    // Function Name: removeCardFromHand
    // Purpose: remove the card from hand
    // Parameters: cardToRemove, a card object that needs to be
    //                   removed from player's hand
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    final public void removeCardFromHand (Card cardToRemove)
    {
        cardsOnHand.remove(cardToRemove);
    }

    // ****************************************************************
    // Function Name: removeCardsFromTable
    // Purpose: remove the group of cards from table
    // Parameters: tableCards, a vector of cards. Holds the cards that are
    //                   in the table.
    //             looseCards, a vector of cards that need to be
    //                   removed from the table
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    final public void removeCardsFromTable(Vector<Card> tableCards, Vector<Card> looseCards)
    {
        for (Card card : looseCards)
        {
            tableCards.remove(card);
        }
    }

    // ****************************************************************
    // Function Name: playerCardsOnHand
    // Purpose: prints the current cards on the player's hand
    // Parameter: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    final public void printCardsOnHand()
    {
        System.out.println(getPlayerName() + " cards on Hand");

        for (Card card : cardsOnHand)
        {
            System.out.print(card.cardToString() + " ");
        }
        System.out.println(" ");
    }

    // ****************************************************************
    // Function Name: playerCardsOnPile
    // Purpose: prints the current cards on the player's pile
    // Parameter: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    final public void printCardsOnPile()
    {
        System.out.println(getPlayerName() + " cards on Pile");

        for (Card card : cardsOnPile)
        {
            System.out.print(card.cardToString() + " ");
        }
        System.out.println(" ");
    }

    // ****************************************************************
    // Function Name: printSingleBuild
    // Purpose: prints the single build of the players
    // Parameter: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    final public void printSingleBuild()
    {
        Vector<Card> singleBuild;

        if (!isSingleBuildEmpty())
        {
            singleBuild = singleBuildCard.get(getPlayerName());

            System.out.print(" [");
            for (Card card : singleBuild)
            {
                System.out.print(card.cardToString() + " ");
            }
            System.out.print("]");
        }
    }

    // ****************************************************************
    // Function Name: printMultipleBuild
    // Purpose: prints the multiple build of the players
    // Parameter: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    final public void printMultipleBuild()
    {
        // get the list of builds
        Vector<Vector<Card>> multiBuild = multipleBuildCard.get(getPlayerName());

        // print the multiple build if it exists
        if(!isMultipleBuildEmpty())
        {
            System.out.print(" [ ");

            for (Vector<Card> builds : multiBuild)
            {
                System.out.print("[");

                for (Card cards : builds)
                {
                    System.out.print(cards.cardToString() + " ");
                }
                System.out.print("]");
            }
            System.out.print(" ]");
            System.out.println();
        }
    }

    // ****************************************************************
    // Function Name: getScore
    // Purpose: gets the score of the player
    // Parameter: none
    // Return value: The current score of the player
    // Assistance Received: none
    // ****************************************************************
    final public int getScore()
    {
        return score;
    }

    // ****************************************************************
    // Function Name: setScore
    // Purpose: updates the score of the player
    // Parameter: score, an integer value. This score will be added
    //            to the player's total
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    final public void setScore(int score)
    {
        this.score = score;
    }

    // ****************************************************************
    // Function Name: getFirstBuildScore
    // Purpose: gets the first build score of the player
    // Parameter: none
    // Return value: The current score of the player's valid build
    // Assistance Received: none
    // ****************************************************************
    final public int getFirstBuildScore()
    {
        return firstBuildScore;
    }

    // ****************************************************************
    // Function Name: setFirstBuildScore
    // Purpose: sets the first build score of the player
    // Parameter: score, an integer value. The score of the valid build.
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    final public void setFirstBuildScore(int firstBuildScore)
    {
        this.firstBuildScore = firstBuildScore;
    }

    // ****************************************************************
    // Function Name: getCardsOnPile
    // Purpose: gets the current cards on the player's pile
    // Parameter: none
    // Return value: A vector of cards of the player's pile
    // Assistance Received: none
    // ****************************************************************
    final public Vector<Card> getCardsOnPile()
    {
        return cardsOnPile;
    }

    // ****************************************************************
    // Function Name: setCardsOnPile
    // Purpose: sets the current cards on the player's pile
    // Parameter: cardSelected, a vector of Cards. Holds the new pile cards for the player
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    final public void setCardsOnPile(Vector<Card> cardsOnPile)
    {
        this.cardsOnPile = cardsOnPile;
    }

    // ****************************************************************
    // Function Name: getCardsOnHand
    // Purpose: gets the current cards on the player's hand
    // Parameter: none
    // Return value: A vector of cards of the player's hand
    // Assistance Received: none
    // ****************************************************************
    final public Vector<Card> getCardsOnHand()
    {
        return cardsOnHand;
    }

    // ****************************************************************
    // Function Name: setCardsOnHand
    // Purpose: sets the current cards on the player's hand
    // Parameter: cardSelected, a vector of Cards. Holds the new hand cards for the player
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    final public void setCardsOnHand(Vector<Card> cardsOnHand)
    {
        this.cardsOnHand = cardsOnHand;
    }

    // ****************************************************************
    // Function Name: getPlayerName
    // Purpose: gets the name of the player
    // Parameter: none
    // Return value: The current name of the player
    // Assistance Received: none
    // ****************************************************************
    final public String getPlayerName()
    {
        return playerName;
    }

    // ****************************************************************
    // Function Name: hasCapturedCard
    // Purpose: checks if the player has captured any cards in current move
    // Parameters: none
    // Return value: a boolean that returns if a player captured any cards
    //                in the current move
    // Assistance Received: none
    // ****************************************************************
    final public boolean hasCapturedCard()
    {
        return hasCapturedCardsInMove;
    }

    // ****************************************************************
    // Function Name: getSingleBuildCard
    // Purpose: gets the successful single build of the player
    // Parameter: none
    // Return value: a Hash Map of owner as string and single build as vector of cards
    // Assistance Received: none
    // ****************************************************************
    final public HashMap<String, Vector<Card>> getSingleBuildCard()
    {
        return singleBuildCard;
    }

    // ****************************************************************
    // Function Name: getMultipleBuildCard
    // Purpose: gets the successful multiple build of the player
    // Parameter: none
    // Return value: a Hash Map of owner as string and multiple builds as
    //                  vector of vector of cards
    // Assistance Received: none
    // ****************************************************************
    final public HashMap<String, Vector<Vector<Card>>> getMultipleBuildCard()
    {
        return multipleBuildCard;
    }

    // ****************************************************************
    // Function Name: setMultipleBuildCard
    // Purpose: sets the multiple build cards on the player's hand
    // Parameter: multipleBuildCard, a hash map of string and vector of vector of Cards. Holds the
    // new
    // multiple build cards for the player
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setMultipleBuildCard (HashMap<String, Vector<Vector<Card>>> multipleBuildCard)
    {
        this.multipleBuildCard = multipleBuildCard;
    }

    // ****************************************************************
    // Function Name: setSingleBuildCard
    // Purpose: sets the single build cards on the player's hand
    // Parameter: singleBuildCard, a hash map of string and vector of Cards. Holds the new
    // multiple build cards for the player
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setSingleBuildCard (HashMap<String, Vector<Card>> singleBuildCard)
    {
        this.singleBuildCard = singleBuildCard;
    }

    // ****************************************************************
    // Function Name: setMoveActionIdentifier
    // Purpose: sets the name of the move that the player is trying to make
    // Parameter: moveActionIdentifier, a string. Holds the player's action name
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setMoveActionIdentifier(String moveActionIdentifier)
    {
        this.moveActionIdentifier = moveActionIdentifier;
    }

    // ****************************************************************
    // Function Name: setClickedTableCards
    // Purpose: saves the table cards clicked by the player
    // Parameter: clickedTableCards. Holds the table cards clicked by the human player
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setClickedTableCards(Vector<Card> clickedTableCards)
    {
        this.clickedTableCards = clickedTableCards;
    }

    // ****************************************************************
    // Function Name: setClickedBuildCards
    // Purpose: saves the build cards clicked by the player
    // Parameter: clickedBuildCards. Holds the build cards clicked by the human player
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setClickedBuildCards(Vector<Card> clickedBuildCards)
    {
        this.clickedBuildCards = clickedBuildCards;
    }

    // ****************************************************************
    // Function Name: setClickedHandCards
    // Purpose: saves the hand card clicked by the human player
    // Parameter: clickedHandCard. Holds the hand card clicked by the human player
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setClickedHandCard(Card clickedHandCard)
    {
        this.clickedHandCard = clickedHandCard;
    }

    // ****************************************************************
    // Function Name: isMoveSuccessful
    // Purpose: returns whether the player made a successful move in the current turn
    // Parameter: none
    // Return value: a boolean based on whether the player made a successful move
    // Assistance Received: none
    // ****************************************************************
    public boolean isMoveSuccessful()
    {
        return moveSuccessful;
    }

    // ****************************************************************
    // Function Name: setMoveSuccessful
    // Purpose: saves the flag if the player made a successful move in the current turn
    // Parameter: moveSuccessful, a boolean. Holds tha flag to identify if the player made a
    // successful move in their current turn
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setMoveSuccessful(boolean moveSuccessful) { this.moveSuccessful = moveSuccessful; }

    // ****************************************************************
    // Function Name: setHelpRequested
    // Purpose: sets the flag if the human player requested help during their turn
    // Parameter: helpRequested, a boolean. Holds the flag if the human player requested help in
    // their turn
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setHelpRequested(boolean helpRequested) { this.helpRequested = helpRequested; }

    // ****************************************************************
    // Function Name: getMoveExplanation
    // Purpose: gets the explanation for the move that the player makes
    // Parameters: none
    // Return value: a string that stores the explanation of the move
    // Assistance Received: none
    // ****************************************************************
    public String getMoveExplanation()
    {
        return moveExplanation;
    }

    // ****************************************************************
    // Function Name: getHelpExplanation
    // Purpose: gets the help explanation for the move that the human can make
    // Parameters: none
    // Return value: a string that stores the explanation of a valid possible move
    // Assistance Received: none
    // ****************************************************************
    public String getHelpExplanation() { return helpExplanation; }

    // ****************************************************************
    // Function Name: isMultipleBuildEmpty
    // Purpose: returns whether the player has a multiple build
    // Parameter: none
    // Return value: a boolean based on whether the player has a multiple build or not
    // Assistance Received: none
    // ****************************************************************
    final public boolean isMultipleBuildEmpty()
    {
        // check if there are non empty builds in the player's multiple build
        if (multipleBuildCard.containsKey(getPlayerName()))
        {
            Vector<Vector<Card>> multipleBuild = multipleBuildCard.get(getPlayerName());
            Vector<Card> buildCards = new Vector<>();

            for (Vector<Card> build : multipleBuild)
            {
                buildCards.addAll(build);
            }
            return (buildCards.size() == 0);
        }
        return multipleBuildCard.isEmpty();
    }

    // ****************************************************************
    // Function Name: isSingleBuildEmpty
    // Purpose: returns whether the player has a single build
    // Parameter: none
    // Return value: a boolean based on whether the player has a single build or not
    // Assistance Received: none
    // ****************************************************************
    final public boolean isSingleBuildEmpty()
    {
        // check if there are non empty builds in the player's single build
        if (singleBuildCard.containsKey(getPlayerName()))
        {
            Vector<Card> build = singleBuildCard.get(getPlayerName());

            return (build.size() == 0);
        }
        return singleBuildCard.isEmpty();
    }

    // ****************************************************************
    // Function Name: isMakeOpponentBuildScoreEmpty
    // Purpose: returns whether the current player has extended the build of the opponent and
    // thus needs to set the opponent's build score to 0
    // Parameter: none
    // Return value: a boolean based on whether the player has extended the opponent's build
    // Assistance Received: none
    // ****************************************************************
    public boolean isMakeOpponentBuildScoreEmpty()
    {
        return makeOpponentBuildScoreEmpty;
    }

    // ****************************************************************
    // Function Name: setMakeOpponentBuildSooreEmpty
    // Purpose: sets the flag if the previous player extended the opponent's build
    // Parameter: makeOpponentBuildScoreEmpty, a boolean. Holds the flag if the current player
    // extended the build of the opponent
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setMakeOpponentBuildScoreEmpty(boolean makeOpponentBuildScoreEmpty)
    {
        this.makeOpponentBuildScoreEmpty = makeOpponentBuildScoreEmpty;
    }

    // ****************************************************************
    // Function Name: getTourneyScore
    // Purpose: gets the player's score of the tournament
    // Parameters: none
    // Return value: the tournament score of the player
    // Assistance Received: none
    // ****************************************************************
    public int getTourneyScore()
    {
        return tourneyScore;
    }

    // ****************************************************************
    // Function Name: setTourneyScore
    // Purpose: sets the player's score of the tournament
    // Parameters: tourneyScore, an integer. Holds the tournament score of the player
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setTourneyScore(int tourneyScore)
    {
        this.tourneyScore = tourneyScore;
    }



    // The following functions will be overridden by the Human and Computer classes.

    public void play (Vector<Card> tableCards, HashMap<String, Vector<Card>> oppoBuild,
                      String opponentPlayerName){}

    public boolean makeSingleBuild (Vector<Card> tableCards){ return false;}

    public boolean makeMultipleBuild (Vector<Card> tableCards){ return false;}

    public boolean increaseOpponentBuild (Vector<Card> tableCards, HashMap<String, Vector<Card>>
                                                                           oppoBuild, String opponentPlayerName)
    { return false;}

    public void initiateMultipleBuild (Vector<Card> looseCardsSelected){}

    public boolean captureSingleBuild() { return false;}

    public boolean captureMultipleBuild() { return false;}

    public boolean captureSetAndIndividualCards(Vector<Card> tableCards) { return false;}

    public boolean trailCard (Vector<Card> tableCards) { return false;}
}