package com.example.quick_driver;

import static com.example.quick_driver.SignUpActivity.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class history extends AppCompatActivity {
    private TableLayout tableLayoutResults;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        fAuth= FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
       String userid=fAuth.getCurrentUser().getUid();

        // Get the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Get a reference to the document
        DocumentReference colref = db.collection("users").document(userid);
        DocumentReference docRef = db.collection("currentride").document("details");

// Retrieve the data from the document
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Get the ride history data from the document
                    Map<String, Object> rideHistoryData = (Map<String, Object>) document.get("details");

                    // Create a table to display the data
                    TableLayout table = findViewById(R.id.tableLayout);

                    // Create a row for the table header
                    TableRow headerRow = new TableRow(this);
                    headerRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                    // Create text views for the table header
                    TextView usernameHeader = new TextView(this);
                    usernameHeader.setText("Username");
                    usernameHeader.setTypeface(null, Typeface.BOLD);
                    headerRow.addView(usernameHeader);

                    TextView distanceHeader = new TextView(this);
                    distanceHeader.setText("Distance");
                    distanceHeader.setTypeface(null, Typeface.BOLD);
                    headerRow.addView(distanceHeader);

                    TextView fareHeader = new TextView(this);
                    fareHeader.setText("Fare");
                    fareHeader.setTypeface(null, Typeface.BOLD);
                    headerRow.addView(fareHeader);

                    TextView startDateHeader = new TextView(this);
                    startDateHeader.setText("Start Date");
                    startDateHeader.setTypeface(null, Typeface.BOLD);
                    headerRow.addView(startDateHeader);

                    TextView endDateHeader = new TextView(this);
                    endDateHeader.setText("End Date");
                    endDateHeader.setTypeface(null, Typeface.BOLD);
                    headerRow.addView(endDateHeader);

                    // Add the header row to the table
                    table.addView(headerRow);

                    // Loop through the ride history data and add it to the table
                    for (Map.Entry<String, Object> entry : rideHistoryData.entrySet()) {
                        Map<String, Object> rideData = (Map<String, Object>) entry.getValue();

                        // Create a row for the table
                        TableRow row = new TableRow(this);
                        row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                        // Add text views for each column in the row
                        TextView username = new TextView(this);
                        username.setText(rideData.get("username").toString());
                        row.addView(username);

                        TextView distance = new TextView(this);
                        distance.setText(rideData.get("distance").toString());
                        row.addView(distance);

                        TextView fare = new TextView(this);
                        fare.setText(rideData.get("fare").toString());
                        row.addView(fare);

                        TextView startDate = new TextView(this);
                        startDate.setText(rideData.get("startDate").toString());
                        row.addView(startDate);

                        TextView endDate = new TextView(this);
                        endDate.setText(rideData.get("endDate").toString());
                        row.addView(endDate);

                        // Add the row to the table
                        table.addView(row);
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

    }
}