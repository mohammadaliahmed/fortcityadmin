package com.appsinventiv.toolsbazzaradmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Purchases.ViewPurchaseOrder;
import com.appsinventiv.toolsbazzaradmin.Models.PurchaseOrderModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by AliAh on 08/09/2018.
 */

public class POListAdapter extends RecyclerView.Adapter<POListAdapter.ViewHolder> {

    Context context;
    ArrayList<PurchaseOrderModel> itemList;
    SettleBills settleBills;
    String path;
    int showCheckbox;
    String from;

    public POListAdapter(Context context, ArrayList<PurchaseOrderModel> itemList, String path, int showCheckbox, String from, SettleBills settleBills) {
        this.context = context;
        this.itemList = itemList;
        this.settleBills = settleBills;
        this.path = path;
        this.showCheckbox = showCheckbox;
        this.from = from;
    }

    @NonNull
    @Override
    public POListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.po_list_item_layout, parent, false);
        POListAdapter.ViewHolder viewHolder = new POListAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull POListAdapter.ViewHolder holder, final int position) {
        final PurchaseOrderModel model = itemList.get(position);
        holder.date.setText(CommonUtils.getFormattedDateOnly(model.getTime()));
        holder.totalCost.setText("Rs: " + CommonUtils.getFormattedPrice(model.getTotal()));
        holder.totalCost.setText("Rs: " + CommonUtils.getFormattedPrice(model.getTotal()));
        holder.ponumber.setText("" + model.getPoNumber());
        holder.vendorname.setText("" + model.getVendor().getVendorName());
        holder.purchaseOfficer.setText(model.getEmployeeName());
        if (showCheckbox == 1) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        settleBills.addToArray(model.getId(), position);
                    } else {
                        settleBills.removeFromArray(model.getId(), position);
                    }
                }
            });
        } else {
            holder.checkBox.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ViewPurchaseOrder.class);
                    i.putExtra("po", model.getId());
                    i.putExtra("path", path);
                    i.putExtra("from", "final");
                    context.startActivity(i);
                }
            });
        }
        if (from.equalsIgnoreCase("pending")) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ViewPurchaseOrder.class);
                    i.putExtra("po", model.getId());
                    String pendingPath = "Accounts/PendingPO";
                    i.putExtra("path", pendingPath);
                    i.putExtra("from", "pending");
                    context.startActivity(i);
                }
            });
        } else if (from.equalsIgnoreCase("completed")) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ViewPurchaseOrder.class);
                    i.putExtra("po", model.getId());
                    String completedPath = "Purchases/Completed";
                    i.putExtra("path", completedPath);
                    i.putExtra("from", "completed");
                    context.startActivity(i);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, ponumber, vendorname, totalCost, purchaseOfficer, newOldCost;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            ponumber = itemView.findViewById(R.id.ponumber);
            vendorname = itemView.findViewById(R.id.vendorname);
            totalCost = itemView.findViewById(R.id.totalCost);
            purchaseOfficer = itemView.findViewById(R.id.purchaseOfficer);
            checkBox = itemView.findViewById(R.id.checkBox);
            newOldCost = itemView.findViewById(R.id.newOldCost);


        }
    }

    public interface SettleBills {
        public void addToArray(String id, int position);

        public void removeFromArray(String id, int position);
    }
}
