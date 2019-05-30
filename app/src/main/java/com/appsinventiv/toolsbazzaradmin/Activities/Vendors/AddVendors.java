package com.appsinventiv.toolsbazzaradmin.Activities.Vendors;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appsinventiv.toolsbazzaradmin.Models.VendorModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddVendors extends AppCompatActivity {
    Button addVendor;
    EditText name, phone, address, email;
    DatabaseReference mDatabase;
    String vendorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendors);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        this.setTitle("Add Vendor");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        vendorId = getIntent().getStringExtra("vendorId");


        addVendor = findViewById(R.id.addVendor);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);

        if (vendorId != null) {
            getVendorFromDB();
        }

        addVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().length() == 0) {
                    name.setError("Enter vendor name");
                } else if (phone.getText().length() == 0) {
                    phone.setError("Enter phone");
                } else if (address.getText().length() == 0) {
                    address.setError("Enter address");
                } else if (email.getText().length() == 0) {
                    email.setError("Enter email");
                } else {
                    String key;
                    if (vendorId != null) {
                        key = vendorId;
                    } else {
                        key = mDatabase.push().getKey();
                    }

                    mDatabase.child("Vendors").child(key).setValue(new VendorModel(
                            key,
                            name.getText().toString(),
                            phone.getText().toString(),
                            address.getText().toString(),
                            System.currentTimeMillis(),
                            email.getText().toString(),
                            "yes"
                    )).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            CommonUtils.showToast("Vendor Added");
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            CommonUtils.showToast("Error " + e.getMessage());
                        }
                    });
                }

            }
        });


    }

    private void getVendorFromDB() {
        mDatabase.child("Vendors").child(vendorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    VendorModel model = dataSnapshot.getValue(VendorModel.class);
                    if (model != null) {
                        name.setText(model.getVendorName());
                        phone.setText(model.getVendorPhone());
                        email.setText(model.getVendorEmail());
                        address.setText(model.getVendorAddress());
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
