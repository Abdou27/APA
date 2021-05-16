package com.univ.tours.apa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import com.univ.tours.apa.R;
import com.univ.tours.apa.database.AppDatabase;
import com.univ.tours.apa.entities.User;

public class MainActivity extends AppCompatActivity {
    private static final String APA = "apa";
    public static AppDatabase db;
    private SharedPreferences settings;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getDatabase(this);

        fm = getSupportFragmentManager();

        settings = getSharedPreferences(APA, Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if user is logged in
            Long user_id = settings.getLong("user_id", 0);
            if (user_id.equals(0L)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                new Thread((Runnable) () -> {
                    User user = MainActivity.db.userDao().findById(user_id);
                    if (user != null) {
                        Looper.prepare();
                        Toast.makeText(this, getString(R.string.logged_in_as) + " " + user.getFullName(), Toast.LENGTH_LONG).show();
                        if (user.getRole().equals("ROLE_DOCTOR")) {
                            Intent intent = new Intent(this, DoctorActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (user.getRole().equals("ROLE_COLLABORATOR")) {
                            Intent intent = new Intent(this, CollaboratorActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(this, PatientActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        Looper.loop();
                    } else {
                        getSharedPreferences(APA, Context.MODE_PRIVATE).edit().putLong("user_id", 0L).apply();
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).start();
            }
    }
}