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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

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
                fileDialog.showDialog();
            }
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
        launchGame.putExtra("gameIntent", "newGame");
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
        // the total number of lines in the file
        int totalLines = 0;

        // the contents of a line from the text file
        String line;

        // the line number that the file pointer is pointing to when reading
        int lineNumber = 0;

        // check if there was any error when reading contents from the file
        boolean readSuccess;

        BufferedReader bufferedReader;
        File file = new File(fileName);

        // Try opening the file to read
        // Handle exceptions accordingly
        try
        {
            FileInputStream fis = new FileInputStream(file);
            // Read from the file
            try
            {
                bufferedReader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
                line = bufferedReader.readLine();
                while (line != null)
                {
                    // read next line from the file and increment number of lines read
                    line = bufferedReader.readLine();
                    totalLines++;
                }
            }catch (IOException e)
            {
                e.printStackTrace();
                return false;
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }

        // Try opening the file to read
        // Handle exceptions accordingly
        try
        {
            FileInputStream fis = new FileInputStream(file);
            // read from the file
            try
            {
                bufferedReader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

                // Declare intent and put values necessary to load a game as Extras
                Intent resumeGameState = new Intent(this, MainActivity.class);

                // read from the file
                line = bufferedReader.readLine();

                while (line != null && (lineNumber <= totalLines))
                {
                    // get the contents of the saved file
                    readSuccess = setSavedPreferences(lineNumber, line, resumeGameState);

                    // errors while reading contents from the file
                    if (!readSuccess)
                    {
                        return false;
                    }

                    // read next line from the file and increment number of lines read
                    line = bufferedReader.readLine();
                    lineNumber++;
                }

                resumeGameState.putExtra("gameIntent", "resumeGame");
                startActivity(resumeGameState);
                return true;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return false;
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    // ****************************************************************
    // Function Name: setSavedPreferences
    // Purpose: sets the values from the file into the current round
    // Parameters: -> lineNumber, an integer. The current linenumber that the filestream is pointing to
    //             -> line, a string. The string that the current line holds
    //             -> resumeGameState, an Intent object. The intent will store the saved credentials
    //                      that will be used to resume the game correctly in MainActivity
    // Return value: none
    // Assistance Received: none
    // ****************************************************************
    public boolean setSavedPreferences(int lineNumber, String line, Intent resumeGameState)
    {
        // Round: 3
        if (lineNumber == 0)
        {
            try
            {
                int roundNumber = Integer.parseInt(line.split(": ")[1]);
                resumeGameState.putExtra("round", roundNumber);
                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }
        // Computer's Score: 17
        else if (lineNumber == 3)
        {
            try
            {
                int compScore = Integer.parseInt(line.split(": ")[1]);
                resumeGameState.putExtra("computerScore", compScore);
                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }
        // Computer's Hand: H5 H6 D4 D7
        else if (lineNumber == 4)
        {
            try
            {
                // check if there are no cards on hand
                int length = line.split(": ").length;
                String[] cards = {};
                // no cards on hand
                if (length == 1) {}

                // cards present
                else { cards = (line.split(": ")[1]).split(" "); }
                resumeGameState.putExtra("computerHand", cards);
                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }
        // Computer's Pile: SX SQ SK D6 H8
        else if (lineNumber == 5)
        {
            try
            {
                // check if there are no cards on hand
                int length = line.split(": ").length;
                String[] cards = {};
                // no cards on hand
                if (length == 1) {}

                // cards present
                else { cards = (line.split(": ")[1]).split(" "); }
                resumeGameState.putExtra("computerPile", cards);
                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }
        // Human's Score: 14
        else if (lineNumber == 8)
        {
            try
            {
                int humanScore = Integer.parseInt(line.split(": ")[1]);
                resumeGameState.putExtra("humanScore", humanScore);
                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }
        // Human's Hand: SA S4 CA C9
        else if (lineNumber == 9)
        {
            try
            {
                // check if there are no cards on hand
                int length = line.split(": ").length;
                String[] cards = {};
                // no cards on hand
                if (length == 1) {}

                // cards present
                else { cards = (line.split(": ")[1]).split(" "); }
                resumeGameState.putExtra("humanHand", cards);
                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }
        // Human's Pile: DJ DA C3 C5
        else if (lineNumber == 10)
        {
            try
            {
                // check if there are no cards on hand
                int length = line.split(": ").length;
                String[] cards = {};
                // no cards on hand
                if (length == 1) {}

                // cards present
                else { cards = (line.split(": ")[1]).split(" "); }
                resumeGameState.putExtra("humanPile", cards);
                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }
        // Table: [ [C6 S3] [S9] ] C8 CJ HA
        else if (lineNumber == 12)
        {
            try
            {
                // check if there are no cards on hand
                int length = line.split(": ").length;
                String[] cards = {};
                // no cards on hand
                if (length == 1) {}

                // cards present
                else
                {
                    String tableCards = line.split(": ")[1];

                    // index for the last build of cards
                    int index = tableCards.lastIndexOf("]");

                    // no build cards present
                    // split only the loose cards
                    if (index == -1)
                    {
                        cards = (line.split(": ")[1]).split(" ");

                    }
                    // build cards present
                    // extract the substring after the last occurence of "]" and split the cards
                    // to get the loose cards in the table
                    else
                    {
                        // we start from "index + 2" because the loose cards start after 2 indices
                        // from where the last "]" is
                        String looseCards = tableCards.substring(index + 2 , tableCards.length());
                        cards = looseCards.split(" ");
                    }
                    resumeGameState.putExtra("tableCards", cards);
                    return true;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return true;
        }
        //Build Owner: [ [C6 S3] [S9] ] Human
        else if (lineNumber == 14)
        {
            try
            {
                String owner = "";
                // check if there are no cards on hand
                int length = line.split(": ").length;
                String[] cards = {};
                // no cards on hand
                if (length == 1) {}

                // cards present
                else
                {
                    // [ [C6 S3] [S9] ] Human
                    String identifier = (line.split(": ")[1]);

                    // get the last index that identifies the build ("]")
                    int index = identifier.lastIndexOf("]");

                    // the string after the last occurence of "]" is the owner of the build
                    // Human
                    owner = identifier.substring(index + 2, identifier.length());
                    System.out.println("owner: " + owner);


                    // TODO: parse the build in build owner

                    // [ [C6 S3] [S9] ]
                    String build = identifier.substring(0, index + 1);
                    System.out.println("build: " + build);
                }
                resumeGameState.putExtra("owner", owner);
                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        // Last Capturer: Human
        else if (lineNumber == 16)
        {
            try
            {
                // check if there is lastCapturer's name specified
                int length = line.split(": ").length;
                String capturer = "";

                if (length == 1) {}

                // last capturer name present
                else { capturer = line.split(": ")[1]; }
                resumeGameState.putExtra("lastCapturer", capturer);
                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }
        // Deck: S7 D3 D5 H2 H3 S5 D8 C2 H9 CX CQ CK HJ S2 S6 D9 DX DQ DK D2 HX HQ HK C4 C7 S8 SJ H4 H7
        else if (lineNumber == 18)
        {
            try
            {
                // check if there are no cards on hand
                int length = line.split(": ").length;
                String[] cards = {};
                // no cards on hand
                if (length == 1) {}

                // cards present
                else { cards = (line.split(": ")[1]).split(" "); }

                resumeGameState.putExtra("deck", cards);
                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }
        // Next Player: Human
        else if (lineNumber == 20)
        {
            try
            {
                // check if there is nextPlayer's name specified
                int length = line.split(": ").length;
                String nextPlayer = "";

                if (length == 1) {}

                // next player name present
                else { nextPlayer = line.split(": ")[1]; }
                resumeGameState.putExtra("nextPlayer", nextPlayer);
                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}