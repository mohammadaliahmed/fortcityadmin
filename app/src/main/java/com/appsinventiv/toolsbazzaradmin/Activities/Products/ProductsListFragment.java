package com.appsinventiv.toolsbazzaradmin.Activities.Products;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appsinventiv.toolsbazzaradmin.Activities.Accounts.TransferToAccountsDone;
import com.appsinventiv.toolsbazzaradmin.Adapters.InvoiceListAdapter;
import com.appsinventiv.toolsbazzaradmin.Adapters.ProductsAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.InvoiceModel;
import com.appsinventiv.toolsbazzaradmin.Models.Product;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.SwipeControllerActions;
import com.appsinventiv.toolsbazzaradmin.Utils.SwipeToDeleteCallback;
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


public class ProductsListFragment extends Fragment {

    DatabaseReference mDatabase;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ProductsAdapter adapter;
    private Drawable icon;
    private ColorDrawable background;
    SwipeToDeleteCallback swipeController = null;


    Context context;
    private ArrayList<Product> productArrayList = new ArrayList<>();


    public ProductsListFragment() {
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

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        FloatingActionButton addProduct =  rootView.findViewById(R.id.addProduct);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,AddProduct.class);
                startActivity(i);
            }
        });
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);

        adapter = new ProductsAdapter(context, productArrayList, new ProductsAdapter.OnProductStatusChanged() {
            @Override
            public void onStatusChanged(Product product, final boolean status) {
                mDatabase.child("Products").child(product.getId()).child("isActive").setValue("" + status)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if (status) {
                                    CommonUtils.showToast("Product is now active");
                                } else {
                                    CommonUtils.showToast("Product is now inactive");
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        CommonUtils.showToast("There was some error");

                    }
                });

            }

            @Override
            public void onDelete(final Product product) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Delete product?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                deleteProduct(product.getId());
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
        });


        recyclerView.setAdapter(adapter);
        swipeController = new SwipeToDeleteCallback(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Delete product?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                deleteProduct(productArrayList.get(position).getId());
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
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

        return rootView;

    }

    private void deleteProduct(String id) {

        mDatabase.child("Products").child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Product deleted");
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        getDataFromDb();
    }

    private void getDataFromDb() {
        mDatabase.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    productArrayList.clear();
//                    context.setTitle(dataSnapshot.getChildrenCount()+" Products");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Product product = snapshot.getValue(Product.class);
                        if (product != null) {
                            if (product.getUploadedBy() != null) {
                                if (product.getUploadedBy().equalsIgnoreCase("admin")) {
                                    productArrayList.add(product);
                                }
                            } else {
                                productArrayList.add(product);
                            }


                        }
                    }
                    Collections.sort(productArrayList, new Comparator<Product>() {
                        @Override
                        public int compare(Product listData, Product t1) {
                            String ob1 = listData.getTitle();
                            String ob2 = t1.getTitle();

                            return ob1.compareTo(ob2);

                        }
                    });
                    adapter.updatelist(productArrayList);

                    adapter.notifyDataSetChanged();
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
//            Intent i = new Intent(MainActivity.this, Search.class);
//            startActivity(i);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//
//    public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
//        ProductsAdapter adapter;
//
//
//        public SwipeToDeleteCallback(ProductsAdapter adapter) {
//            super(0, ItemTouchHelper.LEFT );
//            this.adapter = adapter;
//            icon = ContextCompat.getDrawable(context,
//                    R.drawable.ic_cancel);
//
//            background = new ColorDrawable(Color.RED);
//        }
//
//        @Override
//        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//            return false;
//        }
//
//        @Override
//        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//            int position = viewHolder.getAdapterPosition();
//            CommonUtils.showToast(""+position);
////            adapter.deleteItem(position);
//        }
//
//        @Override
//        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//            super.onChildDraw(c, recyclerView, viewHolder, dX,
//                    dY, actionState, isCurrentlyActive);
//            View itemView = viewHolder.itemView;
//            int backgroundCornerOffset = 20;
////            if (dX > 0) { // Swiping to the right
////                background.setBounds(itemView.getLeft(), itemView.getTop(),
////                        itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
////                        itemView.getBottom());
////
////            } else
//                if (dX < 0) { // Swiping to the left
//                background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
//                        itemView.getTop(), itemView.getRight(), itemView.getBottom());
//            } else { // view is unSwiped
//                background.setBounds(0, 0, 0, 0);
//            }
//            background.draw(c);
//            int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
//            int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
//            int iconBottom = iconTop + icon.getIntrinsicHeight();
//
////            if (dX > 0) { // Swiping to the right
////                int iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
////                int iconRight = itemView.getLeft() + iconMargin;
////                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
////
////                background.setBounds(itemView.getLeft(), itemView.getTop(),
////                        itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
////                        itemView.getBottom());
////            } else
////
//                if (dX < 0) { // Swiping to the left
//                int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
//                int iconRight = itemView.getRight() - iconMargin;
//                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
//
//                background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
//                        itemView.getTop(), itemView.getRight(), itemView.getBottom());
//            } else { // view is unSwiped
//                background.setBounds(0, 0, 0, 0);
//            }
//
//            background.draw(c);
//            icon.draw(c);
//        }
//    }
}
