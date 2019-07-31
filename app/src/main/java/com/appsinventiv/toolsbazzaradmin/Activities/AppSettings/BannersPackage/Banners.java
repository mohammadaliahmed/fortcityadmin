package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings.BannersPackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.appsinventiv.toolsbazzaradmin.R;

public class Banners extends AppCompatActivity {
    private RelativeLayout adminBanner, clientBanner,sellerBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banners);
        this.setTitle("Banners Settings");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }


        adminBanner = findViewById(R.id.adminBanner);
        clientBanner = findViewById(R.id.clientBanner);
        sellerBanner = findViewById(R.id.sellerBanner);

        adminBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Banners.this, HomeBannerSettings.class);
                startActivity(i);
            }
        });
        clientBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Banners.this, ClientBanners.class);
                startActivity(i);
            }
        });
        sellerBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Banners.this, SellerBanners.class);
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
