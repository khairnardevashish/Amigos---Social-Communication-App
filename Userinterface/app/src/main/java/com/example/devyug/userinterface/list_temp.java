package com.example.devyug.userinterface;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by dell on 2/10/17.
 */

public class list_temp extends AppCompatActivity{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        userdatabase u=new userdatabase(this);
        List<String> a=new ArrayList<>();
        a=u.getwords();
        ListIterator<String > i=a.listIterator();
        LinearLayout l=(LinearLayout)findViewById(R.id.LL);
        while(i.hasNext())
        {
            TextView v=new TextView(this);
            v.setText(i.next());
            l.addView(v);

        }
    }

    @Override
    protected void onStop() {

        super.onStop();
    }
}
