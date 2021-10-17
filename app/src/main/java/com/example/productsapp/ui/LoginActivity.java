package com.example.productsapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.productsapp.R;

public class LoginActivity extends Activity {

    Button btnUserLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        InitControls();
        InitControlEvents();
  }

    private void InitControls() {
        btnUserLogin = findViewById(R.id.btnUserLogin);
    }

    private void InitControlEvents() {
        btnUserLogin.setOnClickListener(view -> {

            Intent intent = new Intent(LoginActivity.this, ProductsActivity.class);

            startActivityForResult(intent, 555);

        });
    }

}
