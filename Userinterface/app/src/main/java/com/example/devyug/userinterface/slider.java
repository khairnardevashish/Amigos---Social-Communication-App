package com.example.devyug.userinterface;



        import android.app.NotificationManager;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.design.widget.TabLayout;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentPagerAdapter;
        import android.support.v4.view.ViewPager;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.LinearLayout;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.messaging.FirebaseMessaging;

        import java.util.ArrayList;
        import java.util.List;



public class slider extends AppCompatActivity {

    private  LinearLayout ll;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slider_ui);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons()
    {
        int[] tabIcons = {
                R.drawable.conct,
                R.drawable.indi,
                R.drawable.collaboration,
                R.drawable.conct

        };
        //tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(0).setIcon(tabIcons[1]);
        tabLayout.getTabAt(1).setIcon(tabIcons[2]);
        tabLayout.getTabAt(2).setIcon(tabIcons[3]);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //adapter.addFragment(new individual(),"");
        adapter.addFragment(new profile(), "");
        adapter.addFragment(new group(), "");
        adapter.addFragment(new contacts(), "");


        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();





        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        android.app.FragmentManager fm=getFragmentManager();
        android.app.FragmentTransaction ft=fm.beginTransaction();
        //FrameLayout contentView = (FrameLayout) getActivity().findViewById(R.id.content_view);
        // main=(RelativeLayout)findViewById(R.id.main);
       // ll=(LinearLayout)findViewById(R.id.ll);
        switch(item.getItemId())
        {
            case R.id.view_profile:
                if(item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                Intent i1=new Intent(slider.this,profile_settings.class);
                startActivity(i1);
                //main.setBackgroundColor(Color.RED);
                return true;

            case R.id.notification_settings:
                if(item.isChecked())
                    item.setChecked(false);
                else
                     item.setChecked(true);
                     Intent i2=new Intent(slider.this,notification_setting.class);
                     startActivity(i2);

                //appsetting fragment=new appsetting();
              //  aps= (appsetting)
                      //  getSupportFragmentManager().findFragmentById(R.id.ll);
                // FragAV fav = new FragAV();
                //.replace(R.id.ll,aps);
                ft.commit();
                //  main.setBackgroundColor(Color.GREEN);
                return true;

            case R.id.logout:
                if(item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                FirebaseMessaging.getInstance().unsubscribeFromTopic("use_group");
                FirebaseMessaging.getInstance().unsubscribeFromTopic("use_"+UserDetails.username);
                    FirebaseAuth.getInstance().signOut();
                    Intent i3=new Intent(slider.this,login.class);
                    startActivity(i3);


                //main.setBackgroundColor(Color.BLUE);
                return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with apps
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}