package com.example.iknownothing.instantgram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private Button LoginButton;
    private TextView UserEmail,UserPassword;
    private TextView SignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SignUp = findViewById(R.id.register_account_link);
        LoginButton = findViewById(R.id.login_account);
        UserEmail = findViewById(R.id.login_email);
        UserPassword = findViewById(R.id.login_password);
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
