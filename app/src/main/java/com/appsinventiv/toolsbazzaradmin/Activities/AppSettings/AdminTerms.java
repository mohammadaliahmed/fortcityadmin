package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.appsinventiv.toolsbazzaradmin.Models.AdminTermsModel;
import com.appsinventiv.toolsbazzaradmin.Models.TermsModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminTerms extends AppCompatActivity {
    EditText e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e15, e16, e17, e18, e19, e20, e21, e22, e23, e24;
    DatabaseReference mDatabase;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_terms);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
//
        e1 = findViewById(R.id.e1);
        e2 = findViewById(R.id.e2);
        e3 = findViewById(R.id.e3);
        e4 = findViewById(R.id.e4);
        e5 = findViewById(R.id.e5);
        e6 = findViewById(R.id.e6);
        e7 = findViewById(R.id.e7);
        e8 = findViewById(R.id.e8);
        e9 = findViewById(R.id.e9);
        e10 = findViewById(R.id.e10);
        e11 = findViewById(R.id.e11);
        e12 = findViewById(R.id.e12);
        e13 = findViewById(R.id.e13);
        e15 = findViewById(R.id.e15);
        e16 = findViewById(R.id.e16);
        e17 = findViewById(R.id.e17);
        e18 = findViewById(R.id.e18);
        e19 = findViewById(R.id.e19);
        e20 = findViewById(R.id.e20);
        e21 = findViewById(R.id.e21);
        e22 = findViewById(R.id.e22);
        e23 = findViewById(R.id.e23);
        e24 = findViewById(R.id.e24);
        update = findViewById(R.id.update);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (e1.getText().length() == 0) {
                    e1.setError("Enter text");
                } else {
                    upload();
                }
            }
        });


        this.setTitle("Edit: Terms and Conditions");


        getTermsFromDb();


    }

    private void upload() {


        mDatabase.child("Settings").child("AdminTerms").setValue(new AdminTermsModel(
                e1.getText().toString(),
                e2.getText().toString(),
                e3.getText().toString(),
                e4.getText().toString(),
                e5.getText().toString(),
                e6.getText().toString(),
                e7.getText().toString(),
                e8.getText().toString(),
                e9.getText().toString(),
                e10.getText().toString(),
                e11.getText().toString(),
                e12.getText().toString(),
                e13.getText().toString(),
                e15.getText().toString(),
                e16.getText().toString(),
                e17.getText().toString(),
                e18.getText().toString(),
                e19.getText().toString(),
                e20.getText().toString(),
                e21.getText().toString(),
                e22.getText().toString(),
                e23.getText().toString(),
                e24.getText().toString()


        )).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Done");
            }
        });

    }

    private void getTermsFromDb() {
        mDatabase.child("Settings").child("AdminTerms").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    AdminTermsModel model = dataSnapshot.getValue(AdminTermsModel.class);
                    if (model != null) {
                        e1.setText(model.getTermsofofferandConditions());
                        e2.setText(model.getJobtitle());
                        e3.setText(model.getWorkingschedule());
                        e4.setText(model.getEmploymentRelationship());
                        e5.setText(model.getCashCompensation());
                        e6.setText(model.getSalary());
                        e7.setText(model.getTaxwithholdingEPFETF());
                        e8.setText(model.getTaxadviceEPFETF());
                        e9.setText(model.getBonusorcommissionpotential());
                        e10.setText(model.getEmployeebenefits());
                        e11.setText(model.getPrivacyandConfidentialityAgreements());
                        e12.setText(model.getConflictofInterestpolicy());
                        e13.setText(model.getTerminationConditions());
                        e15.setText(model.getInterpretationAmendmentandEnforcement());
                        e16.setText(model.getCookies());
                        e17.setText(model.getLicense());
                        e18.setText(model.getHyperlinkingtoourContent());
                        e19.setText(model.getiFrames());
                        e20.setText(model.getContentLiability());
                        e21.setText(model.getReservationofRights());
                        e22.setText(model.getRemovaloflinksfromourApplication());
                        e23.setText(model.getDisclaimer());
                        e24.setText(model.getOther());
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
