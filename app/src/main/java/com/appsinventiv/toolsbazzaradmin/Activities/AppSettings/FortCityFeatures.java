package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class FortCityFeatures extends AppCompatActivity {
    EditText promoCode, easyCheckout, cashOnDelivery, onlinePayments, paypalOptions;

    Button update;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forcity_features);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        promoCode = findViewById(R.id.promoCode);
        easyCheckout = findViewById(R.id.easyCheckout);
        cashOnDelivery = findViewById(R.id.cashOnDelivery);
        onlinePayments = findViewById(R.id.onlinePayments);
        paypalOptions = findViewById(R.id.paypalOptions);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Fort City features");
        update = findViewById(R.id.update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (promoCode.getText().length() == 0) {
                    promoCode.setError("Enter text");
                } else if (easyCheckout.getText().length() == 0) {
                    easyCheckout.setError("Enter text");
                } else if (cashOnDelivery.getText().length() == 0) {
                    cashOnDelivery.setError("Enter text");
                } else if (onlinePayments.getText().length() == 0) {
                    onlinePayments.setError("Enter text");
                } else if (paypalOptions.getText().length() == 0) {
                    paypalOptions.setError("Enter text");
                } else {
                    sendDataTodb();
                }
            }
        });


        getDataFromDb();


    }

    private void sendDataTodb() {
        FortcityFeaturesModel model = new FortcityFeaturesModel(
                promoCode.getText().toString(),
                easyCheckout.getText().toString(),
                cashOnDelivery.getText().toString(),
                onlinePayments.getText().toString(),
                paypalOptions.getText().toString()

        );
        mDatabase.child("Settings").child("FortCityFeatures").setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Updated");
            }
        });
    }

    private void getDataFromDb() {
        mDatabase.child("Settings").child("FortCityFeatures").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    FortcityFeaturesModel model = dataSnapshot.getValue(FortcityFeaturesModel.class);
                    if (model != null) {
                        promoCode.setText(model.getPromoCode());
                        easyCheckout.setText(model.getEasyCheckout());
                        cashOnDelivery.setText(model.getCashOnDelivery());
                        onlinePayments.setText(model.getOnlinePayments());
                        paypalOptions.setText(model.getPaypalOptions());
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
