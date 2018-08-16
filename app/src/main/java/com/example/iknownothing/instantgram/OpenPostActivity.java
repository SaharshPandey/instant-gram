package com.example.iknownothing.instantgram;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class OpenPostActivity extends AppCompatActivity {

    private ImageView image_clicked;
    TextView description_clicked;
    private Button edit_clicked,delete_clicked;
    private String PostKey,Current_User_Id,Database_User_Id,description,postimage;
    private DatabaseReference ClickPostRef;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_post);

        mAuth = FirebaseAuth.getInstance();
        Current_User_Id = mAuth.getCurrentUser().getUid();
        PostKey = getIntent().getExtras().getString("PostKey");
        ClickPostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey);

        image_clicked = findViewById(R.id.image_clicked);
        description_clicked = findViewById(R.id.description_clicked);
        edit_clicked = findViewById(R.id.edit_clicked);
        delete_clicked = findViewById(R.id.delete_clicked);

        edit_clicked.setVisibility(View.INVISIBLE);
        delete_clicked.setVisibility(View.INVISIBLE);

        ClickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                description = dataSnapshot.child("description").getValue().toString();
                postimage = dataSnapshot.child("postimage").getValue().toString();
                Database_User_Id = dataSnapshot.child("uid").getValue().toString();

                description_clicked.setText(description);
                Picasso.get().load(postimage).into(image_clicked);

                if(Current_User_Id.equals(Database_User_Id)){
                    edit_clicked.setVisibility(View.VISIBLE);
                    delete_clicked.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
