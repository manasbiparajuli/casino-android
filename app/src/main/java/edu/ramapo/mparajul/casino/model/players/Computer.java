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
import edu.ramapo.mparajul.casino.model.utility.Score;
import edu.ramapo.mparajul.casino.model.utility.Pairs;

public class Computer extends Player
{
    private Score score = new Score();

    public Computer(String name)
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
        // Order in which computer will make a move
        // 1) Increase opponent's build
        // 2) Capture multiple build
        // 3) Make multiple build
        // 4) Capture single build
        // 5) Make single build
        // 6) Capture cards (both as a set or individual cards)
        // 7) Trail card

        // flag for when computer successfully makes a move
        boolean actionSuccess;

        moveExplanation = "";

        // Make moves respectively. Return from the function if successful as a player can only make one move.
        actionSuccess = increaseOpponentBuild(tableCards, oppoBuild, opponentPlayerName);
        if (actionSuccess)
        {
            System.out.println("Increased Opponent's Build.");
            hasCapturedCardsInMove = false;
            moveSuccessful = true;
            return;
        }

        actionSuccess = captureMultipleBuild();
        if (actionSuccess)
        {
            System.out.println("Captured multiple build");
            hasCapturedCardsInMove = true;
            moveSuccessful = true;
            return;
        }

        actionSuccess = makeMultipleBuild(tableCards);
        if (actionSuccess)
        {
            System.out.println("Multiple build successful");
            hasCapturedCardsInMove = false;
            moveSuccessful = true;
            return;
        }

        actionSuccess = captureSingleBuild();
        if (actionSuccess)
        {
            System.out.println("Captured single build");
            hasCapturedCardsInMove = true;
            moveSuccessful = true;
            return;
        }

        actionSuccess = makeSingleBuild(tableCards);
        if (actionSuccess)
        {
            System.out.println("Single build successful");
            hasCapturedCardsInMove = false;
            moveSuccessful = true;
            return;
        }

        actionSuccess = captureSetAndIndividualCards(tableCards);
        if (actionSuccess)
        {
            System.out.println("Captured set and individual cards");
            hasCapturedCardsInMove = true;
            moveSuccessful = true;
            return;
        }

