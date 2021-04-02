package com.example.devyug.userinterface;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class notification_setting extends AppCompatActivity {
    ArrayList list = new ArrayList();


    android.support.v7.widget.SwitchCompat switch1;
    android.support.v7.widget.SwitchCompat switch2;
    ListView lvword;
    List<String> l;
    ArrayAdapter<String>adapter;
    userdatabase db;
    TextView textView;
    Button add;
    Button reset;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        l=new ArrayList<>();
        db=new userdatabase(this);
        setContentView(R.layout.activity_notification_setting);
        switch1=(android.support.v7.widget.SwitchCompat)findViewById(R.id.switch1);
        switch2=(android.support.v7.widget.SwitchCompat)findViewById(R.id.switch2);
        lvword=(ListView)findViewById(R.id.wordlist);
        add=(Button)findViewById(R.id.add);
        reset=(Button)findViewById(R.id.reset);
        adapter=new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1,l);
        if(db.getspecific())
            switch1.setChecked(true);
        else
            switch1.setChecked(false);

        if(db.getfilter())
        {
            switch2.setChecked(true);
            List<String> a=new ArrayList<>();
            a=db.getwords();
            ListIterator<String > i=a.listIterator();

            while(i.hasNext())
            {
                l.add(i.next());
                adapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(lvword);
            }

        }
        else
            switch2.setChecked(false);

            switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switch1.isChecked()) {
                db.setspecific();
                } else {
                db.clearspecific();
                }
            }
        });
        lvword.setAdapter(adapter);
        setListViewHeightBasedOnChildren(lvword);
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switch2.isChecked()) {
                    l.clear();
                    List<String> a=new ArrayList<>();
                    a=db.getwords();
                    ListIterator<String > i=a.listIterator();

                    while(i.hasNext())
                    {
                        l.add(i.next());
                        adapter.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(lvword);
                    }

                    db.setfilter();
                } else {
                    db.clearfilter();
                    l.clear();

                    adapter.notifyDataSetChanged();

                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(l.size()<=5) {
                    if (switch2.isChecked()) {
                        final Dialog d = new Dialog(notification_setting.this);
                        d.setContentView(R.layout.dialogue);
                        d.setTitle("Add word");
                        d.setCancelable(true);
                        final EditText edit = (EditText) d.findViewById(R.id.edword);
                        Button b = (Button) d.findViewById(R.id.add);
                        b.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                String s = edit.getText().toString();
                                l.add(s);
                                db.addword(UserDetails.username,s);

                                adapter.notifyDataSetChanged();
                                setListViewHeightBasedOnChildren(lvword);
                                d.dismiss();
                            }
                        });
                        d.show();
                    } else {
                        Toast.makeText(notification_setting.this, "Filtering is OFF", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(notification_setting.this, "You can add max 5 words", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            l.clear();;
                adapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(lvword);
                db.clearword();

            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notification_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.show) {
            Intent i=new Intent(this,list_temp.class);
            startActivity(i);
        }
        return true;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}