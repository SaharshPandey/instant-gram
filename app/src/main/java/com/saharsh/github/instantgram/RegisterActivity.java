package com.saharsh.github.instantgram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText registerEmail,registerPassword,registerConfirmPassword;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        registerEmail = findViewById(R.id.register_email);
        registerPassword = findViewById(R.id.register_password);
        registerConfirmPassword = findViewById(R.id.register_confirm_password);
        registerButton = findViewById(R.id.register_create_account);
        loadingBar =new ProgressDialog(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
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


    private void CreateNewAccount() {

        String email=registerEmail.getText().toString().trim();
        String password=registerPassword.getText().toString().trim();
        String confirmPassword=registerConfirmPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {Toast.makeText(this,"Please write your Email",Toast.LENGTH_SHORT).show(); }
        else if(TextUtils.isEmpty(password))
        {Toast.makeText(this,"Please write your Password",Toast.LENGTH_SHORT).show(); }
        else if(TextUtils.isEmpty((confirmPassword)))
        {Toast.makeText(this,"Please Confirm your Password",Toast.LENGTH_SHORT).show(); }
        else if (!password.equals(confirmPassword))
        { Toast.makeText(this,"Your Password Doesn't Match!!!!",Toast.LENGTH_SHORT).show(); }

        else {

            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait, while we are creating your new Account...");
            loadingBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        SendUserToSetUpActivity();
                        Toast.makeText(RegisterActivity.this, "Registeration Successfully", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        loadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    private void SendUserToSetUpActivity() {
        Intent setupIntent = new Intent(RegisterActivity.this,SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }
    private void SendUserToMainActivity() {
        Intent homeIntent = new Intent(RegisterActivity.this,MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();

    }
}
