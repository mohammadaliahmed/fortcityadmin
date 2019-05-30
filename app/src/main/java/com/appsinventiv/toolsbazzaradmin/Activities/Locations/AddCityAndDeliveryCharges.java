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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.List;

public class AddCityAndDeliveryCharges extends AppCompatActivity {
    RecyclerView recyclerview;
    DatabaseReference mDatabase;
    AddCityAdapter adapter;
    private ArrayList<CityDeliveryChargesModel> itemList = new ArrayList<>();
    String district;
    String citiesList = "";
    RadioGroup radioGroup;
    RadioButton selected;
    Button create;
    EditText cities;
    TextView districtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city_and_delivery_charges);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        this.setTitle("City and delivery charges");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        radioGroup = findViewById(R.id.radioGroup);
        districtName = findViewById(R.id.district);

        adapter = new AddCityAdapter(this, itemList);
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(adapter);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        selected = findViewById(selectedId);
        cities = findViewById(R.id.cities);


        create = findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cities.getText().length() == 0) {
                    cities.setError("Enter cities");
                } else {
                    sendDataToDb();
                }
            }
        });


        district = getIntent().getStringExtra("district");
        districtName.setText("District: "+district);
        if (district != null) {
            getDataFromDB();
        }
    }

    private void sendDataToDb() {
        HashMap<String, CityDeliveryChargesModel> map = new HashMap<>();
        ArrayList<CityDeliveryChargesModel> list = new ArrayList<>();
        List<String> citiesList = new ArrayList<String>(Arrays.asList(cities.getText().toString().split("\n")));
        for (String abc : citiesList) {
            List<String> citiesListInside = new ArrayList<String>(Arrays.asList(abc.split(",")));
//            list.add(new CityDeliveryChargesModel(citiesListInside.get(0), citiesListInside.get(1), citiesListInside.get(2)));
            map.put(citiesListInside.get(0),
                    new CityDeliveryChargesModel(citiesListInside.get(0),
                            citiesListInside.get(1), citiesListInside.get(2)));

        }

        mDatabase.child("Settings").child("Locations").child("Cities").child(district)
                .setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Cities added");
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getDataFromDB() {
        mDatabase.child("Settings").child("Locations").child("Cities").child(district).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        CityDeliveryChargesModel model=snapshot.getValue(CityDeliveryChargesModel.class);
                        if(model!=null){
                            itemList.add(model);
                            adapter.notifyDataSetChanged();
                            citiesList=citiesList+model.getCityName()+","+model.getOneKg()+","+model.getHalfKg()+"\n";
                            cities.setText(citiesList);
                        }
                    }
                }
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
