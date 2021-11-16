package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;

public class VerificationActivity extends AppCompatActivity {

    private static final String TAG = "VerificationActivity";
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        EditText editText = findViewById(R.id.confirmationCode);
        Button verify = findViewById(R.id.verify);

        Intent intent = getIntent();
        username = intent.getExtras().getString("username", "");
        password = intent.getExtras().getString("password", "");

        verify.setOnClickListener(view -> verification(username, editText.getText().toString()));
    }

    void verification(String username, String confirmationNumber) {
        Amplify.Auth.confirmSignUp(
                username,
                confirmationNumber,
                success -> {
                    Log.i(TAG, "verification: succeeded" + success.toString());
                    Intent goToSignIn = new Intent(VerificationActivity.this, SignInActivity.class);
                    goToSignIn.putExtra("username", username);
                    startActivity(goToSignIn);

//                    silentSignIn(username, password);
                },
                error -> {
                    Log.e(TAG, "verification: failed" + error.toString());
                });
    }

    void silentSignIn(String username, String password) {
        Amplify.Auth.signIn(
                username,
                password,
                success -> {
                    Log.i(TAG, "signIn: worked " + success.toString());
                },
                error -> Log.e(TAG, "signIn: failed" + error.toString()));
    }
}
