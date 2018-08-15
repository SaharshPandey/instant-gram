package com.example.iknownothing.instantgram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button LoginButton;
    private TextView UserEmail,UserPassword;
    private TextView SignUp;
    private ProgressDialog loadingBar;
    private ImageView google_signin;
    private static final int RC_SIGN_IN=1;
    private GoogleApiClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadingBar = new ProgressDialog(this);
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


        //Login Button Listener that redirects to SendUserToHome() method......
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToHome();
            }
        });

        google_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            SendUserToMainActivity();
        }
    }

    private void SendUserToRegisterAccount() {
        Intent registerIntent =new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerIntent);

    }
    private void SendUserToHome() {

        String userEmail = UserEmail.getText().toString().trim();
        String userPassword = UserPassword.getText().toString().trim();
        if(TextUtils.isEmpty(userEmail))
        {Toast.makeText(this,"Enter email..",Toast.LENGTH_LONG).show();}
        else if(TextUtils.isEmpty(userPassword))
        {Toast.makeText(this,"Enter password..",Toast.LENGTH_LONG).show();}
        else{
            loadingBar.setTitle("Logging In");
            loadingBar.setMessage("Please wait until we check the Data");
            loadingBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);


        mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {   SendUserToMainActivity();
                    Toast.makeText(LoginActivity.this,"You're now logged in to your Account",Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
                else {
                    String message = task.getException().getMessage();
                    Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show(); }
                    loadingBar.dismiss();
            }
        });}
    }

    private void SendUserToMainActivity() {
        Intent homeIntent = new Intent(LoginActivity.this,MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();

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