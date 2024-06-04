package com.example.myapplication.Users;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.UserFragments.MainFragment;
import com.example.myapplication.Model.Users;
import com.example.myapplication.R;
import com.example.myapplication.UserFragments.MapFragment;
import com.example.myapplication.UserFragments.SettingsFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityHomeBinding;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    Users usersData;
    private String usersName, usersPhone, usersPassword;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        usersName = bundle.getString("usersName");
        usersPhone = bundle.getString("usersPhone");
        usersPassword = bundle.getString("usersPassword");

        binding.appBarHome.toolbar.setTitle("Каталог");
        setSupportActionBar(binding.appBarHome.toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(244, 164, 96)));


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        TextView twUserNameNavView = navigationView.getHeaderView(0).findViewById(R.id.twUserName);
        TextView twUserPhoneNavView = navigationView.getHeaderView(0).findViewById(R.id.twUserPhone);
        twUserNameNavView.setText(usersName);
        twUserPhoneNavView.setText(usersPhone);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, binding.appBarHome.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, new MainFragment()).commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectFragment = null;
                Log.d("onNavigationItemSelected", "onNavigationItemSelected");
                int id = item.getItemId();
                if(id == R.id.nav_catalog)
                {
                    binding.appBarHome.toolbar.setTitle("Каталог");
                    selectFragment = new MainFragment();
                }else if(id == R.id.nav_map)
                {
                    selectFragment = new MapFragment();
                    Bundle bundle = new Bundle();
                    binding.appBarHome.toolbar.setTitle("Карта");
                    bundle.putBoolean("flag", false);
                    selectFragment.setArguments(bundle);
                }else if(id == R.id.nav_settings)
                {
                    Log.d("fragmentSettings", "fragmentSettings");
                    selectFragment = new SettingsFragment();
                    Bundle bundle = new Bundle();
                    binding.appBarHome.toolbar.setTitle("Настройки");
                    bundle.putString("Name", usersName);
                    bundle.putString("Phone", usersPhone);
                    bundle.putString("Password", usersPassword);
                    selectFragment.setArguments(bundle);
                }else if(id == R.id.nav_logout)
                {
                    Paper.book().destroy();
                    Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    return true;
                }

                if (selectFragment == null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, new MainFragment())
                            .commit();
                    navigationView.setCheckedItem(R.id.nav_settings);
                    Log.d("null fragment", "null fragment");
                    return true;
                }
                else {
                    DrawerLayout drawerLayout = binding.drawerLayout;
                    drawerLayout.closeDrawer(GravityCompat.START);
                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.fragmentContainer, selectFragment).addToBackStack(null).commit();
                    return true;
                }
            }
        });


    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawerLayout = binding.drawerLayout;
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}

























































