package com.appsinventiv.toolsbazzaradmin.Activities.Products;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.appsinventiv.toolsbazzaradmin.Models.Product;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.SwipeToDeleteCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class OutOfStockProductsListFragment extends Fragment {

    DatabaseReference mDatabase;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    //    ProductsAdapter adapter;
    NewProductsAdapter adapter;
    private Drawable icon;
    private ColorDrawable background;
    SwipeToDeleteCallback swipeController = null;


    Context context;
    private ArrayList<Product> productArrayList = new ArrayList<>();
    private ArrayList<NewProductsModel> itemList = new ArrayList<>();
    HashMap<String, NewProductsModel> map = new HashMap<>();
    ProgressBar progress;


    public OutOfStockProductsListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.product_list_fragment_layout, container, false);
        progress = rootView.findViewById(R.id.progress);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        FloatingActionButton addProduct = rootView.findViewById(R.id.addProduct);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AddProduct.class);
                startActivity(i);
            }
        });
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        adapter = new NewProductsAdapter(context, itemList);
        recyclerView.setAdapter(adapter);

        getDataFromDb();
        return rootView;

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void getDataFromDb() {


        mDatabase.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    productArrayList.clear();
                    map.clear();
                    int count = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Product product = snapshot.getValue(Product.class);
                        if (product != null) {

                            if (product.getUploadedBy() != null) {
                                if (product.getUploadedBy().equalsIgnoreCase("admin")) {
                                    if (map.get("Fort City") != null) {

                                        count = map.get("Fort City").getCount();

                                    } else {
                                        count = 0;
                                    }

                                    if (map.containsKey("Fort City")) {
                                        count = count + 1;
                                        if (product.getQuantityAvailable() < 1) {
                                            map.put("Fort City", new NewProductsModel(

                                                    "Fort City", "Fort City", product.getSellerProductStatus(), "", count
                                            ));
                                        }
                                    } else {
                                        if (product.getQuantityAvailable() < 1) {
                                            map.put("Fort City", new NewProductsModel(
                                                    "Fort City", "Fort City", product.getSellerProductStatus(), "", 1
                                            ));
                                        }
                                    }

                                } else if (product.getUploadedBy().equalsIgnoreCase("seller")) {
                                    if (map.get(product.getVendor().getStoreName()) != null) {
                                        count = map.get(product.getVendor().getStoreName()).getCount();
                                    } else {
                                        count = 0;
                                    }

                                    if (map.containsKey(product.getVendor().getStoreName())) {
                                        count = count + 1;
//                                        if(product.getSellerProductStatus()!=null){
                                        if (product.getQuantityAvailable() < 1) {
                                            map.put(product.getVendor().getStoreName(), new NewProductsModel(
                                                    product.getVendor().getUsername(),
                                                    product.getVendor().getStoreName(),
                                                    product.getSellerProductStatus(),
                                                    product.getVendor().getPicUrl(),
                                                    count
                                            ));

                                        }
                                    } else {
                                        if (product.getQuantityAvailable() < 1) {
                                            map.put(product.getVendor().getStoreName(), new NewProductsModel(
                                                    product.getVendor().getUsername(),
                                                    product.getVendor().getStoreName(),
                                                    product.getSellerProductStatus(),
                                                    product.getVendor().getPicUrl(),
                                                    1
                                            ));

                                        }
                                    }
                                }


                            }
                        }


                    }
                    if (map.size() > 0)

                    {
                        itemList.clear();
                        for (Map.Entry<String, NewProductsModel> entry : map.entrySet()) {
                            itemList.add(entry.getValue());
                            progress.setVisibility(View.GONE);
                        }
                        adapter.updatelist(itemList);
                        adapter.notifyDataSetChanged();

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.search_menu, menu);
        final MenuItem mSearch = menu.findItem(R.id.action_search);
//        mSearch.expandActionView();
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
//                if (newText.length() > 0) {
////                    adapter.filter(newText);
////                    getUserCartProductsFromDB();
//                }
                return false;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
