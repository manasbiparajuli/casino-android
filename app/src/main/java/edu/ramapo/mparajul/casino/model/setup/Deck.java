//****************************************************
//* Name: Manasbi Parajuli
//* Project: Casino
//* Class: CMPS 366-01
//* Date: 11/20/2018
//****************************************************
package edu.ramapo.mparajul.casino.model.setup;

import java.util.Collections;
import java.util.Vector;

public class Deck
{
    private static final int TOTAL_CARDS = 52;

    // holds the deck of cards for the game
    private Vector<Card> deck;

    // ****************************************************************
    // Function Name: Deck
    // Purpose: constructor to create an instance of deck of cards
    // Parameters: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    Deck()
    {
        deck = new Vector<>(TOTAL_CARDS);
    }

    // ****************************************************************
    // Function Name: createShuffledDeck
    // Purpose: creates a new deck of cards
    // Parameters: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void createShuffledDeck()
    {
        final String[] suits = { "C", "D", "H", "S" };
        final String[] faces = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "X", "J", "Q", "K" };

        for (String suit: suits)
        {
           for (String face: faces)
           {
                deck.add(new Card(suit, face));
           }
        }
        shuffleDeck();
    }

    // ****************************************************************
    // Function Name: shuffledDeck
    // Purpose: to shuffle the cards in a given deck and reverses the order
    // Parameters: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void shuffleDeck()
    {
        Collections.shuffle(this.deck);
        Collections.reverse(this.deck);
    }

    // ****************************************************************
    // Function Name: dealCard
    // Purpose: to draw one card from the deck
    // Parameters: none
    // Return value: The topmost card on the deck, a Card object
    // Assistance Received: none
    // ****************************************************************
    public Card dealCard()
    {
        Card cardOnTop = deck.lastElement();
        deck.remove(deck.size() - 1);
        return cardOnTop;
    }

    // ****************************************************************
    // Function Name: printDeck
    // Purpose: prints all the cards in the deck
    // Parameters: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void printDeck()
    {
        for (Card card : deck)
        {
            System.out.print(card.cardToString() + " ");
        }
        System.out.println(" ");
    }

    // ****************************************************************
    // Function Name: isDeckEmpty
    // Purpose: checks if the deck is empty
    // Parameters: none
    // Return value: returns true or false based on whether the deck is empty or not
    // Assistance Received: none
    // ****************************************************************
    public boolean isDeckEmpty()
    {
        return deck.isEmpty();
    }

    // ****************************************************************
    // Function Name: getDeck
    // Purpose: get the current deck
    // Parameters: none
    // Return value: returns the current deck used in the game
    // Assistance Received: none
    // ****************************************************************
    public Vector<Card> getDeck()
    {
        return deck;
    }

    // ****************************************************************
    // Function Name: setDeck
    // Purpose: set the current deck
    // Parameters: deck, a vector<Card> type. The deck to be used for the game
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setDeck(Vector<Card> deck)
    {
        Collections.reverse(deck);
        this.deck = deck;
    }
}