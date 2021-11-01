package com.example.taskmaster;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
     public void onResume() { 
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView address = findViewById(R.id.textMain_username);
        address.setText(preferences.getString("username", "Go to Settings to set your username"));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button navToAddTask = MainActivity.this.findViewById(R.id.buttonMain_addTask);
        navToAddTask.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddATask.class);
            startActivity(intent);
        });

        Button navToAllTasks = MainActivity.this.findViewById(R.id.buttonMain_allTask);
        navToAllTasks.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AllTasks.class);
            startActivity(intent);
        });

        ImageButton navToSettingsButton = MainActivity.this.findViewById(R.id.buttonMain_settings);
        navToSettingsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
        });


        NotificationChannel channel = new NotificationChannel("basic", "basic", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("basic notifications");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor = preferences.edit();

        Button selectFirstTask = MainActivity.this.findViewById(R.id.button_firstTask);
        selectFirstTask.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TaskDetail.class);
            Button firstButton = findViewById(R.id.button_firstTask);
            preferenceEditor.putString("taskTitle", firstButton.getText().toString());
            preferenceEditor.apply();
            MainActivity.this.startActivity(intent);
        });

        Button selectSecondTask = MainActivity.this.findViewById(R.id.button_secondTask);
        selectSecondTask.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TaskDetail.class);
            Button secondButton = findViewById(R.id.button_secondTask);
            preferenceEditor.putString("taskTitle", secondButton.getText().toString());
            preferenceEditor.apply();
            MainActivity.this.startActivity(intent);
        });

        Button selectThirdTask = MainActivity.this.findViewById(R.id.button_thirdTask);
        selectThirdTask.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, TaskDetail.class);
            Button thirdButton = findViewById(R.id.button_thirdTask);
            preferenceEditor.putString("taskTitle", thirdButton.getText().toString());
            preferenceEditor.apply();
            MainActivity.this.startActivity(i);
        });

    }
  
}