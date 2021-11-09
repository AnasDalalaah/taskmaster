package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor = preferences.edit();

        findViewById(R.id.saveButton).setOnClickListener((view) -> {
            EditText address = findViewById(R.id.edit_username);
            preferenceEditor.putString("username", address.getText().toString());
            preferenceEditor.apply();

            Toast toast = Toast.makeText(this, "You saved your username", Toast.LENGTH_LONG);

            toast.show();
        });

        findViewById(R.id.goHome).setOnClickListener((view) -> {
                    Intent intent = new Intent(Settings.this, MainActivity.class);
                    Settings.this.startActivity(intent);
                }
        );
    }


}