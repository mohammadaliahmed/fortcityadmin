package com.appsinventiv.toolsbazzaradmin.Activities.Accounts.NewAccounts;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Accounts.SODetails.NewSOScreen;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.NewProductsModel;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.Reviews.ProductReviews;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.Constants;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AliAh on 20/06/2018.
 */

public class NewAccountsAdapter extends RecyclerView.Adapter<NewAccountsAdapter.ViewHolder> {
    Context context;
    ArrayList<NewAccountModel> productList;
    private ArrayList<NewAccountModel> arrayList;


    public NewAccountsAdapter(Context context, ArrayList<NewAccountModel> productList) {
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
        final NewAccountModel model = productList.get(position);


        holder.title.setText(model.getShippingName());
        holder.sellerStatus.setText(model.getStatus());
        holder.count.setText(model.getCount() + "");

        holder.text.setVisibility(View.GONE);
        if (model.getImageUrl() == null) {
            Glide.with(context).load(R.drawable.ic_fort_placeholder).into(holder.image);

        } else if (model.getImageUrl().equalsIgnoreCase("")) {
            Glide.with(context).load(R.drawable.admin_logo).into(holder.image);

        } else {
            Glide.with(context).load(model.getImageUrl()).into(holder.image);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, NewSOScreen.class);
                i.putExtra("vendorId", model.getShippingId());
                i.putExtra("storeName", model.getShippingName());
                if (model.getShippingName().contains("Fort City")) {
                    Constants.SHIPPING_COMPANY = "Fort City";
                } else {
                    Constants.TYPE_OF_SO = model.getShippingName();
                    Constants.SHIPPING_COMPANY = model.getShippingName();
                }
                context.startActivity(i);
            }
        });


    }

    public void updatelist(ArrayList<NewAccountModel> productList) {
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
            for (NewAccountModel product : arrayList) {
                if (product.getShippingName().toLowerCase().contains(charText)
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
        TextView title, sellerStatus, count,text;
        CircleImageView image;


        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            sellerStatus = itemView.findViewById(R.id.sellerStatus);
            count = itemView.findViewById(R.id.count);
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);


        }
    }


}
