//****************************************************
//* Name: Manasbi Parajuli
//* Project: Casino
//* Class: CMPS 366-01
//* Date: 11/20/2018
//****************************************************
package edu.ramapo.mparajul.casino.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;

import edu.ramapo.mparajul.casino.R;

public class MainActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private int deviceWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // store the width of the device as we use it to set the width of the
        // button that display cards in this activity
        deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

        configureToolbar();
        populateComputerHand();
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
        return super.onOptionsItemSelected(item);
    }

    //TODO: save current game state
    public void saveGame()
    {

    }

    //TODO: get help using computer strategy
    public void getHelp()
    {

    }

    public void showDeckAndPile()
    {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.display_deck_pile);
        dialog.show();
    }

    public void populateComputerHand()
    {
        int totalHandCards = 3;
        int width = deviceWidth / 4;

        LinearLayout linearLayout = findViewById(R.id.layout_computer_hand);
        String[] labelNames = { "H2", "SX", "DK", "CA", "H5", "HQ"};

        for (int i = 0; i < totalHandCards; i++)
        {
            Button handCardButton = new Button(this);
            handCardButton.setLayoutParams(new LinearLayout.LayoutParams(width,450));
            handCardButton.setId(i);
            handCardButton.setClickable(true);

            Context context = linearLayout.getContext();
            int id = context.getResources().getIdentifier(labelNames[i].toLowerCase(), "drawable", context
                                                                                             .getPackageName());
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), id, null);
            handCardButton.setBackground(drawable);
            linearLayout.addView(handCardButton);
        }
    }
}
