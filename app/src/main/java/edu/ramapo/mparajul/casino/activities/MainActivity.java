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
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import edu.ramapo.mparajul.casino.R;

public class MainActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private int deviceWidth;
    private View root;

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
        populateCardsOnDisplay();
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

    public void showDeckAndPile()
    {
        final Dialog deck_dialog = new Dialog(MainActivity.this);
        deck_dialog.setContentView(R.layout.display_deck_pile);
        populateDeck(deck_dialog);
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

                // TODO: call the function as a method of Round
                if(!saveGameToFile(saveFileName))
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

    //TODO: delete this function
    public boolean saveGameToFile(String fileName) {return true; }

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

    // TODO: accept card object
    public void populateDeck(Dialog dialog)
    {
        int[] ids = {R.id.layout_deck, R.id.layout_computer_pile, R.id.layout_human_pile};

        for (int id: ids)
        {
            LinearLayout linearLayout = dialog.findViewById(id);
            String[] labelNames = { "H2", "SX", "DK", "CA", "H5", "HQ", "S4", "SA"};

            if (id == R.id.layout_computer_pile)
            {
                labelNames = new String[]{ "H2", "SX", "DK"};
            }
            createCardsDynamically(linearLayout, labelNames);
        }
    }



    // TODO: accept card object
    public void populateHand()
    {
        LinearLayout linearLayout = findViewById(R.id.layout_computer_hand);
        String[] labelNames = { "H2", "SX", "DK", "CA"};

        createCardsDynamically(linearLayout, labelNames);

        linearLayout = findViewById(R.id.layout_human_hand);
        createCardsDynamically(linearLayout, labelNames);
    }

    public void populateTable()
    {
        LinearLayout linearLayout = findViewById(R.id.layout_table_cards);
        String[] labelNames = { "H2", "SX", "DK", "CA", "H5", "HQ", "S4", "SA"};
        createCardsDynamically(linearLayout, labelNames);
    }

    private void createCardsDynamically(LinearLayout linearLayout, String[] labelNames)
    {
        int totalHandCards = labelNames.length;
        int cardWidth = deviceWidth / 5;
        for (int i = 0; i < totalHandCards; i++)
        {
            Button handCardButton = new Button(this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(cardWidth, 300);
            params.setMargins(15,10,30,10);

            handCardButton.setLayoutParams(params);
            handCardButton.setId(i);
            handCardButton.setClickable(true);
            Context context = linearLayout.getContext();
            int id = context.getResources().getIdentifier(labelNames[i].toLowerCase(), "drawable", context.getPackageName());

            Drawable drawable = ResourcesCompat.getDrawable(getResources(), id, null);
            handCardButton.setBackground(drawable);
            linearLayout.addView(handCardButton);
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
