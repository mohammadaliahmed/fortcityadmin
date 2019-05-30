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

import com.appsinventiv.toolsbazzaradmin.Activities.Invoicing.ViewInvoice;
import com.appsinventiv.toolsbazzaradmin.Models.InvoiceModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by AliAh on 12/09/2018.
 */

public class FinalInvoiceListAdapter extends RecyclerView.Adapter<FinalInvoiceListAdapter.ViewHolder> {
    Context context;
    ArrayList<InvoiceModel> itemList;
    String path;

    public FinalInvoiceListAdapter(Context context, ArrayList<InvoiceModel> itemList, String path
    ) {
        this.context = context;
        this.itemList = itemList;
        this.path = path;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.finalized_invoice_item_list_layout, parent, false);
        FinalInvoiceListAdapter.ViewHolder viewHolder = new FinalInvoiceListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final InvoiceModel model = itemList.get(position);
        holder.which.setText(CommonUtils.getFormattedDateOnly(model.getTime()));
        float orderProfit = 0;
        for (int i = 0; i < model.getCountModelArrayList().size(); i++) {
            orderProfit = orderProfit+(((model.getCountModelArrayList().get(i).getProduct().getRetailPrice() * model.getCountModelArrayList().get(i).getQuantity()) -
                    (model.getCountModelArrayList().get(i).getProduct().getCostPrice() * model.getCountModelArrayList().get(i).getQuantity())));
        }
        orderProfit=(orderProfit+model.getDeliveryCharges()+model.getShippingCharges());
        holder.orderDetails.setText(
                "Order Time: " + CommonUtils.getFormattedDate(model.getTime()) +
                        "\nOrder Status: " + model.getOrderStatus() +
                        "\nOrder items: " + model.getCountModelArrayList().size() +
                        "\nOut of stock: " + model.getOutOfStock() +
                        "\nOrder amount: Rs " + model.getGrandTotal() +
                        "\nOrder delivery: Rs " + model.getDeliveryCharges() +
                        "\nOrder profit: Rs " + orderProfit+
                "\n\nOrder delivered by: " + model.getDeliveryBy()
        );

        holder.userDetails.setText(
                "Name: " + model.getCustomer().getName() +
                        "\n\nAddress: " + model.getCustomer().getAddress() + ", "
                        + model.getCustomer().getCity() + ",  "
                        + model.getCustomer().getCountry()
                        + "\n\nPhone: " + model.getCustomer().getPhone()
        );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ViewInvoice.class);
                i.putExtra("path", path);
                i.putExtra("from", "final");
                i.putExtra("invoiceNumber", model.getId());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderDetails, userDetails, which;

        public ViewHolder(View itemView) {
            super(itemView);
            orderDetails = itemView.findViewById(R.id.orderDetails);
            userDetails = itemView.findViewById(R.id.userDetails);
            which = itemView.findViewById(R.id.which);

        }
    }


}
