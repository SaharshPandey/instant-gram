package com.example.iknownothing.instantgram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageButton;

public class find_friends_activity extends AppCompatActivity {

    private EditText SearchInputText;
    private RecyclerView SearchList;
    private ImageButton Back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends_activity);

        SearchList = findViewById(R.id.searchlist);
        SearchList.setHasFixedSize(true);
        SearchList.setLayoutManager(new LinearLayoutManager(this));

        SearchInputText = findViewById(R.id.search_friends);
        Back = findViewById(R.id.going_back1);
    }
}
