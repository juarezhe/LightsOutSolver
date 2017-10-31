package com.example.android.lightsoutsolver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static String TAG = "MainActivity.java";
    int columns;
    int rows;
    int area;
    int[][] matrixA;
    int[] matrixB = {1, 1, 0, 1, 0, 0, 0, 1, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the button that submits grid dimensions
        final Button submitDimensionsButton = findViewById(R.id.submit_dimensions_button);

        // Set a click listener on that View
        submitDimensionsButton.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the Generate Button is clicked
            @Override
            public void onClick(View view) {

                EditText numberOfColumns = findViewById(R.id.number_of_columns);
                columns = Integer.parseInt(numberOfColumns.getText().toString());

                EditText numberOfRows = findViewById(R.id.number_of_rows);
                rows = Integer.parseInt(numberOfRows.getText().toString());

                area = columns * rows;

                // Find the Set Initial State View
                View setInitialStateView = findViewById(R.id.set_initial_state_view);

                // Find Submit Dimensions View
                View submitDimensionsView = findViewById(R.id.submit_dimensions_view);

                // Remove Submit Dimension View
                submitDimensionsView.setVisibility(View.GONE);

                // Replace with Generate View
                setInitialStateView.setVisibility(View.VISIBLE);

                LinearLayout initialStateEntryFields = findViewById(R.id.initial_state_entry_fields);

                for (int i = 0; i < area; i++) {
                    EditText myEditText = new EditText(initialStateEntryFields.getContext()); //Context
                    // myEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    // myEditText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    initialStateEntryFields.addView(new EditText(initialStateEntryFields.getContext()));
                }
            }
        });

        // Find the button that generates a lights out grid
        final Button generateButton = findViewById(R.id.generate_button);

        // Set a click listener on that View
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Should calculate inverse

                Log.v(TAG, "Columns: " + columns);
                Log.v(TAG, "Rows: " + rows);
                Log.v(TAG, "Area: " + area);

                matrixA = new int[area][area];
                // matrixB = new int[area];

                for (int m = 0; m < area; m++) {
                    for (int n = 0; n < area; n++) {
                        if (m == n) {
                            matrixA[m][n] = 1;

                            if (n - 1 >= 0)
                                matrixA[m][n - 1] = 1;
                            if (n + 1 < area)
                                matrixA[m][n + 1] = 1;
                            if (n - columns >= 0)
                                matrixA[m][n - columns] = 1;
                            if (n + columns < area)
                                matrixA[m][n + columns] = 1;
                        } else if (matrixA[m][n] != 1)
                            matrixA[m][n] = 0;
                    }
                }

                String matrixToDisplay = null;

                for (int i = 0; i < area; i++) {
                    for (int j = 0; j < area; j++) {
                        if (i == 0 && j == 0)
                            matrixToDisplay = matrixA[i][j] + " ";
                        else if (j == area - 1) {
                            matrixToDisplay += matrixA[i][j];
                        }
                        else
                            matrixToDisplay += matrixA[i][j] + " ";
                    }
                    matrixToDisplay += " | " + matrixB[i] + "\n";
                }

                TextView matrixOutput = findViewById(R.id.matrix_output);
                matrixOutput.setText(matrixToDisplay);

                calculateInverse(matrixA, matrixB, area);
            }
        });

        // Find the button that generates a lights out grid
        final Button resetButton = findViewById(R.id.reset_button);

        // Set a click listener on that View
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Find the Set Initial State View
                View setInitialStateView = findViewById(R.id.set_initial_state_view);

                // Find Submit Dimensions View
                View submitDimensionsView = findViewById(R.id.submit_dimensions_view);

                // Replace with Generate View
                setInitialStateView.setVisibility(View.GONE);

                // Remove Submit Dimension View
                submitDimensionsView.setVisibility(View.VISIBLE);

                EditText numberOfColumns = findViewById(R.id.number_of_columns);
                numberOfColumns.setText(null);

                EditText numberOfRows = findViewById(R.id.number_of_rows);
                numberOfRows.setText(null);

                TextView matrixOutput = findViewById(R.id.matrix_output);
                matrixOutput.setText(null);
            }
        });
    }

    public void calculateInverse(int[][] matrixA, int[] matrixB, int size) {
        // Consider first dimension of matrixA[][] and matrixB[] to be rows
        // such that matrixB is a single vertical column

        // Check the first column of matrixA (matrixA[i][0]) for values of 1
        // For each row with a 1 in its first column, "add" the topmost row to the others,
        // then swap to get the row and column to be a 1

        int[][] tempA;
        int[][] tempB;

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < size; j++) {
                if (matrixA[i][j] == 1)
                    Log.v(TAG, String.valueOf(i) + String.valueOf(j));
            }
        }

    }

    public int binaryAdd(int valueA, int valueB) {
        if ((valueA == 1 && valueB == 1) || (valueA == 0 && valueB == 0))
            return 0;
        else
            return 1;
    }
}
