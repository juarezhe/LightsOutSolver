package com.example.android.lightsoutsolver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int columns;
    int rows;
    int area;
    int[][] matrixA;
    //TextView matrixOutput = (TextView) findViewById(R.id.matrix_output);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the button that generates a lights out grid
        final Button submitDimensionsButton = (Button) findViewById(R.id.submit_dimensions_button);

        // Set a click listener on that View
        submitDimensionsButton.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the Generate Button is clicked
            @Override
            public void onClick(View view) {

                EditText numberOfColumns = (EditText) findViewById(R.id.number_of_columns);
                columns = Integer.parseInt(numberOfColumns.getText().toString());

                EditText numberOfRows = (EditText) findViewById(R.id.number_of_rows);
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

                /**
                 Log.v("MainActivity.java", "Columns: " + columns);
                 Log.v("MainActivity.java", "Rows: " + rows);
                 Log.v("MainActivity.java", "Area: " + area);

                 matrixA = new int[area][area];

                 for (int m = 0; m < area; m++) {
                 for (int n = 0; n < area; n++) {
                 if (m == n) {
                 matrixA[m][n] = 1;

                 if (n - 1 >= 0)
                 matrixA[m][n - 1] = 1;
                 else
                 Log.v("MainActivity.java", "N - 1 error. m= " + m + ", n= " + n + ", area= " + area);

                 if (n + 1 < area)
                 matrixA[m][n + 1] = 1;
                 else
                 Log.v("MainActivity.java", "N + 1 error. m= " + m + ", n= " + n + ", area= " + area);

                 if (n - columns >= 0)
                 matrixA[m][n - columns] = 1;
                 else
                 Log.v("MainActivity.java", "N - c error. m= " + m + ", n= " + n + ", area= " + area);

                 if (n + columns < area)
                 matrixA[m][n + columns] = 1;
                 else
                 Log.v("MainActivity.java", "N + c error. m= " + m + ", n= " + n + ", area= " + area);
                 } else if (matrixA[m][n] != 1)
                 matrixA[m][n] = 0;
                 }
                 }

                 String matrixToDisplay = null;

                 for (int i = 0; i < area; i++) {
                 for (int j = 0; j < area; j++) {
                 if (j == 0)
                 matrixToDisplay = matrixA[i][j] + " ";
                 else if (j == area - 1)
                 matrixToDisplay += matrixA[i][j];
                 else
                 matrixToDisplay += matrixA[i][j] + " ";
                 }
                 Log.v("MainActivity.java", matrixToDisplay);
                 matrixToDisplay = null;
                 }
                 **/
            }
        });

        // Find the button that generates a lights out grid
        final Button generateButton = (Button) findViewById(R.id.generate_button);

        // Set a click listener on that View
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Generate button should create matrixA from dimensions,
                // calculate
            }
        });

        // Find the button that generates a lights out grid
        final Button resetButton = (Button) findViewById(R.id.reset_button);

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
            }
        });
    }
}
