//****************************************************
//* Name: Manasbi Parajuli
//* Project: Casino
//* Class: CMPS 366-01
//* Date: 11/20/2018
//****************************************************
package edu.ramapo.mparajul.casino.model.utility;

// Programmer defined class that stores a pair of objects
public class Pairs<U,V>
{
    // Store the first object
    public final U first;

    // Store the second object
    public final V second;

    // ****************************************************************
    // Function Name: Pairs
    // Purpose: serves as a default constructor for Pairs class
    // Parameters: -> first, an object. Holds the fist object of the pair
    //             -> second, an object. Holds the second object of the pair
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public Pairs(U first, V second)
    {
        this.first = first;
        this.second = second;
    }

    // ****************************************************************
    // Function Name: getFirst
    // Purpose: gets the first object in the pair
    // Parameter: none
    // Return value: an Object corresponding to the pairs' first value
    // Assistance Received: none
    // ****************************************************************
    public U getFirst()
    {
        return first;
    }

    // ****************************************************************
    // Function Name: getSecond
    // Purpose: gets the second object in the pair
    // Parameter: none
    // Return value: an Object corresponding to the pairs' second value
    // Assistance Received: none
    // ****************************************************************
    public V getSecond()
    {
        return second;
    }
}