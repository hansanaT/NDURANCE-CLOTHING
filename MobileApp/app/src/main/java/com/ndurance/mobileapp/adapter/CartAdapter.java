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
import com.ndurance.mobileapp.activity.CartActivity;
import com.ndurance.mobileapp.model.dto.CartItem;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private Context context;

    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.tvProductName.setText(item.getName());
        holder.tvProductPrice.setText("$" + ( item.getPrice() * item.getQuantity()));
        // Load image (use Glide or Picasso)
        Glide.with(context).load("http://10.0.2.2:8080/product-service/products/images/" + item.getImages().get(0)).into(holder.ivProductImage);

        //holder.btnRemove.setOnClickListener(v -> onRemoveClickListener.onRemoveClick(item));

        holder.btnRemove.setOnClickListener(v -> {
            if (context instanceof CartActivity) {
                ((CartActivity) context).removeFromCart(item.getUser(), item.getCartId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvProductPrice;
        Button btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
