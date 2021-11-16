package com.example.taskmaster;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
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
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.datastore.generated.model.State;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.example.taskmaster.adapter.TaskAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.annotations.NonNull;



public class MainActivity extends AppCompatActivity {

    public static final String TASK_NAME = "task_name";
    public static final String TASK_BODY = "task_body";
    public static final String TASK_STATE = "task_state";

    private static final String TAG = "MainActivity";

    private List<Task> tasks;
    private TaskAdapter adapter;
    private Handler handler;
    private List<Team> teams;
    private String selectedTeam;
    private SharedPreferences preferences;

//    AppDatabase database;
//    private TaskDao taskDao;

    @SuppressLint("SetTextI18n")

    @Override
    public void onResume() { // this is probably the correct place for ALL rendered info

        super.onResume();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView teamName = findViewById(R.id.textMain_teamName);
        TextView loggedInUser = findViewById(R.id.textMain_logInUser);
        selectedTeam = preferences.getString("selectedTeam", "Go to Settings to set your team name");
        teamName.setText(selectedTeam);
        
        String myUser = preferences.getString("loggedInUser", "");
        loggedInUser.setText(myUser);

        if (isNetworkAvailable(getApplicationContext())) {
            queryAPITasks();
            Log.i(TAG, "NET: the network is available");
        } else {
            tasks = queryDataStore();
            Log.i(TAG, "NET: net down");
        }

        //tasks = queryDataStore();
    }


    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        selectedTeam = preferences.getString("selectedTeam", " ")


        /**Lab32**/
        /*try {
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("Task", "Initialized Amplify");


        } catch (AmplifyException e) {
            Log.e("Task", "Could not initialize Amplify", e);
        }
*/
        setContentView(R.layout.activity_main);

        tasks = new ArrayList<>();

        RecyclerView taskRecyclerView = findViewById(R.id.recyclerView_task);
        
        handler = new Handler(Looper.getMainLooper(),
                msg -> {
                    Objects.requireNonNull(taskRecyclerView.getAdapter()).notifyDataSetChanged();
                    return false;
                });
        if (isNetworkAvailable(getApplicationContext())) {
            tasks = queryAPITasks();
            Log.i(TAG, "NET: the network is available");
        } else {
            tasks = queryDataStore();
            Log.i(TAG, "NET: net down");
            
        //tasks = queryDataStore();
 
        }

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


        /***End of Lab28***/

        Button navToAddTask = MainActivity.this.findViewById(R.id.buttonMain_addTask);
        navToAddTask.setOnClickListener(view -> {
           Intent newIntent = new Intent(MainActivity.this, AddATask.class);
            startActivity(newIntent);
        });

        Button navToAllTasks = MainActivity.this.findViewById(R.id.buttonMain_allTask);
        navToAllTasks.setOnClickListener(view -> {
            Intent newIntent = new Intent(MainActivity.this, AllTasks.class);
            startActivity(newIntent);
        });

        ImageButton navToSettingsButton = MainActivity.this.findViewById(R.id.buttonMain_settings);
        navToSettingsButton.setOnClickListener(view -> {
            Intent newIntent = new Intent(MainActivity.this, AllTasks.class);
            startActivity(newIntent);
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
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
            return true;
        }
        
        if (id == R.id.action_task) {
            Intent intent = new Intent(MainActivity.this, AddATask.class);
            startActivity(intent);
            return true;
        }


        if (id == R.id.action_logout) {
            Amplify.Auth.signOut(
                    AuthSignOutOptions.builder().globalSignOut(true).build(),
                    () -> {
                        Log.i("AuthQuickstart", "Signed out globally");
                        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                        startActivity(intent);
                    },
                    error -> Log.e("AuthQuickstart", error.toString())
            );

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

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager
                .getActiveNetworkInfo().isConnected();
    }

    public synchronized List<Task> queryAPITasks() {
        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        Amplify.API.query(
                ModelQuery.list(Task.class),
                response -> {
                    tasks.clear();
                    for (Task task : response.getData()) {
                        if (preferences.contains("selectedTeam")) {
                            if (task.getTeam().getName().equals(selectedTeam)) {
                                tasks.add(task);
                            }
                        } else {
                            tasks.add(task);
                        }
                    }
                    handler.sendEmptyMessage(1);
                    Log.i("amplify.queryItems", "Got this many: " + tasks.size());
                },
                error -> Log.i("Amplify.queryItems", "Did not receive tasks")
        );
        return tasks;
    }


    public synchronized List<Task> queryDataStore() {
    preferences = PreferenceManager.getDefaultSharedPreferences(this);

        List<Task> tasks = new ArrayList<>();
        Amplify.DataStore.query(Task.class,
                amplifyTasks -> {
                    while (amplifyTasks.hasNext()) {
                        Task oneTask = amplifyTasks.next();

                        
                         if (preferences.contains("selectedTeam")) {
                            tasks.add(oneTask);
                        }
                        System.out.println("tttteeeeeeeeeeeeeeeeeeeeeaaaaaaaaaaaaaaaaaaaaaaaaaaam" + oneTask.getTeam().getName());

                        Log.i("Task", "==== Task ====");
                        Log.i("Task", "Title: " + oneTask.getTitle());
                        if (oneTask.getBody() != null) {
                            Log.i("Task", "Body: " + oneTask.getBody());
                        }
                        if (oneTask.getState() != null) {
                            Log.i("Task", "State: " + oneTask.getState().toString());
                        }
                        if (oneTask.getTeam().getName() != null) {
                            Log.i("Task", "State: " + oneTask.getTeam().getName().toString());
                        }
                        Log.i("Tutorial", "==== Task End ====");
                    }
                }, failure -> Log.e("Tutorial", "Could not query DataStore", failure)
        );

        return tasks;
    }
         //lab33
    public void buildTeams() {
        Team team1 = Team.builder()
                .name("team1")
                .build();

        Team team2 = Team.builder()
                .name("team2")
                .build();

        Team team3 = Team.builder()
                .name("team3")
                .build();

        Amplify.API.mutate(ModelMutation.create(team1),
                response -> Log.i("team", "added"),
                error -> Log.e("team", "failed")
        );

        Amplify.API.mutate(ModelMutation.create(team2),
                response -> Log.i("team", "added"),
                error -> Log.e("team", "failed")
        );

        Amplify.API.mutate(ModelMutation.create(team3),
                response -> Log.i("team", "added"),
                error -> Log.e("team", "failed")
        );

    }
}