package com.example.taskmaster;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;


public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        TextView task = findViewById(R.id.text_detail);
        task.setText(pref.getString("taskTitle", "No task selected"));


    }
}