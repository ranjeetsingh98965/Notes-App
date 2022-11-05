package com.android.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText mloginMail , mloginPass;
    private Button mgotosigninBtn, mloginBtn;
    private TextView mgotoforgotPass;
    private FirebaseAuth firebaseAuth;
    ProgressBar mprogressBarOfMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mgotosigninBtn = findViewById(R.id.wantToCreateBtn);
        mgotoforgotPass = findViewById(R.id.forgotPass);
        mloginMail = findViewById(R.id.loginEmail);
        mloginPass = findViewById(R.id.loginPass);
        mloginBtn = findViewById(R.id.loginBtn);
        mprogressBarOfMainActivity = findViewById(R.id.progressBarOfMainActivity);

        if(firebaseUser!=null)
        {
            finish();
            startActivity(new Intent(MainActivity.this , NotesActivity.class));
        }

        mgotosigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , SignupActivity.class));
            }
        });

        mgotoforgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , ForgotPasswordActivity.class));
            }
        });

        mloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mloginMail.getText().toString().trim();
                String password = mloginPass.getText().toString().trim();
                if(mail.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "All Fields are requierd", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // login the user
                    mprogressBarOfMainActivity.setVisibility(View.VISIBLE);

                    firebaseAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                checkMailVerification();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Account Doesn't Exist", Toast.LENGTH_SHORT).show();
                                mprogressBarOfMainActivity.setVisibility(View.INVISIBLE);

                            }
                        }
                    });
                }
            }
        });

    }

    // check mail verification
    private void checkMailVerification()
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser.isEmailVerified())
        {
            Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this , NotesActivity.class));
        }
        else
        {
            mprogressBarOfMainActivity.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Verify your email first", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}