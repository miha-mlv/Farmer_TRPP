package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button registerButton;
    private EditText usernameInput, phoneInput, passwordInput;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = findViewById(R.id.register_btn);
        usernameInput = findViewById(R.id.register_user_name);
        phoneInput = findViewById(R.id.register_phone_input);
        passwordInput = findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    public void CreateAccount() {
        String username = usernameInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String password = passwordInput.getText().toString();
        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Введите имя", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Введите телефон", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Создание аккаунта");
            loadingBar.setMessage("Пожалуйста подождите");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhone(username, phone, password);
        }
    }
    private void  ValidatePhone(String username, String phone, String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if((!snapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("phone", phone);
                    userDataMap.put("name", username);
                    userDataMap.put("password", password);

                    RootRef.child("Users").child(phone).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();

                                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(loginIntent);
                            }
                            else {
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this, "Ошибка регистрации.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Номер " + phone + " уже зарегистрирован", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}