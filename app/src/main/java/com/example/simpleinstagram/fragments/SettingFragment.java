package com.example.simpleinstagram.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.simpleinstagram.LoginActivity;
import com.example.simpleinstagram.R;
import com.parse.ParseUser;

import java.util.Objects;

public class SettingFragment extends Fragment {
    private Button btnLogout;
    public static final String TAG = "SettingFragment";

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogout = view.findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                // Toast.makeText(ProfileActivity.this, "Logging out...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                // prevent user from returning to previous activities
                Objects.requireNonNull(getActivity()).finishAffinity();
            }
        });

    }
}