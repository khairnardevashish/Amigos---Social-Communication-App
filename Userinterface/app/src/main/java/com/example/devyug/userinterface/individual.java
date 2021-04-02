package com.example.devyug.userinterface;

        import android.app.Fragment;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
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
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.Map;

/**
 * Created by devyug on 14/9/17.
 */

/*
public class profile extends android.support.v4.app.Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.profile_ui, container, false);
        return view;
    }
}*/

public class individual extends android.support.v4.app.Fragment {

    private static final String TAG = "UserList";
    private DatabaseReference userlistReference;
    private ValueEventListener mUserListListener;
    ArrayList<String> usernamelist = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    FirebaseListAdapter adapter;
    ListView userListView;
    List<String> al = new ArrayList<>();
    DatabaseReference reference3;
    ArrayAdapter<String>adapter1 ;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //final View view = inflater.inflate(R.layout.profile_ui, container, false);
        final View view = inflater.inflate(R.layout.individual_ui,container,false);
        onAuthSuccess(FirebaseAuth.getInstance().getCurrentUser());
        //setContentView(R.layout.profile_ui);
        userlistReference = FirebaseDatabase.getInstance().getReference().child("usernamelist");
        reference3 = FirebaseDatabase.getInstance().getReference().child("users");
        adapter1 =new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, al);
        userlist(view);
        return view;
    }






    @Override
    public void onStart() {
        super.onStart();
       /* final ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usernamelist = new ArrayList<>((ArrayList) dataSnapshot.getValue());
                usernamelist.remove(usernameOfCurrentUser());
                Log.i(TAG, "onDataChange: "+usernamelist.toString());
                arrayAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,usernamelist);
                userListView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled: ",databaseError.toException());
                Toast.makeText(MainActivity.this, "Failed to load User list.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        userlistReference.addValueEventListener(userListener);

        mUserListListener = userListener;*/
    }

    public String usernameOfCurrentUser() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
      /*  if (mUserListListener != null) {
            userlistReference.removeEventListener(mUserListListener);
        }   */

    }

    // @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), login.class));
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

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
        ArrayList<String> userNames = new ArrayList<>();
    }

    void display(View view) {
        ListView userListView = (ListView) view.findViewById(R.id.usersList1);

        adapter = new FirebaseListAdapter<user>(getActivity(), user.class,
                R.layout.user_list, FirebaseDatabase.getInstance().getReference().child("users")) {
            @Override
            protected void populateView(View v, user model, int position) {
                // Get references to the views of message.xml

                TextView v1 = (TextView) v.findViewById(R.id.username);
                v1.setText(model.getName());
                // Set their text
            }
        };
        userListView.setAdapter(adapter);

    }

    public void userlist(View view)
    {
        userListView= (ListView) view.findViewById(R.id.usersList1);
        reference3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String,String> map = (Map<String, String>) dataSnapshot.getValue();
                String name=map.get("name").toString();
                if(!name.equals(UserDetails.username)&&!al.contains(name))
                    al.add(name);
                // adapter1.notifyDataSetChanged();

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
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userListView.setAdapter(adapter1);
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {

                UserDetails.chatWith=  al.get(i);
                Log.w("user",UserDetails.username+" c");
                startActivity(new Intent(getActivity(), chat.class));

            }
        });
    }
}
