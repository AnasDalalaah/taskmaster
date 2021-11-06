package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;


public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        public static final String TASK_NAME = "task_name";
        public static final String TASK_BODY = "task_body";
        public static final String TASK_STATE = "task_state";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        TextView task = findViewById(R.id.text_detail);
        TextView taskTitle = findViewById(R.id.task_title);
        TextView taskDescription = findViewById(R.id.text_detailDescription);

        detailPageTitle.setText(pref.getString(TASK_NAME, "No task selected"));
        taskTitle.setText(pref.getString(TASK_STATE, "No task selected"));
        taskDescription.setText(pref.getString(TASK_BODY, "No task selected"));
        }

       public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(i, 0);
        return true;


    }
}