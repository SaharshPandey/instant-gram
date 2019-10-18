package com.saharsh.github.instantgram;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;                                         //Firebase Authentication Variable;
    private DatabaseReference UserRef,PostRef;                          //Firebase DatabaseReference Variable;
    private String CurrentUserId,DatabaseUserId;                        //UserId Variables;
    private CircleImageView profileImage;                               //User Profile Image;
    private TextView profile_username,profile_fullname,profile_bio;     //User Details Variables;
    private String user_posts,user_followers,user_following;            //User Numeric Information;
    private RecyclerView profile_posts_recyclerview;                    //Users Posts RecyclerView;
    private TextView followuser;
    private String CURRENT_STATE;
    private DatabaseReference FriendRequestReference,FollowerReference,FollowerReferenceCurrent;
    private EditText profile_username_edit,profile_fullname_edit,profile_bio_edit;
    private ImageButton user_profile_image_button;
    private FirebaseRecyclerAdapter<UserPosts,UserPostsHolder> firebaseRecyclerAdapter;
    //private ImageView going_back;
    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Getting UserKey from Previous Clicked Intent...
        final String UserKey = getIntent().getExtras().getString("UserKey");

        //Instantiating Firebase Objects and getting References....
        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        PostRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        //Differentiating User Posts from Posts..
        //.
        //.
        //.

        FriendRequestReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UserKey).child("FriendRequests");
        FollowerReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UserKey).child("Followers");
        FollowerReferenceCurrent = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUserId).child("Followers");

        //Initialising Widgets from Profile Activity....
        profileImage = findViewById(R.id.post_profile_image);
        profile_username = findViewById(R.id.profile_username);
        profile_fullname = findViewById(R.id.profile_fullname);
        profile_bio = findViewById(R.id.profile_bio);
        followuser = findViewById(R.id.follow_users);

