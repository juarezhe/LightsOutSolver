package com.example.android.lightsoutsolver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

                matrixA = generateMatrixA();

                displayMatrices();

                /**
                 LinearLayout initialStateEntryFields = findViewById(R.id.initial_state_entry_fields);

                 for (int i = 0; i < area; i++) {
                 EditText myEditText = new EditText(initialStateEntryFields.getContext()); //Context
                 // myEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                 // myEditText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                 initialStateEntryFields.addView(new EditText(initialStateEntryFields.getContext()));
                 }
                 **/
            }
        });

        // Find the button that generates A and its inverse (from B)
        final Button calculateInverse = findViewById(R.id.calculate_inverse_button);

        // Set a click listener on that View
        calculateInverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calculateInverse();

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

    public int[][] generateMatrixA() {
        int[][] mMatrixA = new int[area][area];

        for (int i = 0; i < area; i++) {
            for (int j = 0; j < area; j++) {
                if (i == j) {
                    mMatrixA[i][j] = 1;

                    if (j - 1 >= 0)
                        mMatrixA[i][j - 1] = 1;
                    if (j + 1 < area)
                        mMatrixA[i][j + 1] = 1;
                    if (j - columns >= 0)
                        mMatrixA[i][j - columns] = 1;
                    if (j + columns < area)
                        mMatrixA[i][j + columns] = 1;
                } else if (mMatrixA[i][j] != 1)
                    mMatrixA[i][j] = 0;
            }
        }
        return mMatrixA;
    }

    public void calculateInverse() {
        // Consider first dimension of matrixA[][] and matrixB[] to be rows
        // such that matrixB is a single vertical column

        // Check the first column of matrixA (matrixA[i][0]) for values of 1
        // For each row with a 1 in its first column, "add" the topmost row to the others,
        // then swap to get the row and column to be a 1

        int[] tempA = new int[area];
        int tempB = 0;

        for (int i = 0; i < area; i++) {
            for (int j = 0; j < area; j++) {
                if (i == j && matrixA[i][j] == 1) { // At i == j, the value should be 1, all other values should be 0
                    for (int n = 0; n < area; n++)
                        tempA[n] = matrixA[n][j];
                    tempB = matrixB[j];
                } else if (i == j && matrixA[i][j] == 0) {
                    for (int n = 0; n < area; n++) {
                        tempA[n] = matrixA[n][j];
                        matrixA[n][j] = matrixA[n][j + 1];
                        matrixA[n][j + 1] = tempA[n];
                        tempA[n] = matrixA[n][j];
                    }
                    tempB = matrixB[j];
                    matrixB[j] = matrixB[j + 1];
                    matrixB[j + 1] = tempB;
                    tempB = matrixB[j];
                } else if (i - 1 >= 0) {
                    if (matrixA[i][j] == 1 && matrixA[i - 1][j] != 1) {
                        for (int n = 0; n < area; n++)
                            matrixA[n][j] = binaryAdd(tempA[n], matrixA[n][j]);
                        matrixB[j] = binaryAdd(tempB, matrixB[j]);
                    }
                } else if (matrixA[i][j] == 1) {
                    for (int n = 0; n < area; n++)
                        matrixA[n][j] = binaryAdd(tempA[n], matrixA[n][j]);
                    matrixB[j] = binaryAdd(tempB, matrixB[j]);
                }
            }
        }

        displayMatrices();
    }

    public int binaryAdd(int valueA, int valueB) {
        if ((valueA == 1 && valueB == 1) || (valueA == 0 && valueB == 0))
            return 0;
        else
            return 1;
    }

    public void displayMatrices() {
        String matrixToDisplay = null;

        for (int j = 0; j < area; j++) {
            for (int i = 0; i < area; i++) {
                if (j == 0 && i == 0)
                    matrixToDisplay = matrixA[i][j] + " ";
                else if (i == area - 1) {
                    matrixToDisplay += matrixA[i][j] + " | " + matrixB[j];
                } else
                    matrixToDisplay += matrixA[i][j] + " ";
            }
            matrixToDisplay += "\n";
        }

        TextView matrixOutput = findViewById(R.id.matrix_output);
        matrixOutput.setText(matrixToDisplay);
    }
}
