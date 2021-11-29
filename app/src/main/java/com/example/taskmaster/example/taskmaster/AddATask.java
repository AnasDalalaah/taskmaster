package com.example.taskmaster;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

//import androidx.room.Room;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.content.Intent;
import android.os.Looper;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.NewFile;
import com.amplifyframework.datastore.generated.model.State;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.provider.Settings;


public class AddATask extends AppCompatActivity {
    //    AppDatabase database;
    private EditText editTitle;
    private EditText editDescription;
    private static final String[] paths = {"new", "assigned", "in progress", "complete"};
    private int selectedState;
    List<Team> teams;
    RadioButton team1, team2, team3;

     // lab 42
    private FusedLocationProviderClient fusedLocationProviderClient;

    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            Log.i(TAG, "The location is => " + mLastLocation);
        }
    };

    private double lat;
    private double lon;

    private static final int REQUEST_PERMISSION = 123;
    private static final int REQUEST_OPEN_GALLERY = 1111;
    private static final int PERMISSION_ID = 44;

    String fileKey;
    public static final int REQUEST_FOR_FILE = 999;
    
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        
        setContentView(R.layout.activity_add_atask);
        teams = new ArrayList<>();
        team1 = this.findViewById(R.id.radioButton_team1);
        team2 = this.findViewById(R.id.radioButton_team2);
        team3 = this.findViewById(R.id.radioButton_team3);

        if (isNetworkAvailable(getApplicationContext())) {
                    queryAPITeams();

        } else {
                        queryDataStore();
            Log.i(TAG, "NET: net down");
        }
          //lab 42
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Button getLocationBut= findViewById(R.id.getLocationId);
        getLocationBut.setOnClickListener(view -> {
            getLastLocation();
        });

          //**************Lab41**************//
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        image = findViewById(R.id.imageView_showFromS3);
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (imageUri != null) {
                    image.setImageURI(imageUri);
                    image.setImageURI(View.VISIBLE);
                }
            }
        
          //**************Lab37**************//
        Button addPic = findViewById(R.id.button_addImage);
        addPic.setOnClickListener((view -> retrieveFile()));
        //*********************************//

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

                         //**************Lab37**************//

//                NewFile newFile = NewFile.builder()
//                        .belongsTo(newTask)
//                        .fileName(fileKey)
//                        .build();
                //*********************************//

               AnalyticsEvent event = AnalyticsEvent.builder()
                        .name("Add a task")
                        .addProperty("Channel", "SMS")
                        .addProperty("time", Long.toString(new Date().getTime()))
                        .addProperty("Successful", true)
                        .build();

                Amplify.Analytics.recordEvent(event);


                Log.i("Task", "Initialized Amplify");

            } catch (Exception e) {
                Log.e("Task", "Could not initialize Amplify", e);
            }
// lab 42
    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {

            if (isLocationEnabled()) {

                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {

                    Location location = task.getResult();

                    if (location == null) {
                        requestNewLocationData();
                    } else {
                        lat = location.getLatitude();
                        lon = location.getLongitude();
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            }
        }
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5);
        locationRequest.setFastestInterval(0);
        locationRequest.setNumUpdates(10);

        // setting LocationRequest
        // on FusedLocationClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this); // this may or may not be needed
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }

        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getFileFromDevice();
            } else {
                Log.i(TAG, "Error : Permission Field");
            }
        }
    }
            

            Intent goToMainActivity = new Intent(AddATask.this, MainActivity.class);
            AddATask.this.startActivity(goToMainActivity);

        });

    }
    
     public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager
                .getActiveNetworkInfo().isConnected();
    }

    public synchronized void queryAPITeams() {
        Amplify.API.query(
                ModelQuery.list(Team.class),
                response -> {
                    for (Team team : response.getData()) {
                        teams.add(team);
                    }
                    System.out.println("Semasemasemasemasema" + teams.get(0).getName());
                    Log.i("Team", "success");
                },
                error -> Log.e("Team", "failed to retrieve data")
        );
        Log.i(TAG, "NET: the network is available");
    }

    public synchronized void queryDataStore() {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor = preferences.edit();
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

    }
    //**************Lab37**************//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_FOR_FILE && resultCode == RESULT_OK) {
            Log.i(TAG, "onActivityResult: returned from file explorer");
            Log.i(TAG, "onActivityResult: => " + data.getData());
            File uploadFile = new File(getApplicationContext().getFilesDir(), "uploadFile");

            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                FileOutputStream outputStream = new FileOutputStream(uploadFile);
                copyStream(inputStream, outputStream);

//                When I use it, android 9 crashes
//                FileUtils.copy(inputStream, new FileOutputStream(uploadFile));

            } catch (Exception exception) {
                exception.printStackTrace();
                Log.e(TAG, "onActivityResult: file upload failed" + exception.toString());
            }

            fileKey = new Date().toString() + ".png";

            Amplify.Storage.uploadFile(
                    fileKey,
                    uploadFile,
                    success -> {
                        Log.i(TAG, "uploadFileToS3: succeeded " + success.getKey());
                        downloadFile(success.getKey());

                        SharedPreferences preferences =
                                PreferenceManager.getDefaultSharedPreferences(this);
                        SharedPreferences.Editor preferenceEditor = preferences.edit();
                        preferenceEditor.putString("ImageKey", success.getKey());
                        preferenceEditor.apply();


                    },
                    error -> {
                        Log.e(TAG, "uploadFileToS3: failed " + error.toString());
                    }
            );
        }
    }


    public void retrieveFile() {
          AnalyticsEvent event = AnalyticsEvent.builder()
                .name("Going to device to fetch photo")
                .addProperty("time", Long.toString(new Date().getTime()))
                .addProperty("Successful", true)
                .build();
        Amplify.Analytics.recordEvent(event);

        Intent getPicIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getPicIntent.setType("*/*");
        startActivityForResult(getPicIntent, REQUEST_FOR_FILE);
    }

    private void downloadFile(String fileKey) {
        Amplify.Storage.downloadFile(
                fileKey,
                new File(getApplicationContext().getFilesDir() + "/" + fileKey + ".txt"),
                result -> {
                    Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getName());
                    image = findViewById(R.id.imageView_showFromS3);
                    image.setImageBitmap(BitmapFactory.decodeFile(result.getFile().getPath()));
                    image.setVisibility(View.VISIBLE);
                },
                error -> Log.e("MyAmplifyApp", "Download Failure", error)


        );
    }

    public static void copyStream(InputStream in, OutputStream out) throws Exception {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    //**************End Lab37**************//

}
    
