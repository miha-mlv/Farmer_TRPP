package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CheckPasswordFragment extends Fragment {
    private EditText etPassword;
    private Button btnChange;
    private String usersPassword, usersPhone;
    private ImageButton btnCloseChechPassword;
    public CheckPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_password, container, false);
        etPassword = view.findViewById(R.id.etPassword);
        btnChange = view.findViewById(R.id.btnChange);
        btnCloseChechPassword = view.findViewById(R.id.btnCloseChechPassword);
        usersPassword = getArguments().getString("Password");
        usersPhone = getArguments().getString("Phone");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users userData = snapshot.child("Users").child(usersPhone).getValue(Users.class);
                        if (userData.getPassword().equals(etPassword.getText().toString())) {
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("result", true);
                            getParentFragmentManager().setFragmentResult("resultCheckPassword", bundle);
                            getParentFragmentManager().popBackStack();
                        }
                        else {
                            Toast.makeText(getActivity(), "Неверный пароль", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        btnCloseChechPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        return view;
    }
}