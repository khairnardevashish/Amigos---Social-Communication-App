package com.example.devyug.userinterface;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.twitter.sdk.android.core.models.User;

/**
 * Created by dell on 26/9/17.
 */

public class Getdisplayname {
    static String s;
    public static void setname()
    {

//        FirebaseDatabase.getInstance().getReference().child("Profile").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                profile_pojo u=(profile_pojo) dataSnapshot.getValue(profile_pojo.class);
//
//                if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(u.getEmail())) {
//
//                   s = u.getUsername();
//
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//        });
//
//        Log.w("username in method",s+" s");
//        return s;
        FirebaseDatabase.getInstance().getReference().child("Profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               String s=(String)dataSnapshot.getValue(String.class);
                UserDetails.username=s;
                FirebaseMessaging.getInstance().subscribeToTopic("use_"+s);
                FirebaseMessaging.getInstance().subscribeToTopic("use_group");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
