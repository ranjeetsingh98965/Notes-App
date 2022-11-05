package com.android.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class SignupActivity extends AppCompatActivity {

    private EditText msignupEmail , msignupPass;
    private TextView mwantToLogin;
    private Button msignupBtn;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();

        mwantToLogin = findViewById(R.id.wantToLogin);
        msignupEmail = findViewById(R.id.signupEmail);
        msignupPass = findViewById(R.id.signupPass);
        msignupBtn = findViewById(R.id.signupBtn);


        mwantToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this , MainActivity.class));
            }
        });

        msignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = msignupEmail.getText().toString().trim();
                String password = msignupPass.getText().toString().trim();
                if(mail.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "All Fields are requierd", Toast.LENGTH_SHORT).show();
                }
                else if(password.length() < 7)
                {
                    Toast.makeText(getApplicationContext(), "Password Should Greater than 7 digits", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // registered the user to firebase
                    firebaseAuth.createUserWithEmailAndPassword(mail , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Failed To Register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }
    
    // send email verification
    private void sendEmailVerification()
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Verification Email is sent, verify and LogIn again", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Failed to send Verification Email", Toast.LENGTH_SHORT).show();
        }
    }
}