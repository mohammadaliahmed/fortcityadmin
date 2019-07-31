package com.appsinventiv.toolsbazzaradmin.Activities.Accounts.SODetails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.appsinventiv.toolsbazzaradmin.Activities.Accounts.NewAccounts.NewAccountModel;
import com.appsinventiv.toolsbazzaradmin.Activities.Accounts.NewAccounts.NewAccountsAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.InvoiceModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NewSOFragment extends Fragment {

    Context context;
    String orderStatus;
    NewSOAdapter adapter;
    ProgressBar progress;
    DatabaseReference mDatabase;
    String by;

    private ArrayList<NewSOModel> orderModelArrayList = new ArrayList<>();
    private ArrayList<NewSOModel> itemList = new ArrayList<>();
    HashMap<String, NewSOModel> map = new HashMap<>();
    ImageView noOrder;

    public NewSOFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public NewSOFragment(String orderStatus) {
        this.orderStatus = orderStatus;

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
        View rootView = inflater.inflate(R.layout.fragment_orders, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_orders);
        progress = rootView.findViewById(R.id.progress);
        noOrder = rootView.findViewById(R.id.noOrder);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewSOAdapter(context, itemList);
        recyclerView.setAdapter(adapter);
        if (Constants.SHIPPING_COMPANY.equalsIgnoreCase("Fort City")) {
            getDeliveryBoyData();
        } else {
            getVendorSOFromDB();
        }

        return rootView;

    }

    private void getVendorSOFromDB() {
        mDatabase.child("Accounts").child("PendingInvoices").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    map.clear();
                    int count = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        InvoiceModel invoiceModel = snapshot.getValue(InvoiceModel.class);
                        if (invoiceModel != null) {
                            if (invoiceModel.getOrder() != null && invoiceModel.getShipping() != null) {
                                if (invoiceModel.getShipping().getName().equalsIgnoreCase(Constants.SHIPPING_COMPANY)) {
                                    if (invoiceModel.getDeliveryBy().equalsIgnoreCase("")) {

                                        if (map.get(invoiceModel.getOrder().getVendor().getStoreName()) != null) {
                                            count = map.get(invoiceModel.getOrder().getVendor().getStoreName()).getCount();
                                        } else {
                                            count = 0;
                                        }

                                        if (map.containsKey(invoiceModel.getOrder().getVendor().getStoreName())) {
                                            count = count + 1;
                                            map.put(invoiceModel.getOrder().getVendor().getStoreName(), new NewSOModel(invoiceModel.getOrder().getVendor().getStoreName(), invoiceModel.getOrder().getVendor().getStoreName(), orderStatus, invoiceModel.getOrder().getVendor().getPicUrl(), count));

                                        } else {
                                            if (invoiceModel.getOrder().getVendor().getStoreName() != null) {
                                                map.put(invoiceModel.getOrder().getVendor().getStoreName(), new NewSOModel(invoiceModel.getOrder().getVendor().getStoreName(), invoiceModel.getOrder().getVendor().getStoreName(), orderStatus, invoiceModel.getOrder().getVendor().getPicUrl(), 1));
                                            }

                                        }
                                    }
                                }
                            }


                        }
                    }
                    itemList.clear();
                    for (Map.Entry<String, NewSOModel> entry : map.entrySet()) {


                        itemList.add(entry.getValue());

                    }
                    adapter.notifyDataSetChanged();
                } else {
                    noOrder.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getDeliveryBoyData() {
        mDatabase.child("Accounts").child("PendingInvoices").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    map.clear();
                    int count = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        InvoiceModel invoiceModel = snapshot.getValue(InvoiceModel.class);
                        if (invoiceModel != null) {
                            if (invoiceModel.getOrder() != null && invoiceModel.getShipping() != null) {
                                if (invoiceModel.getDeliveryBy() != null && !invoiceModel.getDeliveryBy().equalsIgnoreCase("")) { //fort city self delivery

                                    //other shipping carriers
                                    if (map.get(invoiceModel.getDeliveryBy()) != null) {
                                        count = map.get(invoiceModel.getDeliveryBy()).getCount();
                                    } else {
                                        count = 0;
                                    }

                                    if (map.containsKey(invoiceModel.getDeliveryBy())) {
                                        count = count + 1;
                                        map.put(invoiceModel.getDeliveryBy(), new NewSOModel("Fort City", invoiceModel.getDeliveryBy(), orderStatus, "", count));

                                    } else {

                                        map.put(invoiceModel.getDeliveryBy(), new NewSOModel("Fort City", invoiceModel.getDeliveryBy(), orderStatus, "", 1));


                                    }

                                }
                            }
                        }
                    }
                    itemList.clear();
                    for (Map.Entry<String, NewSOModel> entry : map.entrySet()) {
                        itemList.add(entry.getValue());
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    noOrder.setVisibility(View.VISIBLE);
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
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


}
