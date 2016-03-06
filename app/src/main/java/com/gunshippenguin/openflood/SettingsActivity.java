package com.gunshippenguin.openflood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Activity allowing the user to configure settings.
 */
public class SettingsActivity extends AppCompatActivity {
    Spinner boardSizeSpinner, numColorsSpinner;
    int[] boardSizeChoices, numColorsChoices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set up the board size spinner
        boardSizeSpinner = (Spinner) findViewById(R.id.boardSizeSpinner);
        ArrayAdapter<String> boardSizesAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item);
        boardSizeChoices = getResources().getIntArray(R.array.boardSizeChoices);;
        for (int currBoardSize : boardSizeChoices) {
            boardSizesAdapter.add(currBoardSize + "x" + currBoardSize);
        }
        boardSizeSpinner.setAdapter(boardSizesAdapter);

        int currBoardSize = getIntent().getExtras().getInt("boardSize");
        for (int i = 0; i < boardSizeChoices.length; i++) {
            if (boardSizeChoices[i] == currBoardSize) {
                boardSizeSpinner.setSelection(i);
            }
        }

        // Set up the num colors spinner
        numColorsSpinner = (Spinner) findViewById(R.id.numColorsSpinner);
        ArrayAdapter<String> numColorsAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item);
        numColorsChoices = getResources().getIntArray(R.array.numColorsChoices);
        for (int currNumColors : numColorsChoices) {
            numColorsAdapter.add(Integer.toString(currNumColors));
        }
        numColorsSpinner.setAdapter(numColorsAdapter);

        int currNumColors = getIntent().getExtras().getInt("numColors");
        for (int i = 0; i < numColorsChoices.length; i++) {
            if (numColorsChoices[i] == currNumColors) {
                numColorsSpinner.setSelection(i);
            }
        }

        // Set up the apply button
        Button applyButton = (Button) findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dataIntent = new Intent();
                dataIntent.putExtra("settingsChanged", saveSettings());
                setResult(RESULT_OK, dataIntent);
                finish();
            }
        });
    }

    private boolean saveSettings() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor spEditor = sp.edit();
        boolean settingsChanged = false;

        // Update boardSize
        int selectedBoardSize = boardSizeChoices[boardSizeSpinner.getSelectedItemPosition()];
        int defaultBoardSize = getResources().getInteger(R.integer.default_board_size);
        if (selectedBoardSize != sp.getInt("board_size", defaultBoardSize)) {
            settingsChanged = true;
            spEditor.putInt("board_size", selectedBoardSize);
            spEditor.apply();
        }

        // Update number of colors
        int selectedNumColors = numColorsChoices[numColorsSpinner.getSelectedItemPosition()];
        int defaultNumColors = getResources().getInteger(R.integer.default_num_colors);
        if (selectedBoardSize != sp.getInt("num_colors", defaultNumColors)) {
            settingsChanged = true;
            spEditor.putInt("num_colors", selectedNumColors);
            spEditor.apply();
        }

        return settingsChanged;
    }
}
