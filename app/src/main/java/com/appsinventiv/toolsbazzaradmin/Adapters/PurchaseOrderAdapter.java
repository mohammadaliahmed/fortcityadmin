package com.appsinventiv.toolsbazzaradmin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Models.ProductCountModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by AliAh on 08/09/2018.
 */

public class PurchaseOrderAdapter extends RecyclerView.Adapter<PurchaseOrderAdapter.ViewHolder> {
    Context context;
    ArrayList<ProductCountModel> itemList;

    public PurchaseOrderAdapter(Context context, ArrayList<ProductCountModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.po_item_layout, parent, false);
        PurchaseOrderAdapter.ViewHolder viewHolder = new PurchaseOrderAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductCountModel model = itemList.get(position);
        holder.serial.setText("" + (position + 1) + ")");
        holder.title.setText(model.getProduct().getTitle());
        holder.subtitle.setText(
                "Description: " + model.getProduct().getMeasurement() +
                        "\nQty: " + model.getQuantity() +
                        "\nCost price: Rs." + CommonUtils.getFormattedPrice(model.getProduct().getCostPrice()));
        holder.price.setText("Rs." + CommonUtils.getFormattedPrice(model.getQuantity() * model.getProduct().getCostPrice()));

        if (model.getQuantityPurchased() != 0) {
            holder.edited.setVisibility(View.VISIBLE);
            holder.quantityPurchased.setText("Qty purchased: " + model.getQuantityPurchased() + "");
            holder.outOfStockQty.setText("Out of stock: " + model.getOutOfStock() + "");
            holder.newCostPrice.setText("New / Old cost price: Rs." + CommonUtils.getFormattedPrice(model.getNewCostPrice()) + "");
            holder.puchaseTotal.setText("Purchase Total: Rs." + CommonUtils.getFormattedPrice(model.getPurchaseTotal()));

        }

    }

    @Override
    public int getItemCount() {
        if(itemList.size()==0){
            return 0;
        }else {
            return itemList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView serial, title, price, subtitle;

        RelativeLayout edited;
        TextView quantityPurchased, outOfStockQty, newCostPrice, puchaseTotal;

        public ViewHolder(View itemView) {
            super(itemView);
            serial = itemView.findViewById(R.id.serial);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            subtitle = itemView.findViewById(R.id.subtitle);
            edited = itemView.findViewById(R.id.edited);
            quantityPurchased = itemView.findViewById(R.id.quantityPurchased);
            outOfStockQty = itemView.findViewById(R.id.outOfStockQty);
            newCostPrice = itemView.findViewById(R.id.newCostPrice);
            puchaseTotal = itemView.findViewById(R.id.puchaseTotal);

        }
    }
}
