package com.univ.tours.apa.activities;

import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.univ.tours.apa.R;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Course;
import com.univ.tours.apa.entities.Session;
import com.univ.tours.apa.entities.User;
import com.univ.tours.apa.fragments.patient.PatientSessionReadFragment;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PatientActivity extends BaseToolbarActivity {
    List<WeekViewEvent> events = new ArrayList<>();
    List<Session> userSessions = new ArrayList<>();
    User user;
    SharedPreferences settings;
    private ProgressDialog loadingDialog;
    private WeekView mWeekView;
    private MonthLoader.MonthChangeListener mMonthChangeListener;
    private WeekView.EventClickListener mEventClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mWeekView = findViewById(R.id.weekView);
        mWeekView.goToHour(8);

        // Set an action when any event is clicked.
        mEventClickListener = (event, eventRect) -> {
            findAndReadSession(event);
        };
        mWeekView.setOnEventClickListener(mEventClickListener);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mMonthChangeListener = (newYear, newMonth) -> {
            List<WeekViewEvent> matchedEvents = new ArrayList<>();
            for (WeekViewEvent weekViewEvent : events) {
                if (((weekViewEvent.getStartTime().get(Calendar.YEAR) == newYear) && (weekViewEvent.getStartTime().get(Calendar.MONTH) == (newMonth - 1))) || ((weekViewEvent.getEndTime().get(Calendar.YEAR) == newYear) && (weekViewEvent.getEndTime().get(Calendar.MONTH) == (newMonth - 1)))) {
                    matchedEvents.add(weekViewEvent);
                }
            }
            return matchedEvents;
        };
        mWeekView.setMonthChangeListener(mMonthChangeListener);
    }

    private void findAndReadSession(WeekViewEvent event) {
        loadingDialog = ProgressDialog.show(this, "", getString(R.string.loading_progress_bar_text), true);
        Session s = null;
        for (Session session : userSessions) {
            Calendar startTime = event.getStartTime();
            Calendar endTime = event.getEndTime();
            LocalDateTime localDateTime = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            Integer duration = Math.toIntExact((endTime.getTimeInMillis() - startTime.getTimeInMillis()) / 1000 / 60);
            if (session.getDateTime().equals(localDateTime) && session.getDuration().equals(duration) && session.getActivity().getTitle().equals(event.getName())) {
                s = session;
                break;
            }
        }
        if (s != null) {
            PatientSessionReadFragment patientSessionReadFragment = PatientSessionReadFragment.newInstance(s);
            patientSessionReadFragment.show(getSupportFragmentManager(), "patientReadSessionFragment");
        }
        loadingDialog.dismiss();
    }

    private void populateEvents() {
        if (events.size() != userSessions.size()) {
            events.clear();
            for (Session session : userSessions) {
                LocalDateTime dateTime = session.getDateTime();

                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.MILLISECOND, 0);
                startTime.set(Calendar.SECOND, dateTime.getSecond());
                startTime.set(Calendar.MINUTE, dateTime.getMinute());
                startTime.set(Calendar.HOUR_OF_DAY, dateTime.getHour());
                startTime.set(Calendar.DAY_OF_MONTH, dateTime.getDayOfMonth());
                startTime.set(Calendar.MONTH, dateTime.getMonthValue() - 1);
                startTime.set(Calendar.YEAR, dateTime.getYear());

                Calendar endTime = (Calendar) startTime.clone();
                endTime.add(Calendar.MINUTE, session.getDuration());

                WeekViewEvent event = new WeekViewEvent(1, session.getActivity().getTitle(), startTime, endTime);

                if (dateTime.isAfter(LocalDateTime.now()))
                    event.setColor(getResources().getColor(R.color.design_default_color_primary));
                else
                    event.setColor(getResources().getColor(R.color.design_default_color_secondary));
                events.add(event);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupSessions();
    }

    private void setupSessions() {
        loadingDialog = ProgressDialog.show(this, "", getString(R.string.loading_progress_bar_text), true);
        new Thread(() -> {
            user = MainActivity.db.userDao().findById(MainActivity.getLoggedInUserId());
            List<Course> courses = MainActivity.db.courseDao().findByPatient(user);
            List<Activity> activities = MainActivity.db.activityDao().findByCourses(courses);
            userSessions.clear();
            userSessions.addAll(MainActivity.db.sessionDao().findByActivities(activities));
            runOnUiThread(() -> {
//                Log.d("TAG", "userSessions: " + userSessions.size());
//                Log.d("TAG", "events: " + events.size());
                populateEvents();
//                Log.d("TAG", "events: " + events.size());
                mWeekView.notifyDatasetChanged();
                loadingDialog.dismiss();
            });
        }).start();
    }
}