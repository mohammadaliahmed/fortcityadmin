package com.appsinventiv.toolsbazzaradmin.Activities.Purchases;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Models.Product;
import com.appsinventiv.toolsbazzaradmin.Models.ProductCountModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditPurchase extends AppCompatActivity {
    String id;
    DatabaseReference mDatabase;
    ImageView image;
    TextView title, quantity, orderIds, costPrice, totalPrice;
    EditText newCostPrice, qtyAvail, outOfStockCount, purchaseTotal;
    Button confirm, calculate;

    Product product;
    ProductCountModel model;
    float oldCostPrice;
    float newCostPricee, newRetailPrice, newWholeSalePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_purchase);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        this.setTitle("Edit Purchase");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        confirm = findViewById(R.id.confirm);
        quantity = findViewById(R.id.quantity);
        orderIds = findViewById(R.id.orderIds);
        costPrice = findViewById(R.id.costPrice);
        totalPrice = findViewById(R.id.totalPrice);
        newCostPrice = findViewById(R.id.newCostPrice);
        qtyAvail = findViewById(R.id.qtyAvail);
        outOfStockCount = findViewById(R.id.outOfStockCount);
        purchaseTotal = findViewById(R.id.purchaseTotal);
        title = findViewById(R.id.title);
        image = findViewById(R.id.image);
        calculate = findViewById(R.id.calculate);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reCalculate();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmPurchase();
            }
        });


        Intent i = getIntent();
        id = i.getStringExtra("id");

        getDataFromDb(id);


    }

    private void reCalculate() {
        if (newCostPrice.getText().length() == 0) {
            newCostPrice.setError("Enter cost price");
        } else if (qtyAvail.getText().length() == 0) {
            qtyAvail.setError("Enter quantity");
        } else {
            int abc = (model.getQuantity() - (Integer.parseInt(qtyAvail.getText().toString())));
            outOfStockCount.setText("" + abc);
            purchaseTotal.setText("" + (Integer.parseInt(qtyAvail.getText().toString()) * Float.parseFloat(newCostPrice.getText().toString())));

        }
    }

    private void confirmPurchase() {
        if (newCostPrice.getText().length() == 0) {
            newCostPrice.setError("Enter cost price");
        } else if (qtyAvail.getText().length() == 0) {
            qtyAvail.setError("Cannot be blank");
        } else if (outOfStockCount.getText().length() == 0) {
            outOfStockCount.setError("Cannot be blank");
        } else if (purchaseTotal.getText().length() == 0) {
            purchaseTotal.setError("Cannot be blank");
        } else {

            newCostPricee = Float.parseFloat(newCostPrice.getText().toString());

            mDatabase.child("Products").child(id)
                    .child("costPrice").setValue(newCostPricee);


            mDatabase.child("Purchases").child("PendingPurchases").child(id)
                    .child("newCostPrice").setValue(Float.parseFloat(newCostPrice.getText().toString()));

            mDatabase.child("Purchases").child("PendingPurchases").child(id)
                    .child("quantityPurchased").setValue(Integer.parseInt(qtyAvail.getText().toString()));

            mDatabase.child("Purchases").child("PendingPurchases").child(id)
                    .child("outOfStock").setValue(Integer.parseInt(outOfStockCount.getText().toString()));

            updatePrices();

            mDatabase.child("Purchases").child("PendingPurchases").child(id)
                    .child("purchaseTotal").setValue(Float.parseFloat(purchaseTotal.getText().toString())).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    CommonUtils.showToast("Updated");
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    CommonUtils.showToast("Error: " + e.getMessage());
                }
            });
        }

    }

    private void updatePrices() {

        if ((newCostPricee - oldCostPrice) > 0) {
            mDatabase.child("Products").child(id)
                    .child("retailPrice").setValue(product.getRetailPrice() + (newCostPricee - oldCostPrice));


            mDatabase.child("Products").child(id)
                    .child("wholeSalePrice:").setValue(product.getWholeSalePrice() + (newCostPricee - oldCostPrice));


        }

    }

    private void getDataFromDb(String id) {
        mDatabase.child("Purchases").child("PendingPurchases").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    model = dataSnapshot.getValue(ProductCountModel.class);
                    if (model != null) {
                        product = model.getProduct();
                        oldCostPrice = product.getCostPrice();
                        Glide.with(EditPurchase.this).load(model.getProduct().getThumbnailUrl()).into(image);
                        title.setText(model.getProduct().getTitle());
                        quantity.setText("Total Quantity: " + model.getQuantity());
                        costPrice.setText("Cost Price: " + model.getProduct().getCostPrice());
                        totalPrice.setText("Order Total: Rs " + (model.getProduct().getCostPrice() * model.getQuantity()));
                        ArrayList<String> abc = new ArrayList<>();
                        for (int i = 0; i < model.getOrderId().size(); i++) {

                            abc.add("" + model.getOrderId().keySet().toArray()[i]);
                        }
                        orderIds.setText("Order Ids: " + abc);

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

    @Override
    public void onBackPressed() {
        finish();
    }
}