/*
        //Initialising EditText of UserProfile;
        profile_username_edit=findViewById(R.id.profile_username_edit);
        profile_fullname_edit = findViewById(R.id.profile_fullname_edit);
        profile_bio_edit = findViewById(R.id.profile_bio_edit);
        user_profile_image_button = findViewById(R.id.user_profile_image_button);
        //going_back = findViewById(R.id.going_back);

        //Not Displaying until user edit his profile...
        profile_username_edit.setVisibility(View.GONE);
        profile_fullname_edit.setVisibility(View.GONE);
        profile_bio_edit.setVisibility(View.GONE);
        user_profile_image_button.setVisibility(View.GONE);
*/

        //Initialising RecyclerView of User Posts....
        profile_posts_recyclerview = findViewById(R.id.profile_posts_recyclerview);
        profile_posts_recyclerview.setHasFixedSize(true);
        //Total items that can be reserved ...
        profile_posts_recyclerview.setItemViewCacheSize(20);
        profile_posts_recyclerview.setDrawingCacheEnabled(true);
        profile_posts_recyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        profile_posts_recyclerview.setLayoutManager(gridLayoutManager);

        CURRENT_STATE = "not_friends";

      /*  //Going back to Previous Activity
        going_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
*/


           UserRef.child(UserKey).addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   if(dataSnapshot.exists())
                   {
                       if(dataSnapshot.hasChild("profileImage"))
                       {
                           String profileimage = dataSnapshot.child("profileImage").getValue().toString();
                           Picasso.get().load(profileimage).placeholder(R.drawable.profile).into(profileImage);
                       }

                       if(dataSnapshot.hasChild("fullname"))
                       {
                           String fullname = dataSnapshot.child("fullname").getValue().toString();
                           profile_fullname.setText(fullname);
                       }

                       if(dataSnapshot.hasChild("username"))
                       {
                           String username = dataSnapshot.child("username").getValue().toString();
                           profile_username.setText("@"+username);

                       }

                       if(dataSnapshot.hasChild("status"))
                       {
                           String bio = dataSnapshot.child("status").getValue().toString();
                           profile_bio.setText(bio);
                       }

                       if(UserKey.equals(CurrentUserId))
                       {
                           followuser.setText("Edit Profile");
                           followuser.setBackgroundColor(getResources().getColor(R.color.sendblue1));

                       }
                       else if(dataSnapshot.child("Followers").hasChild(CurrentUserId))
                       {
                           followuser.setText("Unfollow");
                           followuser.setBackgroundColor(getResources().getColor(R.color.sendblue1));
                       }
                       //if user already has sent friend request,then it will show unfollow option
                       else if(!dataSnapshot.child("Followers").hasChild(CurrentUserId))
                       {
                       if(dataSnapshot.child("FriendRequests").hasChild(CurrentUserId))
                       {
                        followuser.setText("Cancel");
                        followuser.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                       }
                       if(!dataSnapshot.child("FriendRequests").hasChild(CurrentUserId))
                       {
                           followuser.setText("Follow");
                           followuser.setBackgroundColor(getResources().getColor(R.color.deep_purple_500));
                       }
                       }

                   }
                   else{
                       Toast.makeText(ProfileActivity.this,"Please Check your Internet Connection",Toast.LENGTH_LONG).show();
                   }
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {
                   Toast.makeText(ProfileActivity.this,"Error : "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
               }
           });


      followuser.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(followuser.getText().equals("Edit Profile"))
              {
                  EditProfile();
              }
              else if(followuser.getText().equals("Follow"))
              {
                  SendFriendRequest();
              }
              else if(followuser.getText().equals("Cancel")){
                  CancelFriendRequest();
              }
              else if(followuser.getText().equals("Unfollow"))
              {
                  RemoveFriend(UserKey);
              }
          }
      });

      //Calling Firebase Recycler View...
      DispalyUserPostGrid(UserKey);
    }

    //THIS MEHTOD IS RESPONSIBLE TO ADD DATA INTO RECYCLER VIEW.....
    public void DispalyUserPostGrid(final String userKey)
    {
        //QUERY FOR FIREBASE RECYCLER ADAPTER....
        FirebaseRecyclerOptions<UserPosts> options = new FirebaseRecyclerOptions.Builder<UserPosts>()
                .setQuery(PostRef.orderByChild("uid").equalTo(userKey),UserPosts.class)
                .build();

        //Initialising firebase recycler adapter..

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserPosts,UserPostsHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull UserPostsHolder holder, int position, @NonNull UserPosts model) {

                // GETTING REFERENCE FOR EACH POST THAT HAS BEEN TAPPED...
                final String PostKey = getRef(position).getKey();

                //BINDING POST IMAGE INTO THE PROFILE...
                    holder.setPostImage(model.postimage);

                //SHOW IMAGE WHEN USER TAP INTO IMAGE....
                holder.mView.findViewById(R.id.user_post_image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent clickPostIntent =new Intent(ProfileActivity.this,OpenPostActivity.class);
                        clickPostIntent.putExtra("PostKey",PostKey);
                        startActivity(clickPostIntent);
                    }
                });

            }

            @NonNull
            @Override
            public UserPostsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_user_post_layout,parent,false);
                return new UserPostsHolder(view);
            }
        };

        profile_posts_recyclerview.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UserPostsHolder extends RecyclerView.ViewHolder
    {
        View mView;
        ImageView UserPostImage;

        public UserPostsHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setPostImage(String postImage)
        {
            UserPostImage = itemView.findViewById(R.id.user_post_image);

            Picasso.get().load(postImage).into(UserPostImage);
        }
    }


    public void SendFriendRequest()
    {

        Long ts=System.currentTimeMillis()/1000;
        String timestamp = ts.toString();

        HashMap userRequests = new HashMap();
        userRequests.put("uid",CurrentUserId);
        userRequests.put("timestamp",timestamp);



        //Sending the Friend Request....
        FriendRequestReference.child(CurrentUserId).updateChildren(userRequests).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(ProfileActivity.this,"Request Sent",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ProfileActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    public void  CancelFriendRequest()
    {
        //Cancelling the Friend Request....
            FriendRequestReference.child(CurrentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(ProfileActivity.this,"Request Cancelled",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(ProfileActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        public void RemoveFriend(String userkey)
        {FollowerReference.child(CurrentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(ProfileActivity.this,"You're no Long Friends",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(ProfileActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });

            FollowerReferenceCurrent.child(userkey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(ProfileActivity.this,"You're no Long Friends",Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        Toast.makeText(ProfileActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
        public void EditProfile()
        {/*
            //Not Displaying until user edit his profile...
            profile_username_edit.setVisibility(View.VISIBLE);
            profile_fullname_edit.setVisibility(View.VISIBLE);
            profile_bio_edit.setVisibility(View.VISIBLE);
            user_profile_image_button.setVisibility(View.VISIBLE);*/
        }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    //Retaining Data....
    @Override
    protected void onPause() {
        super.onPause();

        //So that the Data should be Retained;
        //BUNDLE bundles the DATA;
        mBundleRecyclerViewState =new Bundle();
        //PARCELABLE save the bundle Object...
        Parcelable listState = profile_posts_recyclerview.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable("recycler_state",listState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //So that the Data should be Retained;
        if(mBundleRecyclerViewState != null)
        {
            //If bundle is not null then Parcelable unbind the Data from BUNDLE object and Restores it...
            Parcelable liststate = mBundleRecyclerViewState.getParcelable("recycler_state");
            profile_posts_recyclerview.getLayoutManager().onRestoreInstanceState(liststate);
        }
    }

}
