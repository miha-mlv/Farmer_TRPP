package com.example.myapplication.Users;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.myapplication.BasketFragment;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.MainFragment;
import com.example.myapplication.Model.Users;
import com.example.myapplication.R;
import com.example.myapplication.SettingsFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityHomeBinding;

import java.util.ArrayList;

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

        binding.appBarHome.toolbar.setTitle("Главная");
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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectFragment = null;
                Log.d("onNavigationItemSelected", "onNavigationItemSelected");
                int id = item.getItemId();
                if(id == R.id.nav_catalog)
                {
                    //переход к товарам
                    selectFragment = new BasketFragment();
                }else if(id == R.id.nav_map)
                {
                    //Переход к карте
                }else if(id == R.id.nav_settings)
                {
                    Log.d("fragmentSettings", "fragmentSettings");
                    selectFragment = new SettingsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Name", usersName);
                    bundle.putString("Phone", usersPhone);
                    bundle.putString("Password", usersPassword);
                    selectFragment.setArguments(bundle);

                    //Intent settingsIntent = new Intent(this, SettingsActivity.class);
                    //settingsIntent.putExtra("usersName", usersName);
                    //settingsIntent.putExtra("usersPhone", usersPhone);
                    //settingsIntent.putExtra("usersPassword", usersPassword);
                    //startActivity(settingsIntent);
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
                            replace(R.id.fragmentContainer, selectFragment).commit();
                    return true;
                }
            }
        });

//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
//                .setOpenableLayout(drawer)
//                .build();

        fragmentManager = getSupportFragmentManager();

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

























































