//****************************************************
//* Name: Manasbi Parajuli
//* Project: Casino
//* Class: CMPS 366-01
//* Date: 11/20/2018
//****************************************************
package edu.ramapo.mparajul.casino.model.players;

import android.os.Build;

import java.util.HashMap;
import java.util.Vector;

import edu.ramapo.mparajul.casino.model.setup.Card;
import edu.ramapo.mparajul.casino.model.utility.Pairs;
import edu.ramapo.mparajul.casino.model.utility.Score;

public class Human extends Player
{
    public Human (String name)
    {
        playerName = name;
    }

    // ****************************************************************
    // Function Name: play
    // Purpose: describes the sequence of actions to perform in the current move
    // Parameters: tableCards, a vector of cards. Holds the current cards in play that are on the table
    //             oppoBuild, a hashmap of string and vector of cards. Holds the opponent's single
    // build
    //              opponentPlayerName, a string. Holds the owner of the opponent's build
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void play (Vector<Card> tableCards, HashMap<String, Vector<Card>> oppoBuild,
                      String opponentPlayerName)
    {
        // flag for when human player successfully makes a move
        boolean actionSuccess;

        moveExplanation = "";

        // call move functions based on user choice
        if (moveActionIdentifier.equals("make_single_build"))
        {
            actionSuccess = makeSingleBuild(tableCards);
            if (actionSuccess) { hasCapturedCardsInMove = false; moveSuccessful = true;}
            else { moveSuccessful = false; }
        }
        else if (moveActionIdentifier.equals("make_multiple_build"))
        {
            actionSuccess = makeMultipleBuild(tableCards);
            if (actionSuccess) { hasCapturedCardsInMove = false; moveSuccessful = true;}
            else { moveSuccessful = false; }
        }
        else if (moveActionIdentifier.equals("extend_build"))
        {
            actionSuccess = increaseOpponentBuild(tableCards, oppoBuild, opponentPlayerName);
            if (actionSuccess) { hasCapturedCardsInMove = false; moveSuccessful = true;}
            else { moveSuccessful = false; }
        }
        else if (moveActionIdentifier.equals("capture_individual_set"))
        {
            actionSuccess = captureSetAndIndividualCards(tableCards);
            if (actionSuccess) { hasCapturedCardsInMove = true; moveSuccessful = true;}
            else { moveSuccessful = false; }
        }
        else if (moveActionIdentifier.equals("capture_single_build"))
        {
            actionSuccess = captureSingleBuild();
            if (actionSuccess) { hasCapturedCardsInMove = true; moveSuccessful = true;}
            else { moveSuccessful = false; }
        }
        else if (moveActionIdentifier.equals("capture_multiple_build"))
        {
            actionSuccess = captureMultipleBuild();
            if (actionSuccess) { hasCapturedCardsInMove = true; moveSuccessful = true;}
            else { moveSuccessful = false; }
        }
        else if (moveActionIdentifier.equals("trail"))
        {
            actionSuccess = trailCard(tableCards);
            if (actionSuccess) { hasCapturedCardsInMove = false; moveSuccessful = true;}
            else { moveSuccessful = false; }
        }
    }

    // ****************************************************************
    // Function Name: makeSingleBuild
    // Purpose: performs the sequence of actions to make a single build
    // Parameters: tableCards, a vector of cards. Holds the current cards in play that are on the table
    // Return value: a boolean. Returns whether the player was able to make a single build or not
    // Assistance Received: none
    // ****************************************************************
    public boolean makeSingleBuild (Vector<Card> tableCards)
    {
        // Player cannot make a single build if there are less than two cards on hand
        if (getCardsOnHand().size() < 2)
        {
            moveExplanation = "Invalid. Cannot build when there are less than two cards in hand.";
            return false;
        }

        // Player cannot make a build if there are no cards on the table
        if (tableCards.size() < 1)
        {
            moveExplanation = "Invalid. No cards on table to build with.";
            return false;
        }

        // add the clicked hand card to the player's clicked table cards
        clickedTableCards.add(clickedHandCard);

        // check if there are any cards on hand that will be used to
        // capture the new possible build in the player's next turn
        for (Card card: cardsOnHand)
        {
            // there exists a card on hand that can be used to capture this build in the next turn
            if (calcSingleCardScore(card) == calcLooseCardScore(clickedTableCards))
            {
                // Initialize build process
                // extract the cards from the successful build and add it to the player's
                // list of build
                Vector<Card> build = new Vector<>(clickedTableCards);
                singleBuildCard.put(getPlayerName(), build);

                // remove the card from hand and the table now that the user has made the build
                removeCardFromHand(clickedHandCard);
                removeCardsFromTable(tableCards, clickedTableCards);
                return true;
            }
        }
        moveExplanation = "Invalid build attempted.";
        return false;
    }

