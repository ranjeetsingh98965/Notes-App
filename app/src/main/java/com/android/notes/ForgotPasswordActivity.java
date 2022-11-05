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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextView mgoBack;
    private EditText mforgotPassword;
    private Button mpasswordRecoverBtn;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();

        mforgotPassword = findViewById(R.id.forgotPassword);
        mgoBack = findViewById(R.id.goBackTextView);
        mpasswordRecoverBtn = findViewById(R.id.passwordRecoverBtn);

        mgoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this , MainActivity.class));
            }
        });

        mpasswordRecoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mforgotPassword.getText().toString().trim();
                if(mail.isEmpty())
                {
                    Toast.makeText(getApplicationContext() , "Enter your mail first" , Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //we have to send password recovery email
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), "Mail sent, You can recover your password using mail", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPasswordActivity.this , MainActivity.class));
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Email is wrong or Account not exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}