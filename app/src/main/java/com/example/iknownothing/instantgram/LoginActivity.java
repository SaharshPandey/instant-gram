package com.example.iknownothing.instantgram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button LoginButton;
    private TextView UserEmail,UserPassword;
    private TextView SignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        SignUp = findViewById(R.id.register_account_link);
        LoginButton = findViewById(R.id.login_account);
        UserEmail = findViewById(R.id.login_email);
        UserPassword = findViewById(R.id.login_password);


        //Signup Button Listener that redirects to SendUserToRegisterAccount() method......
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToRegisterAccount();
            }
        });

    }

    private void SendUserToRegisterAccount() {
        Intent registerIntent =new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerIntent);

    }
}
/*

        //Login Button Listener that redirects to SendUserToHome() method......
       SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToHome(UserEmail.getText().toString().trim(),UserPassword.getText().toString().trim());
            }
        });
    }

   private void SendUserToHome(String email,String password) {

        mAuth.signInWithEmailAndPassword(email, password);
    }

 */