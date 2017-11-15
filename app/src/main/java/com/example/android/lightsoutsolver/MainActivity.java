package com.example.android.lightsoutsolver;

import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static String TAG = "MainActivity.java";
    int columns;
    int rows;
    int arrayLength;
    int[][] matrixA;
    int[] matrixB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find Set Dimensions Button and set an OnClickListener
        Button setDimensionsButton = findViewById(R.id.set_dimensions_button);
        setDimensionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText numberOfColumns = findViewById(R.id.number_of_columns);
                columns = Integer.parseInt(numberOfColumns.getText().toString());

                EditText numberOfRows = findViewById(R.id.number_of_rows);
                rows = Integer.parseInt(numberOfRows.getText().toString());

                arrayLength = columns * rows;
                matrixB = new int[arrayLength];

                matrixA = generateMatrixA();
                generateGrid();
                changeToGridLayout();
                changeToCalculateButtonBar();
            }
        });

        // Find Calculate Button and assign an OnClickListener
        Button calculateButton = findViewById(R.id.solve_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inversionFirstPass();
                inversionSecondPass();

                GridLayout gridLayout = findViewById(R.id.grid_layout);

                for (int n = 0; n < arrayLength; n++) {
                    TextView cell = (TextView) gridLayout.getChildAt(n);
                    if (matrixB[n] == 1)
                        cell.setBackgroundColor(Color.BLUE);
                    else
                        cell.setBackgroundColor(Color.BLACK);
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

    private int[][] generateMatrixA() {
        int[][] mMatrixA = new int[arrayLength][arrayLength];

        for (int n = 0; n < rows; n++) {
            for (int y = n * columns; y < columns + (n * columns); y++) {
                for (int x = n * columns; x < columns + (n * columns); x++) {
                    if (x == y) {
                        mMatrixA[x][y] = 1;

                        if (x + 1 < columns + (n * columns))
                            mMatrixA[x + 1][y] = 1;

                        if (x - 1 >= n * columns)
                            mMatrixA[x - 1][y] = 1;

                        if (x + columns < arrayLength)
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

    private void generateGrid() {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        GridLayout gridLayout = findViewById(R.id.grid_layout);
        gridLayout.setColumnCount(columns);
        gridLayout.setRowCount(rows);
        int margin = 2;
        int gridSmallestDimension;

        LinearLayout gridContainer = findViewById(R.id.grid_container);
        if (gridContainer.getMeasuredWidth() < gridContainer.getMeasuredHeight())
            gridSmallestDimension = (gridContainer.getMeasuredWidth() - (columns * margin * 2)) / columns;
        else
            gridSmallestDimension = (gridContainer.getMeasuredHeight() - (columns * margin * 2)) / columns;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(margin, margin, margin, margin);

        for (int n = 0; n < arrayLength; n++) {
            TextView cell = new TextView(getApplicationContext());
            matrixB[n] = 0;
            cell.setBackgroundColor(Color.BLACK);
            cell.setLayoutParams(layoutParams);
            cell.setWidth(gridSmallestDimension);
            cell.setHeight(gridSmallestDimension);
            cell.setId(n);
            cell.setGravity(Gravity.CENTER);
            cell.requestLayout();
            gridLayout.addView(cell);
        }

        for (int n = 0; n < arrayLength; n++) {
            TextView container = (TextView) gridLayout.getChildAt(n);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int n = view.getId();
                    if (matrixB[n] == 1) {
                        matrixB[n] = 0;
                        view.setBackgroundColor(Color.BLACK);
                    } else {
                        matrixB[n] = 1;
                        view.setBackgroundColor(Color.YELLOW);
                    }
                }
            });
        }
    }

    private void inversionFirstPass() {
        int[] tempA = new int[arrayLength];
        int tempB = 0;

        for (int x = 0; x < arrayLength; x++) {
            for (int y = x; y < arrayLength; y++) {
                if (x == y) {
                    if (matrixA[x][y] == 0) {
                        for (int j = y + 1; j < arrayLength; j++) {
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
                    } else {
                        for (int n = 0; n < arrayLength; n++)
                            tempA[n] = matrixA[n][y];
                        tempB = matrixB[y];
                    }
                } else if (matrixA[x][y] == 1) {
                    for (int n = 0; n < arrayLength; n++)
                        matrixA[n][y] = binaryAdd(tempA[n], matrixA[n][y]);
                    matrixB[y] = binaryAdd(tempB, matrixB[y]);
                }
                //Log.v(TAG, "(x, y) = (" + x + ", " + y + ")");
                //debugMatrix(matrixA);
            }
        }
    }

    private void inversionSecondPass() {

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

    private int binaryAdd(int valueA, int valueB) {
        if ((valueA == 1 && valueB == 1) || (valueA == 0 && valueB == 0))
            return 0;
        else
            return 1;
    }

    private void changeToGridLayout() {
        // Find Set Dimensions View and make it GONE
        View setDimensionsView = findViewById(R.id.set_dimensions_view);
        setDimensionsView.setVisibility(View.GONE);

        // Find Grid Layout and make it VISIBLE
        View gridLayout = findViewById(R.id.grid_layout);
        gridLayout.setVisibility(View.VISIBLE);
    }

    private void changeToCalculateButtonBar() {
        // Find Set Dimensions Button and make it GONE
        Button setDimensionsButton = findViewById(R.id.set_dimensions_button);
        setDimensionsButton.setVisibility(View.GONE);

        // Find Calculate Button and make it VISIBLE
        Button calculateButton = findViewById(R.id.solve_button);
        calculateButton.setVisibility(View.VISIBLE);

        // Find Reset Button and make it VISIBLE
        Button resetButton = findViewById(R.id.reset_button);
        resetButton.setVisibility(View.VISIBLE);
    }

    private void reset() {
        // Find Grid Layout and make it GONE; remove all children Views
        GridLayout gridLayout = findViewById(R.id.grid_layout);
        gridLayout.setVisibility(View.GONE);
        gridLayout.removeAllViews();

        // Find Calculate Button and make it GONE
        Button calculateButton = findViewById(R.id.solve_button);
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
    }

    private void debugMatrix(int mMatrix[][]) {

        String messageToDisplay = null;

        for (int y = 0; y < arrayLength; y++) {
            for (int x = 0; x < arrayLength; x++) {
                if (x == 0)
                    messageToDisplay = String.valueOf(mMatrix[x][y]) + " ";
                else
                    messageToDisplay += String.valueOf(mMatrix[x][y]) + " ";

            }
            Log.v(TAG, messageToDisplay);
        }
    }
}