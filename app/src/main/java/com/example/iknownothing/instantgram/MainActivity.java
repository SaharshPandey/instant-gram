package com.example.iknownothing.instantgram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    private DatabaseReference UserRef;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView postList;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    ProgressDialog loadingBar;
    private CircleImageView NavProfileImage;
    private TextView ProfileUserName;
    String CurrentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiating Objects
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");


        drawerLayout = findViewById(R.id.drawable_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView = findViewById(R.id.navigation_view);

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

    }
//onStart method is used for checking that user is LoggedIn or not?......
    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentuser =mAuth.getCurrentUser();
        if(currentuser ==null)
        {
            SendUserToLoginActivity();
        }
        else{
            ChechUserExistence();


        }
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
