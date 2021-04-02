package com.example.devyug.userinterface;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devyug.userinterface.R;
import com.google.android.gms.internal.zzbjp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.List;

public class Registration extends AppCompatActivity{

    //defining view objects

    private Uri imageuri;
    private static final int GALLERY_REQUEST = 1;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignup;
    private ImageView image;
    String name,email;
    private ProgressDialog progressDialog;
    private StorageReference storage;


    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //initializing views
        databaseReference= FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance().getReference().child("Profile_imgs");
        editTextEmail = (EditText) findViewById(R.id.ml);
        editTextPassword = (EditText) findViewById(R.id.ps);


        buttonSignup = (Button) findViewById(R.id.Reg);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        image = (ImageView)findViewById(R.id.imageView);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_REQUEST);
            }
        });



        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startSetupAccount();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                final String email = editTextEmail.getText().toString().trim();
                String password  = editTextPassword.getText().toString().trim();

                EditText un1= (EditText) findViewById(R.id.un1);
                EditText ph1= (EditText) findViewById(R.id.phone);
                String phone = ph1.getText().toString();
                String username = un1.getText().toString();

               if(TextUtils.isEmpty(username) || username.length()<6)
                {
                    Toast.makeText(Registration.this,"Please enter valid username",Toast.LENGTH_LONG).show();
                }
              else if(phone.length()!=10 )
                {
                    Toast.makeText(Registration.this,"Please enter valid mobile number",Toast.LENGTH_LONG).show();
                }

                else if(TextUtils.isEmpty(email) && !email.matches(emailPattern)  )
                {
                    Toast.makeText(Registration.this,"Please enter valid Email",Toast.LENGTH_LONG).show();
                }
               else if(TextUtils.isEmpty(password) || password.length()>6)
               {
                   Toast.makeText(Registration.this,"Please enter valid password",Toast.LENGTH_LONG).show();
               }

                else

                {
                    registerUser();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK)
        {
            Uri uri = data.getData();
            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAutoZoomEnabled(true)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageuri = result.getUri();

                image.setImageURI(imageuri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

//image view end
private void startSetupAccount() {





}

    private void registerUser(){

        //getting email and password from edit texts
        final String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email) && !email.matches(emailPattern)){
            Toast.makeText(this,"Please enter valid email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password) && password.length()>6){
            Toast.makeText(this,"Please enter valid password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog



        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();


        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                          //  sendEmailVerification();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            StorageReference filepath = storage.child(imageuri.getLastPathSegment());

                            filepath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    EditText un= (EditText) findViewById(R.id.un1);
                                     EditText ph= (EditText) findViewById(R.id.phone);
                                    String downUrl = taskSnapshot.getDownloadUrl().toString();
                                    String phone = ph.getText().toString();
                                    String username = un.getText().toString();
                                    String status = "Hey Amigos !!!";

                                    databaseReference.child("Profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("image").setValue(downUrl);
                                    databaseReference.child("Profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").setValue(username);
                                    databaseReference.child("Profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("phone").setValue(phone);
                                    databaseReference.child("Profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue(status);
                                    databaseReference.child("Profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").setValue(email);




                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(Registration.this, "Error", Toast.LENGTH_SHORT).show();

                                }
                            });

                            //display some message here
                            Toast.makeText(Registration.this, "Successfully registered", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Registration.this, login.class);
                            startActivity(intent);



//                            if (user.isEmailVerified()) {
//                                Intent intent = new Intent(Registration.this, login.class);
//                                startActivity(intent);
//                                //ad_mail = user.getEmail().toString();
//
//                            } else {
//                                Toast.makeText(Registration.this, "Please verify your email!",
//                                        Toast.LENGTH_SHORT).show();
//                            }
                        }
                        else
                            {
                            //display some message here
                            Toast.makeText(Registration.this,"Registration Error",Toast.LENGTH_LONG).show();
                            }
                        progressDialog.dismiss();
                    }
                });

    }
    private void sendEmailVerification() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Registration.this, "Check your Email for verification", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }

            });
        }
    }

    private void OnAuth(FirebaseUser user) {
        createAnewUser(user.getUid());
    }

    private void createAnewUser(String uid) {
        user us = BuildNewuser();
        databaseReference.child(uid).setValue(us);
    }


    private user BuildNewuser(){
        return new user(
                getName(),
                getEmail()
                //  new Date().getTime()
        );
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}