package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.core.Amplify;
import java.util.Date;

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
            preferenceEditor.putString("selectedTeam", teamName.getText().toString());

            preferenceEditor.apply();

            AnalyticsEvent event = AnalyticsEvent.builder()
                    .name("add username and team name")
                    .addProperty("Channel", "SMS")
                    .addProperty("time", Long.toString(new Date().getTime()))
                    .addProperty("Successful", true)
                    .build();

            Amplify.Analytics.recordEvent(event);

            Toast toast = Toast.makeText(this, "You saved your username", Toast.LENGTH_LONG);

            toast.show();
        });

        findViewById(R.id.goHome).setOnClickListener((view) -> {
                   AnalyticsEvent event = AnalyticsEvent.builder()
                            .name("moved from settings to home")
                            .addProperty("time", Long.toString(new Date().getTime()))
                            .addProperty("Successful", true)
                            .build();
                    Amplify.Analytics.recordEvent(event);
                    Intent intent = new Intent(Settings.this, MainActivity.class);
                    Settings.this.startActivity(intent);
                }
        );
    }


}