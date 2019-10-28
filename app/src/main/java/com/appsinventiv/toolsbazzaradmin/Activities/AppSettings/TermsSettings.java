package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.appsinventiv.toolsbazzaradmin.Activities.Locations.ListOfCountries;
import com.appsinventiv.toolsbazzaradmin.R;

public class TermsSettings extends AppCompatActivity {
    RelativeLayout adminTerms, userTerms, sellerTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_settings);
        this.setTitle("Settings");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }


        adminTerms = findViewById(R.id.adminTerms);
        userTerms = findViewById(R.id.userTerms);
        sellerTerms = findViewById(R.id.sellerTerms);

        adminTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TermsSettings.this, AdminTerms.class));
            }
        });
        userTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TermsSettings.this, Terms.class));
            }
        });
        sellerTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TermsSettings.this, SellerTerms.class));
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
