package com.example.quick_driver;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AfterHireRequestActivity extends AppCompatActivity {
    Button buttonBackToHome;
    TextView n;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_hire_request);
        n=findViewById(R.id.name);
        Intent intent = getIntent();
        String value = intent.getStringExtra("key"); // replace "key" with the same string key used in the putExtra
        n.setText(value);
        buttonBackToHome=findViewById(R.id.backToHomeButtonInHiringConfirmationMessage);
        buttonBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AfterHireRequestActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AfterHireRequestActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}