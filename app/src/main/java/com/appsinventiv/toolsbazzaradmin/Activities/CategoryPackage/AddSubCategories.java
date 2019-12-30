package com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.SwipeControllerActions;
import com.appsinventiv.toolsbazzaradmin.Utils.SwipeToDeleteCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddSubCategories extends AppCompatActivity {
    DatabaseReference mDatabase;
    EditText mainCategories;
    Button update;
    RecyclerView recyclerView;
    AddCategoryAdapter adapter;
    ArrayList<String> itemList = new ArrayList<>();
    String categories = "";
    String parentCategory;
    private SwipeToDeleteCallback swipeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Add Categories");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        setContentView(R.layout.activity_categories);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        parentCategory = getIntent().getStringExtra("parentCategory");

        recyclerView = findViewById(R.id.recyclerView);
        mainCategories = findViewById(R.id.mainCategories);
        update = findViewById(R.id.update);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new AddCategoryAdapter(this, itemList);

        recyclerView.setAdapter(adapter);

        if (parentCategory == null) {
            getDataFromDB();
        } else {
            getCategoryDataFromDB();
        }

//        swipeController = new SwipeToDeleteCallback(new SwipeControllerActions() {
//            @Override
//            public void onRightClicked(final int position) {
//                showAlert(itemList.get(position));
//
//            }
//        });
//        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
//        itemTouchhelper.attachToRecyclerView(recyclerView);
//
//        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//                swipeController.onDraw(c);
//            }
//        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainCategories.getText().length() == 0) {
                    mainCategories.setError("Enter category");
                } else {
                    List<String> container = new ArrayList<>();

                    String[] sizes = mainCategories.getText().toString().split("\n");
                    container = Arrays.asList(sizes);
                    if (parentCategory != null) {
                        mDatabase.child("Settings/Categories").child(parentCategory).setValue(container).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mainCategories.setText("");
                                CommonUtils.showToast("Done");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                CommonUtils.showToast(e.getMessage());
                            }
                        });
                    } else {
                        mDatabase.child("Settings/Categories").child("MainCategory").setValue(container).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mainCategories.setText("");
                                CommonUtils.showToast("Done");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                CommonUtils.showToast(e.getMessage());
                            }
                        });
                    }

                }
            }
        });

    }

    private void showAlert(final String model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddSubCategories.this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to delete this category?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteCategory(model);
            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteCategory(String model) {
        mDatabase.child("Settings").child("Categories").child(parentCategory).child(model).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Category Deleted");
            }
        });
    }


    private void getCategoryDataFromDB() {
        mDatabase.child("Settings").child("Categories").child(parentCategory).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String value = snapshot.getValue(String.class);
                        itemList.add(value);

                        categories = categories + value + "\n";
                        mainCategories.setText(categories);
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getDataFromDB() {
        mDatabase.child("Settings").child("Categories").child("MainCategory").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String value = snapshot.getValue(String.class);
                        itemList.add(value);

                        categories = categories == null ? "" : categories + value + "\n";
                        mainCategories.setText(categories);
                    }

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
