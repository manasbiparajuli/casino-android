//****************************************************
//* Name: Manasbi Parajuli
//* Project: Casino
//* Class: CMPS 366-01
//* Date: 11/20/2018
//****************************************************
package edu.ramapo.mparajul.casino.model.utility;

import edu.ramapo.mparajul.casino.model.setup.Card;
import java.util.Vector;

public class Score
{
    // stores the score of the players
    private int playerOneScore;
    private int playerTwoScore;
    private int playerOneTotalCards;
    private int playerTwoTotalCards;
    private int playerOneTotalSpades;
    private int playerTwoTotalSpades;

    // the cards on pile of the players
    private Vector<Card> playerOnePile, playerTwoPile;

    // store the list of pairs of possible build scores with their corresponding build cards
    private Vector<Pairs<Integer , Vector<Card>>> buildCombinations = new Vector<>();

    // ****************************************************************
    // Function Name: Score
    // Purpose: default constructor for Score class
    // Parameters: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public Score()
    {
        this.playerOneScore = 0;
        this.playerTwoScore = 0;
        this.playerOneTotalCards = 0;
        this.playerOneTotalSpades = 0;
        this.playerTwoTotalSpades = 0;
        this.playerTwoTotalCards = 0;
    }

    // ****************************************************************
    // Function Name: Score
    // Purpose: default constructor for Score class that takes in the pile cards of the
    //          players and calculates the score
    // Parameters: playerOnePile, a vector Card object. The cards in pile of the first player
    //             playerTwoPile, a vector Card object. The cards in pile of the second player
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public Score(Vector<Card> playerOnePile, Vector<Card> playerTwoPile)
    {
        this.playerOneScore = 0;
        this.playerTwoScore = 0;
        this.playerOnePile = playerOnePile;
        this.playerTwoPile = playerTwoPile;
        this.playerOneTotalCards = 0;
        this.playerOneTotalSpades = 0;
        this.playerTwoTotalSpades = 0;
        this.playerTwoTotalCards = 0;
    }

    // ****************************************************************
    // Function Name: calculateMostCards
    // Purpose: sets the scores of the player with the most number of cards in pile
    // Parameter : none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    private void calculateMostCards()
    {
        int plOneTotal = playerOnePile.size();
        int plTwoTotal = playerTwoPile.size();

        playerOneTotalCards = plOneTotal;
        playerTwoTotalCards = plTwoTotal;

        // Player with most cards gets a score of 3
        if (plOneTotal > plTwoTotal) { setPlayerOneScore(3); }
        if (plOneTotal < plTwoTotal) { setPlayerTwoScore(3); }
    }

    // ****************************************************************
    // Function Name: calculateMostSpades
    // Purpose: sets the scores of the player with the most number of spades in pile
    // Parameter : none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    private void calculateMostSpades()
    {
        int plOneTotal = 0;
        int plTwoTotal = 0;

        for (Card card : playerOnePile)
        {
            if (card.getSuit().equals("S")) {plOneTotal++; }
        }

        for (Card card : playerTwoPile)
        {
            if (card.getSuit().equals("S")) {plTwoTotal++; }
        }

        playerOneTotalSpades = plOneTotal;
        playerTwoTotalSpades = plTwoTotal;

        // Player with most spades gets a score of 1
        if (plOneTotal > plTwoTotal) { setPlayerOneScore(1); }
        if (plOneTotal < plTwoTotal) { setPlayerTwoScore(1); }
    }

    // ****************************************************************
    // Function Name: calculate10OfDiamonds
    // Purpose: sets the score of the player with 10 of Diamonds a value of 2
    // Parameter : none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    private void calculate10OfDiamonds()
    {
        // Player with 10 of Diamonds scores 2 points

        for (Card card : playerOnePile)
        {
            if (card.cardToString().equals("DX"))
            {
                setPlayerOneScore(2);
                break;
            }
        }

        for (Card card : playerTwoPile)
        {
            if (card.cardToString().equals("DX"))
            {
                setPlayerTwoScore(2);
                break;
            }
        }
    }

    // ****************************************************************
    // Function Name: calculate2OfSpades
    // Purpose: sets the score of the player with 2 of Spades a value of 1
    // Parameter : none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    private void calculate2OfSpades()
    {
        // Player with 2 of Spades scores 1 points

        for (Card card : playerOnePile)
        {
            if (card.cardToString().equals("S2"))
            {
                setPlayerOneScore(1);
                break;
            }
        }

        for (Card card : playerTwoPile)
        {
            if (card.cardToString().equals("S2"))
            {
                setPlayerTwoScore(1);
                break;
            }
        }
    }

    // ****************************************************************
    // Function Name: calculatePerAce
    // Purpose: adds a score to every Ace card that a player has in pile
    // Parameter : none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    private void calculatePerAce()
    {
        int plOneTotal = 0;
        int plTwoTotal = 0;

        for (Card card : playerOnePile)
        {
            if (card.getFace().equals("A")) { plOneTotal++; }
        }

        for (Card card : playerTwoPile)
        {
            if (card.getFace().equals("A")) { plTwoTotal++; }
        }

        setPlayerOneScore(plOneTotal);
        setPlayerTwoScore(plTwoTotal);
    }

    // ****************************************************************
    // Function Name: calculateTotalScore
    // Purpose: calculates the total score of the player by applying the scoring rules
    // Parameter : none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void calculateTotalScore()
    {
        calculateMostCards();
        calculateMostSpades();
        calculate10OfDiamonds();
        calculate2OfSpades();
        calculatePerAce();
    }

