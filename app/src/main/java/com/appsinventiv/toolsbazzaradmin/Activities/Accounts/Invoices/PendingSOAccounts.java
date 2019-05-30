package com.appsinventiv.toolsbazzaradmin.Activities.Accounts.Invoices;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appsinventiv.toolsbazzaradmin.Activities.Accounts.TransferToAccountsDone;
import com.appsinventiv.toolsbazzaradmin.Adapters.InvoiceListAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.InvoiceModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class PendingSOAccounts extends Fragment {

    Context context;
    ArrayList<InvoiceModel> itemList = new ArrayList<>();
    InvoiceListAdapter adapter;
    DatabaseReference mDatabase;
    ArrayList<InvoicesSelected> invoicesSelectedList = new ArrayList<>();
    Button finalized;

    public PendingSOAccounts() {
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
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        finalized = rootView.findViewById(R.id.finalized);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new InvoiceListAdapter(context, itemList, 1, "pending", new InvoiceListAdapter.SelectInvoices() {
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

        for (final InvoicesSelected id : invoicesSelectedList) {
            final String key = "" + itemList.get(id.getPosition()).getId();

            String path = "";
            path = CommonUtils.getYear(itemList.get(id.getPosition()).getTime())
                    + "/" + CommonUtils.getMonth(itemList.get(id.getPosition()).getTime())
                    + "/" + CommonUtils.getDate(itemList.get(id.getPosition()).getTime())
                    + "/" + key;
            final String finalPath = path;
            mDatabase.child("Accounts").child("InvoicesFinalized")
                    .child(CommonUtils.getYear(itemList.get(id.getPosition()).getTime()))
                    .child(CommonUtils.getMonth(itemList.get(id.getPosition()).getTime()))
                    .child(CommonUtils.getDate(itemList.get(id.getPosition()).getTime()))
                    .child(key)
                    .setValue(itemList.get(id.getPosition())).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    try {


                        mDatabase.child("Accounts").child("ExpensesAndRevenue")
                                .child(CommonUtils.getYear(itemList.get(id.getPosition()).getTime()))
                                .child(CommonUtils.getMonth(itemList.get(id.getPosition()).getTime()))
                                .child("SO")
                                .child(CommonUtils.getDate(itemList.get(id.getPosition()).getTime()))
                                .child(key)
                                .setValue(itemList.get(id.getPosition()).getTotalPrice());
                        removeFromCompleted(key);
                        addInvoicePathToOrder(itemList.get(id.getPosition()), finalPath);
                    } catch (IndexOutOfBoundsException e) {
                    }

                }
            });

        }

        getDataFromServer();

    }

    private void addInvoicePathToOrder(InvoiceModel model, String finalPath) {
        mDatabase.child("Orders").child(model.getOrderId()).child("invoicePath").setValue(finalPath);
    }


    private void removeFromCompleted(String id) {
        mDatabase.child("Accounts").child("PendingInvoices").child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        super.onResume();
        getDataFromServer();
    }

    private void getDataFromServer() {

        mDatabase.child("Accounts").child("PendingInvoices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        InvoiceModel model = snapshot.getValue(InvoiceModel.class);
                        if (model != null) {
//                            if (model.getInvoiceStatus().equalsIgnoreCase("pendingSO")) {
                            itemList.add(model);
//                            }

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
