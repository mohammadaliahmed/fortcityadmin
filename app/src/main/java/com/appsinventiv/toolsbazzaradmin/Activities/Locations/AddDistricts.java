package com.appsinventiv.toolsbazzaradmin.Activities.Locations;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddDistricts extends AppCompatActivity {
    EditText districts;
    RecyclerView recyclerView;
    Button create, update;
    DatabaseReference mDatabase;
    String province;
    String districtList = "";
    ArrayList<String> itemList = new ArrayList<>();
    AddDistrictAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_districts);
        this.setTitle("Add Country");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        recyclerView = findViewById(R.id.recyclerView);
        districts = findViewById(R.id.districts);

        create = findViewById(R.id.create);
        update = findViewById(R.id.update);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        province = getIntent().getStringExtra("province");
        this.setTitle("Province: " + province);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new AddDistrictAdapter(this, itemList);
        recyclerView.setAdapter(adapter);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (districts.getText().length() == 0) {
                    districts.setError("Enter data");
                } else {
                    sendDataToDB();
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (province != null) {
            getDataFromDB();
        }

    }

    private void getDataFromDB() {
        mDatabase.child("Settings").child("Locations").child("Districts").child(province).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    districtList = "";
//                    CountryModel model = dataSnapshot.getValue(CountryModel.class);
//                    if (model != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String value = snapshot.getValue(String.class);
                        itemList.add(value);

                        districtList = districtList + value + "\n";
                        districts.setText(districtList);
                    }
                    adapter.notifyDataSetChanged();

//                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendDataToDB() {
        List<String> provincesList = new ArrayList<String>(Arrays.asList(districts.getText().toString().split("\n")));

        mDatabase.child("Settings").child("Locations").child("Districts").child(province)
                .setValue(provincesList).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Districts added");
//                finish();
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
