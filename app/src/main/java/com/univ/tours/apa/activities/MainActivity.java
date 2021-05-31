package com.univ.tours.apa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import com.univ.tours.apa.R;
import com.univ.tours.apa.database.AppDatabase;
import com.univ.tours.apa.demo.DatabaseSeeder;
import com.univ.tours.apa.entities.User;

import java.time.LocalDate;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String APA = "apa";
    public static AppDatabase db;
    public static SharedPreferences settings;
    public static ProgressDialog loadingDialog;
    FragmentManager fm;

    public static Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getDatabase(this);

        context = this;

        fm = getSupportFragmentManager();

        settings = getSharedPreferences(APA, Context.MODE_PRIVATE);

        // To setup up the database for the demo
        DatabaseSeeder.setupDatabaseForDemo();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadingDialog = ProgressDialog.show(this, "", getString(R.string.loading_progress_bar_text), true);

//        Check if user is logged in
        Long user_id = getLoggedInUserId();
        if (user_id.equals(0L)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startIntentActivityAndFinishCurrentOne(intent);
        } else {
            new Thread(() -> {
                User user = MainActivity.db.userDao().findById(user_id);
                if (user != null) {
                    Looper.prepare();
                    Toast.makeText(this, getString(R.string.logged_in_as) + " " + user.getFullName(), Toast.LENGTH_LONG).show();
                    if (user.getRole().equals("ROLE_DOCTOR")) {
                        Intent intent = new Intent(this, DoctorActivity.class);
                        startIntentActivityAndFinishCurrentOne(intent);
                    } else if (user.getRole().equals("ROLE_COLLABORATOR")) {
                        Intent intent = new Intent(this, CollaboratorActivity.class);
                        startIntentActivityAndFinishCurrentOne(intent);
                    } else {
                        Intent intent = new Intent(this, PatientActivity.class);
                        startIntentActivityAndFinishCurrentOne(intent);
                    }
                    Looper.loop();
                } else {
                    settings.edit().putLong("user_id", 0L).apply();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startIntentActivityAndFinishCurrentOne(intent);
                }
            }).start();
        }
    }

    private void startIntentActivityAndFinishCurrentOne(Intent intent) {
        startActivity(intent);
        loadingDialog.dismiss();
        finish();
    }

    public static Long getLoggedInUserId() {
        return settings.getLong("user_id", 0);
    }

    public static void logoutCurrentUser() {
        settings.edit().putLong("user_id", 0L).apply();
    }
}