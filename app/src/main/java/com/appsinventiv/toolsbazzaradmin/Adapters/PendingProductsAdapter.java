package com.appsinventiv.toolsbazzaradmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Purchases.EditPurchase;
import com.appsinventiv.toolsbazzaradmin.Models.ProductCountModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by AliAh on 07/09/2018.
 */

public class PendingProductsAdapter extends RecyclerView.Adapter<PendingProductsAdapter.ViewHolder> {
    Context context;
    ArrayList<ProductCountModel> itemList;
    IsPurchased isPurchased;

    public PendingProductsAdapter(Context context, ArrayList<ProductCountModel> itemList, IsPurchased isPurchased) {
        this.context = context;
        this.itemList = itemList;
        this.isPurchased = isPurchased;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pending_product_item_layout, parent, false);
        PendingProductsAdapter.ViewHolder viewHolder = new PendingProductsAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ProductCountModel model = itemList.get(position);

        if (model.isPurchased()) {
            holder.purchased.setChecked(true);
            holder.purchased.setText("Purchased");
        } else {
            holder.purchased.setChecked(false);
            holder.purchased.setText("Pending");

        }

        Glide.with(context).load(model.getProduct().getThumbnailUrl()).into(holder.image);
        holder.title.setText(model.getProduct().getTitle());
        holder.quantity.setText("Total Quantity: " + model.getQuantity());
        holder.costPrice.setText("Cost Price: Rs." + model.getProduct().getCostPrice());
        holder.totalPrice.setText("Order Total: Rs." + (model.getProduct().getCostPrice() * model.getQuantity()));
        ArrayList<String> abc = new ArrayList<>();
        for (int i = 0; i < model.getOrderId().size(); i++) {
            abc.add("" + model.getOrderId().keySet().toArray()[i]);
        }
        holder.orderIds.setText("Order Ids: " + abc);
        holder.purchased.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isPurchased.addToArray(model.getProduct().getId(), position);
                    holder.purchased.setChecked(true);
                } else {
                    isPurchased.removeFromArray(model.getProduct().getId(), position);
                    holder.purchased.setChecked(false);

                }
            }
        });

        if (model.getQuantityPurchased() != 0) {
            holder.edited.setVisibility(View.VISIBLE);
            holder.quantityPurchased.setText("Qty purchased: " + model.getQuantityPurchased() + "");
            holder.outOfStockQty.setText("Out of stock: " + model.getOutOfStock() + "");
            holder.newCostPrice.setText("New / Old cost price: Rs." + model.getNewCostPrice() + "");
            holder.puchaseTotal.setText("Purchase Total: Rs." + model.getPurchaseTotal() + "");

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, EditPurchase.class);
                i.putExtra("id", model.getProduct().getId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, quantity, orderIds, costPrice, totalPrice;
        ImageView image;
        Switch purchased;

        RelativeLayout edited;
        TextView quantityPurchased, outOfStockQty, newCostPrice, puchaseTotal;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            quantity = itemView.findViewById(R.id.quantity);
            orderIds = itemView.findViewById(R.id.orderIds);
            costPrice = itemView.findViewById(R.id.costPrice);
            image = itemView.findViewById(R.id.image);
            purchased = itemView.findViewById(R.id.purchased);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            edited = itemView.findViewById(R.id.edited);
            quantityPurchased = itemView.findViewById(R.id.quantityPurchased);
            outOfStockQty = itemView.findViewById(R.id.outOfStockQty);
            newCostPrice = itemView.findViewById(R.id.newCostPrice);
            puchaseTotal = itemView.findViewById(R.id.puchaseTotal);


        }
    }

    public interface IsPurchased {
        public void addToArray(String id, int position);

        public void removeFromArray(String id, int position);
    }
}
