package com.example.iknownothing.instantgram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {

    private CircleImageView CommentProfile;
    private EditText WriteComment;
    private RecyclerView CommentList;
    private ImageView CommentButton;
    private DatabaseReference UserRef,PostRef;
    private FirebaseAuth mAuth;
    String PostKey,CurrentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();

        PostKey = getIntent().getExtras().getString("PostKey");

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey).child("Comments");

        //Initialising objects;
        CommentProfile = findViewById(R.id.comment_photo);
        WriteComment = findViewById(R.id.write_comment);
        CommentButton = findViewById(R.id.postcomment);

        CommentList = findViewById(R.id.show_comments);
        CommentList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        CommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRef.child(CurrentUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            String userName = dataSnapshot.child("username").getValue().toString();
                            String profileImage = dataSnapshot.child("profileImage").getValue().toString();
                            ValidateComment(userName,profileImage);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    private void ValidateComment(String userName,String profileImage) {
        String commentText = WriteComment.getText().toString();
        if(TextUtils.isEmpty(commentText))
        {
            Toast.makeText(CommentActivity.this,"Enter Valid Comment",Toast.LENGTH_SHORT);
        }
        else
        {
            
        }
    }
}
