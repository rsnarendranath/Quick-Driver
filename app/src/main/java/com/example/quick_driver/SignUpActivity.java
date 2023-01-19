package com.example.quick_driver;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText n,e,m,p,cp;
    Button b;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog ;
    FirebaseAuth mAuth ;
    FirebaseUser mUser ;
    FirebaseFirestore fstore;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        n =(EditText) findViewById(R.id.edtSignUpFullName);
        e =(EditText) findViewById(R.id.edtSignUpEmail);
        m =(EditText) findViewById(R.id.edtSignUpMobile);
        p =(EditText) findViewById(R.id.edtSignUpPassword);
        cp =(EditText) findViewById(R.id.edtSignUpConfirmPassword);
        b =(Button) findViewById(R.id.btnSignUp);
        progressDialog = new ProgressDialog( this ) ;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerforAuth();

            }

            private void PerforAuth() {
                String name = n.getText().toString();
                String email = e.getText().toString();
                String mobno = m.getText().toString();
                String password = p.getText().toString();
                String confrimPassword = cp.getText().toString();
                fstore = FirebaseFirestore.getInstance();

                if (!email.matches(emailPattern)) {
                    e.setError("Enter Connext Email");
                } else if (password.isEmpty() || password.length() < 6) {
                    p.setError("Enter Proper Password ");
                } else if (!password.equals(confrimPassword)) {
                    cp.setError("Password Not match Both field");
                } else {
                    progressDialog.setMessage(" Please Wait While Registration ... ");
                    progressDialog.setTitle(" Registration ");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                sendUserToNextActivity();
                                Toast.makeText(SignUpActivity.this, "Resgistration Successful", Toast.LENGTH_SHORT).show();
                                uid = mAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fstore.collection("users").document(uid);
                                Map<String,Object> user = new HashMap<>();
                                user.put("name",name);
                                user.put("email",email);
                                user.put("phone",mobno);
                                user.put("role", "user");
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "onsuccess: user profile is created for "+uid);
                                    }
                                });
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(SignUpActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
            });
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}