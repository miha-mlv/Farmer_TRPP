package com.example.myapplication.UserFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.Prevalent.Prevalent;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.paperdb.Paper;

public class SettingsFragment extends Fragment {
    private static final String ARG_PARAM1 = "Name";
    private static final String ARG_PARAM2 = "Phone";
    private static final String ARG_PARAM3 = "Password";

    private String usersName, usersPhone, usersPassword;
    EditText settings_phone, settings_fullname;
    Button btnSaveInfo;
    Boolean flag = false;
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Инициализация
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        settings_fullname = view.findViewById(R.id.settings_fullname);
        settings_phone = view.findViewById(R.id.settings_phone);
        btnSaveInfo = view.findViewById(R.id.btnSaveInfo);
        usersName = getArguments().getString(ARG_PARAM1);
        usersPhone = getArguments().getString(ARG_PARAM2);
        usersPassword = getArguments().getString(ARG_PARAM3);
        settings_fullname.setText(usersName);
        settings_phone.setText(usersPhone);
        btnSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Проверка на пустоту и запрос пароль через Fragment
                        if(!settings_fullname.getText().toString().equals("")
                                && !settings_phone.getText().toString().equals(""))
                        {
                            FragmentManager fragmentManager = getParentFragmentManager();
                            Fragment checkPasswordFragment = new CheckPasswordFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Password", usersPassword);
                            bundle.putString("Phone", usersPhone);
                            checkPasswordFragment.setArguments(bundle);
                            fragmentManager.beginTransaction()
                                    .add(R.id.containerFragmentSettings, checkPasswordFragment)
                                    .addToBackStack(null)
                                    .commit();
                            //Принимаем результат проверки пароля
                            getParentFragmentManager().setFragmentResultListener
                                    ("resultCheckPassword"
                                            , SettingsFragment.this, new FragmentResultListener() {
                                @Override
                                public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                                    //Смена в базе
                                    flag = result.getBoolean("result");
                                    if (flag) {
                                        HashMap<String, Object> newUserData = new HashMap<>();
                                        newUserData.put("phone", settings_phone.getText().toString());
                                        newUserData.put("name", settings_fullname.getText().toString());
                                        newUserData.put("password", usersPassword);
                                        //Удаляю старые записи пользователя
                                        ref.child("Users").child(usersPhone).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getActivity(), "Данные изменены успешно 1", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        //Записываю новые данные пользователя
                                        ref.child("Users")
                                                .child(settings_phone.getText().toString())
                                                .updateChildren(newUserData)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            String phone = Paper.book().read("UserPhone");
                                                            String password = Paper.book().read("UserPassword");
                                                            if (phone != "" && password != "") {
                                                                Paper.book().destroy();
                                                                Paper.book().write(Prevalent.UserPhoneKey, settings_phone.getText().toString());
                                                                Paper.book().write(Prevalent.UserPasswordKey, usersPassword);
                                                            }
                                                            Paper.book().destroy();
                                                            Toast.makeText(getActivity(), "Данные успешно изменены", Toast.LENGTH_SHORT).show();
                                                            Intent mainIntent = new Intent(getActivity(), LoginActivity.class);
                                                            startActivity(mainIntent);
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        return view;
    }

}