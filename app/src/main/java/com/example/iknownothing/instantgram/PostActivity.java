package com.example.iknownothing.instantgram;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PostActivity extends AppCompatActivity {

    private StorageReference PostsImageReference;
    private Toolbar mToolbar;
    private ImageButton SelectPostImage;
    private EditText SelectCaption;
    private Button PostImage;
    private Uri ImageUri;
    private String CurrentDAte,CurrentTime,PostRandomName;
    private static final int GalleryPic =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        PostsImageReference = FirebaseStorage.getInstance().getReference();
        SelectPostImage = findViewById(R.id.selectPostImage);
        SelectCaption = findViewById(R.id.selectCaption);
        PostImage = findViewById(R.id.postImage);

        SelectPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenGallery();
            }
        });

        PostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidatePostInfo();
            }
        });

        mToolbar =  findViewById(R.id.update_post_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Update Post");
    }

    private void ValidatePostInfo() {

        String description = SelectCaption.getText().toString();
        if(ImageUri == null)
        {
            Toast.makeText(PostActivity.this,"Please select the Image",Toast.LENGTH_SHORT).show();
        }

        else if(SelectCaption == null)
        {
            Toast.makeText(PostActivity.this,"Please Enter the Caption",Toast.LENGTH_SHORT).show();
        }

        else
        {
            StoringImageToFirebaseStorage();
        }
    }

    private void StoringImageToFirebaseStorage()
    {
        Calendar  calForDAte =Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        CurrentDAte = currentDate.format(calForDAte.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        CurrentTime = currentDate.format(calForDAte.getTime());

        PostRandomName = CurrentDAte+CurrentTime;

        StorageReference filePath = PostsImageReference.child("Post Images").child(ImageUri.getLastPathSegment() + PostRandomName + ".jpg");
        filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                    {
                        Toast.makeText(PostActivity.this,"Image Uploaded Successfully",Toast.LENGTH_SHORT).show();
                    }
                else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(PostActivity.this,message,Toast.LENGTH_SHORT).show();
                    }
            }
        });


    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPic); //User will pick the Picture..
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home){
            SendUserToMainActivity();    
        }
        return super.onOptionsItemSelected(item);
    }

    private void SendUserToMainActivity() {
        Intent homeIntent = new Intent(PostActivity.this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GalleryPic && resultCode == RESULT_OK && data !=null)
        {
            ImageUri = data.getData();
            SelectPostImage.setImageURI(ImageUri);

        }
    }
}
