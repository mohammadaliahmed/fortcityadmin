package com.appsinventiv.toolsbazzaradmin.Activities.Employees;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.appsinventiv.toolsbazzaradmin.Adapters.EmployeesListAdapter;
import com.appsinventiv.toolsbazzaradmin.Adapters.OrdersFragmentAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.Employee;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.SwipeControllerActions;
import com.appsinventiv.toolsbazzaradmin.Utils.SwipeToDeleteCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListOfEmployees extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_employees);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Employees");

        ViewPager viewPager = findViewById(R.id.viewpager);
        ArrayList<String> orderStatusList=new ArrayList<>();
        orderStatusList.add("All");
        orderStatusList.add("Admin");
        orderStatusList.add("Accountant");
        orderStatusList.add("Sales & Marketing");
        orderStatusList.add("Customer care");
        orderStatusList.add("Operations");
        orderStatusList.add("Procument");
        EmployeeFragmentAdapter adapter = new EmployeeFragmentAdapter(this, orderStatusList, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
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
