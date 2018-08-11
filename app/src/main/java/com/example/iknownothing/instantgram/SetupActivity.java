package com.example.iknownothing.instantgram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {
    private Button setup;
    private EditText userName,fullName,countryName;
    private CircleImageView circleImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        userName = findViewById(R.id.username);
        fullName = findViewById(R.id.fullname);
        countryName = findViewById(R.id.country);
        circleImage = findViewById(R.id.circleimage);
        setup = findViewById(R.id.setup);
    }
}