    // ****************************************************************
    // Function Name: makeMultipleBuild
    // Purpose: performs the sequence of actions to make a multiple build
    // Parameters: tableCards, a vector of cards. Holds the current cards in play that are on the table
    // Return value: a boolean. Returns whether the player was able to make a multiple build or not
    // Assistance Received: none
    // ****************************************************************
    public boolean makeMultipleBuild (Vector<Card> tableCards)
    {
        // get the build score of last successful single build
        int previousBuildScore = getFirstBuildScore();

        // flag if there does not exist a card in hand that matches the previous single build score
        boolean buildScoreMismatch = false;

        // the loose cards selected to make a build
        Vector<Card> looseCardsSelected = new Vector<>();

        // Player cannot make a multiple build if there are less than two cards in hand
        if (getCardsOnHand().size() < 2)
        {
            moveExplanation = "Invalid. Cannot make multiple build when there are less than " +
                                      "two cards in hand.";
            return false;
        }

        // check if we have a card in hand that matches score of previous single build score
        for (Card handCards : getCardsOnHand())
        {
            if (calcSingleCardScore(handCards) != previousBuildScore)
            {
                buildScoreMismatch = true;
            }
            else
            {
                // since there exists a card that matches previous build score, we can break from loop
                buildScoreMismatch = false;
                break;
            }
        }

        // if there are no cards to match the previous build score, then return from the function
        if (buildScoreMismatch)
        {
            moveExplanation = "Invalid. No card present on hand that matches previous build " +
                                      "score.";
            return false;
        }

        // check if the player can make a multiple build with just the single hand card
        // If this is the case, then the player should not have clicked on any table cards
        if (clickedTableCards.size() == 0)
        {
            if (calcSingleCardScore(clickedHandCard) == previousBuildScore)
            {
                int sameHandCardScore = 0;

                // check if there are at least two cards on hand that match the
                // score of the new build that the player is trying to make
                for (Card handCard : getCardsOnHand())
                {
                    if (calcSingleCardScore(handCard) == previousBuildScore)
                    {
                        sameHandCardScore++;

                        // initiate multiple build if we have two hand cards with same score as previous build score
                        if (sameHandCardScore >= 2)
                        {
                            // add the selected card to the build that the user is trying to make
                            looseCardsSelected.add(clickedHandCard);
                            initiateMultipleBuild(looseCardsSelected);

                            // as the build is successful, remove the card from hand
                            removeCardFromHand(clickedHandCard);
                            return true;
                        }
                    }
                }
            }
            // The single card that the user is trying to use to make a singular-build
            // does not match the score of the previous build
            else
            {
                moveExplanation = "Invalid. Newest build score does not match the previous score of " +
                                          "" + previousBuildScore + ".";
                return false;
            }
        }

        // Proceed to making multiple build with multiple cards

        // Add the clicked hand and table card to a temporary build
        looseCardsSelected.add(clickedHandCard);
        looseCardsSelected.addAll(clickedTableCards);

        // Check if the newest build score matches the previous single build score
        if (calcLooseCardScore(looseCardsSelected) != previousBuildScore)
        {
            moveExplanation = "Invalid. Newest build score does not match the previous score of"
                                      + previousBuildScore + ".";
            return false;
        }
        else
        {
            // check if there is a card on hand that matches the score of the new build
            // that the user is trying to make
            for (Card handCard : getCardsOnHand())
            {
                // check if the hand card score matches the build that the user is trying to make
                if (calcSingleCardScore(handCard) == previousBuildScore)
                {
                    // make multiple build as the build score matched the previous single build score
                    // and there is a card in hand equal to the new build score
                    initiateMultipleBuild(looseCardsSelected);

                    // remove the card from hand and the table now that the user has made the build
                    removeCardFromHand(clickedHandCard);
                    removeCardsFromTable(tableCards, looseCardsSelected);
                    return true;
                }
            }
        }
        moveExplanation = "Invalid multiple build attempted";
        return false;
    }

