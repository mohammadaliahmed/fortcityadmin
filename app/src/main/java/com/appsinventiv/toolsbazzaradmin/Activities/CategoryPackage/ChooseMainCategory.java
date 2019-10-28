package com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Products.AddProduct;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.EditProduct;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.SwipeControllerActions;
import com.appsinventiv.toolsbazzaradmin.Utils.SwipeToDeleteCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChooseMainCategory extends AppCompatActivity {

    RecyclerView recycler;
    DatabaseReference mDatabase;
    ArrayList<MainCategoryModel> itemList = new ArrayList<>();
    MainCategoryAdapter adapter;
    FloatingActionButton fab;
    public static Activity activity;
    private SwipeToDeleteCallback swipeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_main_category);
        activity = this;
        this.setTitle("Choose Categories");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recycler = findViewById(R.id.recycler);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChooseMainCategory.this, AddMainCategories.class);
                startActivity(i);
            }
        });


        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MainCategoryAdapter(this, itemList, 0, new MainCategoryAdapter.MainCategoryCallBacks() {
            @Override
            public void deleteCategory(MainCategoryModel model) {
                showAlert(model);
            }
        });


        swipeController = new SwipeToDeleteCallback(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                showAlert(itemList.get(position));

            }
        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recycler);

        recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });


        recycler.setAdapter(adapter);
        getMainCategoriesFromDB();


    }

    private void showAlert(final MainCategoryModel model) {


        final Dialog dialog = new Dialog(ChooseMainCategory.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = layoutInflater.inflate(R.layout.alert_dialog_curved, null);

        dialog.setContentView(layout);

        TextView message = layout.findViewById(R.id.message);
        TextView no = layout.findViewById(R.id.no);
        TextView yes = layout.findViewById(R.id.yes);

        message.setText("Do you want to delete this category?");

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                deleteCategory(model);
            }
        });


        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();


    }

    private void deleteCategory(MainCategoryModel model) {
        mDatabase.child("Settings/Categories/MainCategories").child(model.getMainCategory()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Category Deleted");
            }
        });
    }

    private void getMainCategoriesFromDB() {

        mDatabase.child("Settings/Categories/MainCategories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MainCategoryModel model = snapshot.getValue(MainCategoryModel.class);
                        if (model != null) {
                            itemList.add(model);
                        }
                    }
                    adapter.updateList(itemList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

            AddProduct.categoryList.clear();
            EditProduct.categoryList.clear();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AddProduct.productAttributesMap = null;
        AddProduct.categoryList.clear();
        EditProduct.categoryList.clear();
        finish();
    }

}
