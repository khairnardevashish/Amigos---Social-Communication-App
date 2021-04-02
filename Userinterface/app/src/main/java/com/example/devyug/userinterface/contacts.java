package com.example.devyug.userinterface;

import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by devyug on 14/9/17.
 */

public class contacts extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener{
    String[] member_names;

    TypedArray profile_pics;
    DatabaseReference databaseReference;

    List<RowItem> rowItems;
    ListView mylistview;
    List<profile_pojo> profilelist;

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
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
//
  //      String member_name = rowItems.get(position).getMember_name();
       // Toast.makeText(getApplicationContext(), "" + member_name,
         //       Toast.LENGTH_SHORT).show();
    }

    public void display(View v)
    {
        rowItems = new ArrayList<RowItem>();
        profilelist=new ArrayList<profile_pojo>();
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

               // Log.w("username",p.getUsername()+"a");
                RowItem r=new RowItem(p.getUsername(),p.getImage());
                if(p.getUsername()!=UserDetails.username) {
                    rowItems.add(r);
                    profilelist.add(p);
                    adapter.notifyDataSetChanged();
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
            profile_pojo p=new profile_pojo();
                p=profilelist.get(position);
                final Dialog d = new Dialog(getActivity());
                d.setContentView(R.layout.popup);
             d.setTitle(p.getUsername());
               ImageView c=(ImageView)d.findViewById(R.id.circleImageView2);
                Picasso.with(getContext()).load(p.getImage()).into(c);
                TextView email=(TextView)d.findViewById(R.id.tvemail);
                TextView phone=(TextView)d.findViewById(R.id.tvphone);
                TextView status=(TextView)d.findViewById(R.id.tvstatus);
                email.setText(p.getEmail());
                phone.setText(p.getPhone());
                status.setText(p.getStatus());
//                d.setCancelable(true);
                d.show();
            }
        });
    }
}
