//****************************************************
//* Name: Manasbi Parajuli
//* Project: Casino
//* Class: CMPS 366-01
//* Date: 11/20/2018
//****************************************************
package edu.ramapo.mparajul.casino.activities;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import edu.ramapo.mparajul.casino.FileIO.FileDialog;
import edu.ramapo.mparajul.casino.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // setup CardView listener to initiate new game
        CardView startGame = findViewById(R.id.start_cardview);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startGame();
            }
        });


        // setup CardView listener to load from file
        final CardView resumeGame = findViewById(R.id.resume_cardview);

        resumeGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                File mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
                FileDialog fileDialog = new FileDialog(StartActivity.this, mPath, ".txt");
                fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
                    public void fileSelected(File file) {

                        // warn the user if the file format is invalid or could not be read
                        if (!resumeGame(file.toString())){
                            Toast.makeText(StartActivity.this, "Error while loading game.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                fileDialog.showDialog();            }
        });
    }


    // ****************************************************************
    // Function Name: startGame
    // Purpose: Creates intent to start MainActivity
    // Parameters: none
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public void startGame()
    {
        Intent launchGame = new Intent (this, MainActivity.class);

        // put extra information to tell that we are initiating a new game
        launchGame.putExtra("gameIntent", "new_game");
        startActivity(launchGame);
    }

    // ****************************************************************
    // Function Name: resumeGame
    // Purpose:Load game state and save the information as extra message when calling intent
    // Parameters: fileName, a String object. The name of the file to read from
    // Return value: flags whether the file was readable or not
    // Assistance Received: none
    // ****************************************************************
    public boolean resumeGame(String fileName)
    {

        Intent test = new Intent(this, EndActivity.class);
        startActivity(test);


        return true;
    }
}
