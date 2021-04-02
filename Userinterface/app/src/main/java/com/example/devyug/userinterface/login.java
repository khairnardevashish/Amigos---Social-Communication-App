package com.example.devyug.userinterface;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class login extends AppCompatActivity {

    EditText mail, pass;
    String email, password;
    private FirebaseAuth mAuth;
    Button login,Reg;
    TextView forgetpas;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Getdisplayname.setname();
            Intent i = new Intent(login.this, slider.class);
            startActivity(i);

        }
        login = (Button) findViewById(R.id.signin);
        Reg= (Button) findViewById(R.id.register);
        mail = (EditText) findViewById(R.id.mail);
        pass = (EditText) findViewById(R.id.pas);
        forgetpas= (TextView) findViewById(R.id.forgetpas);
        final String email=mail.getText().toString();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_func();
            }
        });

        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, Registration.class);
                startActivity(intent);
            }
        });

        forgetpas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // FirebaseAuth auth = FirebaseAuth.getInstance();

                if((TextUtils.isEmpty(mail.getText().toString())))
                {
                    Toast.makeText(login.this, "Please enter your email",
                            Toast.LENGTH_SHORT).show();
                }
                else {

                    String emailAddress = mail.getText().toString();
                    mAuth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(login.this, "Please check your email to reset your password",
                                                Toast.LENGTH_SHORT).show();
                                        Log.d("", "Email sent.");
                                    }
                                }
                            });
                }
            }
        });


    }

    public void login_func() {

        email = mail.getText().toString().trim();
        password = pass.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
//            Toast.makeText(login.this, email + password,
//                    Toast.LENGTH_SHORT).show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override

                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //String ad_mail;
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();

                                    Getdisplayname.setname();


                                    //ad_mail = user.getEmail().toString();
                                    Intent i = new Intent(login.this, slider.class);
                                    //i.putExtra("admin_mail", ad_mail);
                                    startActivity(i);

                            }
                            else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(login.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
        }
        else
        {
            Toast.makeText(login.this, "Enter mail id and password!", Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        //Changes 'back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i= new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
        return true;
    }





}

