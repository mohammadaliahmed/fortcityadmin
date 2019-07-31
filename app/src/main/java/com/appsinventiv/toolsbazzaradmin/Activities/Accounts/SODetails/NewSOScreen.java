package com.appsinventiv.toolsbazzaradmin.Activities.Accounts.SODetails;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.appsinventiv.toolsbazzaradmin.Activities.Accounts.NewAccounts.NewAccountsFragmentAdapter;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.Constants;

import java.util.ArrayList;

public class NewSOScreen extends AppCompatActivity {

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
        this.setTitle(Constants.SHIPPING_COMPANY + " SO");
        ViewPager viewPager = findViewById(R.id.viewpager);
//        orderStatusList.add("Pending PO");
        orderStatusList.add("Pending SO");
        NewSOFragmentAdapter adapter = new NewSOFragmentAdapter(this, orderStatusList, getSupportFragmentManager());
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
