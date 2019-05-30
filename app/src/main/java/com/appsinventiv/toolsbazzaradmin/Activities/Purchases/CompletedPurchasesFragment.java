package com.appsinventiv.toolsbazzaradmin.Activities.Purchases;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appsinventiv.toolsbazzaradmin.Adapters.POListAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.PurchaseOrderModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class CompletedPurchasesFragment extends Fragment {
    DatabaseReference mDatabase;
    POListAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ArrayList<PurchaseOrderModel> itemList = new ArrayList<>();
    Context context;
    Button fab;
    ArrayList<CompletedArrayListModel> completedPurchasesIds = new ArrayList<>();

    public CompletedPurchasesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromDb();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_completed_purchases, container, false);
        fab = rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (completedPurchasesIds.size() > 0) {
                    moveToPendingAccounts();
                }
            }
        });

        recyclerView = rootView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new POListAdapter(context, itemList,"",1,"completed", new POListAdapter.SettleBills() {
            @Override
            public void addToArray(String id, int position) {
                if (!completedPurchasesIds.contains(id)) {
                    completedPurchasesIds.add(new CompletedArrayListModel(id, position));


                }
            }

            @Override
            public void removeFromArray(String id, int position) {
                if (completedPurchasesIds.contains(id)) {
                    completedPurchasesIds.remove(completedPurchasesIds.indexOf(id));
                }
            }
        });

        recyclerView.setAdapter(adapter);


        return rootView;

    }

    private void moveToPendingAccounts() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Move to Pending accounts?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendDataToDb();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();


    }

    private void sendDataToDb() {
        for (final CompletedArrayListModel id : completedPurchasesIds) {
           final String key=""+itemList.get(id.getPosition()).getId();
            mDatabase.child("Accounts").child("PendingPO").child(key).setValue(itemList.get(id.getPosition())).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    removeFromCompleted(key);
                }
            });

        }

    }

    private void removeFromCompleted(String id) {
        mDatabase.child("Purchases").child("Completed").child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getDataFromDb() {
        mDatabase.child("Purchases").child("Completed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        PurchaseOrderModel model = snapshot.getValue(PurchaseOrderModel.class);
                        if (model != null) {
                            if (!model.isCancelled()) {
                                itemList.add(model);
                            }
                            Collections.sort(itemList, new Comparator<PurchaseOrderModel>() {
                                @Override
                                public int compare(PurchaseOrderModel listData, PurchaseOrderModel t1) {
                                    Long ob1 = listData.getTime();
                                    Long ob2 = t1.getTime();

                                    return ob2.compareTo(ob1);

                                }
                            });
                            adapter.notifyDataSetChanged();

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


    private class CompletedArrayListModel {
        String id;
        int position;

        public CompletedArrayListModel(String id, int position) {
            this.id = id;
            this.position = position;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }


}
