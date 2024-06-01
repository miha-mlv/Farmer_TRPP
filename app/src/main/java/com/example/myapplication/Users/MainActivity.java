package com.example.myapplication.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.Model.Users;
import com.example.myapplication.Prevalent.Prevalent;
import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private Button loginButton, joinButton;

    private ProgressDialog loadingBar;
    private String nameUser, passwordUser, phoneUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        joinButton = findViewById(R.id.main_join_btn);
        loginButton = findViewById(R.id.main_login_btn);

        loadingBar = new ProgressDialog(this);
        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);
        if (UserPhoneKey != "" && UserPasswordKey != "") {
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {
                loadingBar.setTitle("Вход в приложение");
                loadingBar.setMessage("Пожалуйста подождите");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                ValidateUser(UserPhoneKey, UserPasswordKey);
            }
        }
    }

    private void ValidateUser(final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(phone).exists()) {
                    Users usersData = snapshot.child("Users").child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone)) {
                        if (usersData.getPassword().equals(password)) {
                            loadingBar.dismiss();
//                            ArrayList<String> dataArray = new ArrayList<String>();
//                            dataArray.add(usersData.getName());
//                            dataArray.add(usersData.getPhone());
//                            dataArray.add(usersData.getPassword());
                            Toast.makeText(MainActivity.this, "Успешный вход", Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                            homeIntent.putExtra("usersName", usersData.getName());
                            homeIntent.putExtra("usersPhone", usersData.getPhone());
                            homeIntent.putExtra("usersPassword", usersData.getPassword());
                            startActivity(homeIntent);
                        } else {
                            Toast.makeText(MainActivity.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this, "Аккаунт с номером " + phone + "не существует", Toast.LENGTH_SHORT).show();

                    Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(registerIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }







    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
}
