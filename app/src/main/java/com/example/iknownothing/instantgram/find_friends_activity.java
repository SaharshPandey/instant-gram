package com.example.iknownothing.instantgram;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class find_friends_activity extends AppCompatActivity {

    private EditText SearchInputText;
    private RecyclerView SearchList;
    private ImageButton Back;
    private DatabaseReference AllUserRef;
    private String CurrentUserId;
    private FirebaseAuth mAuth;
    private ImageButton SearchFriendsButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends_activity);


        //THIS METHOD DONT OPEN THE KEYBOARD IN STARTUP....

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();
        AllUserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        SearchList = findViewById(R.id.searchlist);
        SearchList.setHasFixedSize(true);
        SearchList.setLayoutManager(new LinearLayoutManager(this));

        SearchInputText = findViewById(R.id.search_friends);
        Back = findViewById(R.id.going_back1);
        SearchFriendsButton = findViewById(R.id.search_friends_button);

        //Showing all the users without searching them first...
        SearchPeople("");

        SearchFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchBoxInput =SearchInputText.getText().toString();

                //HIDING THE KEYBOARD WHEN BUTTON IS CLICKED....
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                SearchPeople(searchBoxInput);

            }
        });

      /*  SearchInputText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String searchBoxInput =SearchInputText.getText().toString();
                SearchPeople(searchBoxInput);
                firebaseRecyclerAdapter.startListening();
                return true;
            }
        });*/
    }

    private void SearchPeople(String searchBoxInput) {

        //QUERY FOR FIREBASE RECYCLER ADAPTER....
        FirebaseRecyclerOptions<FindFriends> options =
                new FirebaseRecyclerOptions.Builder<FindFriends>()
                        .setQuery(AllUserRef.orderByChild("fullname").startAt(searchBoxInput).endAt(searchBoxInput+"\uf8ff"),
                                FindFriends.class)
                        .build();

        FirebaseRecyclerAdapter<FindFriends,FindFriendsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<FindFriends, FindFriendsViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull FindFriendsViewHolder holder, int position, @NonNull FindFriends model) {
           //Binding data to the recycler view.....
                final String UserKey = getRef(position).getKey();

                holder.setFullname(model.fullname);
                holder.setUsername("@"+model.username);
                holder.setProfileImage(model.profileImage);
                holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(find_friends_activity.this,"Open Profile",Toast.LENGTH_SHORT).show();
                    SendUserToHisProfileActivity(UserKey);

                }
            });

            }

            @NonNull
            @Override
            public FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view  = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_user_display_layout,parent,false);

                return new FindFriendsViewHolder(view);
            }
        };

        //Setting Adapter into RecyclerView
        SearchList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public FindFriendsViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
            }

            public void setFullname(String fullname)
            {
                TextView name = mView.findViewById(R.id.searched_name);
                name.setText(fullname);
            }
            public void setProfileImage(String profileImage)
            {
                CircleImageView imageView = mView.findViewById(R.id.searched_profileimage);
                Picasso.get().load(profileImage).placeholder(R.drawable.profile).into(imageView);
            }
            public void setUsername(String username)
            {
                TextView name = mView.findViewById(R.id.searched_username);
                name.setText(username);

            }
    }
    public void SendUserToHisProfileActivity(String UserKey)
            {
                Intent sendProfile = new Intent(find_friends_activity.this,ProfileActivity.class);
                sendProfile.putExtra("UserKey",UserKey);
                startActivity(sendProfile);

            }
}
