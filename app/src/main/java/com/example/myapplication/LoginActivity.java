package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Farmer.MainFarmerActivity;
import com.example.myapplication.Model.Users;
import com.example.myapplication.Prevalent.Prevalent;
import com.example.myapplication.Users.HomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import java.util.ArrayList;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity{
    private Button loginButton;
    private EditText loginPhoneInput, loginPasswordInput;
    private ProgressDialog loadingBar;

    private String parentDbName = "Users";
    private CheckBox checkBoxRemember;
    TextView adminLink, notAdminLink;
    ImageButton btnImageBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_btn);
        loginPhoneInput = findViewById(R.id.login_phone_input);
        loginPasswordInput = findViewById(R.id.login_password_input);
        loadingBar = new ProgressDialog(this);
        checkBoxRemember = findViewById(R.id.checkbox);
        Paper.init(this);

        btnImageBack = findViewById(R.id.btnImageBack);

        adminLink = findViewById(R.id.admin_panel_link);
        notAdminLink = findViewById(R.id.not_admin_panel_link);

        btnImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxRemember.setVisibility(android.widget.CheckBox.INVISIBLE);
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                loginButton.setText("Вход для фермера");
                parentDbName = "Admins";
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxRemember.setVisibility(android.widget.CheckBox.VISIBLE);
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);
                loginButton.setText("Войти");
                parentDbName = "Users";
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }
    private void loginUser()
    {
        String phone = loginPhoneInput.getText().toString();
        String password = loginPasswordInput.getText().toString();

        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Введите телефон", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Вход в приложение");
            loadingBar.setMessage("Пожалуйста подождите");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateUser(phone, password);
        }
    }
    private void ValidateUser(String phone, String password)
    {if(checkBoxRemember.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbName).child(phone).exists()) {
                    Users usersData = snapshot.child(parentDbName).child(phone).getValue(Users.class);
                    if(usersData.getPhone().equals(phone))
                    {if(usersData.getPassword().equals(password))
                        {if(parentDbName.equals("Users"))
                            {
                                loadingBar.dismiss();
                                ArrayList<String> dataArray = new ArrayList<String>();
                                dataArray.add(usersData.getName());
                                dataArray.add(usersData.getPhone());
                                dataArray.add(usersData.getPassword());
                                Toast.makeText(LoginActivity.this, "Успешный вход", Toast.LENGTH_SHORT).show();
                                Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                                homeIntent.putExtra("usersName", usersData.getName());
                                homeIntent.putExtra("usersPhone", usersData.getPhone());
                                homeIntent.putExtra("usersPassword", usersData.getPassword());
                                startActivity(homeIntent);
                            } else if(parentDbName.equals("Admins"))
                            {
                                loadingBar.dismiss();
                                Toast.makeText(LoginActivity.this, "Успешный вход", Toast.LENGTH_SHORT).show();
                                Intent homeIntent = new Intent(LoginActivity.this, MainFarmerActivity.class);
                                homeIntent.putExtra("phone", phone);
                                homeIntent.putExtra("password", password);
                                startActivity(homeIntent);
                            }
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Аккаунт с номером " + phone + " не существует", Toast.LENGTH_SHORT).show();
                    Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(registerIntent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}