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
    int xLength;
    int yHeight;
    int[][] matrixA;

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

                xLength = columns * rows + 1;
                yHeight = columns * rows;

                // Find the Set Initial State View
                View setInitialStateView = findViewById(R.id.set_initial_state_view);

                // Find Submit Dimensions View
                View submitDimensionsView = findViewById(R.id.submit_dimensions_view);

                // Remove Submit Dimension View
                submitDimensionsView.setVisibility(View.GONE);

                // Replace with Generate View
                setInitialStateView.setVisibility(View.VISIBLE);

                matrixA = generateMatrixA();

                int[] matrixB = {1, 1, 0, 1, 0, 0, 0, 1, 0};

                for (int n = 0; n < yHeight; n++) {
                    matrixA[xLength - 1][n] = matrixB[n];
                }

                displayMatrices();
            }
        });

        // Find the button that generates A and its inverse (from B)
        final Button calculateInverse = findViewById(R.id.calculate_inverse_button);

        // Set a click listener on that View
        calculateInverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inversionFirstPass();

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
        int[][] mMatrixA = new int[xLength][yHeight];

        for (int n = 0; n < rows; n++) {
            for (int y = n * rows; y < rows + (n * rows); y++) {
                for (int x = n * columns; x < columns + (n * columns); x++) {
                    if (x == y) {
                        mMatrixA[x][y] = 1;

                        if (x + 1 < columns + (n * columns))
                            mMatrixA[x + 1][y] = 1;

                        if (x - 1 >= n * columns)
                            mMatrixA[x - 1][y] = 1;

                        if (x + columns < columns * columns)
                            mMatrixA[x + columns][y] = 1;

                        if (x - columns >= 0)
                            mMatrixA[x - columns][y] = 1;
                    } else if (mMatrixA[x][y] != 1)
                        mMatrixA[x][y] = 0;
                }
            }
        }

        return mMatrixA;
    }

    public void inversionFirstPass() {
        int[] tempA = new int[xLength];
        int tempB = 0;

        for (int x = 0; x < xLength; x++) {
            for (int y = x; y < yHeight; y++) {
                if (x == y) {
                    if (matrixA[x][y] == 0) {
                        for (int j = y; j < yHeight; j++) {
                            if (matrixA[x][j] == 1) {
                                for (int n = 0; n < xLength; n++) {
                                    tempA[n] = matrixA[n][j];
                                    matrixA[n][j] = matrixA[n][y];
                                    matrixA[n][y] = tempA[n];
                                }
                            }
                        }
                    } else if (matrixA[x][y] == 1) {
                        for (int n = 0; n < xLength; n++)
                            tempA[n] = matrixA[n][y];
                    }
                } else if (matrixA[x][y] == 1) {
                    for (int n = 0; n < xLength; n++)
                        matrixA[n][y] = binaryAdd(tempA[n], matrixA[n][y]);
                }
            }
        }

        //displayMatrices();
        inversionSecondPass();
    }

    public void inversionSecondPass() {

        int[] tempA = new int[xLength];
        int tempB = 0;

        for (int x = xLength - 2; x > 0; x--) {
            for (int y = x; y >= 0; y--) {
                if (x == y) {
                    for (int n = 0; n < xLength; n++)
                        tempA[n] = matrixA[n][y];
                } else if (matrixA[x][y] == 1) {
                    for (int n = 0; n < xLength; n++)
                        matrixA[n][y] = binaryAdd(tempA[n], matrixA[n][y]);
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

        for (int y = 0; y < yHeight; y++) {
            for (int x = 0; x < xLength; x++) {
                if (x == 0 && y == 0)
                    matrixToDisplay = matrixA[x][y] + " ";
                else if (x == xLength - 1)
                    matrixToDisplay += " | " + matrixA[x][y] + "\n";
                else
                    matrixToDisplay += matrixA[x][y] + " ";
            }
        }

        TextView matrixOutput = findViewById(R.id.matrix_output);
        matrixOutput.setText(matrixToDisplay);
    }
}
