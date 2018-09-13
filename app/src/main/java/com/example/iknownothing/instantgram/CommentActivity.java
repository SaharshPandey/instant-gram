package com.example.iknownothing.instantgram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {

    private CircleImageView CommentProfile;
    private EditText WriteComment;
    private RecyclerView CommentList;
    private ImageView CommentButton;
    private DatabaseReference UserRef,PostRef;
    private FirebaseAuth mAuth;
    String PostKey,CurrentUserId;
    private TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        //THIS METHOD DONT OPEN THE KEYBOARD IN STARTUP....

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        name = findViewById(R.id.name);
        name.setText("Comments");

        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();

        PostKey = getIntent().getExtras().getString("PostKey");

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey).child("Comments");

        //Initialising objects;
        CommentProfile = findViewById(R.id.comment_photo);
        WriteComment = findViewById(R.id.write_comment);
        CommentButton = findViewById(R.id.postcomment);

        //Setting RecyclerView with LinearLayoutManager...
        CommentList = findViewById(R.id.show_comments);
        CommentList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        CommentList.setLayoutManager(linearLayoutManager);


        CommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //HIDING THE KEYBOARD WHEN BUTTON IS CLICKED....
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);



                UserRef.child(CurrentUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            String userName = dataSnapshot.child("username").getValue().toString();
                            String profileImage = dataSnapshot.child("profileImage").getValue().toString();
                            ValidateComment(userName,profileImage);

                            //making empty when user click on comment button...
                            WriteComment.setText("");

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Comments> options =
                new FirebaseRecyclerOptions.Builder<Comments>()
                        .setQuery(PostRef.orderByChild("timestamp"), Comments.class)
                        .build();


        FirebaseRecyclerAdapter<Comments,CommentViewHolder> firebaseRecyclerAdapter =
        new FirebaseRecyclerAdapter<Comments,CommentViewHolder>(options){

            @NonNull
            @Override
            public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_comments_layout, parent, false);
                return new CommentViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull Comments model) {

                holder.setusername(model.getUsername());
                holder.setprofileImage(model.getProfileImage());
                holder.setCommenttext(model.getCommenttext());
                holder.setDate(model.getDate());
                holder.setTime(model.getTime());
            }
        };

        CommentList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //Class Holder for RecyclerView...............
    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public CommentViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setusername(String username){
            TextView comment_user = mView.findViewById(R.id.comment_user);
            comment_user.setText(username);
        }

        public void setCommenttext(String commenttext) {
            TextView comment = mView.findViewById(R.id.comment);
            comment.setText(commenttext);
        }

        public void setDate(String date) {
            TextView comment_date = mView.findViewById(R.id.comment_date);
            comment_date.setText(date);
        }

        public void setTime(String time) {
            TextView comment_time = mView.findViewById(R.id.comment_time);
            comment_time.setText(time);
        }

        public void setprofileImage(String profileImage){
            CircleImageView comment_profile = mView.findViewById(R.id.comment_profile);
            Picasso.get().load(profileImage).into(comment_profile);
        }


    }











    private void ValidateComment(String userName,String profileImage) {
        String commentText = WriteComment.getText().toString();
        if(TextUtils.isEmpty(commentText))
        {
            Toast.makeText(CommentActivity.this,"Enter Valid Comment",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Calendar  calForDAte =Calendar.getInstance();

            Long tsLong = System.currentTimeMillis()/1000;
            final String ts = tsLong.toString();

            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            final String CurrentDate = currentDate.format(calForDAte.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            final String CurrentTime = currentTime.format(calForDAte.getTime());

            final String RandomKey = CurrentUserId+CurrentDate+CurrentTime;

            //Making data for node to store in firebase.....
            HashMap commentMap = new HashMap();
            commentMap.put("uid", CurrentUserId);
            commentMap.put("username",userName);
            commentMap.put("commenttext",commentText);
            commentMap.put("date", CurrentDate);
            commentMap.put("time", CurrentTime);
            commentMap.put("timestamp",ts);
            commentMap.put("profileImage",profileImage);

            PostRef.child(RandomKey).updateChildren(commentMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
               if(task.isSuccessful())
               {
                   Toast.makeText(CommentActivity.this,"Comment Posted",Toast.LENGTH_SHORT).show();
               }
               else{
                   Toast.makeText(CommentActivity.this,"Unable to Comment",Toast.LENGTH_SHORT).show();
               }

                }
            });
        }





    }
}
