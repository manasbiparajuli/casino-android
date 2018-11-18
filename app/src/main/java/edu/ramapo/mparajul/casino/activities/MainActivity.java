//****************************************************
//* Name: Manasbi Parajuli
//* Project: Casino
//* Class: CMPS 366-01
//* Date: 11/20/2018
//****************************************************
package edu.ramapo.mparajul.casino.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;

import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import edu.ramapo.mparajul.casino.R;
import edu.ramapo.mparajul.casino.model.setup.Card;
import edu.ramapo.mparajul.casino.model.setup.Round;
import edu.ramapo.mparajul.casino.model.setup.Tournament;
import edu.ramapo.mparajul.casino.model.utility.Pairs;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private int deviceWidth;
    private View root;
    private Tournament tournament = new Tournament();
    private Round round = new Round();
    private Vector<String> cardsClicked = new Vector<>();
    private Vector<String> buildCardClicked = new Vector<>();
    private LinearLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        root = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        setContentView(root);

        // get the rootview id of the current activity that will be used by the snackbar
        rootView = findViewById(R.id.main_activity_root_layout);

        // store the width of the device as we use it to set the width of the
        // button that display cards in this activity
        deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

        configureToolbar();
        loadGameState();
        populateCardsOnDisplay();

        highlightNextPlayer();


        //TODO: test computer logic here
        round.humanActionPlay();

        callSnackbar(round.getComputerMoveExplanation());
    }

    public void configureToolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // populate the toolbar with action menus
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // get the id of the option that the user selected in the toolbar
        int toolbar_option_selected = item.getItemId();

        // based on the user's selections, perform specific actions
        if (toolbar_option_selected == R.id.action_display_deck_pile)
        {
            showDeckAndPile();
            return true;
        }
        else if (toolbar_option_selected == R.id.action_save_game)
        {
            saveGame();
            return true;
        }
        else if (toolbar_option_selected == R.id.action_get_help)
        {
            getHelp();
            return true;
        }
        else if (toolbar_option_selected == R.id.action_display_score)
        {
            displayScore();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadGameState()
    {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // Load new game
        if (getIntent().getExtras().getString("gameIntent").equals("newGame"))
        {
            tournament.setNextPlayer(bundle.getString("firstPlayer"));
            tournament.newGame();
            round = tournament.getRound();
        }
        // Resume game state from saved file
        else if (getIntent().getExtras().getString("gameIntent").equals("resumeGame"))
        {
            // Get the extras stored in the intent
            if (bundle != null)
            {
                tournament.setNextPlayer(getIntent().getExtras().getString("nextPlayer"));
                tournament.setRoundNumber(bundle.getInt("round"));
                tournament.loadGame(intent);
                round = tournament.getRound();
            }
        }
    }

    public void showDeckAndPile()
    {
        final Dialog deck_dialog = new Dialog(MainActivity.this);
        deck_dialog.setContentView(R.layout.display_deck_pile);
        populateDeckPile(deck_dialog);
        deck_dialog.show();
    }

    public void saveGame()
    {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.save_game_dialog);
        dialog.show();

        Button saveFileButton = dialog.findViewById(R.id.save_file_button);
        final EditText editText = dialog.findViewById(R.id.save_dialog);

        saveFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String saveFileName = editText.getText().toString();

                if(!round.saveGameToFile(saveFileName))
                {
                    callToast(MainActivity.this, "Unable to save game");
                }
                else
                {
                    callToast(MainActivity.this, "Game Saved!");

                    // Call StartActivity and pass extra message to completely exit from the app
                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);
                }
                dialog.dismiss();
            }
        });
    }

    //TODO: get help using computer strategy
    public void getHelp()
    {
    }

    public void displayScore()
    {
        final Dialog score_dialog = new Dialog(MainActivity.this);
        score_dialog.setContentView(R.layout.display_scores_dialog);

        Button button;

        // Set last round scores of the players
        button = score_dialog.findViewById(R.id.score_computer_round);
        button.setText(String.valueOf(round.getComputerRoundScore()));

        button = score_dialog.findViewById(R.id.score_human_round);
        button.setText(String.valueOf(round.getHumanRoundScore()));

        // Set tournament scores of the players
        button = score_dialog.findViewById(R.id.score_computer_tournament);
        button.setText(String.valueOf(round.getComputerTourneyScore()));

        button = score_dialog.findViewById(R.id.score_human_tournament);
        button.setText(String.valueOf(round.getHumanTourneyScore()));

        score_dialog.show();
    }

    public void populateCardsOnDisplay()
    {
        populateHand();
        populateTable();
        setBuildCards();
    }

    public void populateDeckPile(Dialog dialog)
    {
        // display computer cards on pile
        LinearLayout linearLayout = dialog.findViewById(R.id.layout_computer_pile);
        Vector<String> labelNames = new Vector<>();

        for (Card card : round.getComputerCardsOnPile())
        {
            labelNames.add(card.cardToString());
        }
        createCardsDynamically(linearLayout, labelNames, false);

        // display human cards on hand
        linearLayout = dialog.findViewById(R.id.layout_human_pile);
        labelNames = new Vector<>();
        for (Card card : round.getHumanCardsOnPile())
        {
            labelNames.add(card.cardToString());
        }
        createCardsDynamically(linearLayout, labelNames, false);

        // display deck of cards
        linearLayout = dialog.findViewById(R.id.layout_deck);
        labelNames = new Vector<>();
        for (Card card : round.getDeck())
        {
            labelNames.add(card.cardToString());
        }
        createCardsDynamically(linearLayout, labelNames, false);
    }


    public void populateHand()
    {
        // display computer cards on hand
        LinearLayout linearLayout = findViewById(R.id.layout_computer_hand);
        Vector<String> labelNames = new Vector<>();

        for (Card card : round.getComputerCardsOnHand())
        {
            labelNames.add(card.cardToString());
        }
        createCardsDynamically(linearLayout, labelNames, false);

        // display human cards on hand
        linearLayout = findViewById(R.id.layout_human_hand);
        labelNames = new Vector<>();
        for (Card card : round.getHumanCardsOnHand())
        {
            labelNames.add(card.cardToString());
        }
        createCardsDynamically(linearLayout, labelNames,true);

    }

    public void populateTable()
    {
        int cardWidth = deviceWidth / 5;

        // display cards on table
        FlexboxLayout flexboxLayout = findViewById(R.id.layout_table_cards);
        flexboxLayout.removeAllViews();
        Vector<String> labelNames = new Vector<>();
        for (Card card : round.getTableCards())
        {
            labelNames.add(card.cardToString());
        }

        for (String label : labelNames)
        {
            ImageView imageView = new ImageView(this);

            // set margins for the image views
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(cardWidth,300);
            params.setMargins(15,10,30,10);
            params.setFlexShrink(1);
            imageView.setLayoutParams(params);
            imageView.setPadding(2,8,2,8);

            // get current context and set the image of the button to the card it corresponds to
            // also set the id and tag of the card
            Context context = flexboxLayout.getContext();
            int id = context.getResources().getIdentifier(label.toLowerCase(), "drawable", context.getPackageName());
            imageView.setTag(label.toLowerCase());
            imageView.setImageResource(id);
            imageView.setId(id);

            // add normal border to the image view
            int card_border = context.getResources().getIdentifier("card_border_normal",
                    "drawable", context.getPackageName());
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), card_border, null);
            imageView.setBackground(drawable);
            imageView.bringToFront();

            // add click listeners for image views
            imageView.setClickable(true);
            imageView.setOnClickListener(this);
            flexboxLayout.addView(imageView);
        }
    }

    public void setBuildCards()
    {
        FlexboxLayout parentFlexboxLayout = findViewById(R.id.layout_build_cards);

        Vector<String> labelNames = new Vector<>();
        String borderDrawable = new String();

        // Display the builds of the players
        if (!round.isComputerMultipleBuildEmpty())
        {
            Vector<Vector<Card>> multipleBuild =
                round.getComputerMultipleBuildCard().get(round.getComputerPlayerName());
            for (Vector<Card> builds : multipleBuild)
            {
                for (Card card : builds)
                {
                    labelNames.add(card.cardToString());
                }
            }
            makeLayoutForBuild(parentFlexboxLayout, labelNames, "computer_player_border");
        }
        if (!round.isComputerSingleBuildEmpty())
        {
            labelNames = new Vector<>();
            Vector<Card> singleBuild = round.getComputerSingleBuildCard().get(round.getComputerPlayerName());

            for (Card card : singleBuild)
            {
                labelNames.add(card.cardToString());
            }
            makeLayoutForBuild(parentFlexboxLayout, labelNames, "computer_player_border");
        }
        if (!round.isHumanMultipleBuildEmpty())
        {
            labelNames = new Vector<>();
            Vector<Vector<Card>> multipleBuild =
                    round.getHumanMultipleBuildCard().get(round.getHumanPlayerName());
            for (Vector<Card> builds : multipleBuild)
            {
                for (Card card : builds)
                {
                    labelNames.add(card.cardToString());
                }
            }
            makeLayoutForBuild(parentFlexboxLayout, labelNames, "human_player_border");
        }
        if (!round.isHumanSingleBuildEmpty())
        {
            labelNames = new Vector<>();
            Vector<Card> singleBuild = round.getHumanSingleBuildCard().get(round.getHumanPlayerName());

            for (Card card : singleBuild)
            {
                labelNames.add(card.cardToString());
            }
            makeLayoutForBuild(parentFlexboxLayout, labelNames, "human_player_border");
        }
    }

    private void makeLayoutForBuild(FlexboxLayout flexboxLayout, Vector<String> temp,
                                    String borderDrawable)
    {
        flexboxLayout.removeAllViews();
        // stores the width of the card
        int cardWidth = deviceWidth / 5;

        // store the previous id so that we can achieve overlapping of cards
        // when stacked into RelativeLayout
        int previousId;

        // The layout for a build
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams innerLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        innerLayoutParams.setMargins(2,2,2,2);
        linearLayout.setLayoutParams(innerLayoutParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        Context context = linearLayout.getContext();

        for (int i = 0; i < temp.size(); i++)
        {
            String label = temp.get(i);
            ImageView imageView = new ImageView(this);

            // get the drawable resource for the specific card
            int id = context.getResources().getIdentifier(label.toLowerCase(), "drawable", context.getPackageName());

            // store the id as we require it to set up a rule when we overlap the cards
            previousId = id;

            // set the imageview attributes for the card
            imageView.setId(id);
            imageView.setTag(label.toLowerCase());
            imageView.setImageResource(id);

            // Store the cards of the build into a relative layout
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(cardWidth, 300);

            // add rules starting from the second card in the build as we want to align them
            // based on the position of the previous card
            if (i != 0)
            {
                params.addRule(RelativeLayout.ALIGN_TOP, previousId);
                params.addRule(RelativeLayout.ALIGN_RIGHT, previousId);

                // we set negative margin to overlap the previous card and let us allow
                // only the card's face value visible when stacked
                params.leftMargin =  - 180;
            }

            // set the normal border of the card and add to the parent linear layout
            imageView.setBackground(getDrawableFromString("card_border_normal"));
            imageView.setLayoutParams(params);
            linearLayout.addView(imageView);
        }

        // differentiate a build with a border
        int card_border = context.getResources().getIdentifier(borderDrawable,
                "drawable", context.getPackageName());
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), card_border, null);

        // differentiate build with specific tag and assign a unique id
        // so that click listeners can identify as a build
        linearLayout.setPadding(3,8,3,8);
        linearLayout.setBackground(drawable);
        linearLayout.setId(ThreadLocalRandom.current().nextInt(0, 5000));
        linearLayout.setTag("buildOfCards");
        linearLayout.setOnClickListener(this);

        // Add the build to the parent that stores the list of builds
        flexboxLayout.addView(linearLayout);
    }


    private void createCardsDynamically(LinearLayout linearLayout, Vector<String> labelNames, boolean clickable)
    {
        linearLayout.removeAllViews();
        // store the width of the card
        int cardWidth = deviceWidth / 5;

        // Loop through the cards and set their ImageViews
        for (String label : labelNames)
        {
            ImageView imageView = new ImageView(this);

            // set margins for the image views
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(cardWidth, 300);
            params.setMargins(15,10,30,10);
            imageView.setLayoutParams(params);
            imageView.setPadding(2,8,2,8);

            // get current context and set the image of the button to the card it corresponds to
            Context context = linearLayout.getContext();
            int id = context.getResources().getIdentifier(label.toLowerCase(), "drawable", context.getPackageName());
            imageView.setTag(label.toLowerCase());
            imageView.setImageResource(id);
            imageView.setId(ThreadLocalRandom.current().nextInt(0, 5000));

            // add normal border to the image view
            imageView.setBackground(getDrawableFromString("card_border_normal"));
            imageView.bringToFront();

            // Implement onClick listeners for human hand cards
            if (clickable)
            {
                imageView.setClickable(true);
                imageView.setOnClickListener(this);
            }
            else { imageView.setClickable(false);}
            linearLayout.addView(imageView);
        }
    }

    @Override
    public void onClick(View view)
    {
        int clickedCardId = view.getId();
        // the clicked card is part of a build
        if (view.getTag().toString().equals("buildOfCards"))
        {
            if (!duplicateBuildCardClicked(clickedCardId))
            {
                LinearLayout parent = findViewById(view.getId());
                parent.setBackground(getDrawableFromString("card_border_clicked"));
                buildCardClicked.add(String.valueOf(clickedCardId));
            }
        }

        // the clicked card is from the hand or a loose card
        else
        {
            // Check if the clicked card has already been clicked
            // If the player clicked on the card again, then remove the highlight border from the card
            if (!duplicateCardClicked(clickedCardId))
            {
                // Not a duplicate card
                // check if the user did not click on a second hand card.
                if (singleHandCardClicked(clickedCardId))
                {
                    // valid card click
                    // Highlight the border of the clicked card
                    ImageView imageView = findViewById(clickedCardId);
                    imageView.setBackground(getDrawableFromString("card_border_clicked"));
                    cardsClicked.add(String.valueOf(clickedCardId));
                }
            }
        }
    }

    public void onClickCardAction(View view)
    {
        switch (view.getId())
        {
            case R.id.make_single_build:
                // check if the player has clicked a hand card before performing an action
                if (cardsClickedContainsHandCard())
                {
                    buildCardClicked = new Vector<>();
                    if (cardsClicked.size() < 2)
                    {
                        callSnackbar("Invalid. Select at least one loose card");
                    }
                    else
                    {
                        round.setMoveActionIdentifier("make_single_build");
                        saveClickedCardsToPlayer();
                        round.humanActionPlay();
                    }
                }
                // no hand card in the list of clicked cards
                else { callSnackbar("Invalid. No hand cards selected!");}
                break;
            case R.id.make_multiple_build:
                // check if the player has clicked a hand card before performing an action
                if (cardsClickedContainsHandCard())
                {
                    buildCardClicked = new Vector<>();
                    if (cardsClicked.size() < 2)
                    {
                        callSnackbar("Invalid. Select at least one loose card");
                    }
                    else
                    {
                        round.setMoveActionIdentifier("make_multiple_build");
                        saveClickedCardsToPlayer();
                        round.humanActionPlay();
                    }
                }
                // no hand card in the list of clicked cards
                else { callSnackbar("Invalid. No hand cards selected!");}

                break;
            case R.id.make_extend_build:
                // check if the player has clicked a hand card before performing an action
                if (cardsClickedContainsHandCard())
                {
                    round.setMoveActionIdentifier("extend_build");
                    saveClickedCardsToPlayer();
                    round.humanActionPlay();
                }
                // no hand card in the list of clicked cards
                else { callSnackbar("Invalid. No hand cards selected!");}

                break;
            case R.id.make_capture:

                //TODO: differentiate between various captures possible

                // check if the player has clicked a hand card before performing an action
                if (cardsClickedContainsHandCard())
                {
                    round.setMoveActionIdentifier("capture_individual_set");
                    saveClickedCardsToPlayer();
                    round.humanActionPlay();
                }
                // no hand card in the list of clicked cards
                else { callSnackbar("Invalid. No hand cards selected!");}

                break;
            case R.id.make_trail:
                // check if the player has clicked a hand card before performing an action
                if (cardsClickedContainsHandCard())
                {
                    if (cardsClicked.size() == 1)
                    {
                        round.setMoveActionIdentifier("trail");
                        saveClickedCardsToPlayer();
                        round.humanActionPlay();
                    }
                    else
                    {
                        callSnackbar("Cannot trail with two cards selected!");
                    }
                }
                // no hand card in the list of clicked cards
                else { callSnackbar("Invalid. No hand cards selected!");}
                break;
            default:
                break;
        }

        //
        if (!round.getHumanIsMoveSuccessful())
        {
            callSnackbar(round.getHumanMoveExplanation());
        }

        resetGameCardClicks();
        populateCardsOnDisplay();
    }

    private void resetGameCardClicks()
    {
        // reset the holder for clicked and build cards
        setCardBorderNormal(cardsClicked);
        setBuildBorderNormal(buildCardClicked);
        cardsClicked = new Vector<>();
        buildCardClicked = new Vector<>();
    }

    private void highlightNextPlayer()
    {
        // Next Player: Computer
        if (round.getNextPlayer().equals(round.getComputerPlayerName()))
        {
            LinearLayout linearLayout = findViewById(R.id.layout_computer_hand);
            // get current context and set the image of the button to the card it corresponds to
            Context context = linearLayout.getContext();
            int id = context.getResources().getIdentifier("computer_player_border", "drawable",
                    context.getPackageName());
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), id, null);
            linearLayout.setBackground(drawable);
        }
        // Next Player: Human
        else if (round.getNextPlayer().equals(round.getHumanPlayerName()))
        {
            LinearLayout linearLayout = findViewById(R.id.layout_human_hand);
            // get current context and set the image of the button to the card it corresponds to
            Context context = linearLayout.getContext();
            int id = context.getResources().getIdentifier("human_player_border", "drawable",
                    context.getPackageName());
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), id, null);
            linearLayout.setBackground(drawable);
        }
    }

    private void setCardBorderNormal(Vector<String> clickedCards)
    {
        ImageView imageView;

        // Set highlighted cards to normal border
        for (String cards: clickedCards)
        {
            imageView = findViewById(Integer.valueOf(cards));
            imageView.setBackground(getDrawableFromString("card_border_normal"));
        }
    }

    private void setBuildBorderNormal(Vector<String> clickedBuildCards)
    {
        LinearLayout buildLayout;
        // set build border to black
        for (String cards: clickedBuildCards)
        {
            buildLayout = findViewById(Integer.valueOf(cards));
            buildLayout.setBackground(getDrawableFromString("card_border_normal"));
        }
    }

    private boolean duplicateBuildCardClicked(int buildCardId)
    {
        if (!buildCardClicked.isEmpty())
        {
            // Check if the newly clicked card is a duplicate click on the build
            // If it is, proceed to remove its highlight border
            for (String clicked : buildCardClicked)
            {
                if (Integer.valueOf(clicked) == buildCardId)
                {
                    Vector<String> temp = new Vector<>();
                    temp.add(String.valueOf(buildCardId));
                    setBuildBorderNormal(temp);
                    buildCardClicked.remove(String.valueOf(buildCardId));
                    return true;
                }
            }
        }
        return false;
    }

    private boolean duplicateCardClicked(int cardId)
    {
        if (!cardsClicked.isEmpty())
        {
            // Check if the newly clicked card is a duplicate click
            // If it is, proceed to remove its highlight border
            for (String clicked : cardsClicked)
            {
                if (Integer.valueOf(clicked) == cardId)
                {
                    Vector<String> temp = new Vector<>();
                    temp.add(String.valueOf(cardId));
                    setCardBorderNormal(temp);
                    cardsClicked.remove(String.valueOf(cardId));
                    return true;
                }
            }
        }
        return false;
    }

    private boolean singleHandCardClicked(int handCardId)
    {
        LinearLayout linearLayout = findViewById(R.id.layout_human_hand);

        // check if the clicked list contains card clicks
        if (!cardsClicked.isEmpty())
        {
            // Check if the clicked card belongs to the linear layout that displays human hand card
            if (belongsToHumanCard(handCardId))
            {
                // get all the child views of human hand linear layout
                for (int i = 0 ; i < linearLayout.getChildCount(); i++)
                {
                    // Check if the user is clicking multiple cards from his hand
                    for (String id : cardsClicked)
                    {
                        // if there are any matching ids in the list of clicked cards
                        // remove the highlight border from the card and return false
                        if (Integer.valueOf(id) == linearLayout.getChildAt(i).getId())
                        {
                            Vector<String> temp = new Vector<>();
                            temp.add(String.valueOf(handCardId));
                            setCardBorderNormal(temp);
                            cardsClicked.remove(String.valueOf(handCardId));
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean belongsToHumanCard(int cardId)
    {
        LinearLayout linearLayout = findViewById(R.id.layout_human_hand);

        // get all the child views of human hand linear layout
        for (int i = 0 ; i < linearLayout.getChildCount(); i++)
        {
            // Check if the clicked card belongs to the linear layout that displays human hand
            if (cardId == linearLayout.getChildAt(i).getId())
            {
                // Card belongs to human hand linear layout
                return true;
            }
        }
        // Card belongs to the table linear layout
        return false;
    }

    private boolean cardsClickedContainsHandCard()
    {
        for (String card: cardsClicked)
        {
            if (belongsToHumanCard(Integer.valueOf(card))) { return true;}
        }
        return false;
    }

    private void saveClickedCardsToPlayer()
    {
        Vector<String> looseCardsList = new Vector<>();

        Vector<String> tempCardsClicked = new Vector<>();
        String temp = new String();

        boolean removedHandCard = false;

        for (String str : cardsClicked)
        {
            tempCardsClicked.add(str);
        }

        if (!tempCardsClicked.isEmpty())
        {
            // set the clicked hand card for the human player
            for (String card : tempCardsClicked)
            {
                if (belongsToHumanCard(Integer.valueOf(card)))
                {
                    ImageView imageView = findViewById(Integer.valueOf(card));
                    round.setClickedHandCard(imageView.getTag().toString().toUpperCase());
                    temp = card;
                    removedHandCard = true;
                }
            }

            if (removedHandCard && temp != null) {tempCardsClicked.remove(temp);}

            // clicked cards list may be empty if the user only clicked one card
            if (!tempCardsClicked.isEmpty())
            {
                // set the clicked table cards for the human player
                for (String looseCards : tempCardsClicked)
                {
                    ImageView imageView = findViewById(Integer.valueOf(looseCards));
                    looseCardsList.add(imageView.getTag().toString().toUpperCase());
                }
                round.setClickedTableCards(looseCardsList);
            }
        }

        if (!buildCardClicked.isEmpty())
        {
            Vector<String> buildCardList = new Vector<>();

            // set the clicked build cards for the human player
            for (String buildCards : buildCardClicked)
            {
                LinearLayout buildLayout = findViewById(Integer.valueOf(buildCards));

                for (int i = 0; i < buildLayout.getChildCount(); i++)
                {
                    buildCardList.add(buildLayout.getChildAt(i).getTag().toString().toUpperCase());
                }
            }
            round.setClickedBuildCards(buildCardList);
        }
    }

    private Drawable getDrawableFromString(String drawableFile)
    {
        Context context = this;
        int card_border = context.getResources().getIdentifier(drawableFile,
                "drawable", context.getPackageName());
        return (ResourcesCompat.getDrawable(getResources(), card_border, null));
    }

    public void callSnackbar (String message)
    {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE);

        // Set dismiss button on the snackbar
        snackbar.setAction("DISMISS", new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    // Display messages to the user as Toast
    // Parameters: mContext -> the current context of the app
    //              message -> the message to be displayed in the toast
    // Return: null
    public void callToast(Context mContext, String message)
    {
        Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
    }
}