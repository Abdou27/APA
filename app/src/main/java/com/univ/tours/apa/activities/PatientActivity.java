package com.univ.tours.apa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.android.material.snackbar.Snackbar;
import com.univ.tours.apa.R;
import com.univ.tours.apa.database.AppDatabase;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Course;
import com.univ.tours.apa.entities.Session;
import com.univ.tours.apa.entities.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PatientActivity extends BaseToolbarActivity {
    private static final String APA = "apa";
    List<WeekViewEvent> events;
    List<Session> userSessions;
    User user;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        settings = getSharedPreferences(APA, Context.MODE_PRIVATE);
        Long user_id = settings.getLong("user_id", 0);
        new Thread(() -> {
            user = MainActivity.db.userDao().findById(user_id);
            List<Course> courses = MainActivity.db.courseDao().findByPatient(user);
            List<Activity> activities = MainActivity.db.activityDao().findByCourses(courses);
            userSessions = MainActivity.db.sessionDao().findByActivities(activities);
        }).start();

        WeekView mWeekView = findViewById(R.id.weekView);
        // Set an action when any event is clicked.
        // mWeekView.setOnEventClickListener(mEventClickListener);
        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        MonthLoader.MonthChangeListener mMonthChangeListener = (newYear, newMonth) -> {
            // Populate the week view with some events.
            List<WeekViewEvent> events = new ArrayList<>();
//            Log.d("TAG", "userSessions: " + userSessions.size());
            if (userSessions != null) {
                for (Session session : userSessions) {
                    LocalDateTime dateTime = session.getDateTime();
                    int sessionYear = dateTime.getYear();
                    int sessionMonth = dateTime.getMonthValue();
//                    Log.d("TAG", "sessionNumber: " + userSessions.indexOf(session));
//                    Log.d("TAG", "sessionYear: " + sessionYear);
//                    Log.d("TAG", "sessionMonth: " + sessionMonth);
//                    Log.d("TAG", "newYear: " + newYear);
//                    Log.d("TAG", "newMonth: " + newMonth);
                    if (sessionYear == newYear && sessionMonth == (newMonth)) {
                        Integer duration = session.getDuration();
                        Log.d("TAG", "session.getDateTime: " + session.getDateTime());
                        Calendar startTime = Calendar.getInstance();
                        startTime.set(Calendar.SECOND, dateTime.getSecond());
                        startTime.set(Calendar.MINUTE, dateTime.getMinute());
                        startTime.set(Calendar.HOUR_OF_DAY, dateTime.getHour());
                        startTime.set(Calendar.DAY_OF_MONTH, dateTime.getDayOfMonth());
                        startTime.set(Calendar.MONTH, sessionMonth - 1);
                        startTime.set(Calendar.YEAR, sessionYear);
                        Calendar endTime = (Calendar) startTime.clone();
                        endTime.add(Calendar.MINUTE, duration);
                        WeekViewEvent event = new WeekViewEvent(1, session.getActivity().getTitle(), startTime, endTime);
                        event.setColor(getResources().getColor(R.color.design_default_color_primary));
                        Log.d("TAG", "event: " + event.getName() + " " + event.getStartTime().getTime().toString());
                        events.add(event);
                    }
                }
            }
            return events;

        };
        mWeekView.setMonthChangeListener(mMonthChangeListener);
    }
}