package com.example.productsapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productsapp.DAOProduct;
import com.example.productsapp.R;
import com.example.productsapp.utils.MyRecylerViewAdapter;
import com.example.productsapp.utils.ProductModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductsActivity extends Activity {

    public EditText editProductCategory,
            editProductHeader,
            editProductDescription,
            editProductPrice;

    Button btnProductAdd, btnProductOrderBy;

    RecyclerView recyclerviewProduct;
    MyRecylerViewAdapter productAdapter;

    String pStatus = "add";
    int pPosition = 0;
    int productAdd = 0;

    DAOProduct daoProduct;
    private ArrayList<ProductModel> productList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_products);

        InitControls();
        InitControlEvents();

        daoProduct = new DAOProduct();

        productList = new ArrayList<>();
        // productAdapter = new MyRecylerViewAdapter(ProductsActivity.this,daoProduct.firebaseRecyclerOptions(), productList);
        productAdapter = new MyRecylerViewAdapter(ProductsActivity.this, productList);
    }

    private void InitControls() {

        editProductCategory = findViewById(R.id.editProductCategory);
        editProductHeader = findViewById(R.id.editProductHeader);
        editProductDescription = findViewById(R.id.editProductDescription);
        editProductPrice = findViewById(R.id.editProductPrice);

        btnProductAdd = findViewById(R.id.btnProductAdd);
        btnProductOrderBy = findViewById(R.id.btnProductOrderBy);

        recyclerviewProduct = findViewById(R.id.recyclerviewProduct);
    }

    private void InitControlEvents() {
        productList = new ArrayList();

        btnProductAdd.setOnClickListener(view -> {

            switch (pStatus) {
                case "update":
                    updateModelDatabase();
                    break;
                case "add":
                    addProduct();
                    break;
        }});

        btnProductOrderBy.setOnClickListener(v -> {
            if(productList.size() > 0)
               daoProduct.orderByValue("Products", "productPrice",
                    productList, productAdapter);
        });

        recyclerviewProduct.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if(e.getAction() == MotionEvent.ACTION_UP)
                   productDetailDialog(pPosition);

                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }


    public void productDetailDialog(int pos) {
        View dialogView = View.inflate(this, R.layout.dialog_product_detail, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        TextView textview_dialog1 = dialogView.findViewById(R.id.textview_dialog1);
        TextView textview_dialog2 = dialogView.findViewById(R.id.textview_dialog2);
        TextView textview_dialog3 = dialogView.findViewById(R.id.textview_dialog3);
        TextView textview_dialog4 = dialogView.findViewById(R.id.textview_dialog4);

        textview_dialog1.setText(productList.get(pos).getProductCategory());
        textview_dialog2.setText(productList.get(pos).getProductHeader());
        textview_dialog3.setText(productList.get(pos).getProductDescription());
        textview_dialog4.setText(String.valueOf(productList.get(pos).getProductPrice()));

        dialogView.findViewById(R.id.btn_set).setOnClickListener((View view) -> {

            alertDialog.dismiss();
        });
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void addProduct() {
        if (!editProductCategory.getText().toString().equals("") ||
                !editProductHeader.getText().toString().equals("") ||
                !editProductDescription.getText().toString().equals("") ||
                !editProductPrice.getText().toString().equals(""))

            addVariableModel();
    }

    private void updateRecyclerview() {
        productAdapter = new MyRecylerViewAdapter(ProductsActivity.this, productList);
        recyclerviewProduct.setAdapter(productAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerviewProduct.setHasFixedSize(true);
        recyclerviewProduct.setNestedScrollingEnabled(false);
        recyclerviewProduct.setLayoutManager(linearLayoutManager);
    }

    private void addModelDatabase() {
 /*       HashMap<String, Object> map = new HashMap<>();
        map.put("id", productAdd);
        map.put("category", editProductCategory.getText().toString());
        map.put("header", editProductHeader.getText().toString());
        map.put("description", editProductDescription.getText().toString());
        map.put("price", editProductPrice.getText().toString());*/

        daoProduct.add(String.valueOf(productAdd), productModel).addOnSuccessListener(unused -> {

            Toast.makeText(getApplicationContext(), "Inserted SuccesFully", Toast.LENGTH_LONG).show();

            productAdd++;

            updateRecyclerview();

        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Could not insert", Toast.LENGTH_LONG).show());

    }

    ProductModel productModel;
    int pPrice = 0;
    private void addVariableModel() {
        if (!editProductPrice.getText().toString().equals(""))
            pPrice = Integer.valueOf(editProductPrice.getText().toString());

        productModel = new ProductModel();
        productModel.setProductId(productAdd);
        productModel.setProductCategory(editProductCategory.getText().toString());
        productModel.setProductHeader( editProductHeader.getText().toString());
        productModel.setProductDescription(editProductDescription.getText().toString());
        productModel.setProductPrice(pPrice);

        productList.add(productModel);

        addModelDatabase();
    }

    public void updateProduct(String status, int productId) {
        pStatus = status;

        btnProductAdd.setText("Save");

        for (int i = 0; i < productList.size(); i++)
            if (productList.get(i).getProductId() == productId)
                pPosition = i;

        if (productList.size() != 0) {
            editProductCategory.setText(productList.get(pPosition).getProductCategory());
            editProductHeader.setText(productList.get(pPosition).getProductHeader());
            editProductDescription.setText(productList.get(pPosition).getProductDescription());
            editProductPrice.setText(String.valueOf(productList.get(pPosition).getProductPrice()));
        }
    }

    private void updateModelDatabase() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", productList.get(pPosition).getProductId());
        map.put("category", editProductCategory.getText().toString());
        map.put("header", editProductHeader.getText().toString());
        map.put("description", editProductDescription.getText().toString());
        if (!editProductPrice.getText().toString().equals(""))
            map.put("price", Integer.valueOf(editProductPrice.getText().toString()));

        productList.get(pPosition).setProductCategory(editProductCategory.getText().toString());
        productList.get(pPosition).setProductHeader(editProductHeader.getText().toString());
        productList.get(pPosition).setProductDescription(editProductDescription.getText().toString());
        productList.get(pPosition).setProductPrice(Integer.valueOf(editProductPrice.getText().toString()));

        daoProduct.update(String.valueOf(pPosition), map).addOnSuccessListener(unused -> {

            Toast.makeText(this, "Updated SuccesFully", Toast.LENGTH_LONG).show();

            pStatus = "add";
            btnProductAdd.setText("Add");

            updateRecyclerview();

        }).addOnFailureListener(e -> Toast.makeText(this, "Could not update", Toast.LENGTH_LONG).show());

    }

    public void deleteModelDatabase(String status, int productId) {
        pStatus = status;
        pPosition = productId;

        daoProduct.remove(String.valueOf(productId)).addOnSuccessListener(unused -> {

            for (int i = 0; i < productList.size(); i++)
                if (productList.get(i).getProductId() == productId)
                    productList.remove(i);

            Toast.makeText(this, "Deleted SuccesFully", Toast.LENGTH_LONG).show();

            pStatus = "add";

            updateRecyclerview();

        }).addOnFailureListener(e -> Toast.makeText(this, "Could not delete", Toast.LENGTH_LONG).show());

    }

    public FirebaseRecyclerOptions<ProductModel> firebaseRecyclerOptions() {

        FirebaseRecyclerOptions<ProductModel> options = new FirebaseRecyclerOptions.Builder<ProductModel>().
                setQuery(FirebaseDatabase.getInstance().getReference().getDatabase().getReference("Products")
                        , ProductModel.class).build();

        return options;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        daoProduct.closed();
    }

    @Override
    protected void onStop() {
        super.onStop();

        daoProduct.closed();
    }

}
