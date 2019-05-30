package com.appsinventiv.toolsbazzaradmin.Activities.Accounts.ExpensesAndRevenue;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Callbacks.WhichKey;
import com.appsinventiv.toolsbazzaradmin.Adapters.FinalInvoiceListAdapter;
import com.appsinventiv.toolsbazzaradmin.Adapters.YearViewInvoiceAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.ExpensesAndRevenueModelMap;
import com.appsinventiv.toolsbazzaradmin.Models.InvoiceModel;
import com.appsinventiv.toolsbazzaradmin.Models.OrderModel;
import com.appsinventiv.toolsbazzaradmin.Models.PurchaseOrderModel;
import com.appsinventiv.toolsbazzaradmin.Models.Temporarymodel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class ExpensesAndRevenueFragment extends Fragment {

    Context context;
    DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    String what = "year";
    String year = "";
    String month = "";
    String day = "";
    public String path = "";
    WhichKey whichKey;
    ArrayList<ExpensesModel> itemList = new ArrayList<>();
    ExpensesAndRevenueAdapter adapter;

    public ExpensesAndRevenueFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_expenses_and_revenue, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        adapter = new ExpensesAndRevenueAdapter(context, itemList, "1", new ExpensesAndRevenueAdapter.ChangeLayout() {
            @Override
            public void onClick(ExpensesModel model, int position, String type, String key) {
                if (what.equalsIgnoreCase("year")) {
                    year = key;

                    getDataFromServer(key);
                    what = "month";
                    whichKey.which("month");

                } else if (what.equalsIgnoreCase("month")) {
                    month = key;
                    Intent i = new Intent(context, ExpensesDetail.class);
                    i.putExtra("path", year + "/" + key);
                    context.startActivity(i);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        return rootView;

    }


    @Override
    public void onResume() {
        super.onResume();
        what = "year";
        year = "";
        month = "";
        day = "";

        getALLDataFromServer();
//        getDataFromServer();


    }

    private void getDataFromServer(final String year) {
        itemList.clear();
        mDatabase.child("Accounts").child("ExpensesAndRevenue").child(year).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

//                    for (DataSnapshot allYears : dataSnapshot.getChildren()) {

                    for (DataSnapshot allMonth : dataSnapshot.getChildren()) {
                        float total = 0;
                        float purchase = 0;
                        float sale = 0;
                        ExpensesAndRevenueModelMap modelMap = allMonth.getValue(ExpensesAndRevenueModelMap.class);
                        float salary = 0;
                        if (allMonth.child("Salaries").getValue() != null) {
                            salary = allMonth.child("Salaries").child("total").getValue(Float.class);
                            total = total + salary;
                        }
                        if (modelMap.getRent() != null) {
                            total = total + modelMap.getRent().getTotal();

                        }
                        if (modelMap.getStationaries() != null) {
                            total = total + modelMap.getStationaries().getTotal();

                        }
                        if (modelMap.getTransportation() != null) {
                            total = total + modelMap.getTransportation().getTotal();

                        }
                        if (modelMap.getUtilityBills() != null) {
                            total = total + modelMap.getUtilityBills().getTotal();

                        }

                        if (modelMap.getMiscellaneous() != null) {
                            total = total + modelMap.getMiscellaneous().getTotal();

                        }

                        if (allMonth.child("PO").getValue() != null) {

                            for (DataSnapshot allDays : allMonth.child("PO").getChildren()) {
                                for (DataSnapshot oneDay : allDays.getChildren()) {
                                    float abc = oneDay.getValue(Float.class);
                                    purchase = purchase + abc;
                                }
                            }

                        }
                        if (allMonth.child("SO").getValue() != null) {

                            for (DataSnapshot allDays : allMonth.child("SO").getChildren()) {
                                for (DataSnapshot oneDay : allDays.getChildren()) {
                                    float abc = oneDay.getValue(Float.class);
                                    sale = sale + abc;
                                }
                            }

                        }

                        float profit = sale - purchase;
                        String leftTotal = "Sale: Rs " + sale + "\nPurchase: Rs" + purchase + "\nProfit: Rs " + (sale - purchase) + "" + "\nCost: Rs " + purchase + "";
                        String rightTotal = "Expense: Rs " + total + "\nPurchase Banking: Rs" + purchase + "\nNet Profit: Rs " + (profit - total) + "\nLoss: Rs " + ((profit - total) < 0 ? (profit - total) : 0);

                        itemList.add(new ExpensesModel(allMonth.getKey(), leftTotal, rightTotal, year, ""));

                    }
                    Collections.sort(itemList, new Comparator<ExpensesModel>() {
                        @Override
                        public int compare(ExpensesModel listData, ExpensesModel t1) {

                            SimpleDateFormat s = new SimpleDateFormat("MMM");
                            Date s1 = null;
                            Date s2 = null;
                            try {
                                s1 = s.parse(listData.getKey());
                                s2 = s.parse(t1.getKey());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return s2.compareTo(s1);

                        }
                    });
//
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getALLDataFromServer() {
        whichKey.which("Year");

        itemList.clear();
        mDatabase.child("Accounts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot allYears : dataSnapshot.child("ExpensesAndRevenue").getChildren()) {
                        float total = 0;
                        float purchase = 0;
                        float sale = 0;
                        for (DataSnapshot allMonth : allYears.getChildren()) {

                            ExpensesAndRevenueModelMap modelMap = allMonth.getValue(ExpensesAndRevenueModelMap.class);
                            float salary = 0;
                            if (allMonth.child("Salaries").getValue() != null) {
                                salary = allMonth.child("Salaries").child("total").getValue(Float.class);
                                total = total + salary;
                            }
                            if (modelMap.getRent() != null) {
                                total = total + modelMap.getRent().getTotal();

                            }
                            if (modelMap.getStationaries() != null) {
                                total = total + modelMap.getStationaries().getTotal();

                            }
                            if (modelMap.getTransportation() != null) {
                                total = total + modelMap.getTransportation().getTotal();

                            }
                            if (modelMap.getUtilityBills() != null) {
                                total = total + modelMap.getUtilityBills().getTotal();

                            }

                            if (modelMap.getMiscellaneous() != null) {
                                total = total + modelMap.getMiscellaneous().getTotal();

                            }

                            if (allMonth.child("PO").getValue() != null) {

                                for (DataSnapshot allDays : allMonth.child("PO").getChildren()) {
                                    for (DataSnapshot oneDay : allDays.getChildren()) {
                                        float abc = oneDay.getValue(Float.class);
                                        purchase = purchase + abc;
                                    }
                                }

                            }
                            if (allMonth.child("SO").getValue() != null) {

                                for (DataSnapshot allDays : allMonth.child("SO").getChildren()) {
                                    for (DataSnapshot oneDay : allDays.getChildren()) {
                                        float abc = oneDay.getValue(Float.class);
                                        sale = sale + abc;
                                    }
                                }

                            }


                        }
                        float profit = sale - purchase;
                        String leftTotal = "Sale: Rs " + sale + "\nPurchase: Rs" + purchase + "\nProfit: Rs " + (sale - purchase) + "" + "\nCost: Rs " + purchase + "";
                        String rightTotal = "Expense: Rs " + total + "\nPurchase Banking: Rs" + purchase + "\nNet Profit: Rs " + (profit - total) + "\nLoss: Rs " + ((profit - total) < 0 ? (profit - total) : 0);

                        itemList.add(new ExpensesModel(allYears.getKey(), leftTotal, rightTotal, "", ""));

                    }
                    Collections.sort(itemList, new Comparator<ExpensesModel>() {
                        @Override
                        public int compare(ExpensesModel listData, ExpensesModel t1) {
                            String ob1 = listData.getKey();
                            String ob2 = t1.getKey();

                            return ob2.compareTo(ob1);

                        }
                    });

                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        what = "year";
        year = "";
        month = "";
        day = "";
        path = "";
//        whichKey.which("");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


}
