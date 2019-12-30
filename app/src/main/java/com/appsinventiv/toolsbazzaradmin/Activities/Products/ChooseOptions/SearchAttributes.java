package com.appsinventiv.toolsbazzaradmin.Activities.Products.ChooseOptions;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage.Attributes.SubAttributeModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchAttributes extends AppCompatActivity {
    DatabaseReference mDatabase;
    RecyclerView recyclerView;
    AssignAttributeOptionAdapter adapter;
    ArrayList<SubAttributeModel> itemList = new ArrayList<>();
    ArrayList<SubAttributeModel> newList = new ArrayList<>();
    ArrayList<String> atList = new ArrayList<>();
    HashMap<String, SubAttributeModel> checkMap = new HashMap<>();

    String type, category;
    Button assign;
    AutoCompleteTextView searchWord;
    EditText chosen;
    ImageView backArraow;
    //    private String[] countries = new String[]{
//            "Belgium", "France", "Italy", "Germany", "Spain"
//    };
    private String[] countries;
    String abc = "";

    CheckBox selectAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        setContentView(R.layout.activity_search_attributes);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        searchWord = findViewById(R.id.searchWord);
        chosen = findViewById(R.id.chosen);
        backArraow = findViewById(R.id.backArraow);
        backArraow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        selectAll = findViewById(R.id.selectAll);
        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ArrayList<SubAttributeModel> models = new ArrayList<>();
                if (b) {
                    models = newList;
                    adapter.setSelectedList(models);
                    for (SubAttributeModel model : models) {
                        checkMap.put(model.getMainCategory(), model);
                    }

                } else {
                    models = new ArrayList<>();
                    adapter.setSelectedList(models);
                    checkMap.clear();

                }
            }
        });


        searchWord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int abcc = atList.indexOf(searchWord.getText().toString());
                abc = abc + "\n" + atList.get(abcc);
                SubAttributeModel abccc = itemList.get(abcc);
                newList.add(abccc);
                adapter.updateList(newList);
                chosen.setText(abc);
                searchWord.setText("");

            }
        });

        type = getIntent().getStringExtra("type");
        category = getIntent().getStringExtra("category");
        setTitle(type);


        recyclerView = findViewById(R.id.recyclerView);
        assign = findViewById(R.id.assign);

        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkMap.size() > 0) {
                    showAlert();
                } else {
                    CommonUtils.showToast("Select some attributes");
                }

            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new AssignAttributeOptionAdapter(this, newList, new AssignAttributeOptionAdapter.ChooseAssignOptionCallback() {
            @Override
            public void onOptionSelected(SubAttributeModel value, boolean checked, int position) {
                if (checked) {
                    checkMap.put(value.getMainCategory(), value);
                } else {
                    checkMap.remove(value.getMainCategory());
                }
            }
        });

        recyclerView.setAdapter(adapter);


        getAttributesList();


    }

    private void getCheckedAttributesList() {
        mDatabase.child("Settings/Attributes").child("AssignedAttributes").child(category).child(type).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SubAttributeModel val = snapshot.getValue(SubAttributeModel.class);
                        if (val != null) {
                            checkMap.put(val.getMainCategory(), val);
                        }
                    }
                    ArrayList<SubAttributeModel> abc = new ArrayList<>();

                    for (Map.Entry<String, SubAttributeModel> entry : checkMap.entrySet()) {
                        abc.add(entry.getValue());
                    }
                    adapter.setSelectedList(abc);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Assign " + type + " ?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child("Settings/Attributes").child("AssignedAttributes").child(category).child(type).setValue(checkMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Assigned");
                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void getAttributesList() {
        mDatabase.child("Settings").child("Attributes").child("SubAttributes").child(type).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SubAttributeModel model = snapshot.getValue(SubAttributeModel.class);
                        if (model != null) {
                            itemList.add(model);
                            atList.add(model.getMainCategory());
                        }
                    }
//                    adapter.updateList(itemList);
//                    adapter.notifyDataSetChanged();
                    if (itemList.size() > 0) {
                        countries = new String[atList.size()];
                        for (int i = 0; i < atList.size(); i++) {
                            countries[i] = atList.get(i);


                        }
//                        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(SearchAttributes.this,
//                                android.R.layout.simple_dropdown_item_1line, countries);
                        searchWord.setAdapter(getAreaAdapter(SearchAttributes.this));

//                        searchWord.setAdapter(adapter1);

                        getCheckedAttributesList();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private ArrayAdapter<String> getAreaAdapter(Context context) {

        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, countries);
    }

//    private void getSubSubAttributesFromDB(final SubAttributeModel subAttributeModel) {
//        this.setTitle(subAttributeModel.getMainCategory());
//        itemList.clear();
//
//        if (subAttributeModel.getSelection().equalsIgnoreCase("userInput")) {
//
//            userInput.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.GONE);
//
//        } else {
//            userInput.setVisibility(View.GONE);
//            recyclerView.setVisibility(View.VISIBLE);
//            wholeLayout.setVisibility(View.VISIBLE);
//            mDatabase.child("Settings/Attributes/SubSubAttributes")
//                    .child(subAttributeModel.getMainCategory()).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.getValue() != null) {
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            String item = snapshot.getValue(String.class);
//                            if (item != null) {
//                                itemList.add(item);
//
//                            }
//                        }
//                        if (subAttributeModel.getSelection().equalsIgnoreCase("single")) {
//                            adapter.setMultiSelect(false);
//                        } else if (subAttributeModel.getSelection().equalsIgnoreCase("multiple")) {
//                            adapter.setMultiSelect(true);
//                        }
//                        adapter.setSelected(-1);
//                        adapter.updateList(itemList);
//                        adapter.notifyDataSetChanged();
//                        wholeLayout.setVisibility(View.GONE);
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.only_search_menu, menu);
        final MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.filter(newText);

                return false;
            }
        });
        return true;
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
