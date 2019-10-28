package com.appsinventiv.toolsbazzaradmin.Activities.Orders;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.appsinventiv.toolsbazzaradmin.Activities.MainPage.MainActivity;
import com.appsinventiv.toolsbazzaradmin.Adapters.OrdersFragmentAdapter;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.Constants;

import java.util.ArrayList;

public class Orders extends AppCompatActivity {
    ArrayList<String> orderStatusList = new ArrayList<>();

    String storeUsername, storename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().setElevation(0);
        setContentView(R.layout.activity_orders);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        storeUsername = getIntent().getStringExtra("username");
        storename = getIntent().getStringExtra("storename");

        this.setTitle(storename + " Orders");
        ViewPager viewPager = findViewById(R.id.viewpager);
        orderStatusList.add("Pending");
        orderStatusList.add("Under Process");
        orderStatusList.add("Invoice");
        orderStatusList.add("Cancelled");
        OrdersFragmentAdapter adapter = new OrdersFragmentAdapter(this, orderStatusList,
                getSupportFragmentManager(), storeUsername);
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setCurrentItem(Constants.TAB);



    }

    @Override
    public void onBackPressed() {

        finish();
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
