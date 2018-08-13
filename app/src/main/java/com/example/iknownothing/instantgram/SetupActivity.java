package com.example.iknownothing.instantgram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {
    private Button setup;
    private EditText userName,fullName,countryName;
    private CircleImageView circleImage;
    FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private String CurrentUserId;
    private ProgressDialog loadingBar;
    final static int GalleryPic=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUserId);
        userName = findViewById(R.id.username);
        fullName = findViewById(R.id.fullname);
        countryName = findViewById(R.id.country);
        circleImage = findViewById(R.id.circleimage);
        setup = findViewById(R.id.setup);
        loadingBar =new ProgressDialog(this);

        setup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                String username = userName.getText().toString().trim();
                String fullname = fullName.getText().toString().trim();
                String countryname = countryName.getText().toString().trim();

                if(TextUtils.isEmpty(username))
                {
                    Toast.makeText(SetupActivity.this,"Provide Username for it",Toast.LENGTH_SHORT);
                }
                else if(TextUtils.isEmpty(fullname))
                {
                    Toast.makeText(SetupActivity.this,"Provide FullName for it",Toast.LENGTH_SHORT);
                }
                else if(TextUtils.isEmpty(countryname))
                {
                    Toast.makeText(SetupActivity.this,"Provide Country you're Living",Toast.LENGTH_SHORT);
                }
                else
                {
                    loadingBar.setTitle("Saving Information");
                    loadingBar.setMessage("Please wait until we save your Information to the Database");
                    loadingBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(true);

                    HashMap userMap =new HashMap();
                    userMap.put("username",username);
                    userMap.put("fullname",fullname);
                    userMap.put("country",countryname);
                    userMap.put("status","Hey there, I am using InstantGram");
                    userMap.put("gender","none");
                    userMap.put("dob","none");
                    userMap.put("realtionshipstatus","none");
                    UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful())
                            {   SendUserToMainActivity();
                                loadingBar.dismiss();
                                Toast.makeText(SetupActivity.this,"Your Account is Created Successfully",Toast.LENGTH_LONG);

                            }
                            else {
                                String message = task.getException().toString();
                                loadingBar.dismiss();
                                Toast.makeText(SetupActivity.this,message,Toast.LENGTH_LONG);
                            }
                        }
                    });
                }
            }
        });

        //Event Listener so we can redirect to our Gallery when we click on profile image...
        circleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GalleryPic);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GalleryPic && requestCode == RESULT_OK && data != null)
        {
            Uri ImageUri = data.getData();
        }

    }

    private void SendUserToMainActivity() {
        Intent homeIntent = new Intent(SetupActivity.this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();

    }
    }
