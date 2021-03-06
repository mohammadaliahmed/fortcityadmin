package com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Products.AddProduct;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.ChooseOptions.AssignAttributes;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.ChooseOptions.ChooseAttributes;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.EditProduct;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChooseCategory extends AppCompatActivity {
    DatabaseReference mDatabase;
    CategoryAdapter adapter;
    RecyclerView recyclerView;
    String parentCategory;
    ArrayList<String> itemList = new ArrayList<>();
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);
        this.setTitle("Choose category");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.recyclerView);
        progress = findViewById(R.id.progress);


        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new CategoryAdapter(this, itemList, new CategoryAdapter.GetNewData() {
            @Override
            public void whichCategory(String title) {
                getCategoryDataFromDB(title);
            }
        });

        recyclerView.setAdapter(adapter);


        parentCategory = getIntent().getStringExtra("parentCategory");


        getCategoryDataFromDB(parentCategory);


    }


    private void getCategoryDataFromDB(final String cate) {
        progress.setVisibility(View.VISIBLE);
        mDatabase.child("Settings").child("Categories").child(cate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String value = snapshot.getValue(String.class);
                        itemList.add(value);
                    }
                    adapter.updateList(itemList);
                    progress.setVisibility(View.GONE);

                    adapter.notifyDataSetChanged();
                } else {
                    progress.setVisibility(View.GONE);

//                    itemList.clear();
//                    adapter.notifyDataSetChanged();
//                    if (ChooseMainCategory.activity != null) {
//                        ChooseMainCategory.activity.finish();
//                    }
//                    ChooseOtherMainCategory.activity.finish();
//                    finish();
                    if (!Constants.ADDING_PRODUCT) {
                        showAttAlert(cate);

                    } else {
                        itemList.clear();
                        adapter.notifyDataSetChanged();
                        if (ChooseMainCategory.activity != null) {
                            ChooseMainCategory.activity.finish();
                        }

                        ChooseOtherMainCategory.activity.finish();
                        Intent i = new Intent(ChooseCategory.this, ChooseAttributes.class);
                        startActivity(i);

                        finish();

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showAttAlert(final String cate) {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = layoutInflater.inflate(R.layout.alert_dialog_attri, null);

        dialog.setContentView(layout);

        RelativeLayout attribute = layout.findViewById(R.id.attribute);
        RelativeLayout sku = layout.findViewById(R.id.sku);

        attribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(ChooseCategory.this, AssignAttributes.class);
                i.putExtra("type", "Attributes");
                i.putExtra("category", cate);
                startActivity(i);
                Constants.SKU_ATT = "attributes";

            }
        });

        sku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(ChooseCategory.this, AssignAttributes.class);
                i.putExtra("type", "SKU");
                i.putExtra("category", cate);
                Constants.SKU_ATT = "sku";


                startActivity(i);

            }
        });


        dialog.show();

    }

    private void getDataFromDB() {
        progress.setVisibility(View.VISIBLE);
        mDatabase.child("Settings").child("Categories").child("MainCategory").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String value = snapshot.getValue(String.class);
                        itemList.add(value);

                    }
                    progress.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        try {
            if (AddProduct.categoryList.size() > 0 || EditProduct.categoryList.size() > 0) {
                AddProduct.categoryList.remove(AddProduct.categoryList.size() - 1);
                EditProduct.categoryList.remove(EditProduct.categoryList.size() - 1);
                if (AddProduct.fromWhere == 1) {

                    getCategoryDataFromDB(AddProduct.categoryList.get(AddProduct.categoryList.size() - 1));
                } else if (EditProduct.fromWhere == 1) {
                    getCategoryDataFromDB(EditProduct.categoryList.get(EditProduct.categoryList.size() - 1));
                }

            } else {
                finish();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.only_search_menu, menu);
        final MenuItem mSearch = menu.findItem(R.id.action_search);
//        mSearch.expandActionView();
        SearchView mSearchView = (SearchView) mSearch.getActionView();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.filter(newText);
//                    getUserCartProductsFromDB();

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            try {


                if (AddProduct.categoryList.size() > 0 || EditProduct.categoryList.size() > 0) {
                    AddProduct.categoryList.remove(AddProduct.categoryList.size() - 1);
                    EditProduct.categoryList.remove(EditProduct.categoryList.size() - 1);
                    if (AddProduct.fromWhere == 1) {
                        getCategoryDataFromDB(AddProduct.categoryList.get(AddProduct.categoryList.size() - 1));
                    } else if (EditProduct.fromWhere == 1) {
                        getCategoryDataFromDB(EditProduct.categoryList.get(EditProduct.categoryList.size() - 1));
                    }

                } else {
                    finish();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                finish();
            }
//            super.onBackPressed();

        }

        return super.onOptionsItemSelected(item);
    }
}
