package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Languages extends AppCompatActivity {

    DatabaseReference mDatabase;
    RecyclerView recyclerView;
    ArrayList<LanguageModel> arrayList = new ArrayList<>();
    LanguageListAdapter adapter;
    Button update;
    EditText languages;
    private String langiList = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);
        this.setTitle("Languages");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.recycler);
        languages = findViewById(R.id.languages);
        update = findViewById(R.id.update);
        ;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new LanguageListAdapter(this, arrayList);

        recyclerView.setAdapter(adapter);
//        adapter.setListener(new LanguageListAdapter.Listener() {
//            @Override
//            public void countryChosen(LanguageModel model) {
//
//            }
//        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataToDB();
            }
        });


        getDataFromDB();
    }

    private void sendDataToDB() {
        List<String> provincesList = new ArrayList<String>(Arrays.asList(languages.getText().toString().split("\n")));

        mDatabase.child("Settings").child("Languages")
                .setValue(provincesList).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Language Added");
            }
        });
    }

    private void getDataFromDB() {
        mDatabase.child("Settings").child("Languages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    arrayList.clear();
                    langiList = "";
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String model = snapshot.getValue(String.class);
                        if (model != null) {

                            arrayList.add(new LanguageModel(model));
                            adapter.notifyDataSetChanged();
                            langiList = langiList + model + "\n";


                        }
                    }
                    languages.setText(langiList);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed() {

        finish();
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

