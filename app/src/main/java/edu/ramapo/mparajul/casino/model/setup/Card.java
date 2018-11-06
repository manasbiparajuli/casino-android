//****************************************************
//* Name: Manasbi Parajuli
//* Project: Casino
//* Class: CMPS 366-01
//* Date: 11/20/2018
//****************************************************
package edu.ramapo.mparajul.casino.model.setup;

public class Card
{
    // holds face value of the card
    private String face;

    // holds suit value of the card
    private String suit;

    // ****************************************************************
    // Function Name: Card
    // Purpose: serves as a default constructor
    // Parameters: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public Card()
    {
    }

    // ****************************************************************
    // Function Name: Card
    // Purpose: constructor to create a card instance
    // Parameters:
    //			suit, a string to hold the suit value of the card
    //			face, a string to hold the value of the face card
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public Card(String suit, String face)
    {
        this.suit = suit;
        this.face = face;
    }

    // ****************************************************************
    // Function Name: getFace
    // Purpose: return the face value of a card
    // Parameters: none
    // Return value: face of the card
    // Assistance Received: none
    // ****************************************************************
    public String getFace()
    {
        return face;
    }

    // ****************************************************************
    // Function Name: getSuit
    // Purpose: return the suit value of a card
    // Parameters: none
    // Return value: suit value of the card
    // Assistance Received: none
    // ****************************************************************
    public String getSuit()
    {
        return suit;
    }

    // ****************************************************************
    // Function Name: cardToString
    // Purpose: get the string value of the card
    // Parameters: none
    // Return value: the string representation of the card
    // Assistance Received: none
    // ****************************************************************
    final public String cardToString()
    {
        return suit+face;
    }


    // ****************************************************************
    // Function Name: equals
    // Purpose: checks if two Card objects are equal
    // Parameters: obj, an Object that will be compared with the current object
    // Return value: returns whether the cards are equal or not
    // Assistance Received: none
    // ****************************************************************
    @Override
    public boolean equals(Object obj)
    {
        // checks if the passed object is the same
        if (this == obj) return true;

        if (this.getClass() != obj.getClass()) return false;

        // compares the string equivalent of the cards from two objects
        return (this.cardToString().equals(((Card) obj).cardToString()));
    }

    // ****************************************************************
    // Function Name: hashCode
    // Purpose: calculates the hash value based on a card
    // Parameters: none
    // Return value: returns a hash value to be used by equals function
    // Assistance Received: none
    // ****************************************************************
    @Override
    public int hashCode()
    {
        int hash = 17;
        hash = 53 * hash + (this.face != null ? this.face.hashCode() : 0);
        hash = 53 * hash + (this.suit != null ? this.suit.hashCode() : 0);
        return hash;
    }
}