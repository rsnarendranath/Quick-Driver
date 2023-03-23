package com.example.quick_driver;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.core.AsyncEventListener;

public class user_profile extends AppCompatActivity {
    Button back;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userid;
    private TableLayout tableLayoutResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        back=findViewById(R.id.button9);
        fAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        userid=fAuth.getCurrentUser().getUid();
        tableLayoutResults = findViewById(R.id.tableLayoutResults);
        DocumentReference documentReference=fstore.collection("users").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                addTableRow("","");
                addTableRow("Name",": "+ value.getString("name"));
                addTableRow("","");
                addTableRow("Phone",": "+ value.getString("phone"));
                addTableRow("","");
                addTableRow("Email",": "+ value.getString("email"));


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user_profile.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(user_profile.this, MainActivity.class);
        startActivity(intent);
        finish();
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