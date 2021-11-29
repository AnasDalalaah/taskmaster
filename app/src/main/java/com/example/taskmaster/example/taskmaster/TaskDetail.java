package com.example.taskmaster;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.google.android.gms.maps.GoogleMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;

public class TaskDetail extends AppCompatActivity {
    public static final String TASK_NAME = "task_name";
    public static final String TASK_BODY = "task_body";
    public static final String TASK_STATE = "task_state";
    private URL url = null;
    private Handler handler;
    private static final String TAG = "TaskDetail";
    private static final int REQUEST_FOR_FILE = 999;
    private GoogleMap googleMap;

    private double lat;
    private double lon;

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
         