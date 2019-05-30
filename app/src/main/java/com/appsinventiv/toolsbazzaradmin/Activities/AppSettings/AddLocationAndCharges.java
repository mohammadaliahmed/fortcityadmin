package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appsinventiv.toolsbazzaradmin.Models.LocationAndChargesModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddLocationAndCharges extends AppCompatActivity {
    EditText country, city, currency, exchangeRate, deliveryCharges, shippingCharges;
    Button update;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location_and_charges);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        this.setTitle("Add Delivery");

        update = findViewById(R.id.update);
        country = findViewById(R.id.country);
        city = findViewById(R.id.city);
        currency = findViewById(R.id.currency);
        exchangeRate = findViewById(R.id.exchangeRate);
        deliveryCharges = findViewById(R.id.deliveryCharges);
        shippingCharges = findViewById(R.id.shippingCharges);


        mDatabase = FirebaseDatabase.getInstance().getReference();


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (country.getText().length() == 0) {
                    country.setError("Enter country");
                } else if (city.getText().length() == 0) {
                    city.setError("Enter city");
                } else if (currency.getText().length() == 0) {
                    currency.setError("Enter currency");
                } else if (exchangeRate.getText().length() == 0) {
                    exchangeRate.setError("Enter rate");
                } else if (deliveryCharges.getText().length() == 0) {
                    deliveryCharges.setError("Enter charges");
                } else if (shippingCharges.getText().length() == 0) {
                    shippingCharges.setError("Enter charges");
                } else {

                    List<String> container = new ArrayList<String>(Arrays.asList(city.getText().toString().split("\n")));
                    String id = mDatabase.push().getKey();
                    mDatabase.child("Settings").child("DeliveryCharges").child(id)
                            .setValue(new LocationAndChargesModel(
                                    id,
                                    country.getText().toString(),
                                    container,
                                    currency.getText().toString(),
                                    Float.parseFloat(exchangeRate.getText().toString()),
                                    Float.parseFloat(deliveryCharges.getText().toString()),
                                    Float.parseFloat(shippingCharges.getText().toString()),
                                    System.currentTimeMillis()
                            )).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            CommonUtils.showToast("Error: " + e.getMessage());
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            CommonUtils.showToast("Delivery charges added");
                            finish();
                        }
                    });
                }
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
