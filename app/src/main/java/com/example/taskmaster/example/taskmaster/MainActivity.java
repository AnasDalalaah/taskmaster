package com.example.taskmaster;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.datastore.generated.model.State;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.example.taskmaster.adapter.TaskAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TASK_NAME = "task_name";
    public static final String TASK_BODY = "task_body";
    public static final String TASK_STATE = "task_state";

    private static final String TAG = "MainActivity";
    private List<Task> tasks;
    private TaskAdapter adapter;
    private Handler handler;


//    AppDatabase database;
//    private TaskDao taskDao;


    @Override
    public void onResume() { // this is probably the correct place for ALL rendered info

        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView address = findViewById(R.id.textMain_username);
        address.setText(preferences.getString("username", "Go to Settings to set your username"));
        queryDataStore();
    }


    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Lab32*/
        try {
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("Task", "Initialized Amplify");


        } catch (AmplifyException e) {
            Log.e("Task", "Could not initialize Amplify", e);
        }

        setContentView(R.layout.activity_main);
        tasks = new ArrayList<>();
        tasks = queryDataStore();

        RecyclerView taskRecyclerView = findViewById(R.id.recyclerView_task);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        adapter = new TaskAdapter(tasks, new TaskAdapter.OnTaskItemClickListener() {

            @Override
            public void onItemClicked(int position) {
                Intent goToDetailsIntent = new Intent(getApplicationContext(), TaskDetail.class);
                preferenceEditor.putString(TASK_NAME, tasks.get(position).getTitle());
                preferenceEditor.putString(TASK_BODY, tasks.get(position).getBody());
                preferenceEditor.putString(TASK_STATE, tasks.get(position).getState().toString());
                preferenceEditor.apply();
                startActivity(goToDetailsIntent);
            }

            @Override
            public void onDeleteItem(int position) {
                Amplify.API.mutate(ModelMutation.delete(tasks.get(position)),
                        response -> Log.i(TAG, "Deleted successfully"),
                        error -> Log.e(TAG, "Delete failed", error)
                );

                Amplify.DataStore.delete(tasks.get(position),
                        success -> Log.i(TAG, "Deleted successfully" + success.item().toString()),
                        failure -> Log.e(TAG, "Delete failed", failure));

                tasks.remove(position);
                Log.i(TAG, "onDeleteItem: our list =>>>> " + tasks.toString());
                listItemDeleted();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false);
        taskRecyclerView.setLayoutManager(linearLayoutManager);
        taskRecyclerView.setAdapter(adapter);

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

    }

    @SuppressLint("NotifyDataSetChanged")
    private void listItemDeleted() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * saveTaskToStoreAndApi
     * @param title
     * @param body
     * @param state
     */
    public static void saveTasksToDataStore(String title, String body, State state) {
        Task item = Task.builder().title(title).body(body).state(state).build();

        Amplify.DataStore.save(item,
                success -> Log.i(TAG, "Saved item: " + success.item().toString()),
                error -> Log.e(TAG, "Could not save item to DataStore", error)
        );


    }

    /**
     * queryDataStore()
     *
     * @return List of amplifyTasks
     */
    public synchronized static List<Task> queryDataStore() {
        List<Task> tasks = new ArrayList<>();
        Amplify.DataStore.query(Task.class,
                amplifyTasks -> {
                    while (amplifyTasks.hasNext()) {
                        Task oneTask = amplifyTasks.next();
                        tasks.add(oneTask);

                        Log.i("Task", "==== Task ====");
                        Log.i("Task", "Title: " + oneTask.getTitle());
                        if (oneTask.getBody() != null) {
                            Log.i("Task", "Body: " + oneTask.getBody().toString());
                        }
                        if (oneTask.getState() != null) {
                            Log.i("Task", "State: " + oneTask.getState().toString());
                        }
                        Log.i("Tutorial", "==== Task End ====");
                    }
                }, failure -> Log.e("Tutorial", "Could not query DataStore", failure)
        );

        return tasks;
    }

}