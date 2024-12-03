package com.ndurance.mobileapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.ndurance.mobileapp.R;
import com.ndurance.mobileapp.model.response.OrderRest;
import com.ndurance.mobileapp.model.response.ProductRest;

public class OrderDetailedAdapter extends RecyclerView.Adapter<OrderDetailedAdapter.OrderDetailedViewHolder>{
    private OrderRest order;
    private Context context;

    public OrderDetailedAdapter(OrderRest order, Context context) {
        this.order = order;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderDetailedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_activity_order_detailed, parent, false);
        return new OrderDetailedAdapter.OrderDetailedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailedViewHolder holder, int position) {
        ProductRest productRest = order.getProducts().get(position);
        holder.tvProductName.setText(productRest.getName());
        holder.tvProductPrice.setText("$" + productRest.getPrice() * productRest.getQuantity());

        Glide.with(context).load("http://10.0.2.2:8080/product-service/products/images/" + productRest.getImages().get(0)).into(holder.ivProductImage);
    }

    @Override
    public int getItemCount() {
        return order.getProducts() != null ? order.getProducts().size() : 0;
    }
    public static class OrderDetailedViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvProductPrice;
        Button btnRemove;

        public OrderDetailedViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
