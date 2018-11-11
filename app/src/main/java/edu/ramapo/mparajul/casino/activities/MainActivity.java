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
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.Vector;

import edu.ramapo.mparajul.casino.R;
import edu.ramapo.mparajul.casino.model.setup.Card;
import edu.ramapo.mparajul.casino.model.setup.Round;
import edu.ramapo.mparajul.casino.model.setup.Tournament;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private Toolbar toolbar;
    private int deviceWidth;
    private View root;
    private Tournament tournament = new Tournament();
    private Round round = new Round();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        root = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        setContentView(root);

        // store the width of the device as we use it to set the width of the
        // button that display cards in this activity
        deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

        configureToolbar();
        loadGameState();
        populateCardsOnDisplay();

        highlightNextPlayer();
    }

    public void configureToolbar()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // get the id of the option that the user selected
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

    //TODO: edit scores for rounds and tournament
    public void displayScore()
    {
        final Dialog score_dialog = new Dialog(MainActivity.this);
        score_dialog.setContentView(R.layout.display_scores_dialog);
        score_dialog.show();
    }

    public void populateCardsOnDisplay()
    {
        populateHand();
        populateTable();
    }

    public void populateDeckPile(Dialog dialog)
    {
        // display computer cards on pile
        LinearLayout linearLayout = dialog.findViewById(R.id.layout_computer_pile);
        Vector<String> labelNames = new Vector<>();

        if (!round.getComputerCardsOnPile().isEmpty())
        {
            for (Card card : round.getComputerCardsOnPile())
            {
                labelNames.add(card.cardToString());
            }
            createCardsDynamically(linearLayout, labelNames, false);
        }

        if (!round.getHumanCardsOnPile().isEmpty())
        {
            // display human cards on hand
            linearLayout = dialog.findViewById(R.id.layout_human_pile);
            labelNames = new Vector<>();
            for (Card card : round.getHumanCardsOnPile())
            {
                labelNames.add(card.cardToString());
            }
            createCardsDynamically(linearLayout, labelNames, false);
        }

        if (!round.getDeck().isEmpty())
        {
            // display deck of cards
            linearLayout = dialog.findViewById(R.id.layout_deck);
            labelNames = new Vector<>();
            for (Card card : round.getDeck())
            {
                labelNames.add(card.cardToString());
            }
            System.out.println(round.getDeck().size());
            createCardsDynamically(linearLayout, labelNames, false);
        }
    }


    public void populateHand()
    {
        // display computer cards on hand
        LinearLayout linearLayout = findViewById(R.id.layout_computer_hand);
        Vector<String> labelNames = new Vector<>();

        if (!round.getComputerCardsOnHand().isEmpty())
        {
            for (Card card : round.getComputerCardsOnHand())
            {
                labelNames.add(card.cardToString());
            }
            createCardsDynamically(linearLayout, labelNames, true);
        }

        if (!round.getHumanCardsOnHand().isEmpty())
        {
            // display human cards on hand
            linearLayout = findViewById(R.id.layout_human_hand);
            labelNames = new Vector<>();
            for (Card card : round.getHumanCardsOnHand())
            {
                labelNames.add(card.cardToString());
            }
            createCardsDynamically(linearLayout, labelNames,true);
        }
    }

    public void populateTable()
    {
        if (!round.getTableCards().isEmpty())
        {
            // display human cards on hand
            LinearLayout linearLayout = findViewById(R.id.layout_table_cards);
            Vector<String> labelNames = new Vector<>();
            for (Card card : round.getTableCards())
            {
                labelNames.add(card.cardToString());
            }
            createCardsDynamically(linearLayout, labelNames, true);
        }
    }

    private void createCardsDynamically(LinearLayout linearLayout, Vector<String> labelNames,
                                        boolean clickable)
    {
        int cardWidth = deviceWidth / 5;

        for (String label : labelNames)
        {
            ImageButton handCardButton = new ImageButton(this);

            // set margins for the button and implement click listener
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(cardWidth, 300);
            params.setMargins(15,10,30,10);
            handCardButton.setLayoutParams(params);

            // Implement onClick listeners for hand and table cards only
            if (clickable)
            {
                handCardButton.setClickable(true);
                handCardButton.setOnClickListener(this);
            }
            else { handCardButton.setClickable(false);}

            // get current context and set the image of the button to the card it corresponds to
            Context context = linearLayout.getContext();
            int id = context.getResources().getIdentifier(label.toLowerCase(), "drawable", context.getPackageName());
            handCardButton.setTag(label.toLowerCase());
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), id, null);
            handCardButton.setBackground(drawable);
            linearLayout.addView(handCardButton);
        }
    }

    @Override
    public void onClick(View view)
    {
    }


    //TODO: get user action clicks
    public void onClickCardAction(View view)
    {
        switch (view.getId())
        {
            case R.id.make_single_build:
                break;
            case R.id.make_multiple_build:
                break;
            case R.id.make_extend_build:
                break;
            case R.id.make_capture:
                break;
            default:
        }
    }

    public void highlightNextPlayer()
    {
        // Next Player: Computer
        if (round.getNextPlayer().equals(round.getComputerPlayerName()))
        {
            LinearLayout linearLayout = findViewById(R.id.layout_computer_hand);
            // get current context and set the image of the button to the card it corresponds to
            Context context = linearLayout.getContext();
            int id = context.getResources().getIdentifier("current_player_border", "drawable",
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
            int id = context.getResources().getIdentifier("current_player_border", "drawable",
                    context.getPackageName());
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), id, null);
            linearLayout.setBackground(drawable);
        }
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
