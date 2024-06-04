package com.example.myapplication.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.myapplication.UserFragments.MapFragment;
import com.example.myapplication.Model.Products;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ProductsAdapter extends ArrayAdapter<Products> {
    private Context context;
    private int resource;
    private String latitude, longitude;
    private Products product;
    private AppCompatActivity activity;
    public ProductsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Products> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.activity = (AppCompatActivity)context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);//(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(resource, parent, false);
        ImageView product_image = convertView.findViewById(R.id.product_image);
        TextView tvProduct_name = convertView.findViewById(R.id.tvProduct_name);
        TextView twPrice_product = convertView.findViewById(R.id.twPrice_product);
        TextView product_decription = convertView.findViewById(R.id.product_decription);
        ImageButton navImageProduct = convertView.findViewById(R.id.navImageProduct);
        product_image.setImageResource(R.drawable.imageproduct);
        tvProduct_name.setText(getItem(position).getName());
        twPrice_product.setText(getItem(position).getPrice());
        product_decription.setText(getItem(position).getDescription());
        latitude = getItem(position).getLatitude();
        longitude = getItem(position).getLongitude();
        navImageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment mapFragment = new MapFragment();
                Bundle bundle = new Bundle();
                bundle.putDouble("latitude", Double.valueOf(latitude));
                bundle.putDouble("longitude",Double.valueOf(longitude));
                bundle.putBoolean("flag", true);
                mapFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                        mapFragment).addToBackStack("mapStack").commit();

            }
        });
        return convertView;
    }
}
