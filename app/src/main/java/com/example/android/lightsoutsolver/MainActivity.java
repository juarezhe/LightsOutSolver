package com.example.android.lightsoutsolver;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    static String TAG = "MainActivity.java";
    int columns;
    int rows;
    int arrayLength;
    int[][] matrixA;
    int[] matrixB;
    TextView[] textViewArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find Set Dimensions Button and set an OnClickListener
        Button setDimensionsButton = findViewById(R.id.set_dimensions_button);
        setDimensionsButton.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the Generate Button is clicked
            @Override
            public void onClick(View view) {

                EditText numberOfColumns = findViewById(R.id.number_of_columns);
                rows = columns = Integer.parseInt(numberOfColumns.getText().toString());

                arrayLength = columns * rows;

                changeToGridView();
                changeToSetStateButtonBar();

                matrixA = generateMatrixA();
                matrixB = new int[]{1, 1, 0, 1, 0, 0, 0, 1, 0};

                textViewArray = new TextView[arrayLength];

                GridLayout gridView = findViewById(R.id.grid_view);
                gridView.setColumnCount(columns);
                gridView.setRowCount(rows);

                for (int n = 0; n < arrayLength; n++) {
                    textViewArray[n] = new TextView(getApplicationContext());
                    textViewArray[n].setText(String.valueOf(matrixB[n]));
                    textViewArray[n].setWidth(128);
                    textViewArray[n].setHeight(128);
                    textViewArray[n].setGravity(Gravity.CENTER);
                    if (matrixB[n] == 1)
                        textViewArray[n].setBackgroundColor(Color.YELLOW);
                    else
                        textViewArray[n].setBackgroundColor(Color.GRAY);
                    gridView.addView(textViewArray[n]);
                }
                // Create onClick listener for each square

            }
        });

        // Find Set State Button and assign an OnClickListener
        Button setStateButton = findViewById(R.id.set_state_button);
        setStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int n = 0; n < arrayLength; n++) {
                    matrixB[n] = Integer.parseInt(textViewArray[n].getText().toString());
                }
                changeToCalculateButtonBar();

            }
        });

        // Find Calculate Button and assign an OnClickListener
        Button calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inversionFirstPass();

                // Cycle through each Grid View child assigning and coloring solution
                GridLayout gridView = findViewById(R.id.grid_view);

                for (int n = 0; n < arrayLength; n++) {
                    TextView tempView = (TextView) gridView.getChildAt(n);
                    tempView.setText(String.valueOf(matrixB[n]));
                    if (matrixB[n] == 1)
                        tempView.setBackgroundColor(Color.BLUE);
                    else
                        tempView.setBackgroundColor(Color.GRAY);
                }

            }
        });

        // Find Reset Button and assign an OnClickListener
        Button resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reset();

            }
        });
    }

    public int[][] generateMatrixA() {
        int[][] mMatrixA = new int[arrayLength][arrayLength];

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
        int[] tempA = new int[arrayLength];
        int tempB = 0;

        for (int x = 0; x < arrayLength; x++) {
            for (int y = x; y < arrayLength; y++) {
                if (x == y) {
                    if (matrixA[x][y] == 0) {
                        for (int j = y; j < arrayLength; j++) {
                            if (matrixA[x][j] == 1) {
                                for (int n = 0; n < arrayLength; n++) {
                                    tempA[n] = matrixA[n][j];
                                    matrixA[n][j] = matrixA[n][y];
                                    matrixA[n][y] = tempA[n];
                                }
                                tempB = matrixB[j];
                                matrixB[j] = matrixB[y];
                                matrixB[y] = tempB;
                            }
                        }
                    } else if (matrixA[x][y] == 1) {
                        for (int n = 0; n < arrayLength; n++)
                            tempA[n] = matrixA[n][y];
                        tempB = matrixB[y];
                    }
                } else if (matrixA[x][y] == 1) {
                    for (int n = 0; n < arrayLength; n++)
                        matrixA[n][y] = binaryAdd(tempA[n], matrixA[n][y]);
                    matrixB[y] = binaryAdd(tempB, matrixB[y]);
                }
            }
        }

        inversionSecondPass();
    }

    public void inversionSecondPass() {

        int[] tempA = new int[arrayLength];
        int tempB = 0;

        for (int x = arrayLength - 1; x >= 0; x--) {
            for (int y = x; y >= 0; y--) {
                if (x == y) {
                    for (int n = 0; n < arrayLength; n++)
                        tempA[n] = matrixA[n][y];
                    tempB = matrixB[y];
                } else if (matrixA[x][y] == 1) {
                    for (int n = 0; n < arrayLength; n++)
                        matrixA[n][y] = binaryAdd(tempA[n], matrixA[n][y]);
                    matrixB[y] = binaryAdd(tempB, matrixB[y]);
                }
            }
        }
    }

    public int binaryAdd(int valueA, int valueB) {
        if ((valueA == 1 && valueB == 1) || (valueA == 0 && valueB == 0))
            return 0;
        else
            return 1;
    }

    public void changeToGridView() {
        // Find Set Dimensions View
        View setDimensionsView = findViewById(R.id.set_dimensions_view);

        // Find the Set State View
        View setStateView = findViewById(R.id.grid_view);

        // Remove Set Dimensions View
        setDimensionsView.setVisibility(View.GONE);

        // Add Set State View
        setStateView.setVisibility(View.VISIBLE);
    }

    public void changeToSetStateButtonBar() {
        // Find Set Dimensions Button and make it GONE
        Button setDimensionsButton = findViewById(R.id.set_dimensions_button);
        setDimensionsButton.setVisibility(View.GONE);

        // Find Set State Button and make it VISIBLE
        Button setStateButton = findViewById(R.id.set_state_button);
        setStateButton.setVisibility(View.VISIBLE);

        // Find Reset Button and make it VISIBLE
        Button resetButton = findViewById(R.id.reset_button);
        resetButton.setVisibility(View.VISIBLE);
    }

    public void changeToCalculateButtonBar() {
        // Find Set State Button and make it GONE
        Button setStateButton = findViewById(R.id.set_state_button);
        setStateButton.setVisibility(View.GONE);

        // Find Calculate Button and make it VISIBLE
        Button calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setVisibility(View.VISIBLE);
    }

    public void reset() {
        // Find Set State View and make it GONE
        View setStateView = findViewById(R.id.grid_view);
        setStateView.setVisibility(View.GONE);

        // Find Set State Button and make it GONE
        Button setStateButton = findViewById(R.id.set_state_button);
        setStateButton.setVisibility(View.GONE);

        // Find Calculate Button and make it GONE
        Button calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setVisibility(View.GONE);

        // Find the Reset Button and make it GONE
        Button resetButton = findViewById(R.id.reset_button);
        resetButton.setVisibility(View.GONE);

        // Find Set Dimensions View and make it VISIBLE
        View setDimensionsView = findViewById(R.id.set_dimensions_view);
        setDimensionsView.setVisibility(View.VISIBLE);

        // Find Set Dimensions Button and make it VISIBLE
        Button setDimensionsButton = findViewById(R.id.set_dimensions_button);
        setDimensionsButton.setVisibility(View.VISIBLE);

        //Find the Grid View and remove all children Views
        GridLayout gridView = findViewById(R.id.grid_view);
        gridView.removeAllViews();
    }
}