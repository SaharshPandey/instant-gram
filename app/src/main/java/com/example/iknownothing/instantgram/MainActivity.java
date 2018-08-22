package com.example.iknownothing.instantgram;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


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
    private ImageButton AddNewPostButton,PostPicture;
    FirebaseRecyclerAdapter<Posts,PostViewHolder> firebaseRecyclerAdapter;
    CardView cardView;
    private Uri ImageUri;
    private String CurrentDAte,CurrentTime,PostRandomName;
    private String DownloadUrl;
    private EditText text_post;
    private static int GalleryPic =1;
    private ImageView post_image_main;
    private ImageButton add_new_upload_button,popup_button;
    private String ts;
    private LinearLayout popup_button_layout;
    private DatabaseReference ClickPostRef;
    private String description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        //instantiating Objects
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");


        //Initialising image buttons.......
        post_image_main = findViewById(R.id.post_image_main);
        add_new_upload_button =findViewById(R.id.add_new_upload_button);
        add_new_upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToPostActivity();
            }
        });

        AddNewPostButton = findViewById(R.id.add_new_post_button);
        AddNewPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text_post.getText().toString().trim() != null && !text_post.getText().toString().trim().equals(""))
                {
                    SavingPostInformationToDatabase();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Nothing in mind",Toast.LENGTH_SHORT).show();
                }
        }



        });



        drawerLayout = findViewById(R.id.drawable_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView = findViewById(R.id.navigation_view);
        cardView = findViewById(R.id.posting);
        text_post = findViewById(R.id.text_post);




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



    public void showPopup(View v,final String PostKey){
        ClickPostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey);

        PopupMenu popupMenu = new PopupMenu(this,v);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.nav_Edit:

                        ClickPostRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                description = dataSnapshot.child("description").getValue().toString();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        //EDITING THE POST...
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Edit Post:");
                        final EditText inputfield = new EditText(MainActivity.this);
                       
                        inputfield.setText(description);
                        builder.setView(inputfield);

                        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ClickPostRef.child("description").setValue(inputfield.getText().toString());
                                Toast.makeText(MainActivity.this,"Updated Successfully",Toast.LENGTH_SHORT);
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        //Creating Dialog Box...
                        Dialog dialog = builder.create();
                        dialog.show();
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
                        break;

                    case R.id.nav_Delete:

                        //DELETING THE POST........
                        ClickPostRef.removeValue();
                        break;

                }
                return true;
            }
        });

        popupMenu.show();
    }



    private void DisplayAllUsersPost() {

        FirebaseRecyclerOptions<Posts> options =
                new FirebaseRecyclerOptions.Builder<Posts>()
                        .setQuery(PostRef.orderByChild("timestamp"), Posts.class)
                        .build();

        firebaseRecyclerAdapter
                =new FirebaseRecyclerAdapter<Posts, PostViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Posts model) {
            // Bind the Chat object to the ChatHolder
                // ...
                final String PostKey = getRef(position).getKey();
                holder.setFullname(model.fullname);
                holder.setProfileImage(getApplicationContext(),model.profileImage);
                holder.setDate(model.date);
                holder.setTime(model.time);
                holder.setDescription(model.description);
                holder.setPostimage(getApplicationContext(), model.postimage);
                //Adding popup button functionality...

                popup_button_layout=holder.mView.findViewById(R.id.popup_button_layout);
                popup_button=holder.mView.findViewById(R.id.popup_button);
                popup_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopup(v,PostKey);
                    }
                });

                popup_button_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopup(v,PostKey);
                    }
                });
               holder.mView.findViewById(R.id.post_image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent clickPostIntent =new Intent(MainActivity.this,OpenPostActivity.class);
                        clickPostIntent.putExtra("PostKey",PostKey);
                        startActivity(clickPostIntent);
                    }
                });


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
            postdescription.setPadding(3,3,3,3);
            postdescription.setTextSize(14);
            postdescription.setText(description);
            postdescription.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }

        public void setPostimage(Context ctx,String postimage) {
            ImageView post_image=mView.findViewById(R.id.post_image);
            if(postimage.equals("none"))
            {
                TextView postdes= mView.findViewById(R.id.post_description);
                if(postdes.getText().toString().length() <= 10) {
                    postdes.setTextSize(35);
                    postdes.setPadding(5, 30, 5, 0);
                    postdes.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }
                else if( postdes.getText().toString().length() > 10 && postdes.getText().toString().length() <= 30 ) {
                    postdes.setTextSize(20);
                    postdes.setPadding(5, 30, 5, 0);
                }
                else if(postdes.getText().toString().length() > 30) {
                    postdes.setTextSize(15);
                    postdes.setPadding(5, 30, 5, 0);
                }
                Picasso.get().load(postimage).into(post_image);

            }
            else{
            Picasso.get().load(postimage).placeholder(R.drawable.loading).into(post_image);
        }
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


    private void SavingPostInformationToDatabase()
    {
        loadingBar.setTitle("Updating Post");
        loadingBar.setMessage("Please wait while we update your post");
        loadingBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(true);

        UserRef.child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {


                    if(ImageUri == null)//checking whether this image is not selected..
                    {
                        Calendar calForDAte =Calendar.getInstance();

                        Long tsLong = System.currentTimeMillis()/1000;
                        ts = tsLong.toString();

                        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                        CurrentDAte = currentDate.format(calForDAte.getTime());

                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                        CurrentTime = currentTime.format(calForDAte.getTime());

                        PostRandomName = CurrentDAte+CurrentTime;
                        DownloadUrl="none";
                    }
                    String userFullname = dataSnapshot.child("fullname").getValue().toString();
                    String profileImage = dataSnapshot.child("profileImage").getValue().toString();

                    String description = text_post.getText().toString();
                    text_post.setText(description);

                    //Making data for node to store in firebase.....
                    HashMap postMap = new HashMap();
                    postMap.put("uid", CurrentUserId);
                    postMap.put("date", CurrentDAte);
                    postMap.put("time", CurrentTime);
                    postMap.put("description", description);
                    postMap.put("postimage", DownloadUrl);
                    postMap.put("profileImage", profileImage);
                    postMap.put("fullname", userFullname);
                    postMap.put("timestamp",ts);

                    //making new node in database....
                    PostRef.child(CurrentUserId +PostRandomName).updateChildren(postMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {

                                    if(task.isSuccessful())
                                    {
                                        //scrolling to top of recycler view
                                        postList.smoothScrollToPosition(postList.getAdapter().getItemCount()-1);
                                        loadingBar.dismiss();

                                        //Toast.makeText(PostActivity.this,"Post is Updated Successfully",Toast.LENGTH_SHORT).show();

                                    }

                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(MainActivity.this,"Error Occurred while Updating the Post,Try Again...",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loadingBar.dismiss();
            }
        });

    }




    //animation that has been added into bindViewHolder method............
    /*private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }*/
}
