package com.appsinventiv.toolsbazzaradmin.Activities.Accounts.Invoices;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.appsinventiv.toolsbazzaradmin.Activities.Callbacks.WhichKey;
import com.appsinventiv.toolsbazzaradmin.Adapters.FinalInvoiceListAdapter;
import com.appsinventiv.toolsbazzaradmin.Adapters.YearViewInvoiceAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.InvoiceModel;
import com.appsinventiv.toolsbazzaradmin.Models.Temporarymodel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FinalizedInvoicesAccountsFragment extends Fragment {

    Context context;
    RelativeLayout wholeLayout;
    ArrayList<InvoiceModel> itemList = new ArrayList<>();
    ArrayList<Temporarymodel> newList = new ArrayList<>();
    YearViewInvoiceAdapter adapter;
    ProgressBar progress;
    DatabaseReference mDatabase;
    String by;
    private RecyclerView recyclerView;

    String what = "year";
    String year = "";
    String month = "";
    String day = "";
    FinalInvoiceListAdapter finalInvoiceAdapter;
    public static String path = "";
    WhichKey whichKey;


    public FinalizedInvoicesAccountsFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        whichKey = (WhichKey) context;
        whichKey.which("Year");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_purchased_order_accounts, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        wholeLayout = rootView.findViewById(R.id.wholeLayout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new YearViewInvoiceAdapter(context, newList, 1, new YearViewInvoiceAdapter.ChangeLayout() {
            @Override
            public void onClick(Temporarymodel model, int position, String type, String key) {
                if (what.equalsIgnoreCase("year")) {
                    getYearDataFromServer(key);
                    what = "month";
                    year = key;
                    whichKey.which("month");

                } else if (what.equalsIgnoreCase("month")) {
                    getMonthDataFromServer(year, key);
                    what = "day";
                    month = key;
                    whichKey.which("days");


                } else if (what.equalsIgnoreCase("day")) {

                    getDayDataFromServer(year, month, key);
//                    what = "itself";
                    day = key;
                    whichKey.which("day");


                }
            }
        });
        recyclerView.setAdapter(adapter);

//        rootView.setFocusableInTouchMode(true);
//        rootView.requestFocus();
//        rootView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
//                if( keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
////                    Log.i(tag, "onKey Back listener is working!!!");
////                    CommonUtils.showToast("back press\n"+what);
////                    if (what.equalsIgnoreCase("year")) {
////                        getYearDataFromServer(year);
////                        what = "month";
////                        year = key;
////
////                    } else if (what.equalsIgnoreCase("month")) {
////                        getMonthDataFromServer( year,key);
////                        what = "day";
////                        month = key;
////
////
////                    } else if (what.equalsIgnoreCase("day")) {
////                        getMonthDataFromServer( year,month);
////                        what = "day";
////
////
////                    } else if (what.equalsIgnoreCase("itself")) {
////                        getYearDataFromServer(key);
////
////                    }
//                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    return true;
//                }
//                return false;
//            }
//
//
//        });

        return rootView;

    }


    private void getDayDataFromServer(String year, String month, String day) {
        wholeLayout.setVisibility(View.VISIBLE);
        String path = year + "/" + month + "/" + day;
        finalInvoiceAdapter = new FinalInvoiceListAdapter(context, itemList, path);
        recyclerView.setAdapter(finalInvoiceAdapter);
        mDatabase.child("Accounts").child("InvoicesFinalized").child(year).child(month).child(day).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    newList.clear();
                    for (DataSnapshot allDays : dataSnapshot.getChildren()) {
                        InvoiceModel model = allDays.getValue(InvoiceModel.class);
                        itemList.add(model);
                        wholeLayout.setVisibility(View.GONE);
                    }
                }
                finalInvoiceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getMonthDataFromServer(String year, String month) {
        wholeLayout.setVisibility(View.VISIBLE);
        mDatabase.child("Accounts").child("InvoicesFinalized").child(year).child(month).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {


                    newList.clear();
                    long count = 0;

                    for (DataSnapshot allMonthSnapshot : dataSnapshot.getChildren()) {
                        float sale = 0;
                        float profit = 0;
                        count = allMonthSnapshot.getChildrenCount();

                        for (DataSnapshot allDays : allMonthSnapshot.getChildren()) {

                            InvoiceModel model = allDays.getValue(InvoiceModel.class);

                            if (model != null) {

                                float otherCharges=((model.getShippingCharges()+model.getDeliveryCharges()));
                                for (int i = 0; i < model.getNewCountModelArrayList().size(); i++) {
                                    sale = sale + (model.getNewCountModelArrayList().get(i).getProduct().getRetailPrice()*model.getNewCountModelArrayList().get(i).getQuantity());
                                    profit = profit  +((model.getNewCountModelArrayList().get(i).getProduct().getRetailPrice() * model.getNewCountModelArrayList().get(i).getQuantity()) -
                                            (model.getNewCountModelArrayList().get(i).getProduct().getCostPrice() * model.getNewCountModelArrayList().get(i).getQuantity())
                                    );
                                }
                                sale=sale+otherCharges;
                                profit=profit+otherCharges;
                            }
                        }
                        newList.add(new Temporarymodel(allMonthSnapshot.getKey(), count, sale, profit));
                        wholeLayout.setVisibility(View.GONE);
                    }
                }
                adapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getYearDataFromServer(String year) {
        wholeLayout.setVisibility(View.VISIBLE);
        mDatabase.child("Accounts").child("InvoicesFinalized").child(year).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    newList.clear();

                    for (DataSnapshot yearSnapshot : dataSnapshot.getChildren()) {
                        float sale = 0;
                        long count = 0;
                        float profit = 0;

                        for (DataSnapshot allMonthSnapshot : yearSnapshot.getChildren()) {
                            count = count + allMonthSnapshot.getChildrenCount();
                            for (DataSnapshot allDays : allMonthSnapshot.getChildren()) {
                                InvoiceModel model = allDays.getValue(InvoiceModel.class);
                                if (model != null) {
                                    float otherCharges=((model.getShippingCharges()+model.getDeliveryCharges()));
                                    for (int i = 0; i < model.getNewCountModelArrayList().size(); i++) {
                                        sale = sale + (model.getNewCountModelArrayList().get(i).getProduct().getRetailPrice()*model.getNewCountModelArrayList().get(i).getQuantity());
                                        profit = profit  +((model.getNewCountModelArrayList().get(i).getProduct().getRetailPrice() * model.getNewCountModelArrayList().get(i).getQuantity()) -
                                                (model.getNewCountModelArrayList().get(i).getProduct().getCostPrice() * model.getNewCountModelArrayList().get(i).getQuantity())
                                        );
                                    }
                                    sale=sale+otherCharges;
                                    profit=profit+otherCharges;
                                }
                            }
                        }

                        newList.add(new Temporarymodel(yearSnapshot.getKey(), count, sale, profit));
                    }
                    wholeLayout.setVisibility(View.GONE);


                }
                adapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getAllDataFromServer() {
        wholeLayout.setVisibility(View.VISIBLE);
        mDatabase.child("Accounts").child("InvoicesFinalized").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    newList.clear();
                    for (DataSnapshot allYearsSnapshot : dataSnapshot.getChildren()) {
                        float sale = 0;
                        long count = 0;
                        float profit = 0;
                        for (DataSnapshot yearSnapshot : allYearsSnapshot.getChildren()) {

                            for (DataSnapshot allMonthSnapshot : yearSnapshot.getChildren()) {
                                count = count + allMonthSnapshot.getChildrenCount();
                                for (DataSnapshot allDays : allMonthSnapshot.getChildren()) {
                                    InvoiceModel model = allDays.getValue(InvoiceModel.class);
                                    if (model != null) {
                                        float otherCharges=((model.getShippingCharges()+model.getDeliveryCharges()));
                                        for (int i = 0; i < model.getNewCountModelArrayList().size(); i++) {
                                            sale = sale + (model.getNewCountModelArrayList().get(i).getProduct().getRetailPrice()*model.getNewCountModelArrayList().get(i).getQuantity());
                                            profit = profit  +((model.getNewCountModelArrayList().get(i).getProduct().getRetailPrice() * model.getNewCountModelArrayList().get(i).getQuantity()) -
                                                    (model.getNewCountModelArrayList().get(i).getProduct().getCostPrice() * model.getNewCountModelArrayList().get(i).getQuantity())
                                            );
                                        }
                                        sale=sale+otherCharges;
                                        profit=profit+otherCharges;
                                    }
                                }
                            }
                        }
                        newList.add(new Temporarymodel(allYearsSnapshot.getKey(), count, sale, profit));
                    }

                    wholeLayout.setVisibility(View.GONE);
                }

                adapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        what = "year";
        year = "";
        month = "";
        day = "";
        path = "";
        getAllDataFromServer();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        what = "year";
        year = "";
        month = "";
        day = "";
        path = "";
        whichKey.which("");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


}
