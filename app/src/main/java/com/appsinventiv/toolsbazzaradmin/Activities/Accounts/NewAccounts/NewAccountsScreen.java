package com.appsinventiv.toolsbazzaradmin.Activities.Accounts.NewAccounts;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.appsinventiv.toolsbazzaradmin.Activities.Accounts.Accounts;
import com.appsinventiv.toolsbazzaradmin.Activities.Orders.NewOrder.NewOrderFragmentAdapter;
import com.appsinventiv.toolsbazzaradmin.Activities.Orders.Orders;
import com.appsinventiv.toolsbazzaradmin.R;

import java.util.ArrayList;

public class NewAccountsScreen extends AppCompatActivity {

    ArrayList<String> orderStatusList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_accounts_screen);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Accounts");
        ViewPager viewPager = findViewById(R.id.viewpager);
//        orderStatusList.add("Pending PO");
        orderStatusList.add("Pending SO");
        NewAccountsFragmentAdapter adapter = new NewAccountsFragmentAdapter(this, orderStatusList, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }


    @Override
    public void onBackPressed() {

        finish();
    }


}
