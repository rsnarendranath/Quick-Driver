package com.example.quick_driver;

import static com.example.quick_driver.foreman_signup.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class cardetail extends AppCompatActivity {
    ProgressDialog progressDialog ;
    FirebaseAuth mAuth ;
    FirebaseUser mUser ;
    FirebaseFirestore fstore;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardetail);
        progressDialog = new ProgressDialog( this ) ;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // Create a Button to submit the car details
        Button submitButton = findViewById(R.id.submit_button);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the car details from the EditText fields
                EditText makeEditText = findViewById(R.id.make_edit_text);
                EditText modelEditText = findViewById(R.id.model_edit_text);
                EditText yearEditText = findViewById(R.id.year_edit_text);
                String make = makeEditText.getText().toString();
                String model = modelEditText.getText().toString();
                int year = Integer.parseInt(yearEditText.getText().toString());
                uid = mAuth.getCurrentUser().getUid();
                Map<String, Object> updatedValues = new HashMap<>();
                updatedValues.put("make", make);
                updatedValues.put("model", model);
                updatedValues.put("year", year);

                fstore = FirebaseFirestore.getInstance();
                DocumentReference userRef = fstore.collection("users").document(uid);

                userRef.update(updatedValues)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Firestore", "Multiple strings successfully updated!");
                                sendUserToNextActivity();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Firestore", "Error updating multiple strings", e);
                            }
                        });
            }
        });
    }
    // Create a FirebaseFirestore object

    private void sendUserToNextActivity() {
        Intent intent = new Intent(cardetail.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}