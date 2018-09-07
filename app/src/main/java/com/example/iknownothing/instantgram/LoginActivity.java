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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button LoginButton;
    private TextView UserEmail,UserPassword;
    private TextView SignUp,ForgotPassword;
    private ProgressDialog loadingBar;
    private ImageView google_signin;
    private static final int RC_SIGN_IN=1;
    private GoogleApiClient mGoogleSignInClient;
    private static final String TAG="LoginActivity";

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
        google_signin =findViewById(R.id.google_signin_button);

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
                signIn();
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(LoginActivity.this,"Connection to Google Login Failed....",Toast.LENGTH_LONG).show();

            }
        })
        .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        {
        }

        //Click Listener to The Forgot Password View..
        ForgotPassword = findViewById(R.id.forgot_password);
        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToForgotPasswordActivity();
            }
        });


    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            loadingBar.setTitle("Google Sign In");
            loadingBar.setMessage("Please wait until we login through your Google Account");
            loadingBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess())
            {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                Toast.makeText(this,"Signing you Google Account..",Toast.LENGTH_LONG).show();

            }
            else { Toast.makeText(this,"Signing Failed",Toast.LENGTH_LONG).show();
            loadingBar.dismiss();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            SendUserToMainActivity();
                            loadingBar.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            String message = task.getException().toString();
                            SendUserToLoginActivity();
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this,"Google Signin Failed.. :" +message,Toast.LENGTH_LONG).show();

                        }

                        // ...
                    }
                });
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
    private void SendUserToLoginActivity() {
        Intent homeIntent = new Intent(LoginActivity.this,LoginActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();

    }
    //Method to redirect User to Forgot Password Activity....
    private void SendUserToForgotPasswordActivity() {

        Intent forgotIntent =new Intent(LoginActivity.this, ForgotPassword.class);
        forgotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(forgotIntent);
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