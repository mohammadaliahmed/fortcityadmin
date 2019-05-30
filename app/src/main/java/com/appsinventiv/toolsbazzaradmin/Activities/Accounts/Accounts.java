package com.appsinventiv.toolsbazzaradmin.Activities.Accounts;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.appsinventiv.toolsbazzaradmin.Activities.Callbacks.WhichKey;
import com.appsinventiv.toolsbazzaradmin.Activities.MainPage.MainActivity;
import com.appsinventiv.toolsbazzaradmin.Adapters.AccountsFragmentAdapter;
import com.appsinventiv.toolsbazzaradmin.R;

import java.util.ArrayList;

public class Accounts extends AppCompatActivity implements WhichKey{
    ArrayList<String> accountsList = new ArrayList<>();
    int id;
    Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        getSupportActionBar().setElevation(0);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        this.setTitle("Accounts");
        ViewPager viewPager = findViewById(R.id.viewpager);
        accountsList.add("Pending PO");
        accountsList.add("Pending SO");
        accountsList.add("Sales");
        accountsList.add("Purchase");
        accountsList.add("Expenses & Revenue");
        AccountsFragmentAdapter adapter = new AccountsFragmentAdapter(this, accountsList, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorRed));
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Accounts.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(Accounts.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void which(String key) {
        MenuItem menuItem = menu.findItem(R.id.which);
        menuItem.setTitle(key);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.accounts_menu, menu);
        this.menu=menu;
        return true;
    }

}
