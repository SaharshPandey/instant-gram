package com.example.iknownothing.instantgram;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestsActivity extends AppCompatActivity {
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
        RequestRef = FirebaseDatabase.getInstance().getReference("Users").child(CurrentUserId).child("FriendRequests");

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
                    protected void onBindViewHolder(@NonNull RequestViewHolder holder, int position, @NonNull Accept_Decline model) {
                        holder.setusername(model.getUsername());
                        holder.setprofileImage(model.getProfileImage());
                        holder.setfullname(model.getFullname());
                        holder.setprofileImage(model.getProfileImage());
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
            Picasso.get().load(profileImage).into(comment_profile);
        }

        public void setfullname(String fullname)
        {
            TextView comment_user = mView.findViewById(R.id.request_fullname);
            comment_user.setText(fullname);
        }
    }
}
