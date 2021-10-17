package com.example.productsapp.presenter;

import androidx.annotation.NonNull;

import com.example.productsapp.model.ProductModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class DAOProduct {

    private DatabaseReference databaseReference;

    public DAOProduct(){

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("Products").push();
    }

    public Task<Void> add(String key, ProductModel hashMap){
       return databaseReference.getDatabase().getReference("Products").child(key).setValue(hashMap);

    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap){

        return databaseReference.getDatabase().getReference("Products").child(key).updateChildren(hashMap);

    }

    public Task<Void> remove(String key){

        return databaseReference.getDatabase().getReference("Products").child(key).removeValue();
    }

    public void orderByValue(String childkey, String orderByChildvalue, ArrayList<ProductModel> modelArrayList, MyRecylerViewAdapter adapter){

        FirebaseDatabase.getInstance().getReference().child(childkey).orderByChild(orderByChildvalue).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelArrayList.clear();
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                        ProductModel productModel = dataSnapshot1.getValue(ProductModel.class);
                        modelArrayList.add(productModel);
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public FirebaseRecyclerOptions<ProductModel> firebaseRecyclerOptions(){

        FirebaseRecyclerOptions<ProductModel> options = new FirebaseRecyclerOptions.Builder<ProductModel>().
                setQuery(databaseReference.getDatabase().getReference().child("Products")
                        ,ProductModel.class).build();

        return options;
    }

    public void eventListener(ValueEventListener listener){

        databaseReference.addValueEventListener(listener);
    }

    public void closed(){

        databaseReference.goOffline();
    }


}
