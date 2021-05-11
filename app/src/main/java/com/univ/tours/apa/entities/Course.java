package com.univ.tours.apa.entities;

import java.util.List;

public class Course {
    private static Long uid = 1L;

    private Long id;
    private String title;
    private String description;
    private String category;
    private Doctor doctor;
    private List<Activity> activities;

    public Course() {
        setId(uid);
        uid++;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void addActivity(Activity activity) {
        this.activities.add(activity);
    }

    public void removeActivity(Activity activity) {
        this.activities.remove(activity);
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
}
