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
    protected Vector<Card> cardsOnPile = new Vector<>();
    protected Vector<Card> cardsOnHand = new Vector<>();
    protected Vector<Card> clickedTableCards = new Vector<>();
    protected Vector<Card> clickedBuildCards = new Vector<>();
    protected Card clickedHandCard = new Card();

    protected String moveActionIdentifier;
    protected String moveExplanation;

    protected HashMap<String, Vector<Card>> singleBuildCard = new HashMap<>();
    protected HashMap<String, Vector<Vector<Card>>> multipleBuildCard = new HashMap<>();

    Player()
    {
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
        this.moveSuccessful = false;
        this.moveActionIdentifier = "";
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
    // Function Name: findCommonCard
    // Purpose: finds the card in common with the player's hand
    // Parameters: matchedTableCards, a vector of cards. Holds the cards that were
    //                   used to perform an action in the current move.
    // Return value: a card object. Returns the card that was in common with the set of matched cards
    // Assistance Received: none
    // ****************************************************************
    protected Card findCommonCard(final Vector<Card> matchedTableCards)
    {
        for (Card handCard : getCardsOnHand())
        {
            // return the card if found
            if (matchedTableCards.contains(handCard))
            {
                return handCard;
            }
        }
        return new Card();
    }

    // ****************************************************************
    // Function Name: isCardOnHand
    // Purpose: checks if the card selected by the player is in the player's hand
    // Parameter: cardSelected, a string. The card selected by the player
    // Return value: returns a boolean that checks if the player has the card in hand
    // Assistance Received: none
    // ****************************************************************
    final boolean isCardOnHand(String cardSelected)
    {
        for (Card card : cardsOnHand)
        {
            if (card.cardToString().equals(cardSelected))
            {
                return true;
            }
        }
        return false;
    }

    // ****************************************************************
    // Function Name: isCardOnPile
    // Purpose: checks if the card selected by the player is in the player's pile
    // Parameter: cardSelected, a string. The card selected by the player
    // Return value: returns a boolean that checks if the player has the card in pile
    // Assistance Received: none
    // ****************************************************************
    final boolean isCardOnPile(String cardSelected)
    {
        for (Card card : cardsOnPile)
        {
            if (card.cardToString().equals(cardSelected)) return true;
        }
        return false;
    }

    // ****************************************************************
    // Function Name: isCardOnTable
    // Purpose: checks if the card selected by the player is in the table
    // Parameter: cardSelected, a string. The card selected by the player
    //            cardsOnTable, a vector of cards. The ongoing cards on the table
    // Return value: returns a boolean that checks if the card is on table
    // Assistance Received: none
    // ****************************************************************
    final boolean isCardOnTable (String cardSelected, Vector<Card> cardsOnTable)
    {
        for (Card card : cardsOnTable)
        {
            if (card.cardToString().equals(cardSelected)) return true;
        }
        return false;
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

    public void setSingleBuildCard (HashMap<String, Vector<Card>> singleBuildCard)
    {
        this.singleBuildCard = singleBuildCard;
    }

    public void setMoveActionIdentifier(String moveActionIdentifier)
    {
        this.moveActionIdentifier = moveActionIdentifier;
    }

    public void setClickedTableCards(Vector<Card> clickedTableCards)
    {
        this.clickedTableCards = clickedTableCards;
    }

    public void setClickedBuildCards(Vector<Card> clickedBuildCards)
    {
        this.clickedBuildCards = clickedBuildCards;
    }

    public boolean isMoveSuccessful()
    {
        return moveSuccessful;
    }

    public String getMoveExplanation()
    {
        return moveExplanation;
    }

    public void setClickedHandCard(Card clickedHandCard)
    {
        this.clickedHandCard = clickedHandCard;
    }

    final public boolean isMultipleBuildEmpty()
    {
        return multipleBuildCard.isEmpty();
    }

    final public boolean isSingleBuildEmpty()
    {
        return singleBuildCard.isEmpty();
    }

    public int getTourneyScore()
    {
        return tourneyScore;
    }

    public void setTourneyScore(int tourneyScore)
    {
        this.tourneyScore = tourneyScore;
    }


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