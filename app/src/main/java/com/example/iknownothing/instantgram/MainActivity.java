package com.example.iknownothing.instantgram;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    //Private objects of FirebaseAuth, NavigationView,DrawerLayout,ToolBar,processDialog
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef,PostRef;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView postList;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    ProgressDialog loadingBar;
    private CircleImageView NavProfileImage;
    private TextView ProfileUserName;
    String CurrentUserId;
    private ImageButton AddNewPostButton;
    FirebaseRecyclerAdapter<Posts,PostViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiating Objects
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        AddNewPostButton = findViewById(R.id.add_new_post_button);
        AddNewPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToPostActivity();
            }
        });

        drawerLayout = findViewById(R.id.drawable_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView = findViewById(R.id.navigation_view);


        postList = findViewById(R.id.all_users_post_list);
        postList.setHasFixedSize(true);
        //this will arrange posts by time
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);



        //Inflating Navigation Header in Navigation Menu....
        View view =navigationView.inflateHeaderView(R.layout.navigation_header);
        NavProfileImage = view.findViewById(R.id.nav_profile_image);
        ProfileUserName = view.findViewById(R.id.nav_user_full_name);

        UserRef.child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("fullname"))
                    {
                        String fullname = dataSnapshot.child("fullname").getValue().toString();
                        ProfileUserName.setText(fullname);
                    }

                    if(dataSnapshot.hasChild("profileImage"))
                    {
                        String profileImage = dataSnapshot.child("profileImage").getValue().toString();
                        Picasso.get()
                                .load(profileImage)
                                .placeholder(R.drawable.profile)
                                .into(NavProfileImage);
                    }
                    else
                        {
                        Toast.makeText(MainActivity.this,"There in no information Stored",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserItemSelector(item);
                return false;
            }
            //Checking when the menu item has been clicked..
            private void UserItemSelector(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_post:
                        SendUserToPostActivity();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
                        Toast.makeText(MainActivity.this,"Profile",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_profile:
                        Toast.makeText(MainActivity.this,"Profile",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_home:
                        Toast.makeText(MainActivity.this,"Home",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_friends:
                        Toast.makeText(MainActivity.this,"Friend List",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_find_friends:
                        Toast.makeText(MainActivity.this,"Find Firends",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_messages:
                        Toast.makeText(MainActivity.this,"Messages",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_settings:
                        Toast.makeText(MainActivity.this,"Settings",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_Logout:
                        loadingBar.setTitle("Logging Out");
                        loadingBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        loadingBar.show();
                        loadingBar.setCanceledOnTouchOutside(true);
                        Toast.makeText(MainActivity.this,"Logging Out",Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        loadingBar.dismiss();
                        SendUserToLoginActivity();
                        break;
                }
            }
        });

        DisplayAllUsersPost();
    }

    private void DisplayAllUsersPost() {

        FirebaseRecyclerOptions<Posts> options =
                new FirebaseRecyclerOptions.Builder<Posts>()
                        .setQuery(PostRef, Posts.class)
                        .build();

        firebaseRecyclerAdapter
                =new FirebaseRecyclerAdapter<Posts, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Posts model) {
            // Bind the Chat object to the ChatHolder
                // ...
                holder.setFullname(model.fullname);
                holder.setProfileImage(getApplicationContext(),model.profileImage);
                holder.setDate(model.date);
                holder.setTime(model.time);
                holder.setDescription(model.description);
                holder.setPostimage(getApplicationContext(),model.postimage);
            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_post_layout, parent, false);
                return new PostViewHolder(view);
            }
        };

        postList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public PostViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
        }
        public void setFullname(String fullname) {
            TextView name= mView.findViewById(R.id.post_username);
            name.setText(fullname);

        }
        public void setProfileImage(Context ctx, String profileImage) {
            CircleImageView image = mView.findViewById(R.id.post_profile_image);
            Picasso.get().load(profileImage).into(image);
        }
        public void setTime(String time)
        {
            TextView posttime= mView.findViewById(R.id.post_time);
            posttime.setText("   "+time);
        }
        public void setDate(String date)
        {
            TextView postdate= mView.findViewById(R.id.post_date);
            postdate.setText("   "+date);
        }

        public void setDescription(String description)
        {
            TextView postdescription= mView.findViewById(R.id.post_description);
            postdescription.setText(description);
        }

        public void setPostimage(Context ctx,String postimage) {
            ImageView post_image=mView.findViewById(R.id.post_image);
            Picasso.get().load(postimage).into(post_image);
        }
    }

    private void SendUserToPostActivity() {
        Intent postIntent = new Intent(MainActivity.this,PostActivity.class);
        startActivity(postIntent);
    }

    //onStart method is used for checking that user is LoggedIn or not?......
    @Override
    protected void onStart()
    {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
        FirebaseUser currentuser =mAuth.getCurrentUser();
        if(currentuser ==null)
        {
            SendUserToLoginActivity();
        }
        else{
            ChechUserExistence();


        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    private void ChechUserExistence() {
        final String Current_User_Id=mAuth.getCurrentUser().getUid();
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(Current_User_Id))
                {
                    SendUserToSetupActivity();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToSetupActivity() {
        Intent setupIntent =new Intent(MainActivity.this,SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }

    //Method to redirect User to Login Activity....
    private void SendUserToLoginActivity() {

        Intent loginIntent =new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    //Menu Item Selection ....
    @Override
public boolean onOptionsItemSelected(MenuItem item)
{
    if(actionBarDrawerToggle.onOptionsItemSelected(item))
    {return true;}
return super.onOptionsItemSelected(item);
}
}
