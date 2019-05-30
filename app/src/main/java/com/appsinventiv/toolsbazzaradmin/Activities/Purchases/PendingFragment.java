package com.appsinventiv.toolsbazzaradmin.Activities.Purchases;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.appsinventiv.toolsbazzaradmin.Adapters.PendingProductsAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.InvoiceModel;
import com.appsinventiv.toolsbazzaradmin.Models.ProductCountModel;
import com.appsinventiv.toolsbazzaradmin.Models.PurchaseOrderModel;
import com.appsinventiv.toolsbazzaradmin.Models.VendorModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.SharedPrefs;
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


public class PendingFragment extends Fragment {

    ArrayList<VendorModel> vendorModelArrayList = new ArrayList<>();
    Spinner spinner;
    VendorModel vendor;
    DatabaseReference mDatabase;
    RecyclerView recyclerview;
    public static ArrayList<ProductCountModel> itemList = new ArrayList<>();
    public static ArrayList<ProductCountModel> newList = new ArrayList<>();
    PendingProductsAdapter adapter;
    ProgressBar progress;
    long poNumber = 10001;
    Button generate;
    ArrayList<String> productIds = new ArrayList<>();
    Context context;

    public PendingFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_pending_purchases, container, false);
        spinner = rootView.findViewById(R.id.chooseVendor);
        progress = rootView.findViewById(R.id.progress);
        generate = rootView.findViewById(R.id.fab);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newList.size() > 0) {
                    showAlertBoxForCompletion();
                } else {
                    CommonUtils.showToast("Products are pending for purchase");
                }


            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerview = rootView.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutManager);
        adapter = new PendingProductsAdapter(context, itemList, new PendingProductsAdapter.IsPurchased() {
            @Override
            public void addToArray(String id, int position) {
                if (!productIds.contains(id)) {
                    productIds.add(id);
                    addToPurchasedProducts(id, position);

                }

            }

            @Override
            public void removeFromArray(String id, int position) {
                if (productIds.contains(id)) {
                    productIds.remove(id);
                }
            }
        });
        recyclerview.setAdapter(adapter);


        getVendorsFromDb();
        getPurchaseOrderCountFromDb();

        return rootView;

    }

    private void showAlertBoxForCompletion() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Move to completed?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String key = mDatabase.push().getKey();

                        mDatabase.child("Purchases").child("Completed")
                                .child(key)
                                .setValue(new PurchaseOrderModel(
                                        key,
                                        newList,
                                        vendor,
                                        calculateTotal()
                                        , System.currentTimeMillis(),
                                        false,
                                        SharedPrefs.getFullName(),
                                        "" + poNumber)


                                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                CommonUtils.showToast("Marked as completed");
                                for (ProductCountModel item : newList) {
                                    removeValueFromDb(item.getProduct().getId());
                                }
                                updatePOCount();
                                adapter.notifyDataSetChanged();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                CommonUtils.showToast("error");
                            }
                        });
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

    private void addToPurchasedProducts(final String productId, final int position) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Mark as purchased?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mDatabase.child("Purchases").child("PendingPurchases").child(productId).child("purchased").setValue(true);

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

    private void removeValueFromDb(String productId) {
        mDatabase.child("Purchases").child("PendingPurchases").child(productId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    private void updatePOCount() {
        mDatabase.child("Accounts").child("PurchaseOrderCount").setValue(poNumber);
    }

    private void getPurchaseOrderCountFromDb() {
        mDatabase.child("Accounts").child("PurchaseOrderCount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    poNumber = dataSnapshot.getValue(Integer.class) + 1;
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private long calculateTotal() {
        long total = 0;
        for (ProductCountModel model : itemList) {
            total += model.getQuantity() * model.getProduct().getCostPrice();
        }
        return total;
    }

    private void getVendorsFromDb() {
        mDatabase.child("Vendors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    vendorModelArrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot != null) {
                            VendorModel model = snapshot.getValue(VendorModel.class);
                            if (model != null) {
                                vendorModelArrayList.add(model);
                                setUpSpinner();
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpSpinner() {


        ArrayList<String> items = new ArrayList<>();
        items.clear();
        for (int i = 0; i < vendorModelArrayList.size(); i++) {
            items.add("" + vendorModelArrayList.get(i).getVendorName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

                vendor = vendorModelArrayList.get(position);
                getDataFromDb(vendor.getVendorId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    private void getDataFromDb(final String vendorId) {
        progress.setVisibility(View.VISIBLE);

        mDatabase.child("Purchases").child("PendingPurchases").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    newList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ProductCountModel model = snapshot.getValue(ProductCountModel.class);
                        if (model != null) {
                            if (vendorId != null) {
                                if (model.getProduct().getVendor().getVendorId().equalsIgnoreCase(vendorId)) {
                                    itemList.add(model);
                                    if (model.isPurchased()) {
                                        newList.add(model);
                                    }
                                    Collections.sort(itemList, new Comparator<ProductCountModel>() {
                                        @Override
                                        public int compare(ProductCountModel listData, ProductCountModel t1) {
                                            Long ob1 = listData.getTime();
                                            Long ob2 = t1.getTime();

                                            return ob2.compareTo(ob1);

                                        }
                                    });
                                    adapter.notifyDataSetChanged();
                                    progress.setVisibility(View.GONE);

                                } else {
                                    progress.setVisibility(View.GONE);
                                }
                            }
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


}
