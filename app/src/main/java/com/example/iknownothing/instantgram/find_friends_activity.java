package com.example.iknownothing.instantgram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageButton;

public class find_friends_activity extends AppCompatActivity {

    private EditText SearchInputText;
    private RecyclerView SearchList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends_activity);
    }
}
