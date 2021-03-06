package com.example.controlroom.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.controlroom.R;


public class Cadastro_ou_Login extends AppCompatActivity {

    private Button cadastrar;
    private Button login;
    private SharedPreferences preferences;
    public static final String userPreferences = "userPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences(userPreferences, Context.MODE_PRIVATE);

        if (preferences.contains("userEmail")) {

            startMenuSalas();
        }

        setContentView(R.layout.activity_cadastro_ou__login);

        cadastrar = findViewById(R.id.cadastrar);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCadastrar();
            }
        });
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogin();
            }
        });
    }

    private void startCadastrar() {
        Intent intent = new Intent(Cadastro_ou_Login.this, Cadastrar.class);
        startActivity(intent);
        finish();
    }

    private void startLogin() {
        Intent intent = new Intent(Cadastro_ou_Login.this, Login.class);
        startActivity(intent);
        finish();
    }

    private void startMenuSalas() {
        Intent intent = new Intent(Cadastro_ou_Login.this, MenuSalas.class);
        startActivity(intent);
        finish();

    }


}

