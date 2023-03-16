package com.example.quick_driver;

import static com.example.quick_driver.SignUpActivity.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class dstartride extends AppCompatActivity {
    private TextView rideDetailsTextView,f;
    Button c,l,b;
    String userid;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dstratride);
        rideDetailsTextView = findViewById(R.id.textView);
        f = findViewById(R.id.textViewf);
        c=findViewById(R.id.button1);
        b=findViewById(R.id.button8);

        EditText editText = findViewById(R.id.textView2);
        Button button = findViewById(R.id.button6);



        // Get current ride details from Firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String driverid = currentUser.getUid();
        DocumentReference documentReference=db.collection("foreman").document(driverid);
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
                    String startreading = (String) docData.get("stratreading");
                    if(startreading==null || startreading.isEmpty()){
                        button.setVisibility(View.VISIBLE);
                        editText.setVisibility(View.VISIBLE);

                    }
                    else{
                        editText.setVisibility(View.GONE);
                        button.setVisibility(View.GONE);

                        String timestampString = docData.get("stimestamp").toString();
                        timestampString = timestampString.replaceAll("[^\\d]", ""); // remove non-numeric characters
                        long timestampLong = Long.parseLong(timestampString); // convert string to long

                        Date date = new Date(timestampLong); // create date object from timestamp
                        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss"); // create date formatter
                        String formattedDate = formatter.format(date);

                        displayText += "Start Time  : " + "2023-"+ formattedDate +"\n";
                        displayText += "Car Reading: " + docData.get("stratreading") +"\n";
                    }
                    userid= docData.get("userid").toString();
                    TextView textView = findViewById(R.id.textView);
                    textView.setText(displayText);

                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error getting document", e);
            }
        });




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(text)) {
                    // Access a Cloud Firestore instance from your Activity
                    DocumentReference documentReferenceu=db.collection("users").document(userid);
                    DocumentReference docRefu = documentReferenceu.collection("currentride").document("details");
                    Map<String, Object> status = new HashMap<>();
                    status.put("status", "started");
                    documentReferenceu.set(status, SetOptions.merge())
                            .addOnSuccessListener(documentReference -> {
                                Log.d(TAG, "Data added successfully: " + documentReferenceu.getId());
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error adding data: " + e.getMessage());
                            });
                    documentReference.set(status, SetOptions.merge())
                            .addOnSuccessListener(documentReference -> {
                                Log.d(TAG, "Data added successfully: " + "xx");
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error adding data: " + e.getMessage());
                            });
                    // Create a new document with a generated ID
                    Map<String, Object> data = new HashMap<>();
                    data.put("stratreading", text);
                    data.put("stimestamp", Timestamp.now());
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
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dstartride.this,dendride.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dstartride.this,forman_homepage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
