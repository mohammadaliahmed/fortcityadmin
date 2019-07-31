package com.appsinventiv.toolsbazzaradmin.Activities.Accounts.NewAccounts;

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

import com.appsinventiv.toolsbazzaradmin.Activities.Orders.NewOrder.NewOrderModel;
import com.appsinventiv.toolsbazzaradmin.Activities.Orders.NewOrder.NewOrdersAdapter;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.NewProductsModel;
import com.appsinventiv.toolsbazzaradmin.Models.InvoiceModel;
import com.appsinventiv.toolsbazzaradmin.Models.OrderModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NewAccountsFragment extends Fragment {

    Context context;
    String orderStatus;
    NewAccountsAdapter adapter;
    ProgressBar progress;
    DatabaseReference mDatabase;
    String by;

    private ArrayList<NewAccountModel> orderModelArrayList = new ArrayList<>();
    private ArrayList<NewAccountModel> itemList = new ArrayList<>();
    HashMap<String, NewAccountModel> map = new HashMap<>();
    ImageView noOrder;

    public NewAccountsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public NewAccountsFragment(String orderStatus) {
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
        adapter = new NewAccountsAdapter(context, itemList);
        recyclerView.setAdapter(adapter);
        if (orderStatus.equalsIgnoreCase("pending so")) {
            getPendingInvoicesFromDB();
        }

        return rootView;

    }

    private void getPendingInvoicesFromDB() {
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
                                    if (map.get("Fort City") != null) {
                                        count = map.get("Fort City").getCount();

                                    } else {
                                        count = 0;
                                    }
                                    if (map.containsKey("Fort City")) {
                                        map.put("Fort City", new NewAccountModel("Fort City", "Fort City Sales Orders", orderStatus, "", count));

                                    } else {
                                        map.put("Fort City", new NewAccountModel("Fort City", "Fort City Sales Orders", orderStatus, "", 1));
                                    }

                                } else { //other shipping carriers
                                    if (map.get(invoiceModel.getShipping().getId()) != null) {
                                        count = map.get(invoiceModel.getShipping().getId()).getCount();
                                    } else {
                                        count = 0;
                                    }

                                    if (map.containsKey(invoiceModel.getShipping().getId())) {
                                        count = count + 1;
                                        map.put(invoiceModel.getShipping().getId(), new NewAccountModel(invoiceModel.getShipping().getId(), invoiceModel.getShipping().getName(), orderStatus, invoiceModel.getShipping().getPicUrl(), count));

                                    } else {
                                        map.put(invoiceModel.getShipping().getId(), new NewAccountModel(invoiceModel.getShipping().getId(), invoiceModel.getShipping().getName(), orderStatus, invoiceModel.getShipping().getPicUrl(), 1));

                                    }
                                }
                            }

                        }
                    }
                    itemList.clear();
                    for (Map.Entry<String, NewAccountModel> entry : map.entrySet()) {
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
        getDataFromServer();
    }

    private void getDataFromServer() {


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


}