    // ****************************************************************
    // Function Name: increaseOpponentBuild
    // Purpose: performs the sequence of actions to increase an opponent's build
    // Parameters: -> tableCards, a vector of cards. Holds the current cards in play that are on the table
    //             -> oppoBuild, a tuple of string and vector of cards. Holds the opponent's single build
    // Return value: a boolean. Returns whether the player was able to increase opponent's single build or not
    // Assistance Received: none
    // ****************************************************************
    public boolean increaseOpponentBuild (Vector<Card> tableCards, HashMap<String, Vector<Card>>
                                                                           oppoBuild, String opponentPlayerName)
    {
        // get the build of the opponent
        Vector<Card> opponentBuild = oppoBuild.get(opponentPlayerName);

        // ensure that the player is not increasing their own build or that the opponent's build is not empty
        if (opponentPlayerName.equals(getPlayerName()) || opponentBuild.size() == 0)
        {
            moveExplanation = "Cannot increment your own build or opponent's build is empty!";
            return false;
        }

        // add the selected card from hand to the opponent's build to check if
        // extending the build is possible
        opponentBuild.add(clickedHandCard);

        // check if there is a handcard that the player can use to increase the opponent's build
        // by matching the handcard score against the score of the possible extended build
        for (Card handCard : getCardsOnHand())
        {
            if (calcSingleCardScore(handCard) == calcLooseCardScore(opponentBuild))
            {
                // Stores all the cards in the opponent's single build which will be used to check
                // against the list of build cards clicked by the user and to verify if
                // the correct builds have been selected to extend
                int clickedBuildCount = 0;
                for (Card card : opponentBuild)
                {
                    // check if all the cards clicked exists in the player's single build
                    if (clickedBuildCards.contains(card))
                    {
                        clickedBuildCount++;
                    }
                }

                // Check if the player selected the wrong build to capture
                if (clickedBuildCount != opponentBuild.size())
                {
                    moveExplanation = "Invalid selection of opponent's build cards.";
                    return false;
                }
                firstBuildScore = calcSingleCardScore(handCard);

                // store the new build into the human's single build
                // and set the opponent's build to be empty
                singleBuildCard.put(getPlayerName(), opponentBuild);
                Vector<Card> empty = new Vector<>();
                oppoBuild.put(opponentPlayerName, empty);

                // As the player successfully increased the opponent's build,
                // remove the card from the player's hand
                removeCardFromHand(clickedHandCard);
                return true;
            }
        }
        moveExplanation = "Invalid attempt to extend opponent's build.";
        return false;
    }

    // ****************************************************************
    // Function Name: initiateMultipleBuild
    // Purpose: makes multiple build from the successfully created multiple build
    // Parameters: -> looseCardsSelected, a vector of cards. Holds the cards that make up the new multiple build
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void initiateMultipleBuild (Vector<Card> looseCardsSelected)
    {
        // cards in the single build
        Vector<Card> singleBuild = singleBuildCard.get(getPlayerName());

        // store multiple build card values
        Vector<Vector<Card>> multipleBuild = new Vector<>();

        // push the previous single build into the multiple build
        multipleBuild.add(singleBuild);

        // push the newest build into the multiple build
        multipleBuild.add(looseCardsSelected);

        // add the newest multiple builds to the HashMap
        multipleBuildCard.put(getPlayerName(), multipleBuild);

        // empty the single build HashMap
        singleBuild = new Vector<>();
        singleBuildCard.put(getPlayerName(), singleBuild);
    }

