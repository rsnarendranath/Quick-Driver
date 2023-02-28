
package com.example.quick_driver;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;

public class foreman_login extends AppCompatActivity {
    TextView txtSignUp;
    EditText e,p;
    Button b;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog ;
    FirebaseAuth mAuth ;
    FirebaseUser mUser ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreman_login);
        getSupportActionBar();

        txtSignUp =(TextView) findViewById(R.id.txtSignUp);
        e =(EditText) findViewById(R.id.edtSignInEmail);
        p =(EditText) findViewById(R.id.edtSignInPassword);
        b =(Button) findViewById(R.id.btnSignIn);
        progressDialog = new ProgressDialog( this ) ;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                perforLogin();
            }
        });


        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(foreman_login.this, foreman_signup.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void perforLogin() {
        String email = e.getText().toString();
        String password = p.getText().toString();

        if(!email.matches(emailPattern)) {
            e.setError("Enter Connext Email");
        }else if (password.isEmpty() || password.length() < 6) {
            p.setError("Enter Proper Password ");
        }else {
            progressDialog.setMessage(" Please Wait While Login ... ");
            progressDialog.setTitle(" Logging in ");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(foreman_login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(foreman_login.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
    private void sendUserToNextActivity() {
        Intent intent = new Intent(foreman_login.this,forman_homepage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