    // ****************************************************************
    // Function Name: cardScore
    // Purpose: calculate the numerical equivalent of a card
    // Parameters: card, a Card object
    // Return value: integer value, the numerical equivalent of a card
    // Assistance Received: none
    // ****************************************************************
    private int cardScore(Card card)
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
    // Function Name: calcBuildScore
    // Purpose: calculate the score of the build
    // Parameters: buildCards, a vector of strings. It holds the build combination.
    // Return value: integer value, the score of the build
    // Assistance Received: none
    // ****************************************************************
    private int calcBuildScore(Vector<Card> buildCards)
    {
        int score = 0;
        // Go through the build and add the score of each of the cards
        for (Card cards : buildCards)
        {
            score += cardScore(cards);
        }
        return score;
    }

    // ****************************************************************
    // Function Name: powerSet
    // Purpose: calculate the power set based on the size of the table
    // Parameters: tableSize, an integer value. It holds the size of the table
    // Return value: a vector of string that lists all the possible valid power sets
    // Assistance Received: none
    // ****************************************************************
    public Vector<String> powerSet (int tableSize)
    {
        // the power set that will be used to create a map of possible scores
        Vector<String> validSets = new Vector<>();

        // all the power sets possible
        StringBuilder temp;
        Vector<String> totalSets = new Vector<>();

        Vector<Character> index = new Vector<>();

        // input numbers into the vector that will be used to create the possible power set
        for (int i = 0; i < tableSize; i++) {
            index.add(Character.forDigit(i, 10));
        }

        // total number of power set elements possible
        int N = (int) Math.pow(2, tableSize);

        for (int i = 0; i < N; i++) {
            temp = new StringBuilder();

            // check every bit of i
            for (int j = 0; j < tableSize; j++) {
                // if jth bit of i set, print index[j]
                if ((i & (1 << j)) > 0) {
                    temp.append(String.valueOf(index.get(j)));
                }
            }
            totalSets.add(temp.toString());
        }

        // Get only the combinations that has more than one card as we don't form a build with only a single card
        for (String a : totalSets)
        {
            if (a.length() > 1)
            {
                validSets.add(a);
            }
        }
        return validSets;
    }

    // ****************************************************************
    // Function Name: buildScoreMap
    // Purpose: constructs a map of possible builds with the current table cards corresponding to
    //          the build score
    // Parameters: -> powerSets, a vector of string. It holds the possible power set combinations that
    //                      can be formed using the current cards in the table.
    //             -> tableCards, a vector of cards. It holds the current table cards.
    //
    //       Both parameters have been passed by reference.
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void buildScoreMap(Vector<String> powerSets, Vector<Card> tableCards)
    {
        // the build combination score
        int buildCombiScore = 0;

        // the combination of cards for the build
        Vector<Card> buildCards;

        // the integer representation of index that was converted from a character
        int buildIndex;

        // map the generated power set to the respective indices of cards in the table
        for (String build : powerSets)
        {
            buildCards = new Vector<>();

            for (char c : build.toCharArray())
            {
                buildIndex = c - '0';
                buildCards.add(tableCards.elementAt(buildIndex));
            }

            // calculate the build score and insert into the vector of pairs of score and build
            buildCombiScore = calcBuildScore(buildCards);
            buildCombinations.add(new Pairs(buildCombiScore, buildCards));
        }
    }

    // ****************************************************************
    // Function Name: printBuildCombinations
    // Purpose: prints the pair of the build and its corresponding score
    // Parameters: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void printBuildCombinations()
    {
        for (Pairs<Integer, Vector<Card>> build : buildCombinations)
        {
            System.out.print(build.first);

            for (Card card : build.second)
            {
                System.out.print(card.cardToString() + " ");
            }
            System.out.println(" ");
        }
    }

    // ****************************************************************
    // Function Name: setPlayerOneScore
    // Purpose: updates the score of player one
    // Parameter: score, an integer value. This score will be added
    //            to the player's total
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setPlayerOneScore(int score)
    {
        playerOneScore += score;
    }

    // ****************************************************************
    // Function Name: setPlayerTwoScore
    // Purpose: updates the score of player two
    // Parameter: score, an integer value. This score will be added
    //            to the player's total
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void setPlayerTwoScore(int score)
    {
        playerTwoScore += score;

    }

    public int getPlayerOneTotalCards()
    {
        return playerOneTotalCards;
    }

    public int getPlayerTwoTotalCards()
    {
        return playerTwoTotalCards;
    }

    public int getPlayerOneTotalSpades()
    {
        return playerOneTotalSpades;
    }

    public int getPlayerTwoTotalSpades()
    {
        return playerTwoTotalSpades;
    }

    // ****************************************************************
    // Function Name: getPlayerOneScore
    // Purpose: gets the score of player one
    // Parameter: none
    // Return value: The current score of the player one
    // Assistance Received: none
    // ****************************************************************
    public int getPlayerOneScore()
    {
        return playerOneScore;
    }

    // ****************************************************************
    // Function Name: getPlayerTwoScore
    // Purpose: gets the score of player two
    // Parameter: none
    // Return value: The current score of player two
    // Assistance Received: none
    // ****************************************************************
    public int getPlayerTwoScore()
    {
        return playerTwoScore;
    }

    // ****************************************************************
    // Function Name: getBuildCombination
    // Purpose: gets the vector of pair of possible build and corresponding scores
    // Parameter: none
    // Return value: vector of pair of possible build and corresponding scores
    // Assistance Received: none
    // ****************************************************************
    public Vector<Pairs<Integer, Vector<Card>>> getBuildCombination()
    {
        return buildCombinations;
    }
}