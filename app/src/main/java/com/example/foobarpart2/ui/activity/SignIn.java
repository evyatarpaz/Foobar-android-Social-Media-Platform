package com.example.foobarpart2.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.foobarpart2.R;
import com.example.foobarpart2.databinding.ActivitySignInBinding;
import com.example.foobarpart2.models.LoggedInUser;
import com.example.foobarpart2.ui.viewmodels.UserViewModel;


public class SignIn extends AppCompatActivity {
    private ActivitySignInBinding binding;
    SwitchCompat switchMode;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        switchMode = findViewById(R.id.switchMode);

        sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("nightMode", false);

        if (!nightMode) {
            switchMode.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        switchMode.setOnClickListener(v -> {
            if (nightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor = sharedPreferences.edit();
                editor.putBoolean("nightMode", false);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor = sharedPreferences.edit();
                editor.putBoolean("nightMode", true);
            }
            editor.apply();
        });

        binding.btnSignIn.setOnClickListener(v -> {

            String username = binding.username.getText().toString();
            String password = binding.password.getText().toString();

            UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
            userViewModel.authenticate(username, password);
            userViewModel.getAuthenticateResult().observe(this, isSuccess -> {
                if (isSuccess) {
                    userViewModel.getLoggedInUser(username).observe(this,user -> {
                        LoggedInUser.getInstance().setUser(user);
                        Intent i = new Intent(this, Feed.class);
                        startActivity(i);
                    });
                } else {
                    Toast.makeText(this, "Failed to Sign In please try again later.."
                            , Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.retSignUp.setOnClickListener(v -> {
            Intent i = new Intent(this, SignUp.class);
            startActivity(i);
        });
    }
}