        actionSuccess = trailCard(tableCards);
        if (actionSuccess)
        {
            System.out.println("Card trailed");
            hasCapturedCardsInMove = false;
            moveSuccessful = true;
            return;
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
        // ensure that we check all the possible keys before we return
        boolean singleBuildPossible = false;

        // the valid build cards used to make a single build
        Vector<Card> matchedTableCards = new Vector<>();

        // player cannot make a build if there are less than two cards in their hand
        if (getCardsOnHand().size() < 2) { return false; }

        // Temporarily add a card to table to check if a build is possible
        for (Card handCardToBuildWith : getCardsOnHand())
        {
            // add card from hand temporarily to check if computer can make a build using this card
            tableCards.add(handCardToBuildWith);

            // enlist the possible power sets based on tableSize
            Vector<String> buildComb = new Vector<>();
            buildComb = score.powerSet(tableCards.size());

            // make a map of the scores possible from valid builds
            score.buildScoreMap(buildComb, tableCards);

            // get the map of <BuildScores, BuildCombination>
            Vector<Pairs<Integer, Vector<Card>>> map = new Vector<>();
            map = score.getBuildCombination();

            // Loop through the hand cards to check if a card score matches the key in map
            for (Card handCard : getCardsOnHand())
            {
                // calculate the hand card score for each card
                int handCardScore = calcSingleCardScore(handCard);

                // iterate through the map and check if the key matches the handCardScore
                for (Pairs<Integer, Vector<Card>> build : map)
                {
                    // match found
                    if (build.first == handCardScore)
                    {
                        setFirstBuildScore(handCardScore);
                        singleBuildPossible = true;
                        matchedTableCards = build.second;
                    }
                }
            }
            // remove the card from the table that we temporarily added to get possible build combinations
            tableCards.remove(handCardToBuildWith);
        }

        // Since hand card score matched the key in map at least once, we return true
        if (singleBuildPossible)
        {
            // remove card that made up build successfully from the player's hand
            Card cardToRemove = new Card();

            // Check for any matching card in the card list
            for (Card handCard : getCardsOnHand())
            {
                // return the card if found
                if (matchedTableCards.contains(handCard))
                {
                    cardToRemove = handCard;
                }
            }

            // if the user requested help using computer strategy, return the move explanation
            if (helpRequested)
            {
                // Explain move reasoning
                helpExplanation = getPlayerName() + " should play " + cardToRemove.cardToString()
                                          + " to create a single build of ";
                for (Card cards : matchedTableCards)
                {
                    helpExplanation += cards.cardToString() + " ";
                }
                return true;
            }

            singleBuildCard.put(getPlayerName(), matchedTableCards);

            // Explain move reasoning
            moveExplanation = getPlayerName() + " played " + cardToRemove.cardToString() + " to " +
                                      "create a single build of ";

            for (Card cards : matchedTableCards)
            {
                moveExplanation += cards.cardToString() + " ";
            }

            removeCardFromHand(cardToRemove);

            // remove cards that made up build successfully from the table
            removeCardsFromTable(tableCards, matchedTableCards);
            return true;
        }
        // no matching keys found
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

        // ensure that we check all the possible keys before we return
        boolean multipleBuildPossible = false;

        // the valid build cards used to make a multiple build
        Vector<Card> matchedTableCards = new Vector<>();

        // player cannot make a multibuild if there are less than two cards in their hand
        if (getCardsOnHand().size() < 2) { return false; }

        // multibuild is not possible if the player does not have a single build already
        if (isSingleBuildEmpty()) { return false; }

        for (Card handCards : getCardsOnHand())
        {
            // check if we have a card in hand that matches score of previous single build score
            if (calcSingleCardScore(handCards) != previousBuildScore)
            {
                buildScoreMismatch = true;
            }
            // if found, break from the loop
            else
            {
                buildScoreMismatch = false;
                break;
            }
        }

        // if there are no cards to match the previous build score, then return from the function
        if (buildScoreMismatch) { return false; }

        //////////
        // We now repeat the steps that we took to build a single build
        //////////

        // we check if there are two hand cards equal the score
        // of the previous single build score. We need two cards because one card will be used to build a multibuild
        // while the other card will be used to capture the multiple build in the player's next turn

        // store the number of cards with the same score as the previous build score
        int sameCardScoreCount = 0;

        // loop through the cards in the player's hand
        for (Card handCardToBuildWith : getCardsOnHand())
        {
            if (calcSingleCardScore(handCardToBuildWith) == previousBuildScore)
            {
                sameCardScoreCount++;

                // we have cards with the same score. So, we can make a multiple build
                if (sameCardScoreCount >= 2)
                {
                    matchedTableCards.add(handCardToBuildWith);

                    // if the user requested help using computer strategy, return the move explanation
                    if (helpRequested)
                    {
                        // Explain move reasoning
                        helpExplanation = getPlayerName() + " should play " + handCardToBuildWith.cardToString() +
                                                  " to create a multiple build of ";
                        for (Card cards : matchedTableCards)
                        {
                            helpExplanation += cards.cardToString() + " ";
                        }
                        helpExplanation += " so that you can increase your number of builds.";

                        return true;
                    }

                    // Explain move reasoning
                    moveExplanation = getPlayerName() + " played " + handCardToBuildWith.cardToString() + "" +
                                              " to create a multiple build of ";
                    for (Card cards : matchedTableCards)
                    {
                        moveExplanation += cards.cardToString() + " ";
                    }
                    moveExplanation += ". It wanted to increase the number of builds.";

                    removeCardFromHand(handCardToBuildWith);
                    initiateMultipleBuild(matchedTableCards);

                    // remove cards that made up build successfully from the table
                    removeCardsFromTable(tableCards, matchedTableCards);
                    return true;
                }
            }
        }

        // if there are no more than 1 card that match the score of the previous build,
        // then multibuild is not possible
        if (sameCardScoreCount < 2)
        {
            return false;
        }
        if (tableCards.size() > 0)
        {
            // Temporarily add a card to table to check if a build is possible
            for (Card handCardToBuildWith : getCardsOnHand())
            {
                // add card from hand temporarily to check if computer can make a build using this card
                tableCards.add(handCardToBuildWith);

                // enlist the possible power sets based on tableSize
                Vector<String> buildComb = new Vector<>();
                buildComb = score.powerSet(tableCards.size());

                // make a map of the scores possible from valid builds
                score.buildScoreMap(buildComb, tableCards);

                // get the map of <BuildScores, BuildCombination>
                Vector<Pairs<Integer, Vector<Card>>> map = new Vector<>();
                map = score.getBuildCombination();

                // Loop through the hand cards to check if a card score equivalent to 
                // previous build score matches a key in map
                for (Card handCard : getCardsOnHand())
                {
                    // calculate the hand card score for each card
                    int handCardScore = calcSingleCardScore(handCard);

                    // only traverse through map if the hand card score is equal to the previous build score
                    if (handCardScore == previousBuildScore)
                    {
                        // iterate through the map and check if the key matches the handCardScore
                        for (Pairs<Integer, Vector<Card>> build : map)
                        {
                            // match found
                            if (build.first == handCardScore)
                            {
                                multipleBuildPossible = true;
                                matchedTableCards = build.second;
                            }
                        }
                    }
                }
                // remove the card from the table that we temporarily added to get possible build combinations
                tableCards.remove(handCardToBuildWith);
            }
        }

        // Since hand card score matched the key in map at least once, we return true
        if (multipleBuildPossible)
        {
            // remove card that made up build successfully from the player's hand
            Card cardToRemove = new Card();

            // Check for any matching card in the card list
            for (Card handCard : getCardsOnHand())
            {
                // return the card if found
                if (matchedTableCards.contains(handCard))
                {
                    cardToRemove = handCard;
                }
            }

            if (helpRequested)
            {
                // Explain move reasoning
                helpExplanation = getPlayerName() + " should play " + cardToRemove.cardToString() +
                                          " to create a multiple build of ";
                for (Card cards : matchedTableCards)
                {
                    helpExplanation += cards.cardToString() + " ";
                }
                helpExplanation += " to increase the number of builds.";

                return true;
            }

            // Explain move reasoning
            moveExplanation = getPlayerName() + " played " + cardToRemove.cardToString() + "" +
                                      " to create a multiple build of ";
            for (Card cards : matchedTableCards)
            {
                moveExplanation += cards.cardToString() + " ";
            }
            moveExplanation += ". It wanted to increase the number of builds.";

            removeCardFromHand(cardToRemove);
            initiateMultipleBuild(matchedTableCards);

            // remove cards that made up build successfully from the table
            removeCardsFromTable(tableCards, matchedTableCards);
            return true;
        }
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
    public boolean increaseOpponentBuild (Vector<Card> tableCards,
                                          HashMap<String, Vector<Card>> oppoBuild, String opponentPlayerName)
    {
        // cards in the build of the opponent
        Vector<Card> oppnBuildCard = oppoBuild.get(opponentPlayerName);

        // return false if the build is not owned by the player or if the opponent's build is empty
        if (!oppoBuild.containsKey(getPlayerName()) || (oppnBuildCard.size() == 0))
        {
            return false;
        }

        // player cannot increase an opponent's build if there are less than two cards in their hand
        if (getCardsOnHand().size() < 2) { return false; }

        // temporarily add cards to the opponent's build and loop through the cards in the hand
        // to check if there exists a hand card that will match the score of the extended build
        for (Card handCardToIncrease : getCardsOnHand())
        {
            // add card from hand temporarily to check if computer can increase opponent's build using this card
            oppnBuildCard.add(handCardToIncrease);

            for (Card handCard : getCardsOnHand())
            {
                // check if there is any card on hand that matches the score of the new extended build
                if (calcSingleCardScore(handCard) == calcLooseCardScore(oppnBuildCard))
                {
                    if (helpRequested)
                    {
                        // Explain move reasoning
                        helpExplanation = getPlayerName() + " should play " + handCardToIncrease.cardToString()
                                                  + " to increase opponent's build and own their " +
                                                  "build to dent their chances of capturing that build.";
                        return true;
                    }

                    // if increasing opponent's build is valid, own the new extended build and remove
                    // ownership of the opponent's build
                    singleBuildCard.put(getPlayerName(), oppnBuildCard);

                    Vector<Card> empty = new Vector<>();
                    oppoBuild.put(opponentPlayerName, empty);

                    removeCardFromHand(handCardToIncrease);

                    // Explain move reasoning
                    moveExplanation = getPlayerName() + " played " + handCardToIncrease.cardToString()
                                               + "" + " to increase opponent's build.";
                    moveExplanation += "It wanted to own the build of the opponent and thus dent "
                                               + "their " + "chances of capturing that build.";
                    return true;
                }
            }
            // remove the card from the opponent's build that we temporarily added to check if we can increase the build or not
            oppnBuildCard.remove(handCardToIncrease);
        }
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
        for (Card handCard : getCardsOnHand())
        {
            if (calcSingleCardScore(handCard) == getFirstBuildScore())
            {
                // capture multiple builds, if there exist a matching card in the player's hand
                if (!isMultipleBuildEmpty())
                {
                    // get the list of multiple builds of the player
                    Vector<Vector<Card>> multipleBuild = multipleBuildCard.get(getPlayerName());

                    if (helpRequested)
                    {
                        // Explain move reasoning
                        helpExplanation = getPlayerName() + " should play " + handCard.cardToString() +
                                                  " to capture your multiple build of ";

                        // loop through the builds stored in the multibuild
                        for (Vector<Card> parsedBuild : multipleBuild)
                        {
                            for (Card buildCards : parsedBuild)
                            {
                                moveExplanation += buildCards.cardToString() + " ";
                            }
                        }
                        moveExplanation += " and maximize the number of captured cards.";
                        return true;
                    }

                    // Explain move reasoning
                    moveExplanation = getPlayerName() + " played " + handCard.cardToString() + "" +
                                               " to capture a multiple build of ";

                    // loop through the builds stored in the multibuild
                    for (Vector<Card> parsedBuild : multipleBuild)
                    {
                        // push the cards of the single build into the player's pile
                        for (Card buildCards : parsedBuild)
                        {
                            cardsOnPile.add(buildCards);
                            moveExplanation += buildCards.cardToString() + " ";
                        }
                    }
                    moveExplanation += ". It wanted to maximize the number of captured cards.";

                    // empty the multiple build as we have captured these cards
                    // Also, we need to empty the single build as multiple build has already captured those cards
                    multipleBuild = new Vector<>();
                    Vector<Card> singleBuild = new Vector<>();
                    multipleBuildCard.put(getPlayerName(), multipleBuild);
                    singleBuildCard.put(getPlayerName(), singleBuild);

                    // remove card that was used to capture the build successfully from the player's hand and add to pile
                    removeCardFromHand(handCard);
                    cardsOnPile.add(handCard);
                    return true;
                }
            }
        }
        // cannot capture multiple builds
        return false;
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
        for (Card handCard : getCardsOnHand())
        {
            if (calcSingleCardScore(handCard) == getFirstBuildScore())
            {
                // capture single build
                if (!isSingleBuildEmpty())
                {
                    // store the single build
                    Vector<Card> singleBuild = singleBuildCard.get(getPlayerName());

                    if (helpRequested)
                    {
                        // Explain move reasoning
                        helpExplanation = getPlayerName() + " should play " + handCard.cardToString() +
                                                  " to capture your single build of ";
                        // push the cards in the single build into the player's pile
                        for (Card buildCards : singleBuild)
                        {
                            helpExplanation += buildCards.cardToString() + " ";
                        }
                        return true;
                    }

                    // Explain move reasoning
                    moveExplanation = getPlayerName() + " played " + handCard.cardToString() + "" +
                                              " to capture a single build of ";
                    // push the cards in the single build into the player's pile
                    for (Card buildCards : singleBuild)
                    {
                        cardsOnPile.add(buildCards);
                        moveExplanation += buildCards.cardToString() + " ";
                    }
                    moveExplanation += ". It wanted to capture its single build.";

                    // empty the single build as we have captured these cards
                    singleBuild = new Vector<>();
                    singleBuildCard.put(getPlayerName(), singleBuild);

                    // push the hand card into the player's pile and remove it from player's hand
                    cardsOnPile.add(handCard);
                    removeCardFromHand(handCard);
                    return true;
                }
            }
        }
        // Cannot capture single builds
        return false;
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
        // Since there are no cards on hand that matches the score of the builds, we move
        // to capture set of cards or loose cards from the table
        for (Card handCard : getCardsOnHand())
        {
            // flag that sets if capturing set of cards was possible
            boolean captureSetSuccess = captureSetCards(tableCards, handCard);

            // if we captured set of cards, then return true from the function
            if (captureSetSuccess)
            {
                return true;
            }
        }

        // the loose individual cards that can be captured from the table
        Vector<Card> matchedTableCards = new Vector<>();

        // flag if individual cards have been captured
        boolean individualCaptured = false;

        // Selected hand card to capture individual cards
        Card cardToRemove = new Card();

        // If there were no sets to capture, check if there are any individual loose cards to capture
        for (Card handCard : getCardsOnHand())
        {
            int handCardScore = calcSingleCardScore(handCard);

            // Add any single card from the table that matches the score of the current card in player's hand
            for (Card tableCard : tableCards)
            {
                if (calcSingleCardScore(tableCard) == handCardScore)
                {
                    if (helpRequested)
                    {
                        helpExplanation = getPlayerName() + " should play " + handCard.cardToString() + " to capture " +
                                                  "individual card " + tableCard.cardToString();
                    }
                    // Explain move reasoning
                    moveExplanation = getPlayerName() + " played " + handCard.cardToString() + " to capture " +
                                               "individual card " + tableCard.cardToString();
                    cardToRemove = handCard;
                    cardsOnPile.add(tableCard);
                    matchedTableCards.add(tableCard);
                    individualCaptured = true;
                }
            }

            if (individualCaptured)
            {
                cardsOnPile.add(cardToRemove);
                removeCardFromHand(cardToRemove);
                removeCardsFromTable(tableCards, matchedTableCards);
                return true;
            }
        }
        // not possible to capture any cards as set of cards or as individual cards
        return false;
    }

    // ****************************************************************
    // Function Name: captureSetCards
    // Purpose: performs the sequence of actions to capture set of cards
    // Parameters: -> tableCards, a vector of cards. Holds the current cards in play that are on the table
    //             -> selectedHandCard, a Card object. Holds the card that will be used to capture
    // set of cards
    // Return value: a boolean. Returns whether the player was able to capture set of cards or not
    // Assistance Received: none
    // ****************************************************************
    private boolean captureSetCards(Vector<Card> tableCards, Card selectedHandCard)
    {
        // the valid build cards used to make a set
        Vector<Card> matchedTableCards = new Vector<>();

        // enlist the possible power sets based on tableSize
        Vector<String> buildComb = score.powerSet(tableCards.size());

        // make a map of the scores possible from valid builds
        score.buildScoreMap(buildComb, tableCards);

        // get the map of <BuildScores, BuildCombination>
        Vector<Pairs<Integer, Vector<Card>>> map = score.getBuildCombination();

        // calculate the hand card score
        int handCardScore = calcSingleCardScore(selectedHandCard);

        // iterate through the map and check if the key matches the handCardScore
        for (Pairs<Integer, Vector<Card>> buildList : map)
        {
            // match found
            if (buildList.first == handCardScore)
            {
                if (helpRequested)
                {
                    // Explain move reasoning
                    helpExplanation = getPlayerName() + " should play " + selectedHandCard.cardToString()
                                           + "to capture the set of ";

                    for (Card match : buildList.second)
                    {
                        helpExplanation += match.cardToString();
                    }
                    helpExplanation += " and maximize the number of captured cards.";

                    // Add any single card from the table that matches the score of the current card in player's hand
                    for (Card tableCard : tableCards)
                    {
                        if (calcSingleCardScore(tableCard) == handCardScore)
                        {
                            // Explain move reasoning
                            helpExplanation += getPlayerName() + " should play " + selectedHandCard.cardToString() + " " +
                                                       "to capture individual card " + tableCard.cardToString();
                        }
                    }
                    return true;
                }

                // Explain move reasoning
                moveExplanation = getPlayerName() + " played " + selectedHandCard.cardToString() + " " +
                                          "to capture the set of ";

                // add the possible set of cards to the player's pile
                // also, add the cards temporarily to a vector of cards to remove
                // them from the table after capturing is done
                for (Card match : buildList.second)
                {
                    cardsOnPile.add(match);
                    matchedTableCards.add(match);
                    moveExplanation += match.cardToString();
                }
                moveExplanation += ". It wanted to maximize the number of captured cards.";

                // Add any single card from the table that matches the score of the current card in player's hand
                for (Card tableCard : tableCards)
                {
                    if (calcSingleCardScore(tableCard) == handCardScore)
                    {
                        // Explain move reasoning
                        moveExplanation += getPlayerName() + " played " + selectedHandCard.cardToString() + " " +
                                                   "to capture individual card " + tableCard.cardToString();
                        cardsOnPile.add(tableCard);
                        matchedTableCards.add(tableCard);
                    }
                }

                // add the hand card that was used to capture to computer's pile
                // then remove it from its hand
                cardsOnPile.add(selectedHandCard);
                removeCardFromHand(selectedHandCard);

                // if capturing of sets was possible, then remove the captured cards from the table
                removeCardsFromTable(tableCards, matchedTableCards);
                return true;
            }
        }
        // no matching keys found
        return false;
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
        // Cannot trail if there exists a build owned by the owner
        if (!isSingleBuildEmpty() || !isMultipleBuildEmpty())
        {
            return false;
        }

        // store the card to trail by the player
        Card trailCard = new Card();

        // set the minimum value of the card to the maximum value of the card in the game
        int minimumCardValue = 14;

        if (cardsOnHand.size() > 1)
        {
            // We do not want to trail a card with face "A" because it is the highest card in the game
            // Search if there are any cards with minimum value than the current card
            for (Card currentCard : getCardsOnHand())
            {
                // Do not calculate the score of the "A" card
                // Update the minimum card value and the trail card if the newly compared card is
                // lower than the value set as minimum
                if (!currentCard.getFace().equals("A") &&
                            calcSingleCardScore(currentCard) < minimumCardValue)
                {
                    minimumCardValue = calcSingleCardScore(currentCard);
                    trailCard = currentCard;
                }
            }
        }

        // If there are only cards with faces "A" in the hand, then we have not updated our
        // trailCard. If that is the case, then trail the first card in our hand.
        // We will also trail the first card if the player only has one card in hand

        // Check if we have updated the values in trailCard
        if (trailCard.getFace() == null || trailCard.getFace().equals(""))
        {
            trailCard = getCardsOnHand().get(0);
        }

        if (helpRequested)
        {
            helpExplanation = getPlayerName() + " should trail " + trailCard.cardToString() +
                                      "as the lowest card value b/c there were no other possible " +
                                      "moves.";
            return true;
        }

        tableCards.add(trailCard);
        removeCardFromHand(trailCard);

        moveExplanation = getPlayerName() + " trailed " + trailCard.cardToString() + " as the " +
                                  "lowest card value b/c there were no other possible moves.";
        return true;
    }

    public static void main(String[] args)
    {
        String[] stringTable = { "C4", "H2", "DA", "C7"};
        String[] stringHand = { "S3", "H7", "D2", "D7", "S5" };

        Computer computer = new Computer("Computer");

        Vector<Card> handCard = new Vector<>();
        Vector<Card> tableCards = new Vector<>();

        for (String str: stringHand)
        {
            handCard.add(new Card(String.valueOf(str.charAt(0)), String.valueOf(str.charAt(1))));
        }
        computer.setCardsOnHand(handCard);

        for (String str: stringTable)
        {
            tableCards.add(new Card(String.valueOf(str.charAt(0)), String.valueOf(str.charAt(1))));
        }

        boolean move = computer.captureSetAndIndividualCards(tableCards);

        if (move)
        {
            System.out.println("success");
        }
        else
        {
            System.out.println("capture failed");
        }
    }
}