package com.example.taskmaster;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent; 
import android.content.SharedPreferences;
import android.os.Bundle;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.State;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;

public class AddATask extends AppCompatActivity {
    //    AppDatabase database;
    private EditText editTitle;
    private EditText editDescription;
    private static final String[] paths = {"new", "assigned", "in progress", "complete"};
    private int selectedState;
    List<Team> teams;
    RadioButton team1, team2, team3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor preferenceEditor = preferences.edit();
        setContentView(R.layout.activity_add_atask);
        teams = new ArrayList<>();
        team1 = this.findViewById(R.id.radioButton_team1);
        team2 = this.findViewById(R.id.radioButton_team2);
        team3 = this.findViewById(R.id.radioButton_team3);

        if (isNetworkAvailable(getApplicationContext())) {
        Amplify.API.query(
                  ModelQuery.list(Team.class),
                  response -> {
                      for (Team team : response.getData()) {
                            teams.add(team);
                        }
                        System.out.println("aaaaaaaaaaaaaaaaaaaa" + teams.get(0).getName());
                        Log.i("Team", "success");
                    },
                    error -> Log.e("Team", "failed to retrieve data")
            );
            Log.i(TAG, "NET: the network is available");
        } else {
            Amplify.DataStore.query(Team.class
                    ,
                    amplifyTeam -> {
                        while (amplifyTeam.hasNext()) {
                            Team team = amplifyTeam.next();
                            teams.add(team);
                            Log.i("Team", "==== Team ====");
                            Log.i("Team", "Name: " + team.getName());
                            if (team.getTasks() != null) {
                                Log.i("Team", "Tasks: " + team.getTasks().toString());
                            }
                            Log.i("Team", "==== Team End ====");

                            preferenceEditor.putString("selectedTeamName", team.getName());
                            preferenceEditor.apply();
                        }

                    }, failure -> Log.e("Tutorial", "Could not query DataStore", failure)
            );
            Log.i(TAG, "NET: net down");
        }



        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddATask.this,
                android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                        int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    selectedState = item.toString().equals("new") ? 0 : item.toString().equals("assigned") ? 1 : item.toString().equals("in progress") ? 2 : 3;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedState = 1;
            }
        });
        /**
         * spinnerPartIsFinished
         **/

        /**
         * Title and body part
         **/
        editTitle = AddATask.this.findViewById(R.id.edit_myTask);
        editDescription = AddATask.this.findViewById(R.id.edit_doSomething);
        Context context = getApplicationContext();
        CharSequence text = "Submitted!";
        int duration = Toast.LENGTH_SHORT;
        final Toast toast = Toast.makeText(context, text, duration);
        Button addTaskButton = AddATask.this.findViewById(R.id.button_addTask);
        addTaskButton.setOnClickListener(view -> {
            toast.show();
            // save data
            try {

                 RadioGroup radioGroup = AddATask.this.findViewById(R.id.radioGroup);
                RadioButton selectedTeam = AddATask.this.findViewById(radioGroup.getCheckedRadioButtonId());
                String teamName = selectedTeam.getText().toString();
                Team myTeam = null;
                //for finding the team
                for (int i = 0; i < teams.size(); i++) {
                    if (teams.get(i).getName().equals(teamName)) {
                        myTeam = teams.get(i);
                    }
                }

                Task newTask = Task.builder()
                        .title(editTitle.getText().toString())
                        .body(editDescription.getText().toString())
                        .state(State.values()[selectedState])
                        .team(myTeam)
                        .build();
        // API
                Amplify.DataStore.save(newTask,
                        success -> Log.i("Task", "Saved item: " + success.item().getTitle()),
                        error -> Log.e("Task", "Could not save item to DataStore", error)
                );
                 Amplify.API.mutate(
                        ModelMutation.create(newTask),
                        response -> Log.i("Task", "success!"),
                        error -> Log.e("Task", "Failure", error));

                Log.i("Task", "Initialized Amplify");

            } catch (Exception e) {
                Log.e("Task", "Could not initialize Amplify", e);
            }

            

            Intent goToMainActivity = new Intent(AddATask.this, MainActivity.class);
            AddATask.this.startActivity(goToMainActivity);

        });

    }
    
     @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    public void onClick(View view) {
//        Log.i(TAG, "onClick: new way was called");

        switch (view.getId()) {
            case R.id.buttonMain_addTask:
                editTitle.setText("Hello");
                break;
            case R.id.button_addTask:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("input", editTitle.getText().toString());
                startActivity(intent);
                break;
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager
                .getActiveNetworkInfo().isConnected();
    }


}
    
