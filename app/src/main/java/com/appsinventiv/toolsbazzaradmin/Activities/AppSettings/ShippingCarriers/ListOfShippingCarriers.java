package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings.ShippingCarriers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.appsinventiv.toolsbazzaradmin.Activities.AppSettings.AddLocationAndCharges;
import com.appsinventiv.toolsbazzaradmin.Adapters.CountriesListAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.LocationAndChargesModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListOfShippingCarriers extends AppCompatActivity {
    DatabaseReference mDatabase;
    ArrayList<ShippingCompanyModel> itemList = new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ShippingCompaniesAdapter adapter;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_location_and_charges);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Shipping Carrier");
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ListOfShippingCarriers.this, AddShippingCarriers.class);
                startActivity(i);
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ShippingCompaniesAdapter(this, itemList);

        recyclerView.setAdapter(adapter);


        getDataFromDb();

    }

    private void getDataFromDb() {
        mDatabase.child("Settings").child("ShippingCompanies").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    ShippingCompanyModel model = dataSnapshot.getValue(ShippingCompanyModel.class);
                    if (model != null) {
                        itemList.add(model);
                        adapter.notifyDataSetChanged();
                    }
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
