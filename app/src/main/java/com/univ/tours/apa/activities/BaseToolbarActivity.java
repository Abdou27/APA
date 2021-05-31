package com.univ.tours.apa.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.univ.tours.apa.R;
import com.univ.tours.apa.fragments.common.ProfileFragment;

public class BaseToolbarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        boolean inStats = getIntent().getBooleanExtra("inStats", false);

        if (inStats) {
            menu.findItem(R.id.action_stats).setVisible(false);
            menu.findItem(R.id.action_home).setVisible(true);
        } else {
            menu.findItem(R.id.action_stats).setVisible(true);
            menu.findItem(R.id.action_home).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_logout :
                MainActivity.logoutCurrentUser();
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(this, getString(R.string.logged_out), Toast.LENGTH_LONG).show();
                finish();
                break;
            case R.id.action_profile :
                ProfileFragment.newInstance().show(getSupportFragmentManager(), "profileFragment");
                break;
            case R.id.action_stats :
                intent = new Intent(this, StatisticsActivity.class);
                intent.putExtra("inStats", true);
                startActivity(intent);
                break;
            case R.id.action_home :
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}