//****************************************************
//* Name: Manasbi Parajuli
//* Project: Casino
//* Class: CMPS 366-01
//* Date: 11/20/2018
//****************************************************
package edu.ramapo.mparajul.casino.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.ramapo.mparajul.casino.R;

public class EndActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        mainMenu();
        playAgain();
        editResult();
    }

    // ****************************************************************
    // Function Name: mainMenu
    // Purpose: Setup card listener to go to the main menu of the app
    // Parameters: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void mainMenu()
    {
        CardView menu = findViewById(R.id.main_menu_cardview);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuClick();
            }
        });
    }

    // ****************************************************************
    // Function Name: mainMenu
    // Purpose: Setup card listener for the player to restart the game
    // Parameters: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void playAgain()
    {
        CardView playAgain = findViewById(R.id.play_again_cardview);
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClick();
            }
        });
    }

    // ****************************************************************
    // Function Name: editResult
    // Purpose: Edit the text view based on the result of the game
    //        and whose values are passed from the Main Activity as extra message in the intent
    // Parameters: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void editResult()
    {
        // Get the string value from the passed intent
        String resultValue = getIntent().getExtras().getString("result");
        TextView textView = findViewById(R.id.game_result);

        // set text of the text view based on the result of the game
        if (resultValue.equals("computer"))
        {
            textView.setText(R.string.computer_won);
        }else if (resultValue.equals("human"))
        {
            textView.setText(R.string.human_won);
        }else
        {
            textView.setText(R.string.match_drawn);
        }
    }

    // ****************************************************************
    // Function Name: playClick
    // Purpose: Displays dialog that shows the summary of the tournament
    // Parameters: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void playClick()
    {
        final Dialog helpDialog = new Dialog(EndActivity.this);
        helpDialog.setContentView(R.layout.display_card_action_explanation);

        Button button = helpDialog.findViewById(R.id.card_explanation_button);
        button.setText(getIntent().getExtras().getString("end_round_status"));
        helpDialog.show();
    }

    // ****************************************************************
    // Function Name: menuClick
    // Purpose: Creates intent to start StartActivity
    // Parameters: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void menuClick()
    {
        Intent main_menu = new Intent(this, StartActivity.class);
        startActivity(main_menu);
    }
}