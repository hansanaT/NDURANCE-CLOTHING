package com.ndurance.mobileapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ndurance.mobileapp.R;
import com.ndurance.mobileapp.model.response.OrderResponse;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final List<OrderResponse> orders;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(OrderResponse order);
    }

    public OrderAdapter(List<OrderResponse> orders, OnItemClickListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderResponse order = orders.get(position);
        holder.orderId.setText("Order ID: " + order.getId());
        holder.orderDate.setText("Date: " + order.getDate());
        holder.orderPrice.setText("Price: $" + order.getPrice());

        // Set click listener
        holder.itemView.setOnClickListener(v -> listener.onItemClick(order));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderDate, orderPrice;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.order_Id);
            orderDate = itemView.findViewById(R.id.order_Date);
            orderPrice = itemView.findViewById(R.id.order_Price);
        }
    }
}
