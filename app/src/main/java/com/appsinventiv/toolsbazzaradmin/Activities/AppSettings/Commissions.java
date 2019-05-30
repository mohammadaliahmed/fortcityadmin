package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.appsinventiv.toolsbazzaradmin.Activities.Products.MainCategoryModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Commissions extends AppCompatActivity {
    RecyclerView recyclerview;
    CommissionsAdapter adapter;
    DatabaseReference mDatabase;
    ArrayList<MainCategoryModel> itemList = new ArrayList<>();
    ArrayList<CommissionModel> commissionsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commissions);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        this.setTitle("Manage Commissions");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new CommissionsAdapter(this, commissionsList, new CommissionsAdapter.CommissionsCallback() {
            @Override
            public void value(CommissionModel model, int value) {
                updateValueToDB(model, value);
            }
        });
        recyclerview.setAdapter(adapter);
        getDataFromDB();

    }

    private void updateValueToDB(CommissionModel model, int value) {
        mDatabase.child("Commissions").child(model.getCategoryName()).child("commission").setValue(value).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Updated");
            }
        });
    }

    private void getDataFromDB() {
        mDatabase.child("Commissions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    commissionsList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        CommissionModel model = snapshot.getValue(CommissionModel.class);
                        if (model != null) {
                            commissionsList.add(model);
                        }
                    }
                    adapter.notifyDataSetChanged();
//                    CommonUtils.showToast("Exits");
                } else {
                    getMainCategoriesFromDB();
//                    CommonUtils.showToast("No commission");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getMainCategoriesFromDB() {
        mDatabase.child("Settings").child("Categories").child("MainCategories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MainCategoryModel mainCategoryModel = snapshot.getValue(MainCategoryModel.class);
                        if (mainCategoryModel != null) {
                            itemList.add(mainCategoryModel);
                        }
                    }
                    if (itemList.size() > 0) {
                        updateCommissionsToDB();
                    } else {

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateCommissionsToDB() {
        if (itemList.size() > 0) {
            for (MainCategoryModel item : itemList) {
                mDatabase.child("Commissions").child(item.getMainCategory()).setValue(new CommissionModel(
                        item.getMainCategory(), item.getMainCategory(), 0
                ));
            }
        }
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

