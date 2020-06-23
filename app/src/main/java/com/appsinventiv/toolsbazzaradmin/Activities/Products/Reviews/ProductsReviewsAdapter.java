package com.appsinventiv.toolsbazzaradmin.Activities.Products.Reviews;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Products.EditProduct;
import com.appsinventiv.toolsbazzaradmin.Models.Product;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.Constants;
import com.appsinventiv.toolsbazzaradmin.Utils.SharedPrefs;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by AliAh on 23/06/2018.
 */

public class ProductsReviewsAdapter extends RecyclerView.Adapter<ProductsReviewsAdapter.Viewholder> {
    Context context;
    ArrayList<Product> productList;
    //    private ArrayList<Product> arrayList = new ArrayList<>();
    SellerProductsAdapterCallbacks callbacks;

    public ProductsReviewsAdapter(Context context,
                                  ArrayList<Product> productList
    ) {
        this.context = context;
        this.productList = productList;

    }

    public void setCallbacks(SellerProductsAdapterCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    @NonNull
    @Override
    public ProductsReviewsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.seller_product_review_item_layout, parent, false);
        ProductsReviewsAdapter.Viewholder viewHolder = new ProductsReviewsAdapter.Viewholder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductsReviewsAdapter.Viewholder holder, final int position) {
        final Product model = productList.get(position);


        HashMap<String, Double> map = SharedPrefs.getCommentsCount();

        if (map != null && map.size() > 0) {

            if (map.get(model.getId()) != null) {
                if (map.get(model.getId()) == 0) {
                    holder.unreadCommentCount.setVisibility(View.GONE);
                } else {
                    holder.unreadCommentCount.setVisibility(View.VISIBLE);
                    holder.unreadCommentCount.setText("" + map.get(model.getId()).toString().replace(".0", ""));

                }

            } else {
                holder.unreadCommentCount.setVisibility(View.GONE);
            }
        } else {
            holder.unreadCommentCount.setVisibility(View.GONE);
        }
        holder.title.setText(model.getTitle());
        holder.productStatus.setText("Status: " + model.getSellerProductStatus());

        holder.ratingBar.setRating(model.getRating());
        holder.subtitle.setText(model.getSubtitle());
        Glide.with(context).load(model.getThumbnailUrl()).placeholder(R.drawable.placeholder).into(holder.image);
        holder.quantity.setText("Quantity in stock: " + model.getQuantityAvailable());

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProductComments.class);
                i.putExtra("productId", model.getId());
                context.startActivity(i);
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, EditProduct.class);
                i.putExtra("productId", model.getId());
                Constants.PRODUCT_ID=model.getId();

                context.startActivity(i);
//                callbacks.onReject(model);

            }
        });

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View layout = layoutInflater.inflate(R.layout.popup_approve_design, null);

                dialog.setContentView(layout);

                TextView title = layout.findViewById(R.id.title);
                Switch approveSwitch = layout.findViewById(R.id.approveSwitch);
                Switch active = layout.findViewById(R.id.active);
                ImageView img = layout.findViewById(R.id.img);

                Glide.with(context).load(model.getThumbnailUrl()).into(img);

                title.setText("" + (model.getTitle().length() > 30 ? model.getTitle().substring(0, 30) + "..." : model.getTitle()));

                if (model.getSellerProductStatus().equalsIgnoreCase("Approved")) {
                    approveSwitch.setChecked(true);
                } else if (model.getSellerProductStatus().equalsIgnoreCase("Pending")) {
                    approveSwitch.setChecked(false);
                }
                if (model.isActive()) {
                    active.setChecked(true);
                } else {
                    active.setChecked(false);
                }
                approveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (compoundButton.isPressed()) {
//                            callbacks.onChangeStatus(model, b);
                            callbacks.onStatusChange(model, b ? "Approved" : "Pending");
                        }
                    }
                });
                active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (compoundButton.isPressed()) {
                            model.setActive(b);

                            callbacks.onActiveStatusChange(model, b);
                        }
                    }
                });

                dialog.show();


            }
        });


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        ImageView image, menu;
        TextView quantity, unreadCommentCount, productStatus;
        RatingBar ratingBar;
        RelativeLayout comments;

        public Viewholder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            image = itemView.findViewById(R.id.image);
            quantity = itemView.findViewById(R.id.quantity);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            comments = itemView.findViewById(R.id.comments);
            productStatus = itemView.findViewById(R.id.productStatus);
            unreadCommentCount = itemView.findViewById(R.id.unreadCommentCount);
            menu = itemView.findViewById(R.id.menu);


        }
    }

    public interface SellerProductsAdapterCallbacks {
        public void onStatusChange(Product product, String status);

        public void onActiveStatusChange(Product product, boolean status);

        public void onReject(Product product);
    }
}
