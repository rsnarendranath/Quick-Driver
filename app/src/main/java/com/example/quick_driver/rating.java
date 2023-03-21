package com.example.quick_driver;

import static com.example.quick_driver.SignUpActivity.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class rating extends AppCompatActivity {
    private RatingBar ratingBar;
    private TextView textView;
    String userid,newid;
    private Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        ratingBar = findViewById(R.id.ratingBar);
        textView = findViewById(R.id.textView);
        submitButton = findViewById(R.id.submitButton);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String driverid = currentUser.getUid();
        DocumentReference documentReferenced=db.collection("foreman").document(driverid);
        DocumentReference docRef = documentReferenced.collection("currentride").document("details");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> docData = documentSnapshot.getData();
                    userid= docData.get("userid").toString();
                    Timestamp startTimeStamp = documentSnapshot.getTimestamp("stimestamp");
                    Date startDate = startTimeStamp.toDate();
                    newid=startDate.toString();
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error getting document", e);
            }
        });


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String ratingText = "Rating: " + rating;
                textView.setText(ratingText);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float rating = ratingBar.getRating();
                DocumentReference documentReferenceu=db.collection("users").document(userid);
                DocumentReference docRefu = documentReferenceu.collection("currentride").document("details");

                Map<String, Object> status = new HashMap<>();
                status.put("driverrating", rating);
                docRefu.set(status, SetOptions.merge())
                        .addOnSuccessListener(documentReference -> {
                            Log.d(TAG, "Data added successfully: " + docRefu.getId());
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error adding data: " + e.getMessage());
                        });
                Map<String, Object> statusd = new HashMap<>();
                statusd.put("status", "free");
                documentReferenced.set(status, SetOptions.merge())
                        .addOnSuccessListener(documentReference -> {
                            Log.d(TAG, "Data added successfully: " + docRefu.getId());
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error adding data: " + e.getMessage());
                        });


                docRef.set(status, SetOptions.merge())
                        .addOnSuccessListener(documentReference -> {
                            Log.d(TAG, "Data added successfully: " + docRef.getId());
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error adding data: " + e.getMessage());
                        });
//To copy the trip details into history

                // Get a reference to the source document
                DocumentReference sourceDocRef = documentReferenced.collection("currentride").document("details");

// Get the data from the source document
                sourceDocRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Create a reference to the destination document with "tree" as the new name
                        DocumentReference destinationDocRef = documentReferenced.collection("currentride").document(newid);

                        // Set the data from the source document to the new destination document
                        destinationDocRef.set(documentSnapshot.getData()).addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "Duplicated document with new name: tree");
                        }).addOnFailureListener(e -> {
                            Log.w(TAG, "Error duplicating document: " + e.getMessage());
                        });
                    }
                });

                DocumentReference sourceDocRefu = documentReferenceu.collection("currentride").document("details");

// Get the data from the source document
                sourceDocRefu.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Create a reference to the destination document with "tree" as the new name
                        DocumentReference destinationDocRef = documentReferenceu.collection("currentride").document(newid);

                        // Set the data from the source document to the new destination document
                        destinationDocRef.set(documentSnapshot.getData()).addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "Duplicated document with new name: tree");
                        }).addOnFailureListener(e -> {
                            Log.w(TAG, "Error duplicating document: " + e.getMessage());
                        });
                    }
                });







                //TODO: save rating to database or perform other actions
                Toast.makeText(rating.this, "Rating submitted: " + rating, Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(rating.this,forman_homepage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(rating.this, dendride.class);
        startActivity(intent);
        finish();
    }
}