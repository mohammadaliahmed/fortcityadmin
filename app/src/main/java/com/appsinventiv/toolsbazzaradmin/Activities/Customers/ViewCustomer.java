package com.appsinventiv.toolsbazzaradmin.Activities.Customers;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Adapters.InvoiceListAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.Customer;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewCustomer extends AppCompatActivity {
    DatabaseReference mDatabase;
    String customerId, sellerId;
    Button back;
    TextView name, username, lastOrder, totalOrders, customerSince, customerName,
            type, storeName, email, shipping, city, country, telephone, mobile, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer);
        this.setTitle("Customer info");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        shipping = findViewById(R.id.shipping);
        city = findViewById(R.id.city);
        country = findViewById(R.id.country);
        telephone = findViewById(R.id.telephone);
        mobile = findViewById(R.id.mobile);
        country = findViewById(R.id.country);
        lastOrder = findViewById(R.id.lastOrder);
        totalOrders = findViewById(R.id.totalOrders);
        customerSince = findViewById(R.id.customerSince);
        customerName = findViewById(R.id.customerName);
        type = findViewById(R.id.type);
        storeName = findViewById(R.id.storeName);
        back = findViewById(R.id.back);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        customerId = getIntent().getStringExtra("customerId");
        sellerId = getIntent().getStringExtra("sellerId");
        if (customerId != null) {
            getDataFromDB(customerId);

        } else if (sellerId != null) {
            getSellerFromDB(sellerId);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void getDataFromDB(final String customerId) {
        mDatabase.child("Customers").child(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    if (customer != null) {
                        ViewCustomer.this.setTitle("Customer: " + customer.getName());
//                        name.setText(customer.getName());
                        username.setText(customer.getUsername());
                        shipping.setText(customer.getAddress());
                        city.setText(customer.getCity());
                        country.setText(customer.getCountry());
                        telephone.setText(customer.getTelNumber());
                        mobile.setText(customer.getPhone());
                        lastOrder.setText(customer.getName());
                        customerSince.setText(CommonUtils.getFormattedDateOnly(customer.getTime()));
                        type.setText(customer.getCustomerType());
                        email.setText(customer.getEmail());
                        storeName.setText(customer.getStoreName());
                        customerName.setText(customer.getName());
                        password.setText(customer.getPassword());

                        totalOrders.setText("" + dataSnapshot.child("Orders").getChildrenCount());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getSellerFromDB(final String sellerId) {
        mDatabase.child("Sellers").child(sellerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    if (customer != null) {
                        ViewCustomer.this.setTitle("Seller : " + customer.getName());
//                        name.setText(customer.getName());
                        username.setText(customer.getUsername());
                        shipping.setText(customer.getAddress());
                        city.setText(customer.getCity());
                        country.setText(customer.getCountry());
                        telephone.setText(customer.getTelNumber());
                        mobile.setText(customer.getPhone());
                        lastOrder.setText(customer.getName());
                        customerSince.setText(CommonUtils.getFormattedDateOnly(customer.getTime()));
                        type.setText(customer.getCustomerType());
                        email.setText(customer.getEmail());
                        storeName.setText(customer.getStoreName());
                        customerName.setText(customer.getName());
                        password.setText(customer.getPassword());

//                        totalOrders.setText("" + dataSnapshot.child("Orders").getChildrenCount());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
