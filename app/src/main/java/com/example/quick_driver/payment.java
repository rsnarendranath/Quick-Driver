package com.example.quick_driver;

import static com.example.quick_driver.SignUpActivity.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class payment extends AppCompatActivity {
    private TableLayout tableLayoutResults;
    Button c,o;
    String Totalfare,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        tableLayoutResults = findViewById(R.id.tableLayoutResults);
        c= findViewById(R.id.ratingBar1);
        o=findViewById(R.id.online);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String driverid = currentUser.getUid();
        DocumentReference documentReference=db.collection("users").document(driverid);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name = documentSnapshot.getString("name");
                Log.d(TAG, "Name: " + name);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error getting document", e);
            }
        });

        DocumentReference docRef = documentReference.collection("currentride").document("details");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> docData = documentSnapshot.getData();
                    addTableRow("Total Distance (km)",": "+ docData.get("totalkm"));
                    addTableRow("Distance Fare",": "+  docData.get("distanceFare"));
                    addTableRow("NightStay Allowance",": "+  docData.get("nightStayFare"));
                    addTableRow("Perday Allowance",": "+  docData.get("perDayFare"));
                    addTableRow("Total Fare",": "+  docData.get("totalFare"));
                    Totalfare=docData.get("totalFare").toString();

                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error getting document", e);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(payment.this,urating.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(payment.this,pay.class);
                intent.putExtra("key",Totalfare);
                intent.putExtra("name",name);
                startActivity(intent);
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
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(payment.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}