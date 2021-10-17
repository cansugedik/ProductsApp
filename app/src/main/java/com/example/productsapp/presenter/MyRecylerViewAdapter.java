package com.example.productsapp.presenter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productsapp.R;
import com.example.productsapp.model.ProductModel;
import com.example.productsapp.ui.ProductsActivity;

import java.util.ArrayList;

public class MyRecylerViewAdapter extends RecyclerView.Adapter<MyRecylerViewAdapter.MyViewHolder> {

    private ArrayList<ProductModel> products;

    private static ProductsActivity pActivity;

    public MyRecylerViewAdapter(ProductsActivity activity, ArrayList productList) {
        this.pActivity = activity;
        this.products = productList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recyclerview_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (position % 2 == 0) {
            // çift
            holder.setData(
                    products.get(position).getProductCategory(),
                    products.get(position).getProductHeader(),
                    products.get(position).getProductDescription(),
                    products.get(position).getProductPrice(),
                    products.get(position).getProductId());
        } else {
            // tek
            holder.setData(
                    products.get(position).getProductCategory(),
                    products.get(position).getProductHeader(),
                    products.get(position).getProductDescription(),
                    products.get(position).getProductPrice(),
                    products.get(position).getProductId());

        }


        holder.btnProductUpdate.setOnClickListener(view -> {
            if (products.size() != 0)
                pActivity.updateProduct("update", products.get(position).getProductId());
        });

        holder.btnProductDelete.setOnClickListener(view -> {
            if (products.size() != 0)
                pActivity.deleteModelDatabase("delete", products.get(position).getProductId());
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProductCategory, tvProductId,
                tvHeader, tvDescription, tvPrice;

        Button btnProductUpdate, btnProductDelete;

        public ImageView imgviewProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductId = itemView.findViewById(R.id.tvProductId);
            tvProductCategory = itemView.findViewById(R.id.tvProductCategory);
            tvHeader = itemView.findViewById(R.id.tvHeader);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imgviewProduct = itemView.findViewById(R.id.imgviewProduct);

            btnProductUpdate = itemView.findViewById(R.id.btnProductUpdate);
            btnProductDelete = itemView.findViewById(R.id.btnProductDelete);
        }

        @SuppressLint({"ResourceAsColor", "ResourceType"})
        public void setData(String productCategory, String productHeader, String productDescription, int productPrice, int productId) {

            tvProductId.setText(String.valueOf(productId));
            tvProductCategory.setText(productCategory);
            tvHeader.setText(productHeader);
            tvDescription.setText(productDescription);
            tvPrice.setText(String.valueOf(productPrice));

            tvProductId.setBackgroundColor(pActivity.getResources().getColor(R.color.green));
        }


    }
}

