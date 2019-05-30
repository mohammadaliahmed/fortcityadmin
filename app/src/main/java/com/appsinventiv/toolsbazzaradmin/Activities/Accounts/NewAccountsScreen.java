package com.appsinventiv.toolsbazzaradmin.Activities.Accounts;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.appsinventiv.toolsbazzaradmin.Activities.Orders.Orders;
import com.appsinventiv.toolsbazzaradmin.Activities.SellerOrders.SellerOrders;
import com.appsinventiv.toolsbazzaradmin.R;

public class NewAccountsScreen extends AppCompatActivity {
    private LinearLayout sellerAccounts;
    private LinearLayout accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_accounts_screen);
        sellerAccounts = findViewById(R.id.sellerAccounts);
        accounts = findViewById(R.id.accounts);


        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        accounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewAccountsScreen.this, Accounts.class);
                startActivity(i);
            }
        });
        sellerAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewAccountsScreen.this, Orders.class);
                startActivity(i);
            }
        });
    }


    @Override
    public void onBackPressed() {

        finish();
    }


}
