package com.appsinventiv.toolsbazzaradmin.Activities.Orders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ProgressBar;

import com.appsinventiv.toolsbazzaradmin.Activities.Accounts.Invoices.PendingSOAccounts;
import com.appsinventiv.toolsbazzaradmin.Activities.Accounts.TransferToAccountsDone;
import com.appsinventiv.toolsbazzaradmin.Adapters.InvoiceListAdapter;
import com.appsinventiv.toolsbazzaradmin.Adapters.OrdersAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.InvoiceModel;
import com.appsinventiv.toolsbazzaradmin.Models.OrderModel;
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


public class InvoiceListFragment extends Fragment {

    DatabaseReference mDatabase;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    InvoiceListAdapter adapter;
    ArrayList<InvoiceModel> itemList = new ArrayList<>();
    ArrayList<InvoiceModel> newList = new ArrayList<>();
    Context context;
    Button finalized;

    ArrayList<InvoicesSelected> invoicesSelectedList = new ArrayList<>();

    public InvoiceListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pending_po_accounts, container, false);
        finalized = rootView.findViewById(R.id.finalized);
        finalized.setText("Transfer to accounts");

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new InvoiceListAdapter(context, itemList, 1, "", new InvoiceListAdapter.SelectInvoices() {
            @Override
            public void addToArray(long id, int position) {
                if (!invoicesSelectedList.contains(id)) {
                    invoicesSelectedList.add(new InvoicesSelected(id, position));
                }
            }

            @Override
            public void removeFromArray(long id, int position) {
                if (invoicesSelectedList.contains(id)) {
                    invoicesSelectedList.remove(position);

                }
            }
        });

        recyclerView.setAdapter(adapter);

        finalized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (invoicesSelectedList.size() > 0) {
                    InvoicesFinalized();
                } else {
                    CommonUtils.showToast("Nothing selected");
                }
            }
        });


        return rootView;

    }

    private void InvoicesFinalized() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Purchase Order finalized?");
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
        for (InvoicesSelected id : invoicesSelectedList) {
            final String key = "" + itemList.get(id.getPosition()).getId();
            mDatabase.child("Accounts").child("PendingInvoices").child(key).child("invoiceStatus").setValue("pendingSO").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Intent i = new Intent(context, TransferToAccountsDone.class);
                    i.putExtra("orderId", key);
                    context.startActivity(i);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    CommonUtils.showToast(e.getMessage());
                }
            });
        }
        adapter.notifyDataSetChanged();
    }


    private void getDataFromDb() {
        mDatabase.child("Accounts").child("PendingInvoices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        InvoiceModel model = snapshot.getValue(InvoiceModel.class);
                        if (model != null) {
                            if (model.getInvoiceStatus().equalsIgnoreCase("waiting")) {
                                itemList.add(model);

                            }
//                            progress.setVisibility(View.GONE);

                        }
                    }
                    Collections.sort(itemList, new Comparator<InvoiceModel>() {
                        @Override
                        public int compare(InvoiceModel listData, InvoiceModel t1) {
                            Long ob1 = listData.getTime();
                            Long ob2 = t1.getTime();

                            return ob2.compareTo(ob1);

                        }
                    });
                    adapter.notifyDataSetChanged();

                } else {
//                    CommonUtils.showToast("Nothing to show");
//                    progress.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        getDataFromDb();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    private class InvoicesSelected {
        long id;
        int position;

        public InvoicesSelected(long id, int position) {
            this.id = id;
            this.position = position;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
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
