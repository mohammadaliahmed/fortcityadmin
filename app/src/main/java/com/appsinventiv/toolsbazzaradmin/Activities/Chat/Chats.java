package com.appsinventiv.toolsbazzaradmin.Activities.Chat;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Adapters.ChatListAdapter;
import com.appsinventiv.toolsbazzaradmin.Adapters.OrdersFragmentAdapter;
import com.appsinventiv.toolsbazzaradmin.Interfaces.TabCountCallbacks;
import com.appsinventiv.toolsbazzaradmin.Models.ChatModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Chats extends AppCompatActivity {

    ArrayList<String> orderStatusList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        getSupportActionBar().setElevation(0);
        this.setTitle("Chats");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }

        this.setTitle("Chats");
        ViewPager viewPager = findViewById(R.id.viewpager);
        orderStatusList.add("Wholesale");
        orderStatusList.add("Client");
        orderStatusList.add("Seller");
        viewPager.setOffscreenPageLimit(4);

        ChatFragmentAdapter adapter = new ChatFragmentAdapter(this, orderStatusList, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        LinearLayout tabone = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        final TextView countt = (TextView) tabone.findViewById(R.id.txtview);
        TextView title = (TextView) tabone.findViewById(R.id.tabTitle);
        title.setText("Wholesale");
        countt.setText("1");

        LinearLayout tabtwo = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);

        final TextView counttt = (TextView) tabtwo.findViewById(R.id.txtview);
        TextView titlee = (TextView) tabtwo.findViewById(R.id.tabTitle);
        titlee.setText("Client");
        counttt.setText("1");

        LinearLayout tabthree = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);

        final TextView countttt = (TextView) tabthree.findViewById(R.id.txtview);
        TextView titleee = (TextView) tabthree.findViewById(R.id.tabTitle);
        titleee.setText("Seller");
        countttt.setText("1");


        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.getTabAt(0).setCustomView(tabone);
        tabLayout.getTabAt(1).setCustomView(tabtwo);
        tabLayout.getTabAt(2).setCustomView(tabthree);

        adapter.setListener(new TabCountCallbacks() {
            @Override
            public void newCount(int count, int position) {
                if (position == 0) {
                    if (count > 0) {
                        countt.setVisibility(View.VISIBLE);
                        countt.setText("" + count);
                    } else {
                        countt.setVisibility(View.GONE);

                    }
                } else if (position == 1) {
                    if (count > 0) {
                        counttt.setVisibility(View.VISIBLE);
                        counttt.setText("" + count);
                    } else {
                        counttt.setVisibility(View.GONE);

                    }
                } else if (position == 2) {
                    if (count > 0) {
                        countttt.setVisibility(View.VISIBLE);
                        countttt.setText("" + count);
                    } else {
                        countttt.setVisibility(View.GONE);

                    }
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
