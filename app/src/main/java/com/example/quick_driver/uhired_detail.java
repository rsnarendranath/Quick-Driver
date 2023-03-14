package com.example.quick_driver;

import static com.example.quick_driver.SignUpActivity.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.model.LatLng;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class uhired_detail extends AppCompatActivity {
    private TextView rideDetailsTextView,f;
    Button c,l,b;
    String driverid;
    LatLng latLng;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uhired_detail);
        rideDetailsTextView = findViewById(R.id.textView);
        f = findViewById(R.id.textViewf);
        c=findViewById(R.id.button1);
        b=findViewById(R.id.button8);
        l=findViewById(R.id.button7);
        // Get current ride details from Firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        DocumentReference documentReference=db.collection("users").document(userId);
        DocumentReference docRef = documentReference.collection("currentride").document("details");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    String displayText = "";
                    Map<String, Object> docData = documentSnapshot.getData();
                    displayText += "Address    : " + docData.get("address") +"\n";
                    displayText += "City            : " + docData.get("city") +"\n";
                    displayText += "From Date: " + docData.get("date") +"\n";
                    displayText += "To Date     : " + docData.get("datet") +"\n";
                    displayText += "Type          : " + docData.get("status") +"\n";


                    TextView textView = findViewById(R.id.textView);
                    textView.setText(displayText);
                    DocumentReference ddocumentReference=db.collection("foreman").document((String) docData.get("driverid"));

                    ddocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                HashMap<String, Double> location = (HashMap<String, Double>) documentSnapshot.get("location");
                                if (location != null) {
                                    latLng = new LatLng(location.get("latitude"), location.get("longitude"));
                                    // use geoPoint as needed
                                }

                                String displayText = "";
                                Map<String, Object> docData = documentSnapshot.getData();
                                displayText += "Name           : " + docData.get("name") +"\n";
                                displayText += "Phone          : " + docData.get("phone") +"\n";
                                displayText += "Email           : " + docData.get("email") +"\n";
                                displayText += "Languages : " + docData.get("language") +"\n";
                                displayText += "City              : " + docData.get("city") +"\n";

                                TextView f = findViewById(R.id.textViewf);
                                f.setText(displayText);


                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error getting document", e);
                        }
                    });
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error getting document", e);
            }
        });

        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(uhired_detail.this,umap.class);
                intent.putExtra("location", latLng);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(uhired_detail.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(uhired_detail.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
