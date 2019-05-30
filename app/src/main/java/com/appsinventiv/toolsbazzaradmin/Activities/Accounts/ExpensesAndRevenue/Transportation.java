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
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Transportation extends AppCompatActivity {
    DatabaseReference mDatabase;
    EditText officeTransportation, staffTransportation, officeFuel, staffFuel, shipping, delivery;
    Button save;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation);
        this.setTitle("Transportation");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        Intent i =getIntent();
        path=i.getStringExtra("path");
        officeTransportation = findViewById(R.id.officeTransportation);
        staffTransportation = findViewById(R.id.staffTransportation);
        officeFuel = findViewById(R.id.officeFuel);
        staffFuel = findViewById(R.id.staffFuel);
        shipping = findViewById(R.id.shipping);
        delivery = findViewById(R.id.delivery);

        save = findViewById(R.id.save);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getDataFromDb(path);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (officeTransportation.getText().length() == 0) {
                    officeTransportation.setError("Enter value");
                } else if (staffTransportation.getText().length() == 0) {
                    staffTransportation.setError("Enter value");
                } else if (officeFuel.getText().length() == 0) {
                    officeFuel.setError("Enter value");
                } else if (staffFuel.getText().length() == 0) {
                    staffFuel.setError("Enter value");
                } else if (shipping.getText().length() == 0) {
                    shipping.setError("Enter value");
                } else if (delivery.getText().length() == 0) {
                    delivery.setError("Enter value");
                } else {
                    sendDataToDb();
                }
            }
        });


    }

    private void getDataFromDb(String path) {
        mDatabase.child("Accounts").child("ExpensesAndRevenue").child(path).child("Transportation").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    TransportationModel model = dataSnapshot.getValue(TransportationModel.class);
                    if (model != null) {
                        officeTransportation.setText("" + model.getOfficeTransportation());
                        staffTransportation.setText("" + model.getStaffTransportation());
                        officeFuel.setText("" + model.getOfficeFuel());
                        staffFuel.setText("" + model.getStaffFuel());
                        shipping.setText("" + model.getShipping());
                        delivery.setText("" + model.getDelivery());
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
        mDatabase.child("Accounts").child("ExpensesAndRevenue")
                .child(path)
                .child("Transportation").setValue(new TransportationModel(
                Float.parseFloat(officeTransportation.getText().toString()),
                Float.parseFloat(staffTransportation.getText().toString()),
                Float.parseFloat(officeFuel.getText().toString()),
                Float.parseFloat(staffFuel.getText().toString()),
                Float.parseFloat(shipping.getText().toString()),
                Float.parseFloat(delivery.getText().toString()),
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
        total = total + (Float.parseFloat(officeTransportation.getText().toString()) +
                Float.parseFloat(staffTransportation.getText().toString()) +
                Float.parseFloat(officeFuel.getText().toString()) +
                Float.parseFloat(staffFuel.getText().toString()) +
                Float.parseFloat(shipping.getText().toString()) +
                Float.parseFloat(delivery.getText().toString()));
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
