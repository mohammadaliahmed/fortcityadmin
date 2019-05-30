package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appsinventiv.toolsbazzaradmin.Models.CompanyDetailsModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CompanySettings extends AppCompatActivity {
    EditText name, address, telephone, phone, email;
    Button update;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_settings);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        this.setTitle("Edit company details");
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        telephone = findViewById(R.id.telephone);
        phone = findViewById(R.id.mobile);
        update = findViewById(R.id.update);
        email = findViewById(R.id.email);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getDataFromDb();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().length() == 0) {
                    name.setError("Cannot be null");
                } else if (address.getText().length() == 0) {
                    address.setError("Cannot be null");

                } else if (telephone.getText().length() == 0) {
                    telephone.setError("Cannot be null");

                } else if (phone.getText().length() == 0) {
                    phone.setError("Cannot be null");

                } else if (email.getText().length() == 0) {
                    email.setError("Cannot be null");

                } else {
                    sendDataToServer();
                }
            }
        });
    }

    private void getDataFromDb() {
        mDatabase.child("Settings").child("CompanyDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    CompanyDetailsModel model = dataSnapshot.getValue(CompanyDetailsModel.class);
                    if (model != null) {
                        name.setText(model.getName());
                        address.setText(model.getAddress());
                        telephone.setText(model.getTelephone());
                        phone.setText(model.getPhone());
                        email.setText(model.getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendDataToServer() {
        mDatabase.child("Settings").child("CompanyDetails").setValue(new CompanyDetailsModel(
                name.getText().toString(),
                address.getText().toString(),
                telephone.getText().toString(),
                phone.getText().toString(),
                email.getText().toString()

        )).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                CommonUtils.showToast("Error: " + e.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
