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

import com.appsinventiv.toolsbazzaradmin.Models.StationariesModel;
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

public class Stationaries extends AppCompatActivity {
    DatabaseReference mDatabase;
    EditText officeStationaries, staffStationaries, advertising, billBooksPrint, packingMaterial, businessCardPrint;
    Button save;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stationaries);

        this.setTitle("Stationaries");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        Intent i =getIntent();
        path=i.getStringExtra("path");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        save = findViewById(R.id.save);
        officeStationaries = findViewById(R.id.officeStationaries);
        staffStationaries = findViewById(R.id.staffStationaries);
        advertising = findViewById(R.id.advertising);
        billBooksPrint = findViewById(R.id.billBooksPrint);
        packingMaterial = findViewById(R.id.packingMaterial);
        businessCardPrint = findViewById(R.id.businessCardPrint);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (officeStationaries.getText().length() == 0) {
                    officeStationaries.setError("Enter value");
                } else if (staffStationaries.getText().length() == 0) {
                    staffStationaries.setError("Enter value");
                } else if (advertising.getText().length() == 0) {
                    advertising.setError("Enter value");
                } else if (billBooksPrint.getText().length() == 0) {
                    billBooksPrint.setError("Enter value");
                } else if (packingMaterial.getText().length() == 0) {
                    packingMaterial.setError("Enter value");
                } else if (businessCardPrint.getText().length() == 0) {
                    businessCardPrint.setError("Enter value");
                } else {
                    sendDataToDb();
                }
            }
        });
        getDataFromDb(path);
    }

    private void getDataFromDb(String path) {
        mDatabase.child("Accounts").child("ExpensesAndRevenue").child(path).child("Stationaries").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    StationariesModel model = dataSnapshot.getValue(StationariesModel.class);
                    if (model != null) {
                        officeStationaries.setText("" + model.getOfficeStationaries());
                        staffStationaries.setText("" + model.getStaffStationaries());
                        advertising.setText("" + model.getAdvertising());
                        billBooksPrint.setText("" + model.getBillBooksPrint());
                        packingMaterial.setText("" + model.getPackingMaterial());
                        businessCardPrint.setText("" + model.getBusinessCardPrint());

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
                .child("Stationaries").setValue(new StationariesModel(
                Float.parseFloat(officeStationaries.getText().toString()),
                Float.parseFloat(staffStationaries.getText().toString()),
                Float.parseFloat(advertising.getText().toString()),
                Float.parseFloat(billBooksPrint.getText().toString()),
                Float.parseFloat(packingMaterial.getText().toString()),
                Float.parseFloat(businessCardPrint.getText().toString()),
                time,
                calcuclateTotal()

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

    private float calcuclateTotal() {
        float total = 0;
        total = total + (Float.parseFloat(officeStationaries.getText().toString()) +
                Float.parseFloat(staffStationaries.getText().toString()) +
                Float.parseFloat(advertising.getText().toString()) +
                Float.parseFloat(billBooksPrint.getText().toString()) +
                Float.parseFloat(packingMaterial.getText().toString()) +
                Float.parseFloat(businessCardPrint.getText().toString()));
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
