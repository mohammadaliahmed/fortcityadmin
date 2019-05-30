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

import com.appsinventiv.toolsbazzaradmin.Models.MiscellaneousModel;
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

public class Miscellaneous extends AppCompatActivity {
    DatabaseReference mDatabase;
    EditText remarks1, remarks2, remarks3, remarks4, remarks5, cost1, cost2, cost3, cost4, cost5;
    Button save;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miscellaneous);
        this.setTitle("Miscellaneous");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        Intent i =getIntent();
        path=i.getStringExtra("path");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        save = findViewById(R.id.save);
        remarks1 = findViewById(R.id.remarks1);
        remarks2 = findViewById(R.id.remarks2);
        remarks3 = findViewById(R.id.remarks3);
        remarks4 = findViewById(R.id.remarks4);
        remarks5 = findViewById(R.id.remarks5);
        cost1 = findViewById(R.id.cost1);
        cost2 = findViewById(R.id.cost2);
        cost3 = findViewById(R.id.cost3);
        cost4 = findViewById(R.id.cost4);
        cost5 = findViewById(R.id.cost5);

        getDataFromDb(path);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long time = System.currentTimeMillis();
                mDatabase.child("Accounts").child("ExpensesAndRevenue")
                        .child(path)
                        .child("Miscellaneous").setValue(new MiscellaneousModel(
                        remarks1.getText().toString() + " ",
                        remarks2.getText().toString() + " ",
                        remarks3.getText().toString() + " ",
                        remarks4.getText().toString() + " ",
                        remarks5.getText().toString() + " ",
                        Float.parseFloat(cost1.getText().toString().length() > 0 ? cost1.getText().toString() : 0 + ""),
                        Float.parseFloat(cost2.getText().toString().length() > 0 ? cost2.getText().toString() : 0 + ""),
                        Float.parseFloat(cost3.getText().toString().length() > 0 ? cost3.getText().toString() : 0 + ""),
                        Float.parseFloat(cost4.getText().toString().length() > 0 ? cost4.getText().toString() : 0 + ""),
                        Float.parseFloat(cost5.getText().toString().length() > 0 ? cost5.getText().toString() : 0 + ""),
                        time,
                        calcualteTotal()


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
        });

    }

    private void getDataFromDb(String path) {
        mDatabase.child("Accounts").child("ExpensesAndRevenue").child(path).child("Miscellaneous").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    MiscellaneousModel model = dataSnapshot.getValue(MiscellaneousModel.class);
                    if (model != null) {
                        remarks1.setText("" + model.getRemarks1());
                        remarks2.setText("" + model.getRemarks2());
                        remarks3.setText("" + model.getRemarks3());
                        remarks4.setText("" + model.getRemarks4());
                        remarks5.setText("" + model.getRemarks5());
                        cost1.setText("" + model.getCost1());
                        cost2.setText("" + model.getCost2());
                        cost3.setText("" + model.getCost3());
                        cost4.setText("" + model.getCost4());
                        cost5.setText("" + model.getCost5());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private float calcualteTotal() {
        float total = 0;
        total = total + (
                Float.parseFloat(cost1.getText().toString().length() > 0 ? cost1.getText().toString() : 0 + "")
                        + Float.parseFloat(cost2.getText().toString().length() > 0 ? cost2.getText().toString() : 0 + "")
                        + Float.parseFloat(cost3.getText().toString().length() > 0 ? cost3.getText().toString() : 0 + "")
                        + Float.parseFloat(cost4.getText().toString().length() > 0 ? cost4.getText().toString() : 0 + "")
                        + Float.parseFloat(cost5.getText().toString().length() > 0 ? cost5.getText().toString() : 0 + ""));

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
