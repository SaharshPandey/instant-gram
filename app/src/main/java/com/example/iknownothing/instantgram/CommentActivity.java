package com.example.iknownothing.instantgram;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
    String comments;
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
            protected void onBindViewHolder(@NonNull final CommentViewHolder holder, final int position, @NonNull final Comments model) {


                holder.setCommenttext(model.getCommenttext());
                holder.setDate(model.getDate());
                holder.setTime(model.getTime());

                UserRef.child(model.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        holder.setusername(dataSnapshot.child("username").getValue().toString());
                        holder.setprofileImage(dataSnapshot.child("profileImage").getValue().toString());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                ImageButton CommentEdit = holder.mView.findViewById(R.id.comment_edit);
                //hiding the edit bar in initial time.
                CommentEdit.setVisibility(View.GONE);

                Log.d("Message",getRef(position).getKey()+"    "+CurrentUserId);
                if(getRef(position).getKey().substring(0,28).equals(CurrentUserId))
                {
                    CommentEdit.setVisibility(View.VISIBLE);
                }

                CommentEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCommentPopup(v,getRef(position).getKey());
                    }
                });
            }
        };

        CommentList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    private void showCommentPopup(View v, String postKey) {
        //Referencing Comments

        //Log.d("Message",postKey+"         "+PostKey);
        final DatabaseReference CommentRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey).child("Comments").child(postKey);

        CommentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                comments = dataSnapshot.child("commenttext").getValue().toString();
                   // Log.d("Message","successfull"+"   "+comments);

                }

                else{
                    Toast.makeText(CommentActivity.this,"Data doesn't Exists ! ",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //CREATING POPUP MENU AND ADDING MENU XML INTO IT...
        final PopupMenu popupMenu = new PopupMenu(this,v);

        //METHOD TO ADD ICONS TO THE POPUP MENU
        try{
            Field[] fields =popupMenu.getClass().getDeclaredFields();
            for(Field field : fields )
            {
                if("mPopup".equals(field.getName()))
                {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon",boolean.class);
                    setForceIcons.invoke(menuPopupHelper,true);

                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        //ICONS ADDED TO POPUP SUCCEED..


        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.comment_menu,popupMenu.getMenu());

        //Setting On ClickListener for PopupMenu;

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.modify_comment:
                        //AlertDialog Box
                        final AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this,R.style.MyAlertDialogStyle);
                        builder.setTitle("Edit Your Comment");

                        //ADDING INPUT FIELD...
                        final EditText inputfield = new EditText(CommentActivity.this);


                        //STYLING
                        inputfield.setPadding(20,100,20,20);
                        inputfield.setMaxLines(8);
                        inputfield.setText(comments);

                        //Displaying Edit Text in AlertDialogBox;
                        builder.setView(inputfield);

                  //Adding Positive and Negative Buttons;

                        //Positive Button;
                        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CommentRef.child("commenttext").setValue(inputfield.getText().toString());
                                Toast.makeText(CommentActivity.this,"Updated Successfully",Toast.LENGTH_SHORT);

                            }
                        });

                        //Negative Button;
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        Dialog dialog = builder.create();
                        dialog.show();
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
                        break;

                    case R.id.delete_comment:

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CommentActivity.this,R.style.MyAlertDialogStyle);
                        builder1.setTitle("Delete Comment");

                //Adding Positive and Negative Buttons;

                        //+ button;
                        builder1.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CommentRef.removeValue();
                                Toast.makeText(CommentActivity.this,"Deleted Successfully",Toast.LENGTH_SHORT);

                            }
                        });

                        //- button;
                        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });

                        Dialog dialog1 = builder1.create();
                        dialog1.show();
                        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.white);
                        break;

                }

                return true;
            }
        });

        popupMenu.show();


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
            Picasso.get().load(profileImage).placeholder(R.drawable.profile).into(comment_profile);
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
            commentMap.put("commenttext",commentText);
            commentMap.put("date", CurrentDate);
            commentMap.put("time", CurrentTime);
            commentMap.put("timestamp",ts);

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
