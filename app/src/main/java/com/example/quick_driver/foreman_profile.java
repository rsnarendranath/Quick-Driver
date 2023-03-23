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

public class foreman_profile extends AppCompatActivity {
Button back;
FirebaseAuth fAuth;
FirebaseFirestore fstore;
    private TableLayout tableLayoutResults;
String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreman_profile);
        back=findViewById(R.id.button9);
        tableLayoutResults = findViewById(R.id.tableLayoutResults);
        fAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        userid=fAuth.getCurrentUser().getUid();
        DocumentReference documentReference=fstore.collection("foreman").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                addTableRow("","");
                addTableRow("Name",": "+ value.getString("name"));
                addTableRow("","");
                addTableRow("Phone",": "+ value.getString("phone"));
                addTableRow("","");
                addTableRow("Email",": "+ value.getString("email"));
                addTableRow("","");
                addTableRow("Language",": "+ value.getString("language"));
                addTableRow("","");
                addTableRow("City",": "+ value.getString("city"));

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(foreman_profile.this,forman_homepage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(foreman_profile.this, forman_homepage.class);
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