    // ****************************************************************
    // Function Name: captureMultipleBuild
    // Purpose: performs the sequence of actions to capture player's multiple build
    // Parameters: none
    // Return value: a boolean. Returns whether the player was able to capture their multiple build
    // Assistance Received: none
    // ****************************************************************
    public boolean captureMultipleBuild()
    {
        // Check if the player has multiple build to capture
        if (isMultipleBuildEmpty())
        {
            moveExplanation = "Invalid. No multiple builds exist to capture!!";
            return false;
        }

        // check if the selected hand card score matches the multiple build card score
        if (calcSingleCardScore(clickedHandCard) == firstBuildScore)
        {
            Vector<Vector<Card>> multipleBuild = multipleBuildCard.get(getPlayerName());

            // Stores all the cards in the multiple build which will be used to check
            // against the list of build cards clicked by the user and to verify if
            // the correct builds have been selected
            Vector<Card> buildList = new Vector<>();

            int clickedBuildCount = 0;

            for (Vector<Card> tempSingleBuild : multipleBuild)
            {
                buildList.addAll(tempSingleBuild);
            }

            for (Card card : buildList)
            {
                if (clickedBuildCards.contains(card))
                {
                    clickedBuildCount++;
                }
            }

            // Check if the player selected the wrong build to capture
            if (clickedBuildCount != buildList.size())
            {
                moveExplanation = "Invalid build cards selected to capture.";
                return false;
            }

            // add the cards in the multiple build to the pile of the human player
            for (Vector<Card> singleBuild : multipleBuild)
            {
                cardsOnPile.addAll(singleBuild);
            }

            // after capturing the card, empty the multiple build of the
            // player and set the build score to 0
            multipleBuild = new Vector<>();
            multipleBuildCard.put(getPlayerName(), multipleBuild);
            firstBuildScore = 0;

            // add selected hand card to the pile and then remove it from the player's hand
            cardsOnPile.add(clickedHandCard);
            removeCardFromHand(clickedHandCard);

            return true;
        }
        else
        {
            moveExplanation = "Invalid. Selected hand card does not match the multiple build " +
                                      "score of " + firstBuildScore;
            return false;
        }
    }

    // ****************************************************************
    // Function Name: captureSingleBuild
    // Purpose: performs the sequence of actions to capture player's single build
    // Parameters: none
    // Return value: a boolean. Returns whether the player was able to capture their single build
    // Assistance Received: none
    // ****************************************************************
    public boolean captureSingleBuild()
    {
        // Check if the player has single build to capture
        if (isSingleBuildEmpty())
        {
            moveExplanation = "Invalid. No single build exists to capture!!";
            return false;
        }

        // check if the selected hand card score matches the single build card score
        if (calcSingleCardScore(clickedHandCard) == firstBuildScore)
        {
            // Check if the player has selected the correct build to capture with the hand card
            if (calcSingleCardScore(clickedHandCard) != calcLooseCardScore(clickedBuildCards))
            {
                moveExplanation = "Invalid. Build and hand card score mismatch.";
                return false;
            }

            // Stores all the cards in the single build which will be used to check
            // against the list of build cards clicked by the user and to verify if
            // the correct builds have been selected
            Vector<Card> buildList = new Vector<>(singleBuildCard.get(getPlayerName()));

            int clickedBuildCount = 0;

            for (Card card : buildList)
            {
                // check if all the cards clicked exists in the player's single build
                if (clickedBuildCards.contains(card))
                {
                    clickedBuildCount++;
                }
            }

            // Check if the player selected the wrong build to capture
            if (clickedBuildCount != buildList.size())
            {
                moveExplanation = "Invalid build cards selected to capture.";
                return false;
            }

            // single build card of the player
            Vector<Card> singleBuild = singleBuildCard.get(getPlayerName());

            // add cards from the single build to the pile
            cardsOnPile.addAll(singleBuild);

            // after capturing the card, empty the single build of the player
            // and set the build score to 0
            singleBuild = new Vector<>();
            singleBuildCard.put(getPlayerName(), singleBuild);
            firstBuildScore = 0;

            // add selected hand card to the pile and then remove it from the player's hand
            cardsOnPile.add(clickedHandCard);
            removeCardFromHand(clickedHandCard);

            return true;
        }
        else
        {
            moveExplanation = "Invalid. Selected hand card does not match the single build score of "
                                      + firstBuildScore;
            return false;
        }
    }

