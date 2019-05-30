package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Terms extends AppCompatActivity {
    EditText terms, cookies, license, hyperlink, iframes, contentLiability, reservation, removal, disclaimer, replacement, other;
    ;
    Button update;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        this.setTitle("Edit: Terms and Conditions");


        update = findViewById(R.id.update);

        terms = findViewById(R.id.terms);
        cookies = findViewById(R.id.cookies);
        license = findViewById(R.id.license);
        hyperlink = findViewById(R.id.hyperlink);
        iframes = findViewById(R.id.iframes);
        contentLiability = findViewById(R.id.contentLiability);
        reservation = findViewById(R.id.reservation);
        removal = findViewById(R.id.removal);
        disclaimer = findViewById(R.id.disclaimer);
        replacement = findViewById(R.id.replacement);
        other = findViewById(R.id.other);


        getTermsFromDb();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("Settings").child("Terms").setValue(
                        new TermsModel(
                                terms.getText().toString(),
                                cookies.getText().toString(),
                                license.getText().toString(),
                                hyperlink.getText().toString(),
                                iframes.getText().toString(),
                                contentLiability.getText().toString(),
                                reservation.getText().toString(),
                                removal.getText().toString(),
                                disclaimer.getText().toString(),
                                replacement.getText().toString(),
                                other.getText().toString()



                                )

                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Updated Terms");
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });


    }

    private void getTermsFromDb() {
        mDatabase.child("Settings").child("Terms").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    TermsModel model = dataSnapshot.getValue(TermsModel.class);
                    terms.setText(model.getTerms());
                    cookies.setText(model.getCookies());
                    license.setText(model.getLicense());
                    hyperlink.setText(model.getHyperlink());
                    iframes.setText(model.getIframes());
                    contentLiability.setText(model.getContentLiability());
                    reservation.setText(model.getReservation());
                    removal.setText(model.getRemoval());
                    disclaimer.setText(model.getDisclaimer());
                    replacement.setText(model.getReplacement());
                    other.setText(model.getOther());
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
