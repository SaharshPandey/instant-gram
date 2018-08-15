package com.example.iknownothing.instantgram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    private String PostKey;
    private DatabaseReference ClickPostRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_post);

        PostKey = getIntent().getExtras().getString("PostKey");
        ClickPostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey);

        image_clicked = findViewById(R.id.image_clicked);
        description_clicked = findViewById(R.id.description_clicked);
        edit_clicked = findViewById(R.id.edit_clicked);
        delete_clicked = findViewById(R.id.delete_clicked);

        ClickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String description = dataSnapshot.child("description").getValue().toString();
                String postimage = dataSnapshot.child("postimage").getValue().toString();

                description_clicked.setText(description);
                Picasso.get().load(postimage).into(image_clicked);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
