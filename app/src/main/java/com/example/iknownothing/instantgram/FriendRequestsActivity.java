package com.example.iknownothing.instantgram;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestsActivity extends AppCompatActivity {
    private RecyclerView RequestList;
    private DatabaseReference UserRef,PostRef,RequestRef,FollowersRef;
    private FirebaseAuth mAuth;
    String PostKey,CurrentUserId;
    private TextView name;
    private TextView accept,decline;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        name = findViewById(R.id.name);
        name.setText("Friend Requests");


        mAuth  = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();

        UserRef = FirebaseDatabase.getInstance().getReference("Users");
        RequestRef = FirebaseDatabase.getInstance().getReference("Users").child(CurrentUserId).child("FriendRequests");
        FollowersRef=UserRef.child(CurrentUserId).child("Followers");

        RequestList = findViewById(R.id.friend_requests);
        RequestList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        RequestList.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Accept_Decline> options =
                new FirebaseRecyclerOptions.Builder<Accept_Decline>()
                        .setQuery(RequestRef, Accept_Decline.class)
                        .build();

        FirebaseRecyclerAdapter<Accept_Decline,RequestViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Accept_Decline,RequestViewHolder>(options){


                    @NonNull
                    @Override
                    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        // Create a new instance of the ViewHolder, in this case we are using a custom
                        // layout called R.layout.message for each item

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.accept_decline, parent, false);
                        return new RequestViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final RequestViewHolder holder, final int position, @NonNull Accept_Decline model) {
                        Log.d("result1",getRef(position).getKey());

                        accept = holder.mView.findViewById(R.id.accept);
                        decline = holder.mView.findViewById(R.id.decline);

                        //Accepting Friend Request....
                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                HashMap follower_users= new HashMap();
                                follower_users.put("uid",getRef(position).getKey());


                                FollowersRef.child(getRef(position).getKey()).updateChildren(follower_users).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(FriendRequestsActivity.this,"You're now Friends",Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(FriendRequestsActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                                HashMap follower_users1= new HashMap();
                                follower_users.put("uid",CurrentUserId);

                                UserRef.child(getRef(position).getKey()).child("Followers").child(CurrentUserId).updateChildren(follower_users1).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(FriendRequestsActivity.this,"You're now Friends with each other",Toast.LENGTH_SHORT).show();

                                        }
                                        else
                                        {
                                            Toast.makeText(FriendRequestsActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                                CancelFriendRequest(CurrentUserId);
                                CancelFriendRequest1(getRef(position).getKey());
                            }
                        });



                        //Cancelling Incoming FriendRequests......
                        decline.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CancelFriendRequest(getRef(position).getKey());
                            }
                        });
                        UserRef.child(model.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists())
                                {
                                    holder.setfullname(dataSnapshot.child("fullname").getValue().toString());
                                    holder.setusername(dataSnapshot.child("username").getValue().toString());
                                    holder.setprofileImage(dataSnapshot.child("profileImage").getValue().toString());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }
                };

        RequestList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    //Class Holder for RecyclerView...............
    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public RequestViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setusername(String username){
            TextView comment_user = mView.findViewById(R.id.request_username);
            comment_user.setText(username);
        }


        public void setprofileImage(String profileImage){
            CircleImageView comment_profile = mView.findViewById(R.id.request_profileImage);
            Picasso.get().load(profileImage).placeholder(R.drawable.profile).into(comment_profile);
        }

        public void setfullname(String fullname)
        {
            TextView comment_user = mView.findViewById(R.id.request_fullname);
            comment_user.setText(fullname);
        }
    }

    public void CancelFriendRequest(String id)
    {
        //Cancelling the Friend Request....
        RequestRef.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(FriendRequestsActivity.this,"Request Cancelled",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(FriendRequestsActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void CancelFriendRequest1(String id)
    {
        //Cancelling the Friend Request....
        UserRef.child(id).child("Followers").child(CurrentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(FriendRequestsActivity.this,"Request Cancelled",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(FriendRequestsActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
