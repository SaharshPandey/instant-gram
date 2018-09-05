package com.example.iknownothing.instantgram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {

    private CircleImageView CommentProfile;
    private EditText WriteComment;
    private RecyclerView ShowComments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        //Initialising objects;
        CommentProfile = findViewById(R.id.comment_photo);
        WriteComment = findViewById(R.id.write_comment);
        ShowComments = findViewById(R.id.show_comments);

    }
}
