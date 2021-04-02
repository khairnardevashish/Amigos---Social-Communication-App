package com.example.devyug.userinterface;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by devyug on 14/9/17.
 */

public class profile extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener{
    String[] member_names;

    TypedArray profile_pics;
    DatabaseReference databaseReference;

    List<RowItem> rowItems;
    ListView mylistview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.contacts_ui, container, false);
        display(view);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i,
                            long id) {
        RowItem r=rowItems.get(i);
        UserDetails.chatWith=r.getMember_name();
        startActivity(new Intent(getActivity(), chat.class));

    }

    public void display(View v)
    {
        rowItems = new ArrayList<RowItem>();

        // member_names = getResources().getStringArray(R.array.Member_names);

        //profile_pics = getResources().obtainTypedArray(R.array.profile_pics);
        mylistview = (ListView)v.findViewById(R.id.list);
        final CustomAdapter adapter = new CustomAdapter(getActivity(), rowItems);
        mylistview.setAdapter(adapter);
        registerForContextMenu(mylistview);

        databaseReference  = FirebaseDatabase.getInstance().getReference().child("Profile");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                profile_pojo p=new profile_pojo();
                p=(profile_pojo)dataSnapshot.getValue(profile_pojo.class);

                RowItem r=new RowItem(p.getUsername(),p.getImage());
                if(p.getUsername()!=UserDetails.username)
                rowItems.add(r);
                adapter.notifyDataSetChanged();
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


       /* for (int i = 0; i < member_names.length; i++) {
            RowItem item = new RowItem(member_names[i],
                    profile_pics.getResourceId(i, -1));
            rowItems.add(item);
        }*/

     mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener()
       {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                RowItem r=rowItems.get(position);
                UserDetails.chatWith=r.getMember_name();
                Intent intent=new Intent(getActivity().getApplicationContext(),chat.class);
                startActivity(intent);
            }
        });
    }
}
