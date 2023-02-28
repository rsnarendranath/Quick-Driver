package com.example.quick_driver;

import static com.example.quick_driver.SignUpActivity.TAG;
//import com.google.cloud.firestore.DocumentSnapshot;
//import com.google.cloud.firestore.Firestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

//import com.google.api.core.ApiFuture;
import com.google.firebase.firestore.*;
//import com.google.cloud.firestore.Firestore;
//import com.google.cloud.firestore.QuerySnapshot;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class user_hiringpage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextView workingDate, workingDatet;
    int flag = 0;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore fstore;
    String uid, driverid;
    EditText workingAddress, workingCity;
    Button datePickUp, datePickUpt, hire, button1, button2, button3;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_user_hiringpage);
        workingDate = findViewById(R.id.hireWorkingDate);
        workingDatet = findViewById(R.id.hireWorkingDatet);
        workingAddress = findViewById(R.id.hireWorkingAdress);
        workingCity = findViewById(R.id.hireWorkingCity);
        datePickUp = findViewById(R.id.hirePickDateButton);
        datePickUpt = findViewById(R.id.hirePickDateButtont);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        hire = findViewById(R.id.hireButton);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uid = mAuth.getCurrentUser().getUid();
        fstore = FirebaseFirestore.getInstance();
        DocumentReference userRef = fstore.collection("users").document(uid);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSelectedButton("incity");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSelectedButton("outcity1");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSelectedButton("outcity2");
            }
        });
        datePickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        datePickUpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser();
                uid = mAuth.getCurrentUser().getUid();
                fstore = FirebaseFirestore.getInstance();

//                //CollectionReference citiesRef = fstore.collection("foreman");
//                CollectionReference collectionReference = fstore.collection("foreman");
//                List<String> namesList = new ArrayList<>();
//                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                String name = document.getString("name");
//                                namesList.add(name);
//                            }
//
//
//                        } else {
//                            Log.d("TAG", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
                String pass_value="";
                if(workingCity.getText().toString().equals("coimbatore")){
                    driverid="RMvpSNB2kUWzchWCl3pwa4GITTu1";
                    pass_value="You have Applied Successfully to Hire a driver .\n You can see other detail Hired Detail page";
                } else if (workingCity.getText().toString().equals("chennai")) {
                    driverid = "nq7HapEgmhYIeBGE3E8smRY2WVH3";
                    pass_value="You have Applied Successfully to Hire a driver .\n You can see other detail Hired Detail page";
                }else{
                    driverid = "nq7HapEgmhYIeBGE3E8smRY2WVH3";
                    pass_value="Sorry there is no driver for the city \n you have provided";
                }



                DocumentReference userRef = fstore.collection("users").document(uid);
                DocumentReference userreference = userRef.collection("currentride").document("details");
                Map<String, Object> updatedValues = new HashMap<>();
                DocumentReference driverref = fstore.collection("foreman").document(driverid);
                DocumentReference driverreference = driverref.collection("currentride").document("details");
                Map<String, Object> status = new HashMap<>();
                status.put("status", "booked");
                userRef.set(status, SetOptions.merge())
                        .addOnSuccessListener(documentReference -> {
                            Log.d(TAG, "Data added successfully: " + userRef.getId());
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error adding data: " + e.getMessage());
                        });
                driverref.set(status, SetOptions.merge())
                        .addOnSuccessListener(documentReference -> {
                            Log.d(TAG, "Data added successfully: " + driverref.getId());
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error adding data: " + e.getMessage());
                        });
                updatedValues.put("date", workingDate.getText().toString());
                updatedValues.put("datet", workingDatet.getText().toString());
                updatedValues.put("address", workingAddress.getText().toString());
                updatedValues.put("city", workingCity.getText().toString());
                updatedValues.put("driverid", driverid);
                userreference.set(updatedValues, SetOptions.merge())
                        .addOnSuccessListener(documentReference -> {
                            Log.d(TAG, "Data added successfully: " + userRef.getId());
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error adding data: " + e.getMessage());
                        });
                updatedValues.put("userid", uid);
                driverreference.set(updatedValues, SetOptions.merge())
                        .addOnSuccessListener(documentReference -> {
                            Log.d(TAG, "Data added successfully: " + driverref.getId());
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error adding data: " + e.getMessage());
                        });

                Intent intent = new Intent(user_hiringpage.this, AfterHireRequestActivity.class);
                intent.putExtra("key", pass_value);
                startActivity(intent);
            }
        });
    }


    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)

        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String date = day + "/" + month + "/" + year;
        if (flag == 0) {
            workingDate.setText(date);
            flag = 1;
        } else workingDatet.setText(date);


    }

    public void updateSelectedButton(String buttonId) {

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uid = mAuth.getCurrentUser().getUid();
        fstore = FirebaseFirestore.getInstance();
        DocumentReference userRef = fstore.collection("users").document(uid);
        DocumentReference userreference = userRef.collection("currentride").document("details");
        Map<String, Object> updatedValues = new HashMap<>();
        updatedValues.put("status", buttonId);
        userreference.set(updatedValues, SetOptions.merge())
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Data added successfully: " + userRef.getId());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding data: " + e.getMessage());
                });

    }
}
