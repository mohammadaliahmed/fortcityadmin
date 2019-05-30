package com.appsinventiv.toolsbazzaradmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Chat.LiveChat;
import com.appsinventiv.toolsbazzaradmin.Activities.Chat.SellerChat;
import com.appsinventiv.toolsbazzaradmin.Activities.Chat.WholesaleChat;
import com.appsinventiv.toolsbazzaradmin.Activities.Customers.SellerModel;
import com.appsinventiv.toolsbazzaradmin.Models.ChatModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AliAh on 25/06/2018.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    Context context;
    ArrayList<ChatModel> itemList;
    String with;
    HashMap<String, Boolean> map = new HashMap<>();
    HashMap<String, Integer> unreadCount = new HashMap<>();
    SellerModel seller;


    public void setSeller(SellerModel seller) {
        this.seller = seller;
        notifyDataSetChanged();
    }


    public ChatListAdapter(Context context, ArrayList<ChatModel> itemList, String with) {
        this.context = context;
        this.itemList = itemList;
        this.with = with;

    }

    public void setUserStatus(HashMap<String, Boolean> map) {
        this.map = map;
        notifyDataSetChanged();
    }

    public void setUnreadCount(HashMap<String, Integer> unreadCount) {
        this.unreadCount = unreadCount;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_list_layout, parent, false);
        ChatListAdapter.ViewHolder viewHolder = new ChatListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ChatModel model = itemList.get(position);
        if (map != null && map.size() > 0) {
            if (map.get(model.getInitiator()) != null) {
                if (map.get(model.getInitiator())) {
                    holder.onlineStatus.setVisibility(View.VISIBLE);
                } else {
                    holder.onlineStatus.setVisibility(View.GONE);

                }
            }
        }
        if (unreadCount != null && map.size() > 0) {
            if (unreadCount.get(model.getInitiator()) != null) {
                if (unreadCount.get(model.getInitiator()) == 0) {
                    holder.count.setVisibility(View.GONE);
                } else {
                    holder.count.setVisibility(View.VISIBLE);
                    holder.count.setText("" + unreadCount.get(model.getInitiator()));

                }

            }
        } else {
            holder.count.setVisibility(View.GONE);
        }
        holder.username.setText(model.getNameToShow() == null ? model.getInitiator() : model.getNameToShow());
        holder.message.setText(model.getText());


        holder.time.setText(CommonUtils.getFormattedDate(model.getTime()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (with.equalsIgnoreCase("Wholesale")) {
                    Intent i = new Intent(context, WholesaleChat.class);
                    i.putExtra("username", model.getInitiator());
                    context.startActivity(i);
                } else if (with.equalsIgnoreCase("Client")) {
                    Intent i = new Intent(context, LiveChat.class);
                    i.putExtra("username", model.getInitiator());
                    context.startActivity(i);
                } else if (with.equalsIgnoreCase("Seller")) {
                    Intent i = new Intent(context, SellerChat.class);
                    i.putExtra("username", model.getInitiator());
                    context.startActivity(i);
                }
            }
        });

        if (model.getPicUrl() != null && !model.getPicUrl().equalsIgnoreCase("")) {
            Glide.with(context).load(model.getPicUrl()).into(holder.imageView2);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, message, time, count;
        ImageView onlineStatus;
        CircleImageView imageView2;

        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            count = itemView.findViewById(R.id.count);
            onlineStatus = itemView.findViewById(R.id.onlineStatus);
            imageView2 = itemView.findViewById(R.id.imageView2);

        }
    }
}
