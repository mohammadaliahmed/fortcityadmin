package com.appsinventiv.toolsbazzaradmin.Activities.Locations;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.appsinventiv.toolsbazzaradmin.Activities.Products.AddProduct;
import com.appsinventiv.toolsbazzaradmin.Models.Product;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListOfCountries extends AppCompatActivity {
    ListOfCountriesAdapter adapter;
    RecyclerView recyclerView;
    DatabaseReference mDatabase;
    ArrayList<CountryModel> countriesList = new ArrayList<>();
    FloatingActionButton fab;
    public static boolean internationalShipping = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_countries);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("List of countries");
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        adapter = new ListOfCountriesAdapter(this, countriesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert();
//
            }
        });


        getCountriesFromDB();

    }

    private void showAlert() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ListOfCountries.this);
        builderSingle.setTitle("Select warranty type");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ListOfCountries.this, android.R.layout.simple_list_item_1);
        arrayAdapter.add("International Shipping country");
        arrayAdapter.add("Local Shipping country");


        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    internationalShipping = true;

                } else {
                    internationalShipping = false;

                }
                Intent i = new Intent(ListOfCountries.this, AddCountry.class);
                startActivity(i);

            }
        });
        builderSingle.show();
    }

    private void getCountriesFromDB() {
        mDatabase.child("Settings").child("Locations").child("Countries").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            CountryModel country = snapshot1.getValue(CountryModel.class);
                            if (country != null) {
                                countriesList.add(country);

                            }
                        }
                    }
                    Collections.sort(countriesList, new Comparator<CountryModel>() {
                        @Override
                        public int compare(CountryModel listData, CountryModel t1) {
                            String ob1 = listData.getCountryName();
                            String ob2 = t1.getCountryName();

                            return ob1.compareTo(ob2);

                        }
                    });
                    adapter.updateList(countriesList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.only_search_menu, menu);
        final MenuItem mSearch = menu.findItem(R.id.action_search);
//        mSearch.expandActionView();
        SearchView mSearchView = (SearchView) mSearch.getActionView();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.filter(newText);
//                    getUserCartProductsFromDB();

                return false;
            }
        });
        return true;
    }
}
