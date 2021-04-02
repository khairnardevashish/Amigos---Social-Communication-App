package com.example.devyug.userinterface;


/**
 * Created by dell on 21/9/17.
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class chat extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;


    DatabaseReference reference1, reference2,reference3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle(UserDetails.chatWith);
        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout) findViewById(R.id.layout2);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView=(ScrollView)findViewById(R.id.scrollView);
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                scrollView.post(new Runnable() {
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });
        // FirebaseDatabase.setAndroidContext(this);
        reference1 = FirebaseDatabase.getInstance().getReference().child("messages1").child(UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = FirebaseDatabase.getInstance().getReference().child("messages1").child(UserDetails.chatWith + "_" + UserDetails.username);
        reference3=FirebaseDatabase.getInstance().getReference().child("notifications");
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if (!messageText.equals("")) {
                    // Map<String, String> map = new HashMap<String, String>();
                    message m=new message();
                    m.setMessageText(messageText);
                    m.setMessageUser(UserDetails.username);
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd hh:mm a");
                    String dateAndTime = formatter.format(date);
                    m.setMessageTime(dateAndTime);
                    notification_pojo n=new notification_pojo();
                    n.setText(m.getMessageText());
                    n.setSender(UserDetails.username);
                    n.setReceiver(UserDetails.chatWith);
                   /* map.put("message", messageText);
                    map.put("user", UserDetails.username);*/
                    // reference1.push().setValue(map);
                    //reference2.push().setValue(map);
                    reference1.push().setValue(m);
                    reference2.push().setValue(m);
                    reference3.push().setValue(n);
                    messageArea.setText("");
                }
            }
        });

        reference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               /* Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                String message = map.get("message").toString();
                String userName = map.get("user").toString();*/

                message m=(message) dataSnapshot.getValue(message.class);
                String userName=m.getMessageUser();

                String message=m.getMessageText();
                if (userName.equals(UserDetails.username)) {
                    addMessageBox("" + message,m.getMessageTime(), 1);
                } else {
                    addMessageBox("" + message,m.getMessageTime(), 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message, String time,int type) {
        TextView textView = new TextView(chat.this);
        TextView tv = new TextView(chat.this);
        textView.setText(message);
        tv.setText(time);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if (type == 1) {
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        } else {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        tv.setLayoutParams(lp2);
        layout.addView(textView);
        layout.addView(tv);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

}