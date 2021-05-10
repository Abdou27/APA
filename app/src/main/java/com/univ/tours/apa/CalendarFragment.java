package com.univ.tours.apa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {
    FragmentManager fm;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CalendarFragment.
     */
    public static CalendarFragment newInstance(FragmentManager fm) {
        CalendarFragment fragment = new CalendarFragment();
        fragment.fm = fm;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        // Get a reference for the week view in the layout.
        WeekView mWeekView = (WeekView) view.findViewById(R.id.weekView);
        // Set an action when any event is clicked.
        // mWeekView.setOnEventClickListener(mEventClickListener);
        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        MonthLoader.MonthChangeListener mMonthChangeListener = new MonthLoader.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                // Populate the week view with some events.
                List<WeekViewEvent> events = getEvents(newYear, newMonth);
                return events;
            }
        };
        mWeekView.setMonthChangeListener(mMonthChangeListener);
        // Set long press listener for events.
        // mWeekView.setEventLongPressListener(mEventLongPressListener);
        return view;
    }

    private List<WeekViewEvent> getEvents(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 9);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 2);
        WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.design_default_color_primary));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 13);
        startTime.set(Calendar.MINUTE, 20);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.MINUTE, 90);
        event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.design_default_color_secondary));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 15);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        startTime.add(Calendar.DATE, 1);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.MINUTE, 120);
        event = new WeekViewEvent(3, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.design_default_color_primary));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
        startTime.set(Calendar.HOUR_OF_DAY, 10);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.MINUTE, 120);
        event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.design_default_color_secondary));
        events.add(event);

        return events;
    }

    protected String getEventTitle(Calendar time) {
        Random r = new Random();
        return String.format(getString(R.string.session_of) + " %02d:%02d"
                + "\n" + getString(R.string.activity) + " " + r.nextInt(10), time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
    }
}