package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.appsinventiv.toolsbazzaradmin.Activities.AppSettings.BannersPackage.Banners;
import com.appsinventiv.toolsbazzaradmin.Activities.AppSettings.ShippingCarriers.ListOfShippingCarriers;
import com.appsinventiv.toolsbazzaradmin.Activities.Locations.ListOfCountries;
import com.appsinventiv.toolsbazzaradmin.R;

public class Settings extends AppCompatActivity {
    RelativeLayout aboutUs, terms, banner, deliveryCharges, company, cod, commissions, languages, fort_features, shippingCarrier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.setTitle("Settings");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }

        aboutUs = findViewById(R.id.aboutUs);
        shippingCarrier = findViewById(R.id.shippingCarrier);
        banner = findViewById(R.id.banner);
        terms = findViewById(R.id.terms);
        company = findViewById(R.id.company);
        deliveryCharges = findViewById(R.id.deliveryCharges);
        commissions = findViewById(R.id.commissions);
        fort_features = findViewById(R.id.fort_features);
        cod = findViewById(R.id.cod);
        languages = findViewById(R.id.languages);

        shippingCarrier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Settings.this, ListOfShippingCarriers.class);
                startActivity(i);
            }
        });
        languages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Settings.this, Languages.class);
                startActivity(i);
            }
        });


        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Settings.this, CODLimit.class);
                startActivity(i);
            }
        });

        commissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Settings.this, Commissions.class);
                startActivity(i);
            }
        });

        deliveryCharges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Settings.this, ListOfCountries.class);
//                i.putExtra("country","Sri Lanka");
                startActivity(i);
            }
        });


        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Settings.this, AboutUs.class);
                startActivity(i);
            }
        });
        fort_features.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Settings.this, FortCityFeatures.class);
                startActivity(i);
            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Settings.this, TermsSettings.class);
                startActivity(i);
            }
        });

        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Settings.this, Banners.class);
                startActivity(i);
            }
        });

        company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Settings.this, CompanySettings.class);
                startActivity(i);
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
