package com.appsinventiv.toolsbazzaradmin.Activities.Products;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Products.Reviews.ProductReviews;
import com.appsinventiv.toolsbazzaradmin.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AliAh on 20/06/2018.
 */

public class NewProductsAdapter extends RecyclerView.Adapter<NewProductsAdapter.ViewHolder> {
    Context context;
    ArrayList<NewProductsModel> productList;
    private ArrayList<NewProductsModel> arrayList;


    public NewProductsAdapter(Context context, ArrayList<NewProductsModel> productList) {
        this.context = context;
        this.productList = productList;
        this.arrayList = new ArrayList<>(productList);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.new_product_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final NewProductsModel model = productList.get(position);


        holder.title.setText(model.getStoreName());
        holder.sellerStatus.setText(model.getStatus());
        holder.count.setText(model.getCount()+"");

//        holder.count.setVisibility(View.GONE);

        if (model.getImageUrl() == null) {
            Glide.with(context).load(R.drawable.ic_fort_placeholder).into(holder.image);

        } else if(model.getImageUrl().equalsIgnoreCase("")) {
            Glide.with(context).load(R.drawable.app_icon).into(holder.image);

        }else{
            Glide.with(context).load(model.getImageUrl()).into(holder.image);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProductReviews.class);
                i.putExtra("vendorId", model.getStoreId());
                i.putExtra("storeName", model.getStoreName());
                context.startActivity(i);
            }
        });


    }

    public void updatelist(ArrayList<NewProductsModel> productList) {
        this.productList = productList;
        arrayList.clear();
        arrayList.addAll(productList);
        notifyDataSetChanged();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        productList.clear();
        if (charText.length() == 0) {
            productList.addAll(arrayList);
        } else {
            for (NewProductsModel product : arrayList) {
                if (product.getStoreName().toLowerCase().contains(charText)
                        ) {
                    productList.add(product);
                }
            }


        }
        notifyDataSetChanged();

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, sellerStatus, count;
        CircleImageView image;


        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            sellerStatus = itemView.findViewById(R.id.sellerStatus);
            count = itemView.findViewById(R.id.count);
            image = itemView.findViewById(R.id.image);


        }
    }


}
