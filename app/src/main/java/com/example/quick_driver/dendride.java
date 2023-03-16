package com.example.quick_driver;

import static com.example.quick_driver.SignUpActivity.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class dendride extends AppCompatActivity {
    private EditText editTextCarReading;
    private Button buttonSubmit;
    String userid;
    private static final double PER_DAY_ALLOWANCE = 500.0;
    private static final double NIGHT_STAY_ALLOWANCE = 250.0;
    private TableLayout tableLayoutResults;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dendride);
        editTextCarReading = findViewById(R.id.editTextCarReading);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        tableLayoutResults = findViewById(R.id.tableLayoutResults);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String driverid = currentUser.getUid();
        DocumentReference documentReference=db.collection("foreman").document(driverid);
        DocumentReference docRef = documentReference.collection("currentride").document("details");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> docData = documentSnapshot.getData();
                    userid= docData.get("userid").toString();
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error getting document", e);
            }
        });

        // Set click listener for submit button
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editTextCarReading.getText().toString().trim();
                if (!TextUtils.isEmpty(text)) {
                    // Access a Cloud Firestore instance from your Activity
                    DocumentReference documentReferenceu=db.collection("users").document(userid);
                    DocumentReference docRefu = documentReferenceu.collection("currentride").document("details");
                    Map<String, Object> status = new HashMap<>();
                    status.put("status", "payment");
                    documentReferenceu.set(status, SetOptions.merge())
                            .addOnSuccessListener(documentReference -> {
                                Log.d(TAG, "Data added successfully: " + documentReferenceu.getId());
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error adding data: " + e.getMessage());
                            });
                    Map<String, Object> statusd = new HashMap<>();
                    status.put("status", "free");
                    documentReference.set(statusd, SetOptions.merge())
                            .addOnSuccessListener(documentReference -> {
                                Log.d(TAG, "Data added successfully: " + "xx");
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error adding data: " + e.getMessage());
                            });
                    // Create a new document with a generated ID
                    Map<String, Object> data = new HashMap<>();
                    data.put("endreading", text);
                    data.put("etimestamp", Timestamp.now());
                    docRefu.set(data, SetOptions.merge())
                            .addOnSuccessListener(documentReference -> {
                                Log.d(TAG, "Data added successfully: " + docRefu.getId());
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error adding data: " + e.getMessage());
                            });

                    docRef.set(data, SetOptions.merge())
                            .addOnSuccessListener(documentReference -> {
                                Log.d(TAG, "Data added successfully: " + docRef.getId());
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error adding data: " + e.getMessage());
                            });                }
                // Get car reading entered by user
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String displayText = "";
                            Map<String, Object> docData = documentSnapshot.getData();


                            // Calculate the total kilometers traveled
                            double totalKm = Double.parseDouble(text) - Double.parseDouble(docData.get("stratreading").toString());

                            // Retrieve the two timestamp values from Firestore
                            Timestamp startTimeStamp = documentSnapshot.getTimestamp("stimestamp");
                            Timestamp endTimeStamp = documentSnapshot.getTimestamp("etimestamp");

// Convert the timestamp values to Date objects
                            Date startDate = startTimeStamp.toDate();
                            Date endDate = endTimeStamp.toDate();

                            long totalMillis = endDate.getTime() - startDate.getTime();
                            double totalHours = (double) totalMillis / (1000 * 60 * 60);
                            long totalDays = TimeUnit.MILLISECONDS.toDays(totalMillis);

// Format the output as a string


                            // Calculate the per-kilometer rate based on the total kilometers traveled
                            double perKmRate = (totalKm < 100) ? 10.0 : (totalKm < 200) ? 9.0 : 8.0;

                            // Calculate the fare for the distance traveled
                            double distanceFare = totalKm * perKmRate;

                            // Calculate the per-day allowance for the trip

                            double perDayFare = totalDays * PER_DAY_ALLOWANCE;

                            // Calculate the night-stay allowance for the trip
                            double nightStayFare = (totalHours > 8) ? NIGHT_STAY_ALLOWANCE : 0.0;

                            // Calculate the total fare for the trip
                            double totalFare = distanceFare + perDayFare + nightStayFare;
                            // Get the Firestore instance


// Create a Map to store the fare values
                            Map<String, Object> fareData = new HashMap<>();
                            fareData.put("totalkm", totalKm);
                            fareData.put("distanceFare", distanceFare);
                            fareData.put("perDayFare", perDayFare);
                            fareData.put("nightStayFare", nightStayFare);
                            fareData.put("totalFare", totalFare);
                            addTableRow("Total Distance (km)",": "+ totalKm);
                            addTableRow("Distance Fare",": "+  distanceFare);
                            addTableRow("NightStay Allowance",": "+  nightStayFare);
                            addTableRow("Perday Allowance",": "+  perDayFare);
                            addTableRow("Total Fare",": "+  totalFare);

// Add the fare data to the Firestore collection
                            DocumentReference documentReferenceu=db.collection("users").document(userid);
                            DocumentReference docRefu = documentReferenceu.collection("currentride").document("details");
                            docRefu.set(fareData, SetOptions.merge())
                                    .addOnSuccessListener(documentReference -> {
                                        Log.d(TAG, "Data added successfully: " + docRefu.getId());
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Error adding data: " + e.getMessage());
                                    });

                            docRef.set(fareData, SetOptions.merge())
                                    .addOnSuccessListener(documentReference -> {
                                        Log.d(TAG, "Data added successfully: " + docRef.getId());
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Error adding data: " + e.getMessage());
                                    });

                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error getting document", e);
                    }
                });

            }
        });
    }
    private void addTableRow(String title, String value) {
        TableRow row = new TableRow(this);
        TextView textViewTitle = new TextView(this);
        TextView textViewValue = new TextView(this);
        textViewTitle.setText(title);
        textViewTitle.setTextSize(21); // Increase font size here
        textViewValue.setText(value);
        textViewValue.setTextSize(20);
        row.addView(textViewTitle);
        row.addView(textViewValue);
        tableLayoutResults.addView(row);
    }
}


