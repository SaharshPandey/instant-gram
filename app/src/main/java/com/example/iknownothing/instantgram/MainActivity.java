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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    //Private objects of FirebaseAuth, NavigationView,DrawerLayout,ToolBar,processDialog
    private FirebaseAuth mAuth;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView postList;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiating Objects
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
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
