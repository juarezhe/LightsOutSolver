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


                View generateView = findViewById(R.id.generate_view);

                //Remove Submit Dimensions button
                submitDimensionsButton.setVisibility(View.GONE);

                //Replace with Generate and Reset buttons
                generateView.setVisibility(View.VISIBLE);

                EditText numberOfColumns = (EditText) findViewById(R.id.number_of_columns);
                columns = Integer.parseInt(numberOfColumns.getText().toString());

                EditText numberOfRows = (EditText) findViewById(R.id.number_of_rows);
                rows = Integer.parseInt(numberOfRows.getText().toString());

                area = columns * rows;

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
                                Log.v("MainActivity.java", "N - 1 error. m= " + m + ", n= " + n + ", area= "  + area);

                            if (n + 1 < area)
                                matrixA[m][n + 1] = 1;
                            else
                                Log.v("MainActivity.java", "N + 1 error. m= " + m + ", n= " + n + ", area= "  + area);

                            if (n - columns >= 0)
                                matrixA[m][n - columns] = 1;
                            else
                                Log.v("MainActivity.java", "N - c error. m= " + m + ", n= " + n + ", area= "  + area);

                            if (n + columns < area)
                                matrixA[m][n + columns] = 1;
                            else
                                Log.v("MainActivity.java", "N + c error. m= " + m + ", n= " + n + ", area= "  + area);
                        }
                        else if (matrixA[m][n] != 1)
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
            }
        });

        //matrixOutput.setText(matrixA.toString());

        /**
         GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout);
         gridLayout.setColumnCount(columns);
         gridLayout.setRowCount(rows);

         int count = gridLayout.getChildCount();

         Log.v("MainActivity.java", "Child count: " + count);

         GridLayout gridLayout = new GridLayout(this);

         //define how many rows and columns to be used in the layout
         gridLayout.setColumnCount(columns);
         gridLayout.setRowCount(rows);


         for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
         editTexts[i][j] = new EditText(this);
         setPos(editTexts[i][j], i, j);
         gridLayout.addView(editTexts[i][j]);
         }
         }
         setContentView(gridLayout);
         **/
    }
    /**
     private void update() {
     TableLayout table = (TableLayout) findViewById(R.id.table_layout);

     TableRow tr = new TableRow(this);
     tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

     TextView tv = new TextView(this);
     tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
     tv.setText("Off");
     tr.addView(tv);

     table.addView(tr);
     }
     **/
}