    // ****************************************************************
    // Function Name: captureSetAndIndividualCards
    // Purpose: performs the sequence of actions to capture set of cards and individual loose cards
    // Parameters: -> tableCards, a vector of cards. Holds the current cards in play that are on the table
    // Return value: a boolean. Returns whether the player was able to capture set of cards or
    //                      loose cards
    // Assistance Received: none
    // ****************************************************************
    public boolean captureSetAndIndividualCards(Vector<Card> tableCards)
    {
        // calculate the hand card score
        int handCardScore = calcSingleCardScore(clickedHandCard);

        int setCountFound = 0;

        // list of possible sets
        Vector<Card> possibleSets = new Vector<>();

        // If there is only one clicked card from the table, then it must be a matching card with
        // the hand card that the player is trying to capture
        if (clickedTableCards.size() == 1)
        {
            for (Card card : clickedTableCards)
            {
                // check if there are any single cards in the player's clicked table cards
                // that matches the score of the clicked hand card
                if (calcSingleCardScore(card) == handCardScore)
                {
                    // Add the card to pile as capturing has been successful and
                    // remove the selected card from hand and the table
                    cardsOnPile.add(clickedHandCard);
                    cardsOnPile.addAll(clickedTableCards);
                    removeCardFromHand(clickedHandCard);
                    removeCardsFromTable(tableCards, clickedTableCards);

                    return true;
                }
            }
            moveExplanation = "Please select a matching loose card to capture from the table";
            return false;
        }

        Score score = new Score();


        // enlist the possible power sets based on number of player's clicked table cards
        Vector<String> buildComb = score.powerSet(clickedTableCards.size());

        // make a map of the scores possible from clicked table cards
        score.buildScoreMap(buildComb, clickedTableCards);

        // get the map of <set score , possible set Combination>
        Vector<Pairs<Integer, Vector<Card>>> mapSet = score.getBuildCombination();

        // Check if the player has selected the individual loose card that matches the score
        // of their picked hand card
        // It is mandatory to capture the loose card if the selected hand card matches any single
        // cards in the table
        for (Card card : tableCards)
        {
            // Player did not select a table card that matched the score of the clicked hand card
            if (calcSingleCardScore(card) == handCardScore && !clickedTableCards.contains(card))
            {
                moveExplanation = "Matching single loose card must be captured using the hand card";
                return false;
            }
        }

        // iterate through the map and check if the key matches the handCardScore
        // add the possible set combination to the list
        for (Pairs<Integer, Vector<Card>> buildList : mapSet)
        {
            // the build combination score matches the hand card of the player
            if (buildList.first == handCardScore)
            {
                // keep track of all the possible sets in the build
                possibleSets.addAll(buildList.second);
            }
        }

        for (Card card : clickedTableCards)
        {
            // count the number of common cards in the player's clicked table cards
            // with the possible sets.
            if (possibleSets.contains(card))
            {
                setCountFound++;
            }

            // check if there are any single cards in the player's clicked table cards
            // that matches the score of the clicked hand card
            if (calcSingleCardScore(card) == handCardScore)
            {
                possibleSets.add(card);
                setCountFound++;
            }
        }

        // the player's table cards does not match the sets count
        if (setCountFound != clickedTableCards.size())
        {
            moveExplanation = "Cannot capture set of cards";
            return false;
        }

        // Add the cards to pile as capturing has been successful and
        // remove the selected cards from hand and the table
        cardsOnPile.add(clickedHandCard);
        cardsOnPile.addAll(clickedTableCards);
        removeCardFromHand(clickedHandCard);
        removeCardsFromTable(tableCards, clickedTableCards);

        return true;
    }

    // ****************************************************************
    // Function Name: trailCard
    // Purpose: performs the sequence of actions to trail a card
    // Parameters: -> tableCards, a vector of cards. Holds the current cards in play that are on the table
    // Return value: a boolean. Returns whether the player was able to trail a card or not
    // Assistance Received: none
    // ****************************************************************
    public boolean trailCard (Vector<Card> tableCards)
    {
        // Warn the human player that trailing is not possible when they own a build
        if (!isSingleBuildEmpty() || !isMultipleBuildEmpty())
        {
            moveExplanation = "Cannot trail if you have a build owned!";
            return false;
        }

        // Check if there are any loose cards on table that the player did not capture using this
        // card
        if (tableCards.size() > 0)
        {
            for (Card table : tableCards)
            {
                if (calcSingleCardScore(clickedHandCard) == calcSingleCardScore(table))
                {
                    moveExplanation = "Invalid. Cannot trail when you have a matching loose card" +
                                              " in the table.";
                    return false;
                }
            }
        }

        // add the selected card to trail on the table and then remove from the player's hand
        tableCards.add(clickedHandCard);
        removeCardFromHand(clickedHandCard);
        return true;
    }
}