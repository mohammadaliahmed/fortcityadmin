package com.appsinventiv.toolsbazzaradmin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Models.Temporarymodel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by AliAh on 08/10/2018.
 */

public class YearViewInvoiceAdapter extends RecyclerView.Adapter<YearViewInvoiceAdapter.ViewHolder> {
    Context context;
    ArrayList<Temporarymodel> itemList = new ArrayList<>();
    int type;
    ChangeLayout changeLayout;

    public YearViewInvoiceAdapter(Context context, ArrayList<Temporarymodel> itemList, int type, ChangeLayout changeLayout) {
        this.context = context;
        this.itemList = itemList;
        this.type = type;
        this.changeLayout = changeLayout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.finalized_po_list_item_layout, parent, false);
        YearViewInvoiceAdapter.ViewHolder viewHolder = new YearViewInvoiceAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Temporarymodel model = itemList.get(position);
//        float cost = 0;
        holder.count.setText("" + model.getPurchaseCount() + " sales");
        holder.cost.setText("Sale: Rs " + CommonUtils.getFormattedPrice(model.getTotalCost()));
        holder.purchase.setText("Profit: Rs " + CommonUtils.getFormattedPrice(model.getTotalPurchaseCost()));
        holder.which.setText("" + model.getTime());
        holder.date.setText("" + model.getTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 1) {
                    changeLayout.onClick(model, position, "year", model.getTime());
                } else if (type == 2) {
                    changeLayout.onClick(model, position, "month", model.getTime());
                } else if (type == 3) {
                    changeLayout.onClick(model, position, "day", model.getTime());
                } else if (type == 4) {
                    changeLayout.onClick(model, position, "itself", model.getTime());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView count, which, date, cost, purchase;

        public ViewHolder(View itemView) {
            super(itemView);
            cost = itemView.findViewById(R.id.cost);
            count = itemView.findViewById(R.id.count);
            purchase = itemView.findViewById(R.id.purchase);
            which = itemView.findViewById(R.id.which);
            date = itemView.findViewById(R.id.date);
        }
    }

    public interface ChangeLayout {
        public void onClick(Temporarymodel model, int position, String type, String key);
    }
}
