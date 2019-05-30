package com.appsinventiv.toolsbazzaradmin.Activities.Products;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ViewHolder> {
    Context context;
    ArrayList<MainCategoryModel> itemList;
    MainCategoryCallBacks callBacks;
    int to = 0;


    public MainCategoryAdapter(Context context, ArrayList<MainCategoryModel> itemList, int to, MainCategoryCallBacks callBacks) {
        this.context = context;
        this.itemList = itemList;
        this.callBacks = callBacks;
        this.to = to;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_category_item_layout, parent, false);
        MainCategoryAdapter.ViewHolder viewHolder = new MainCategoryAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MainCategoryModel model = itemList.get(position);
        holder.maincategory.setText(model.getMainCategory());
        holder.subCategories.setText(model.getSubCategories() == null ? "" : model.getSubCategories());
        Glide.with(context).load(model.getUrl()).into(holder.icon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProduct.categoryList.add(model.getMainCategory());
                EditProduct.categoryList.add(model.getMainCategory());
                if (to == 1) {
                    Intent i = new Intent(context, AddSubCategories.class);
                    i.putExtra("parentCategory", model.getMainCategory());

                    context.startActivity(i);
//                    ((ChooseMainCategory) context).finish();
                } else if (to == 0) {
                    Intent i = new Intent(context, ChooseCategory.class);
                    i.putExtra("parentCategory", model.getMainCategory());

                    context.startActivity(i);
//                    ((ChooseMainCategory) context).finish();
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBacks.deleteCategory(model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView maincategory, subCategories;
        ImageView icon, delete;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            subCategories = itemView.findViewById(R.id.subCategories);
            maincategory = itemView.findViewById(R.id.maincategory);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    public interface MainCategoryCallBacks {
        public void deleteCategory(MainCategoryModel model);
    }
}
