package com.example.quick_driver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class update_driver_profile extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseUser user;
    private EditText nameEditText, emailEditText, phoneEditText;
    private Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_driver_profile);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        saveButton = findViewById(R.id.saveButton);

        db.collection("foreman").document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("city");
                        String email = documentSnapshot.getString("language");
                        String phone = documentSnapshot.getString("phone");

                        nameEditText.setText(name);
                        emailEditText.setText(email);
                        phoneEditText.setText(phone);
                    }
                });

        saveButton.setOnClickListener(view -> {
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String phone = phoneEditText.getText().toString();

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("city", name);
            userMap.put("language", email);
            userMap.put("phone", phone);

            db.collection("users").document(user.getUid()).set(userMap)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(update_driver_profile.this, "Profile updated", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(update_driver_profile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}

