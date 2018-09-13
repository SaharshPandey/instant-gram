package com.example.iknownothing.instantgram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestsActivity extends AppCompatActivity {

    private CircleImageView profileImage;
    private TextView accept;
    private TextView recieve;
    private TextView fullname;
    private TextView username;
    private RecyclerView RequestList;
    private DatabaseReference UserRef,PostRef,RequestRef;
    private FirebaseAuth mAuth;
    String PostKey,CurrentUserId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        mAuth  = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();

        UserRef = FirebaseDatabase.getInstance().getReference("Users");
        RequestRef = FirebaseDatabase.getInstance().getReference("Users").child("FriendRequests");

        RequestList = findViewById(R.id.friend_requests);
        RequestList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        RequestList.setLayoutManager(linearLayoutManager);

    }
}
