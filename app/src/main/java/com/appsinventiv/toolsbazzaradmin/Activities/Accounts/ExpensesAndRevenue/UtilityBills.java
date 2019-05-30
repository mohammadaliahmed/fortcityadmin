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

import com.appsinventiv.toolsbazzaradmin.Models.TransportationModel;
import com.appsinventiv.toolsbazzaradmin.Models.UtilityBillsModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UtilityBills extends AppCompatActivity {
    DatabaseReference mDatabase;
    EditText electricCityBill, waterBill, internetBill, staffInternetBill, officeTelephoneBill, staffMobileBill, governmentTax;
    Button save;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility_bills);
        this.setTitle("Utility Bills");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        Intent i =getIntent();
        path=i.getStringExtra("path");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        save = findViewById(R.id.save);
        electricCityBill = findViewById(R.id.electricCityBill);
        waterBill = findViewById(R.id.waterBill);
        internetBill = findViewById(R.id.internetBill);
        staffInternetBill = findViewById(R.id.staffInternetBill);
        officeTelephoneBill = findViewById(R.id.officeTelephoneBill);
        staffMobileBill = findViewById(R.id.staffMobileBill);
        governmentTax = findViewById(R.id.governmentTax);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (electricCityBill.getText().length() == 0) {
                    electricCityBill.setError("Enter value");
                } else if (waterBill.getText().length() == 0) {
                    waterBill.setError("Enter value");
                } else if (internetBill.getText().length() == 0) {
                    internetBill.setError("Enter value");
                } else if (staffInternetBill.getText().length() == 0) {
                    staffInternetBill.setError("Enter value");
                } else if (officeTelephoneBill.getText().length() == 0) {
                    officeTelephoneBill.setError("Enter value");
                } else if (staffMobileBill.getText().length() == 0) {
                    staffMobileBill.setError("Enter value");
                } else if (governmentTax.getText().length() == 0) {
                    governmentTax.setError("Enter value");
                } else {
                    sendDataToDb();
                }
            }
        });
        getDataFromDb(path);
    }

    private void getDataFromDb(String path) {
        mDatabase.child("Accounts").child("ExpensesAndRevenue").child(path).child("UtilityBills").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    UtilityBillsModel model = dataSnapshot.getValue(UtilityBillsModel.class);
                    if (model != null) {
                        electricCityBill.setText("" + model.getElectricCityBill());
                        waterBill.setText("" + model.getWaterBill());
                        staffInternetBill.setText("" + model.getStaffInternetBill());
                        internetBill.setText("" + model.getInternetBill());
                        officeTelephoneBill.setText("" + model.getOfficeTelephoneBill());
                        staffMobileBill.setText("" + model.getStaffMobileBill());
                        governmentTax.setText("" + model.getGovernmentTax());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendDataToDb() {
        long time = System.currentTimeMillis();
        mDatabase.child("Accounts")
                .child("ExpensesAndRevenue")
                .child(path)
                .child("UtilityBills").setValue(new UtilityBillsModel(
                Float.parseFloat(electricCityBill.getText().toString()),
                Float.parseFloat(waterBill.getText().toString()),
                Float.parseFloat(staffInternetBill.getText().toString()),
                Float.parseFloat(internetBill.getText().toString()),
                Float.parseFloat(officeTelephoneBill.getText().toString()),
                Float.parseFloat(staffMobileBill.getText().toString()),
                Float.parseFloat(governmentTax.getText().toString()),
                time,
                calculateTotal()

        )).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Updated");
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                CommonUtils.showToast(e.getMessage());
            }
        });
    }

    private float calculateTotal() {

        float total = 0;
        total = total + (Float.parseFloat(electricCityBill.getText().toString()) +
                Float.parseFloat(waterBill.getText().toString()) +
                Float.parseFloat(staffInternetBill.getText().toString()) +
                Float.parseFloat(internetBill.getText().toString()) +
                Float.parseFloat(officeTelephoneBill.getText().toString()) +
                Float.parseFloat(staffMobileBill.getText().toString()) +
                Float.parseFloat(governmentTax.getText().toString()));
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
