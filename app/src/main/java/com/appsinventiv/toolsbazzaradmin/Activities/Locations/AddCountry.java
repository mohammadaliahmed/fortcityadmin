package com.appsinventiv.toolsbazzaradmin.Activities.Locations;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AddCountry extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText data;
    Button create;
    DatabaseReference mDatabase;
    String country;
    String provincesList = "";
    AddCountryAdapter adapter;

    ImageView pickFlag, flag;
    StorageReference mStorageRef;
    private static final int REQUEST_CODE_CHOOSE = 23;
    List<Uri> mSelected = new ArrayList<>();
    ArrayList<CountryModel> countryList = new ArrayList<>();
    String contriList = "";
    HashMap<String, CountryModel> provincesMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_country);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        recyclerView = findViewById(R.id.recyclerView);
        data = findViewById(R.id.data);

        create = findViewById(R.id.create);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.setTitle("Add Country ");
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new AddCountryAdapter(this, countryList);
        recyclerView.setAdapter(adapter);


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.getText().length() == 0) {
                    data.setError("Enter data");
                } else {
                    sendDataToDB();
                }
            }
        });


        getDataFromDB();


    }

    private void getDataFromDB() {
        mDatabase.child("Settings").child("Locations").child("Countries").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    countryList.clear();
                    contriList = "";
                    if (ListOfCountries.internationalShipping) {
                        dataSnapshot = dataSnapshot.child("international");
                    } else {
                        dataSnapshot = dataSnapshot.child("local");

                    }
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        CountryModel model = snapshot.getValue(CountryModel.class);
                        if (model != null) {

                            countryList.add(model);
                            adapter.notifyDataSetChanged();
                            provincesMap.put(model.getCountryName(), model);
                            contriList = contriList + model.getCountryName() + "," + model.getCurrencySymbol() + "," + model.getMobileCode() + "," + model.getCurrencyRate() + "\n";


                        }
                        data.setText(contriList);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void sendDataToDB() {
        List<String> a = new ArrayList<>();
        HashMap<String, CountryModel> map = new HashMap<>();
        ArrayList<CityDeliveryChargesModel> list = new ArrayList<>();
        List<String> dataList = new ArrayList<String>(Arrays.asList(data.getText().toString().split("\n")));
        for (String abc : dataList) {
            List<String> countryListInside = new ArrayList<String>(Arrays.asList(abc.split(",")));
//            list.add(new CityDeliveryChargesModel(citiesListInside.get(0), citiesListInside.get(1), citiesListInside.get(2)));
            map.put(countryListInside.get(0),
                    new CountryModel(
                            countryListInside.get(0),
                            countryListInside.get(1).replace(" ", ""),
                            Float.parseFloat(countryListInside.get(3).replace(" ", "")),
                            countryListInside.get(2),
                            provincesMap.get(countryListInside.get(0)) == null ? a : provincesMap.get(countryListInside.get(0)).getProvinces(),
                            provincesMap.get(countryListInside.get(0)) == null ? "" : provincesMap.get(countryListInside.get(0)).getImageUrl(),
                            ListOfCountries.internationalShipping


                    ));

        }

        mDatabase.child("Settings").child("Locations").child("Countries").child(ListOfCountries.internationalShipping ? "international" : "local")
                .setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Countries added");
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
