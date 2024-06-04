package com.example.myapplication.UserFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.myapplication.Adapter.ProductsAdapter;
import com.example.myapplication.Model.Products;
import com.example.myapplication.R;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private ListView lwListProducts;
    // creating a new array list.
    ArrayList<Products> arrayListProducts;
    private ImageButton navImageProduct;
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        lwListProducts = view.findViewById(R.id.lwListProducts);
        navImageProduct = lwListProducts.findViewById(R.id.navImageProduct);
        arrayListProducts = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Admins");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot farmer: snapshot.getChildren())
                {
                    for(DataSnapshot product: farmer.child("Products").getChildren())
                    {
                        Products pr = product.getValue(Products.class);
                        arrayListProducts.add(pr);
                    }
                }
                ProductsAdapter adapter = new ProductsAdapter(getContext(),
                        R.layout.products_item, arrayListProducts);
                lwListProducts.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ref.addListenerForSingleValueEvent(valueEventListener);
        return view;
    }
}