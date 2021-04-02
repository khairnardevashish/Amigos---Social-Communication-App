package com.example.devyug.userinterface;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.devyug.userinterface.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by devyug on 14/9/17.
 */

public class group extends android.support.v4.app.Fragment {

    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    DatabaseReference reference1, reference2;
    private FirebaseListAdapter<ChatMessage> adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.group_ui, container, false);
        layout = (LinearLayout) view.findViewById(R.id.layout3);
        layout_2 = (RelativeLayout) view.findViewById(R.id.layout4);
        sendButton = (ImageView) view.findViewById(R.id.sendButton);
        messageArea = (EditText) view.findViewById(R.id.messageArea);
        scrollView=(ScrollView)view.findViewById(R.id.scrollView1);
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
        //onAuthSuccess(FirebaseAuth.getInstance().getCurrentUser());
        reference1=FirebaseDatabase.getInstance().getReference().child("message");
        reference2=FirebaseDatabase.getInstance().getReference().child("notifications");
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
                   /* map.put("message", messageText);
                    map.put("user", UserDetails.username);*/
                    // reference1.push().setValue(map);
                    //reference2.push().setValue(map);
                    notification_pojo n=new notification_pojo();
                    n.setText(m.getMessageText());
                    n.setSender(UserDetails.username);
                    n.setReceiver("group");
                    reference1.push().setValue(m);
                    reference2.push().setValue(n);
                    messageArea.setText("");
                }
            }
        });
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               /* Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                String message = map.get("message").toString();
                String userName = map.get("user").toString();*/

                message m=(message) dataSnapshot.getValue(message.class);
                String userName=m.getMessageUser();

                String message=m.getMessageText();
                if (userName.equals(UserDetails.username)) {
                    addMessageBox("You",message,m.getMessageTime(), 1);
                } else {
                    addMessageBox(userName,message,m.getMessageTime(), 2);
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
        //displayChatMessages(view);
        return view;
    }
    /*private void displayChatMessages(View v1) {
        ListView listOfMessages = (ListView)v1.findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                if(!messageUser.getText().toString().equals(FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getDisplayName()))
                    //  sendNotification(messageText.getText().toString());
                    // Format the date before showing it
                    messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                            model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }*/
    public void addMessageBox(String name,String message,String time, int type) {
        TextView textView = new TextView(getActivity());
        TextView tname=new TextView(getActivity());
        tname.setText(name);
        textView.setText(message);
        TextView timet=new TextView(getActivity());
        TextView blank=new TextView(getActivity());
        timet.setText(time);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if (type == 1) {
            tname.setTextColor(Color.parseColor("#bdbdbd"));
            lp2.gravity = Gravity.RIGHT;

            textView.setBackgroundResource(R.drawable.bubble_in);
        } else {
            tname.setTextColor(Color.parseColor("#bdbdbd"));
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        timet.setLayoutParams(lp2);
        blank.setLayoutParams(lp2);
        tname.setLayoutParams(lp2);
        layout.addView(tname);
        layout.addView(textView);
        layout.addView(timet);
        layout.addView(blank);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
    public String usernameOfCurrentUser() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        UserDetails.username=username;

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());

        // Go to MainActivity

    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void writeNewUser(String userId, String name, String email) {
        user user1 = new user();
        user1.setEmail(email);
        user1.setName(name);


        FirebaseDatabase.getInstance().getReference().child("users").child(userId).setValue(user1);
        // ArrayList<String> userNames = new ArrayList<>();
    }

}