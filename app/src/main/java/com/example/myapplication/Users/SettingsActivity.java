package com.example.myapplication.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Model.Users;
import com.example.myapplication.Prevalent.Prevalent;
import com.example.myapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oginotihiro.cropview.CropView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private CircleImageView profileImageView;
    private DatabaseReference RootRef;
    private EditText fullname, phone, adress;
    private TextView saveTextbtn, closeTextBtn;
    private String cheker = "";
    private Uri imageUri;
    private String usersName, usersPhone, usersPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Bundle bundle = getIntent().getExtras();
        usersName = bundle.getString("usersName");
        usersPhone = bundle.getString("usersPhone");
        usersPassword = bundle.getString("usersPassword");
        init();

        RootRef = FirebaseDatabase.getInstance().getReference();
    }
    private void init()
    {
        profileImageView = findViewById(R.id.settings_account_image);
        fullname = findViewById(R.id.settings_fullname);
        fullname.setText(usersName);
        phone = findViewById(R.id.settings_phone);
        phone.setText(usersPhone);
        adress = findViewById(R.id.settings_adres);
        saveTextbtn = findViewById(R.id.save_settings_tw);
        closeTextBtn = findViewById(R.id.close_settings_tw);

        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent closeSettingsIntent = new Intent(SettingsActivity.this, HomeActivity.class);
                startActivity(closeSettingsIntent);
            }
        });
        saveTextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOnlyUserInfo();
//                if(cheker.equals("clicked"))
//                {
//                    userInfoSaved();
//                }
//                else {
//                    updateOnlyUserInfo();
//                }
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cheker = "clicked";

                CropView cropView = findViewById(R.id.cropView);
                cropView.of(imageUri)
                        .withAspect(1,1)
                        .withOutputSize(50,50)
                        .initialize(SettingsActivity.this);
            }
        });
    }

    private void userInfoSaved() {
        if(TextUtils.isEmpty(fullname.getText().toString()))
        {
            Toast.makeText(this, "Заполните имя", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(adress.getText().toString()))
        {
            Toast.makeText(this, "Заполните адрес", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone.getText().toString()))
        {
            Toast.makeText(this, "Заполните номер телефона", Toast.LENGTH_SHORT).show();
        }
        else if(cheker.equals("cliker"))
        {
            uploadImage();
        }
    }

    private void uploadImage() {

    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        if(adress.getText().toString().equals(""))
        {
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("name", fullname.getText().toString());
            userMap.put("phone", phone.getText().toString());
            ref.child(usersPhone).updateChildren(userMap);

            startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
            Toast.makeText(SettingsActivity.this, "Успешно сохранено", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("name", fullname.getText().toString());
            userMap.put("adress", adress.getText().toString());
            userMap.put("phone", phone.getText().toString());
            ref.child(Prevalent.currnetOnlineUser.getPhone()).updateChildren(userMap);

            startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
            Toast.makeText(SettingsActivity.this, "Успешно сохранено", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


}










































