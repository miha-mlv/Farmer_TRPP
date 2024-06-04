package com.example.myapplication.Farmer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.myapplication.Farmer.FarmerFragments.AddProductsFragment;
import com.example.myapplication.Farmer.FarmerFragments.AllProductsFragment;
import com.example.myapplication.Farmer.FarmerFragments.ProfileFarmerFragment;
import com.example.myapplication.UserFragments.MainFragment;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainFarmerActivity extends AppCompatActivity {
    String farmerPhone, farmerPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_farmer);

        Bundle bundle = getIntent().getExtras();
        farmerPhone = bundle.getString("phone");
        farmerPassword = bundle.getString("password");

        ProfileFarmerFragment profileFragment = new ProfileFarmerFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.contentFrameLayout, profileFragment)
                .commit();
        BottomNavigationView bottomBar = findViewById(R.id.bottomNavigationBar);
        bottomBar.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        Fragment selectedFragment = null;
                        if (id == R.id.addProducts) {
                            selectedFragment = new AddProductsFragment();
                        } else if (id == R.id.profile) {
                            selectedFragment = new ProfileFarmerFragment();
                        } else if (id == R.id.allProducts) {
                            selectedFragment = new AllProductsFragment();
                        }
                        if (selectedFragment == null) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.contentFrameLayout, new MainFragment())
                                    .commit();
                            Log.d("null fragment", "null fragment");
                            return true;
                        } else {
                            selectedFragment.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().
                                    replace(R.id.contentFrameLayout, selectedFragment).commit();
                            return true;
                        }
                    }
                }
        );
    }
}