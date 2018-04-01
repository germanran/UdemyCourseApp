package com.example.rangerman.udemycourseapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private ProgressDialog registerProgressDialog;
    private TextView loginTextView;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        registerNewUser();
        goToLoginPage();
    }

    private void initViews() {

        usernameEditText = findViewById(R.id.usernameEditTextRegister);
        emailEditText = findViewById(R.id.emailEditTextLogin);
        passwordEditText = findViewById(R.id.passwordEditTextLogin);
        registerButton = findViewById(R.id.loginButtonLogin);
        registerProgressDialog = new ProgressDialog(this);
        loginTextView = findViewById(R.id.loginTextViewRegister);

        mAuth = FirebaseAuth.getInstance();
    }

    private void goToLoginPage(){

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void registerNewUser(){

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usernameStr = usernameEditText.getText().toString().trim();
                final String emailStr = emailEditText.getText().toString().trim();
                final String passwordStr = passwordEditText.getText().toString().trim();

                if (emailStr.equals("") || !Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
                    emailEditText.setError("please enter valid email address");
                    emailEditText.requestFocus();
                } else if (passwordStr.length() < 6) {
                    passwordEditText.setError("password minimum contain 6 character");
                            passwordEditText.requestFocus();
                } else if (passwordStr.equals("")) {
                    passwordEditText.setError("please enter password");
                            passwordEditText.requestFocus();
                } else {

                    registerProgressDialog.setMessage("Please wait...");
                    registerProgressDialog.show();
                    createAccount(emailStr, passwordStr);

                }

            }

        });
    }
            private void createAccount(String email, String password){

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                registerProgressDialog.dismiss();

                                if (task.isSuccessful()) {
//                                    FirebaseUser user = mAuth.getCurrentUser();
                                    finish();
                                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Error", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

}

