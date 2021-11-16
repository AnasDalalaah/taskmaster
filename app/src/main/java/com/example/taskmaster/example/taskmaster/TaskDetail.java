package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;


public class TaskDetail extends AppCompatActivity {
    public static final String TASK_NAME = "task_name";
    public static final String TASK_BODY = "task_body";
    public static final String TASK_STATE = "task_state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);


        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        TextView detailPageTitle = findViewById(R.id.text_detail);
        TextView taskTitle = findViewById(R.id.task_title);
        TextView taskDescription = findViewById(R.id.text_detailDescription);

        detailPageTitle.setText(pref.getString(TASK_NAME, "No task selected"));
        taskTitle.setText(pref.getString(TASK_STATE, "No task selected"));
        taskDescription.setText(pref.getString(TASK_BODY, "No task selected"));


      //**************Lab37**************//
        downloadFile(pref.getString("ImageKey", ""));

    }

    private void downloadFile(String key) {
        Amplify.Storage.downloadFile(
                key,
                new File(getApplicationContext().getFilesDir() + "/" + key + ".txt"),
                result -> {
                    Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getName());
                    ImageView image = findViewById(R.id.imageView_fromS3);
                    image.setImageBitmap(BitmapFactory.decodeFile(result.getFile().getPath()));
                    image.setVisibility(View.VISIBLE);
                },
                error -> Log.e("MyAmplifyApp", "Download Failure", error)
        );
    }
    //**************End Lab37**************//

}    
         