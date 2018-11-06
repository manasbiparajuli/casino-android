//****************************************************
//* Name: Manasbi Parajuli
//* Project: Casino
//* Class: CMPS 366-01
//* Date: 11/20/2018
//****************************************************
package edu.ramapo.mparajul.casino.model.utility;

class Pairs<U,V>
{
    public final U first;
    public final V second;


    // default constructor for our custom Pairs class
    public Pairs(U first, V second)
    {
        this.first = first;
        this.second = second;
    }

    public U getFirst()
    {
        return first;
    }

    public V getSecond()
    {
        return second;
    }
}