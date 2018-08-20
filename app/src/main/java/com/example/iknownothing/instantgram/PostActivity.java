package com.example.iknownothing.instantgram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class PostActivity extends AppCompatActivity {

    private StorageReference PostsImageReference;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef , PostRef;
    private Toolbar mToolbar;
    private ImageView SelectPostImage;
    private EditText SelectCaption;
    private ImageButton PostImage;
    private Uri ImageUri;
    private String CurrentDAte,CurrentTime,PostRandomName;
    private String DownloadUrl;
    private String Current_User_Id;
    private String description;
    private String ts;
    private Compressor compressedImageFile;

    private static final int GalleryPic =1;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        OpenGallery();

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);
        Current_User_Id = mAuth.getCurrentUser().getUid();
        PostsImageReference = FirebaseStorage.getInstance().getReference();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        SelectPostImage = findViewById(R.id.selectPostImage);
        SelectCaption = findViewById(R.id.text_post);
        PostImage = findViewById(R.id.add_new_post_button);

       /* SelectPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenGallery();
            }
        });
*/
        PostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidatePostInfo();
            }
        });

        //Deleted Toolbar.....

        /*mToolbar =  findViewById(R.id.update_post_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Update Post");*/
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void ValidatePostInfo() {

        description = SelectCaption.getText().toString();
        if(ImageUri == null)
        {
           // Toast.makeText(PostActivity.this,"Please select the Image",Toast.LENGTH_SHORT).show();

            SavingPostInformationToDatabase();
        }



        else
        {
            loadingBar.setTitle("Updating Post");
            loadingBar.setMessage("Please wait while we update your post");
            loadingBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            StoringImageToFirebaseStorage();
        }
    }

    private void StoringImageToFirebaseStorage()
    {
       Calendar  calForDAte =Calendar.getInstance();

        Long tsLong = System.currentTimeMillis()/1000;
        ts = tsLong.toString();

       SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        CurrentDAte = currentDate.format(calForDAte.getTime());

       SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
       CurrentTime = currentTime.format(calForDAte.getTime());

        PostRandomName = CurrentDAte+CurrentTime;



        StorageReference filePath = PostsImageReference.child("Post Images").child(ImageUri.getLastPathSegment() + PostRandomName + ".jpg");
        filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                    {
                        DownloadUrl  = task.getResult().getDownloadUrl().toString();
                        Toast.makeText(PostActivity.this,"Image Uploaded Successfully",Toast.LENGTH_SHORT).show();
                        SavingPostInformationToDatabase();
                    }
                else
                    {
                        loadingBar.dismiss();
                        String message = task.getException().getMessage();
                        Toast.makeText(PostActivity.this,message,Toast.LENGTH_SHORT).show();
                    }
            }
        });


    }

    private void SavingPostInformationToDatabase()
    {
        UserRef.child(Current_User_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {


                    if(ImageUri == null)//checking whether this image is not selected..
                    {
                        Calendar  calForDAte =Calendar.getInstance();

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

                    //Making data for node to store in firebase.....
                    HashMap postMap = new HashMap();
                    postMap.put("uid", Current_User_Id);
                    postMap.put("date", CurrentDAte);
                    postMap.put("time", CurrentTime);
                    postMap.put("description", description);
                    postMap.put("postimage", DownloadUrl);
                    postMap.put("profileImage", profileImage);
                    postMap.put("fullname", userFullname);
                    postMap.put("timestamp",ts);

                    //making new node in database....
                    PostRef.child(Current_User_Id +PostRandomName).updateChildren(postMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if(task.isSuccessful())
                            {
                                loadingBar.dismiss();
                                Toast.makeText(PostActivity.this,"Post is Updated Successfully",Toast.LENGTH_SHORT).show();
                                SendUserToMainActivity();
                            }

                            else
                            {
                                loadingBar.dismiss();
                                Toast.makeText(PostActivity.this,"Error Occurred while Updating the Post,Try Again...",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

                    //When the user Select the image he will be redirected to the Image Cropping Activity...
                    CropImage.activity(ImageUri)
                            .setAspectRatio(2, 3)
                            .setCropShape(CropImageView.CropShape.RECTANGLE);
                SelectPostImage.setImageURI(ImageUri);

        }
    }
}
