package com.appsinventiv.toolsbazzaradmin.Activities.Accounts.ExpensesAndRevenue;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appsinventiv.toolsbazzaradmin.Models.RentModel;
import com.appsinventiv.toolsbazzaradmin.Models.TransportationModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Rent extends AppCompatActivity {
    DatabaseReference mDatabase;
    EditText rentACar, officeRent;
    Button save;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);
        this.setTitle("Rent");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        Intent i =getIntent();
        path=i.getStringExtra("path");
        save = findViewById(R.id.save);
        rentACar = findViewById(R.id.rentACart
        );
        officeRent = findViewById(R.id.officeRent);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (officeRent.getText().length() == 0) {
                    officeRent.setError("Enter value");
                } else if (rentACar.getText().length() == 0) {
                    rentACar.setError("Enter value");
                } else {
                    sendDataToDB();
                }
            }
        });
        getDataFromDb(path);
    }

    private void getDataFromDb(String path) {
        mDatabase.child("Accounts").child("ExpensesAndRevenue").child(path).child("Rent").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    RentModel model = dataSnapshot.getValue(RentModel.class);
                    if (model != null) {
                        officeRent.setText("" + model.getOfficeRent());
                        rentACar.setText("" + model.getRentACar());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendDataToDB() {
        long time = System.currentTimeMillis();
        mDatabase.child("Accounts").child("ExpensesAndRevenue")
                .child(path)
                .child("Rent").setValue(new RentModel(
                Float.parseFloat(officeRent.getText().toString()),
                Float.parseFloat(rentACar.getText().toString()),
                time, calculateTotal()
        )).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                CommonUtils.showToast(e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Updated");
                finish();
            }
        });
    }

    private float calculateTotal() {

        float total = 0;
        total = total + (Float.parseFloat(officeRent.getText().toString()) +
                Float.parseFloat(rentACar.getText().toString()));
        return total;
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
